# -*- coding: utf-8 -*-
import os
import httpx
import uvicorn
import shutil
from fastapi import FastAPI, HTTPException, UploadFile, File
from fastapi.middleware.cors import CORSMiddleware
from pydantic import BaseModel
from typing import List, Optional
from pathlib import Path

# 모듈 임포트
from src.preprocess.ocr_extract import extract_text_from_image, process_pdf
from src.preprocess.normalize_text import normalize_legal_text
from src.serving.database import init_db, save_analysis, get_history

# 1. 앱 선언 (가장 먼저 해야 함)
app = FastAPI(title="Legal QA Integrated API")

@app.on_event("startup")
async def startup():
    try:
        init_db()
    except Exception as e:
        print(f"[DB 초기화 경고] MySQL 연결 실패 — DB 없이 실행됩니다: {e}")

# 2. CORS 설정 (한 번만)
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

# 설정
OLLAMA_BASE = os.environ.get("OLLAMA_BASE_URL", "http://127.0.0.1:11434").rstrip("/")
OLLAMA_MODEL = os.environ.get("OLLAMA_MODEL", "law_gemma4.1")
DEFAULT_SYSTEM_MESSAGE = "당신은 법률 질문에 답변하는 AI 어시스턴트입니다. 정확한 근거를 바탕으로 답변하세요."

# 업로드 경로
UPLOAD_DIR = Path("data/uploads")
UPLOAD_DIR.mkdir(parents=True, exist_ok=True)

# 데이터 스키마
class ChatRequest(BaseModel):
    question: str
    domain:   Optional[str] = None

class ChatResponse(BaseModel):
    answer: str
    log_id: Optional[int] = None

class AnalysisResponse(BaseModel):
    ocr_text: str
    status:   str
    log_id:   Optional[int] = None

# --- 공통 로직 ---
async def call_ollama(prompt: str, system_msg: str):
    payload = {
        "model": OLLAMA_MODEL,
        "messages": [
            {"role": "system", "content": system_msg},
            {"role": "user", "content": prompt}
        ],
        "stream": False
    }
    async with httpx.AsyncClient(timeout=180.0) as client:
        response = await client.post(f"{OLLAMA_BASE}/api/chat", json=payload)
        response.raise_for_status()
        return response.json().get("message", {}).get("content", "").strip()

# --- 엔드포인트 1: 일반 챗봇 대화 ---
@app.post("/ask-chatbot", response_model=ChatResponse)
async def chat(request: ChatRequest):
    answer = await call_ollama(request.question, DEFAULT_SYSTEM_MESSAGE)
    log_id = None
    try:
        log_id = save_analysis(
            question=request.question,
            answer=answer,
            model=OLLAMA_MODEL,
            domain=request.domain,
        )
    except Exception as e:
        print(f"[DB 저장 실패] {e}")
    return ChatResponse(answer=answer, log_id=log_id)

# --- 엔드포인트 2: 문서 OCR 분석 ---
@app.post("/analyze-document", response_model=AnalysisResponse)
async def analyze_document(file: UploadFile = File(...)):
    file_path = UPLOAD_DIR / file.filename
    with file_path.open("wb") as buffer:
        shutil.copyfileobj(file.file, buffer)
    
    try:
        if file.filename.lower().endswith('.pdf'):
            raw_result = process_pdf(str(file_path))
        else:
            raw_result = extract_text_from_image(str(file_path))
            
        clean_result = normalize_legal_text(raw_result)
        log_id = None
        try:
            log_id = save_analysis(
                question=file.filename,
                answer=clean_result,
                model=OLLAMA_MODEL,
                filename=file.filename,
            )
        except Exception as e:
            print(f"[DB 저장 실패] {e}")
        return AnalysisResponse(ocr_text=clean_result, status="success", log_id=log_id)
    
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"문서 분석 실패: {str(e)}")
    finally:
        if file_path.exists():
            os.remove(file_path)

@app.get("/history")
async def history(limit: int = 50, domain: Optional[str] = None):
    """최근 분석 기록 조회"""
    try:
        rows = get_history(limit=limit, domain=domain)
        return {"count": len(rows), "data": rows}
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"DB 조회 실패: {e}")

@app.get("/health")
async def health():
    return {"status": "ok", "model": OLLAMA_MODEL}

if __name__ == "__main__":
    uvicorn.run(app, host="0.0.0.0", port=8000)