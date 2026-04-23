# -*- coding: utf-8 -*-
import os
import httpx
import uvicorn
from fastapi import FastAPI, HTTPException
from fastapi.middleware.cors import CORSMiddleware
from pydantic import BaseModel
from typing import List, Optional

# 환경 변수 설정
OLLAMA_BASE = os.environ.get("OLLAMA_BASE_URL", "http://127.0.0.1:11434").rstrip("/")
OLLAMA_MODEL = "law_gemma4"  # 생성하신 모델명으로 고정
DEFAULT_SYSTEM_MESSAGE = "당신은 법률 질문에 답변하는 AI 어시스턴트입니다. 정확한 근거를 바탕으로 답변하세요."

app = FastAPI(title="Legal QA Chatbot API", version="1.0.0")

# CORS 설정 (Next.js 연동 대비)
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

# 데이터 스키마 정의
class ChatRequest(BaseModel):
    question: str
    domain: Optional[str] = "일반"

class ChatResponse(BaseModel):
    answer: str
    references: Optional[List[str]] = []

async def call_ollama(prompt: str, system_msg: str):
    payload = {
        "model": OLLAMA_MODEL,
        "messages": [
            {"role": "system", "content": system_msg},
            {"role": "user", "content": prompt}
        ],
        "stream": False,
        "options": {
            "temperature": 0.2,
            "top_p": 0.95
        }
    }
    async with httpx.AsyncClient(timeout=30.0) as client:
        try:
            response = await client.post(f"{OLLAMA_BASE}/api/chat", json=payload)
            response.raise_for_status()
            data = response.json()
            return data.get("message", {}).get("content", "").strip()
        except Exception as e:
            raise HTTPException(status_code=500, detail=f"Ollama 연동 오류: {str(e)}")

@app.post("/chat", response_model=ChatResponse)
async def chat(request: ChatRequest):
    """
    사용자의 질문에 대해 파인튜닝된 Gemma3 모델이 답변을 생성합니다.
    """
    answer = await call_ollama(request.question, DEFAULT_SYSTEM_MESSAGE)
    return ChatResponse(answer=answer, references=[])

@app.get("/health")
async def health_check():
    return {"status": "ok", "model": OLLAMA_MODEL}

if __name__ == "__main__":
    # 포트는 CLAUDE.md 설계에 따라 8000번 사용
    uvicorn.run(app, host="0.0.0.0", port=8000)