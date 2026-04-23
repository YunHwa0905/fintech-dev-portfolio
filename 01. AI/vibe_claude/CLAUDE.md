# CLAUDE.md — 법률 QA 챗봇 프로젝트

## 프로젝트 개요

AIHub 법률 데이터셋을 알파카 포맷으로 변환하고, Gemma3-4b 모델을 파인튜닝하여
법률 질문에 답변하는 챗봇 서비스를 구축하는 프로젝트.

추가 목표:
- 사용자가 업로드한 PDF, 이미지, 스캔 문서를 분석
- OCR 단계로 텍스트 추출
- 텍스트 정규화 후 법령/판례 근거 검색
- FastAPI + Next.js + MCP로 법률 QA 챗봇 서비스 완성

**목표 스택**: 데이터 전처리 → 파인튜닝 → FastAPI 서빙 → Next.js 챗봇 UI → MCP 연동 → OCR/문서분석

---

## 기술 스택

| 레이어 | 기술 |
|---|---|
| 데이터 전처리 | Python, pandas, HuggingFace datasets |
| OCR/문서분석 | Ollama Vision, OCR, PDF 파싱, 이미지 전처리 |
| 텍스트 정규화 | Python, re, konlpy(선택), pandas |
| 파인튜닝 | Gemma3-4b, QLoRA, BitsAndBytes (4bit) |
| 모델 서빙 | FastAPI, uvicorn |
| 프론트엔드 | Next.js 15, TypeScript, Tailwind CSS |
| MCP 도구 | 국가법령정보 Open API, 대법원 판례 API |
| 환경 | Python venv (.venv), .env 환경변수 관리 |

---

## 프로젝트 구조 (목표)

```bash
vibe_claude/
├── .venv/                        # Python 가상환경 (건드리지 말 것)
├── .env                          # 환경변수 (HF_TOKEN, API 키 등)
├── pyvenv.cfg
│
├── data/
│   ├── raw/                      # AIHub 원본 데이터 (JSON)
│   │   └── 법률_분야명/
│   │       └── *.json
│   ├── processed/                # 알파카 포맷 변환 결과
│   │   ├── alpaca_legal.json
│   │   └── ocr_texts.json
│   └── sample/                   # 검증용 샘플
│
├── notebooks/
│   ├── 01_data_preprocessing.ipynb
│   ├── 02_ocr_pipeline.ipynb
│   └── 03_legal_normalization.ipynb
│
├── src/
│   ├── preprocess/
│   │   ├── to_alpaca.py          # 알파카 변환 스크립트
│   │   ├── ocr_extract.py        # OCR 추출
│   │   └── normalize_text.py     # 텍스트 정규화
│   ├── training/
│   │   └── finetune.py           # Gemma3-4b QLoRA 파인튜닝
│   ├── serving/
│   │   ├── main.py               # FastAPI 앱 엔트리포인트
│   │   ├── model.py              # 모델 로드 및 추론
│   │   ├── schemas.py            # Pydantic 요청/응답 스키마
│   │   └── document_service.py   # 업로드 파일 분석 서비스
│   └── retrieval/
│       └── legal_search.py       # 법령/판례 검색 보조 로직
│
├── mcp/
│   ├── law_search_server.py      # 국가법령정보 MCP 서버
│   ├── precedent_server.py       # 대법원 판례 MCP 서버
│   └── ocr_server.py             # OCR 보조 서버(선택)
│
├── web/                          # Next.js 챗봇 프론트엔드
│   ├── app/
│   │   ├── page.tsx              # 메인 챗봇 페이지
│   │   └── api/
│   │       ├── chat/
│   │       │   └── route.ts      # FastAPI 프록시 라우트
│   │       └── upload/
│   │           └── route.ts      # 파일 업로드 처리
│   ├── components/
│   │   ├── ChatWindow.tsx
│   │   ├── MessageBubble.tsx
│   │   ├── LawReference.tsx      # 판례/법령 인용 카드
│   │   └── FileUploader.tsx      # PDF/이미지 업로드
│   └── package.json
│
├── CLAUDE.md                     # 이 파일
└── README.md
```

---

## 환경변수 (.env)

```env
# HuggingFace
HF_TOKEN=your_hf_token
HF_DATASET_REPO=yunhwa/law_instruct
HF_MODEL_REPO=yunhwa/law_alpaca

# Ollama
OLLAMA_BASE_URL=http://localhost:11434
OLLAMA_VISION_MODEL=gemma3
OLLAMA_OCR_MODEL=llama3.2-vision:11b

# 국가법령정보 Open API
LAW_API_KEY=your_law_api_key
LAW_API_BASE=https://www.law.go.kr/DRF

# 대법원 판례 API
COURT_API_KEY=your_court_api_key
COURT_API_BASE=https://www.lawnb.com/Api

# FastAPI 서버
API_HOST=0.0.0.0
API_PORT=8000

# Next.js
NEXT_PUBLIC_API_URL=http://localhost:8000
```

---

## 데이터 파이프라인

### 알파카 포맷 구조

```json
{
  "instruction": "계약 해지 시 손해배상 청구 가능한가요?",
  "input": "민법",
  "output": "민법 제390조에 따라 채무불이행 시 손해배상을 청구할 수 있습니다..."
}
```

### AIHub JSON → 알파카 변환 규칙

- 폴더명 패턴: `법률_민법/`, `법률_형법/` → `input` 값으로 사용 (`_` 이후 추출)
- JSON 키 매핑: `question` → `instruction`, `answer` → `output`
- 인코딩: `utf-8-sig` 우선, 실패 시 `ms949` 폴백
- 업로드 대상: `HF_DATASET_REPO`

---

## OCR 및 문서 분석

