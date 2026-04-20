# -*- coding: utf-8 -*-
"""Ollama(hkcode_gemma3 GGUF) LLM 서빙 — llama_part6_llm모델_서빙_gemma_rev 구조 유지.

로컬 Ollama에 `hkcode_gemma3` 모델이 있어야 합니다.
환경변수: OLLAMA_HOST (기본 http://127.0.0.1:11434), OLLAMA_MODEL (기본 hkcode_gemma3)
"""

try:
    import google.colab
    inColab = True
except ImportError:
    inColab = False

if inColab is True:
    # !pip install -U nest-asyncio==1.6.0 pyngrok==7.2.4 uvicorn==0.34.2 fastapi==0.115.12 httpx==0.28.1
    pass

import asyncio
import os

import httpx
import uvicorn

"""# 1. Ollama 모델 설정 (GGUF는 Ollama에서 로드됨)"""

OLLAMA_BASE = os.environ.get("OLLAMA_HOST", "http://127.0.0.1:11434").rstrip("/")
OLLAMA_MODEL = os.environ.get("OLLAMA_MODEL", "hkcode_gemma3")

"""### ★★★ 수정 포인트 ★★★ — Ollama 모델명은 위 OLLAMA_MODEL / hkcode_gemma3"""

DEFAULT_SYSTEM_MESSAGE = "당신은 문제를 정확하게 답변하는 AI입니다."
system_message = DEFAULT_SYSTEM_MESSAGE


def generate_gemma_answer(
    user_message: str,
    system_message: str = DEFAULT_SYSTEM_MESSAGE,
    max_new_tokens: int = 512,
    temperature: float = 0.2,
    top_p: float = 0.95,
    top_k: int = 50,
) -> str:
    """Ollama /api/chat 으로 Gemma 계열 대화 생성 (모델이 알맞은 템플릿 적용)."""
    messages = []
    if system_message:
        messages.append({"role": "system", "content": system_message})
    messages.append({"role": "user", "content": user_message})

    payload = {
        "model": OLLAMA_MODEL,
        "messages": messages,
        "stream": False,
        "options": {
            "temperature": temperature,
            "top_p": top_p,
            "top_k": top_k,
            "num_predict": max_new_tokens,
        },
    }
    with httpx.Client(timeout=600.0) as client:
        r = client.post(f"{OLLAMA_BASE}/api/chat", json=payload)
        r.raise_for_status()
        data = r.json()
    content = (data.get("message") or {}).get("content") or ""
    return content.strip()


# 문제(질문) 예시
user_message = (
    "hkcode 유튜브 채널은 누가 운영하나요?<end_of_turn>\n"
)
# 시스템 메시지(옵션)
system_message = DEFAULT_SYSTEM_MESSAGE
# 답변 생성
output = generate_gemma_answer(user_message, system_message)
print("\n[질문]\n", user_message)
print("\n[답변]\n", output)

# 문제(질문) 예시
user_message = (
    "스마트금융과는 어디에 위치하나요?<end_of_turn>\n"
)
# 시스템 메시지(옵션)
system_message = DEFAULT_SYSTEM_MESSAGE
# 답변 생성
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
