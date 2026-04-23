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

# 설정
OLLAMA_BASE = os.environ.get("OLLAMA_BASE_URL", "http://127.0.0.1:11434").rstrip("/")
OLLAMA_MODEL = "law_gemma4"
DEFAULT_SYSTEM_MESSAGE = "당신은 법률 질문에 답변하는 AI 어시스턴트입니다. 정확한 근거를 바탕으로 답변하세요."

app = FastAPI(title="Legal QA Integrated API")

# 업로드 파일 임시 저장 경로
UPLOAD_DIR = Path("data/uploads")
UPLOAD_DIR.mkdir(parents=True, exist_ok=True)

app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

# 데이터 스키마
class ChatRequest(BaseModel):
    question: str
    domain: Optional[str] = "일반"

class ChatResponse(BaseModel):
    answer: str
    references: List[str] = []

class AnalysisResponse(BaseModel):
    ocr_text: str
    status: str

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
    # 모델 로딩 시간을 고려하여 timeout을 180초로 설정
    async with httpx.AsyncClient(timeout=180.0) as client:
        response = await client.post(f"{OLLAMA_BASE}/api/chat", json=payload)
        response.raise_for_status()
        return response.json().get("message", {}).get("content", "").strip()

# --- 엔드포인트 ---

@app.post("/chat", response_model=ChatResponse)
async def chat(request: ChatRequest):
    """일반 법률 질의응답"""
    answer = await call_ollama(request.question, DEFAULT_SYSTEM_MESSAGE)
    return ChatResponse(answer=answer)

@app.post("/analyze-document", response_model=AnalysisResponse)
async def analyze_document(file: UploadFile = File(...)):
    """PDF 또는 이미지를 업로드받아 Tesseract OCR 분석만 수행 (지어내기 방지)"""
    file_path = UPLOAD_DIR / file.filename
    
    # 1. 파일 저장
    with file_path.open("wb") as buffer:
        shutil.copyfileobj(file.file, buffer)
    
    try:
        # 2. Tesseract OCR 수행 (이제 ocr_extract.py의 함수는 Tesseract를 씁니다)
        if file.filename.lower().endswith('.pdf'):
            raw_result = process_pdf(str(file_path))
        else:
            raw_result = extract_text_from_image(str(file_path))
            
        # 3. 정규화 (불필요한 공백 제거 및 조문 번호 가독성 처리)
        # LLM(call_ollama)을 거치지 않고 바로 정규화로 보냅니다.
        clean_result = normalize_legal_text(raw_result)
            
        return AnalysisResponse(ocr_text=clean_result, status="success")
    
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"문서 분석 실패: {str(e)}")
    finally:
        # 4. 임시 파일 삭제
        if file_path.exists():
            os.remove(file_path)

@app.get("/health")
async def health():
    return {"status": "ok", "model": OLLAMA_MODEL}

if __name__ == "__main__":
    uvicorn.run(app, host="0.0.0.0", port=8000)