### 지원 입력
- PDF
- JPG, PNG, JPEG
- 스캔된 계약서
- 민원서류
- 법률 관련 이미지

### 처리 흐름

1. 파일 업로드
2. PDF면 페이지별 이미지 추출 또는 텍스트 추출
3. 이미지면 OCR 수행
4. OCR 결과를 텍스트 정규화
5. 법률 도메인 키워드, 조문, 사건번호, 날짜, 금액 추출
6. 국가법령정보 API 및 판례 API로 근거 검색
7. 최종 답변 생성
8. "참고용" 고지와 함께 응답 반환

### OCR 규칙

- 스캔 문서는 우선 OCR 대상
- 표, 조문 번호, 날짜, 금액은 최대한 보존
- 줄바꿈, 공백, 특수문자 정규화
- 인식이 불안정한 구간은 원문 일부와 함께 표시
- OCR 결과만으로 단정하지 말고 근거 검색을 반드시 수행

### 텍스트 정규화 규칙

- 연속 공백 → 단일 공백
- 줄바꿈 정리
- 특수문자 통합
- 한글/숫자/법률기호 패턴 보정
- 조문 패턴 예: `제\d+조`, `민법 제\d+조`, `형법 제\d+조`
- 날짜 패턴, 금액 패턴, 사건번호 패턴 추출

---

## FastAPI 서빙

### 엔드포인트

```http
POST /chat
Body: { "question": "...", "domain": "민법" }
Response: { "answer": "...", "references": [...] }

POST /analyze-document
Body: multipart/form-data with file
Response: {
  "ocr_text": "...",
  "normalized_text": "...",
  "references": [...]
}

GET /health
Response: { "status": "ok", "model": "gemma3-4b-legal" }
```

### 모델 설정 (RTX 2000 Ada 기준)

```python
# BitsAndBytesConfig 4bit 양자화
load_in_4bit=True
bnb_4bit_compute_dtype=torch.float16
bnb_4bit_use_double_quant=True
```

### 문서 분석 원칙

- 이미지/스캔 파일은 OCR 우선
- 텍스트 문서는 추출 후 정규화
- 법률 조항/판례 검색은 별도 단계로 분리
- 답변은 근거를 함께 제공
- 과도한 추론보다 검증 가능한 근거를 우선

---

## MCP 서버

### 1. 국가법령정보 MCP (`law_search_server.py`)

```text
도구명: search_law
입력: { "query": "손해배상", "law_name": "민법" }
출력: { "article": "제390조", "content": "..." }
API: https://www.law.go.kr/DRF/lawSearch.do
```

### 2. 대법원 판례 MCP (`precedent_server.py`)

```text
도구명: search_precedent
입력: { "query": "계약 해지 손해배상" }
출력: { "case_no": "2023다12345", "summary": "..." }
```

### 3. OCR 보조 흐름

- OCR 결과에서 핵심 키워드를 추출
- 법령/판례 검색에 사용할 질의로 정규화
- 관련 조문, 판례, 참고 문구를 응답에 포함

### Claude Code에서 MCP 등록 방법

```bash
claude mcp add law-search python mcp/law_search_server.py
claude mcp add precedent python mcp/precedent_server.py
claude mcp add ocr python mcp/ocr_server.py
```

---

## Next.js 챗봇 UI

### 핵심 컴포넌트

- `ChatWindow`: 메시지 히스토리 + 입력창
- `MessageBubble`: 사용자/AI 말풍선 구분
- `LawReference`: 관련 법령/판례 인용 카드
- `FileUploader`: PDF/이미지 업로드

### API 연동 흐름

```text
사용자 입력 또는 파일 업로드
  → Next.js /api/upload 또는 /api/chat
  → FastAPI /analyze-document 또는 /chat
  → OCR / 텍스트 정규화 / 법령 검색
  → Gemma3-4b 추론
  → 응답 스트리밍 반환
```

---

## 개발 명령어

```bash
# 가상환경 활성화
.venv\Scripts\activate          # Windows
source .venv/bin/activate       # Mac/Linux

# 패키지 설치
pip install fastapi uvicorn transformers bitsandbytes datasets peft pillow pydantic ollama

# FastAPI 실행
uvicorn src.serving.main:app --reload --host 0.0.0.0 --port 8000

# Next.js 실행
cd web && npm run dev

# MCP 서버 테스트
python mcp/law_search_server.py
python mcp/precedent_server.py

# OCR/문서분석 테스트
python src/preprocess/ocr_extract.py
```

---

## 개발 우선순위

```text
Phase 1  데이터 전처리 → 알파카 변환 → HF 업로드
Phase 2  Gemma3-4b QLoRA 파인튜닝
Phase 3  FastAPI 서빙 구축
Phase 4  OCR/문서 분석 파이프라인 구축
Phase 5  MCP 서버 (국가법령 API, 판례 API) 제작
Phase 6  Next.js 챗봇 UI 개발
Phase 7  MCP ↔ Next.js ↔ FastAPI ↔ OCR 통합 연동
```

---

## 주의사항

- `.venv/` 폴더는 수정하지 말 것, 패키지는 pip으로만 관리
- `.env` 파일은 절대 커밋하지 말 것 (`.gitignore`에 추가)
- 모델 추론 시 CUDA 메모리 부족 시 `max_new_tokens` 줄이기 (기본값: 512)
- OCR 결과는 항상 검증이 필요함
- 스캔 문서는 이미지 품질에 따라 결과가 크게 달라질 수 있음
- 법률 답변은 "참고용"임을 UI에서 반드시 명시할 것
- AIHub 데이터는 비상업적 연구 목적으로만 사용
- 법률 문서는 원문 보존과 정규화의 균형을 유지할 것