# -*- coding: utf-8 -*-
"""로컬 GGUF 직접 로드 추론 — hkcode.py / llama_part6 구조 유지.

필요: pip install llama-cpp-python
  (GPU: CUDA 빌드에 맞는 wheel 사용, https://github.com/abetlen/llama-cpp-python )

환경변수:
  HKCODE_GGUF_PATH  — .gguf 파일 절대/상대 경로 (필수 권장)
  LLAMA_N_CTX       — 컨텍스트 길이 (기본 8192)
  LLAMA_N_GPU_LAYERS — GPU에 올릴 레이어 수 (기본 -1 = 가능한 만큼, CPU만이면 0)
"""

try:
    import google.colab
    inColab = True
except ImportError:
    inColab = False

if inColab is True:
    # !pip install -U nest-asyncio==1.6.0 pyngrok==7.2.4 uvicorn==0.34.2 fastapi==0.115.12 llama-cpp-python
    pass

import asyncio
import os
import threading

import uvicorn

from llama_cpp import Llama

"""# 1. GGUF 로드"""

# ★★★ 수정 포인트 ★★★ — 환경변수 HKCODE_GGUF_PATH 또는 아래 기본 경로
_DEFAULT_GGUF_PATH = ""  # 예: r"C:\models\hkcode_gemma3\model.gguf"
GGUF_PATH = os.environ.get("HKCODE_GGUF_PATH", _DEFAULT_GGUF_PATH).strip()
N_CTX = int(os.environ.get("LLAMA_N_CTX", "8192"))
N_GPU_LAYERS = int(os.environ.get("LLAMA_N_GPU_LAYERS", "-1"))

if not GGUF_PATH:
    raise RuntimeError(
        "GGUF 경로를 지정하세요: 환경변수 HKCODE_GGUF_PATH 또는 hkcode_gguf.py의 _DEFAULT_GGUF_PATH"
    )
if not os.path.isfile(GGUF_PATH):
    raise FileNotFoundError(f"GGUF 파일을 찾을 수 없습니다: {GGUF_PATH}")

print(f"[hkcode_gguf] GGUF 로드 중: {GGUF_PATH}")
_llm = Llama(
    model_path=GGUF_PATH,
    n_ctx=N_CTX,
    n_gpu_layers=N_GPU_LAYERS,
    verbose=False,
)
_llm_lock = threading.Lock()
print("[hkcode_gguf] 로드 완료")

DEFAULT_SYSTEM_MESSAGE = "당신은 문제를 정확하게 답변하는 AI입니다."
system_message = DEFAULT_SYSTEM_MESSAGE


def _build_gemma_turn_prompt(user_message: str, system_message: str) -> str:
    """Gemma 계열 수동 프롬프트 (chat 템플릿이 없을 때 폴백)."""
    if system_message:
        return (
            f"<start_of_turn>system\n{system_message}<end_of_turn>\n"
            f"<start_of_turn>user\n{user_message}<end_of_turn>\n"
            f"<start_of_turn>model\n"
        )
    return (
        f"<start_of_turn>user\n{user_message}<end_of_turn>\n"
        f"<start_of_turn>model\n"
    )


def generate_gemma_answer(
    user_message: str,
    system_message: str = DEFAULT_SYSTEM_MESSAGE,
    max_new_tokens: int = 512,
    temperature: float = 0.2,
    top_p: float = 0.95,
    top_k: int = 50,
) -> str:
    """GGUF 모델로 답변 생성. chat 템플릿 우선, 실패 시 Gemma turn 프롬프트 + create_completion."""
    messages = []
    if system_message:
        messages.append({"role": "system", "content": system_message})
    messages.append({"role": "user", "content": user_message})

    with _llm_lock:
        try:
            out = _llm.create_chat_completion(
                messages=messages,
                max_tokens=max_new_tokens,
                temperature=temperature,
                top_p=top_p,
                top_k=top_k,
            )
            text = (out.get("choices") or [{}])[0].get("message", {}).get("content") or ""
            text = text.strip()
            if text:
                return text
        except Exception:
            pass

        prompt = _build_gemma_turn_prompt(user_message, system_message or "")
        out = _llm.create_completion(
            prompt=prompt,
            max_tokens=max_new_tokens,
            temperature=temperature,
            top_p=top_p,
            top_k=top_k,
            stop=["<end_of_turn>"],
        )
        text = (out.get("choices") or [{}])[0].get("text") or ""
        return text.strip()


# 문제(질문) 예시
user_message = (
    "hkcode 유튜브 채널은 누가 운영하나요?<end_of_turn>\n"
)
system_message = DEFAULT_SYSTEM_MESSAGE
output = generate_gemma_answer(user_message, system_message)
print("\n[질문]\n", user_message)
print("\n[답변]\n", output)

user_message = (
    "스마트금융과는 어디에 위치하나요?<end_of_turn>\n"
)
system_message = DEFAULT_SYSTEM_MESSAGE
output = generate_gemma_answer(user_message, system_message)
print("\n[질문]\n", user_message)
print("\n[답변]\n", output)

"""### 정상동작 확인 #2"""

from fastapi import FastAPI

import pickle

import pandas as pd
import numpy as np
from pydantic import BaseModel

from fastapi.middleware.cors import CORSMiddleware

origins = ["*"]

app = FastAPI(title="ML API")

app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["GET", "POST", "PUT", "DELETE"],
    allow_headers=["*"],
)


class InDataset(BaseModel):
    question: str


@app.post("/predict", status_code=200)
async def predict_tf(x: InDataset):
    print(x)
    response = await asyncio.to_thread(
        generate_gemma_answer,
        x.question,
        system_message,
    )
    print(response)
    return {"prediction": response}


@app.get("/")
async def root():
    return {"message": "online"}


if inColab is True:
    import nest_asyncio
    from pyngrok import ngrok

    _token = os.environ.get("NGROK_AUTH_TOKEN")
    if _token:
        ngrok.set_auth_token(_token)
        ngrokTunnel = ngrok.connect(8000)
        print("공용 URL", ngrokTunnel.public_url)
    else:
        print("NGROK_AUTH_TOKEN 환경변수를 설정하면 ngrok 터널을 띄울 수 있습니다.")
    nest_asyncio.apply()
    uvicorn.run(app, port=8000)

else:
    import nest_asyncio

    nest_asyncio.apply()

    if __name__ == "__main__":
        uvicorn.run(app, host="0.0.0.0", port=9999, log_level="debug")
