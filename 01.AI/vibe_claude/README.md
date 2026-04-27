# LegalAI — AI 법률 QA 챗봇

AIHub 법률 데이터셋으로 파인튜닝한 Gemma3-4b 모델을 Ollama로 서빙하고,  
FastAPI + Next.js로 법률 질문 답변 및 문서(OCR) 분석 서비스를 제공하는 프로젝트입니다.

---

## 시스템 구성

```
사용자 (브라우저)
    │
    ▼
Next.js 챗봇 UI  (web/ — port 3000)
    │  POST /ask-chatbot  │  POST /analyze-document
    ▼
FastAPI 서버  (src/serving/main.py — port 8000)
    │                     │
    ▼                     ▼
Ollama API           OCR 파이프라인
(law_gemma 모델)     (ocr_extract.py + normalize_text.py)
    │
    ▼
MySQL DB  (analysis_log 테이블 — 질문/답변/모델/도메인 저장)
```

---

## 기술 스택

| 레이어 | 기술 |
|---|---|
| 데이터 전처리 | Python, pandas, HuggingFace datasets |
| 파인튜닝 | Gemma3-4b · QLoRA · BitsAndBytes 4-bit |
| GGUF 변환 | llama.cpp, Ollama Modelfile |
| OCR / 문서 분석 | Ollama Vision (llama3.2-vision), PyMuPDF |
| 텍스트 정규화 | Python re (법령 조문 패턴, 공백 정리) |
| API 서버 | FastAPI, uvicorn, httpx |
| DB | MySQL 8 + pymysql |
| 프론트엔드 | Next.js 15 · Tailwind CSS v4 |
| 모델 런타임 | Ollama |

---

## 프로젝트 구조

```
vibe_claude/
├── .env                          # 환경변수 (커밋 금지)
├── .venv/                        # Python 가상환경
│
├── data/
│   ├── raw/                      # AIHub 원본 JSON (분야별 폴더)
│   │   └── 민사법_라벨링데이터/
│   │       └── 민사법_판결문/
│   └── processed/
│       └── alpaca_legal_400.json # 파인튜닝용 400개 샘플 (분야별 100개씩)
│
├── notebooks/
│   ├── 01. data_preprocessing.ipynb   # AIHub → 알파카 포맷 변환 + HF 업로드
│   ├── 02. Fine_tune.ipynb            # Gemma3-4b QLoRA 파인튜닝
│   ├── 03. Load_And_Save.ipynb        # 모델 로드 · 추론 테스트 · HF 업로드
│   └── 04. gguf_Load_Save.ipynb       # GGUF 변환 · Ollama 등록
│
├── src/
│   ├── preprocess/
│   │   ├── ocr_extract.py        # PDF / 이미지 OCR (Ollama Vision)
│   │   └── normalize_text.py     # 법령 텍스트 정규화
│   └── serving/
│       ├── main.py               # FastAPI 엔트리포인트
│       └── database.py           # MySQL 연동 (analysis_log)
│
└── web/                          # Next.js 챗봇 프론트엔드
    ├── app/
    │   ├── layout.js
    │   ├── page.js
    │   └── globals.css           # Tailwind v4 (@import "tailwindcss")
    └── components/
        └── ChatInterface.js      # 전체 챗봇 UI (사이드바 + 도메인 선택 + 메시지)
```

---

## 빠른 시작

### 1. Python 환경 설정

```bash
cd 01.AI/vibe_claude

# 가상환경 활성화 (Windows)
.venv\Scripts\activate

# 필수 패키지 설치
pip install fastapi uvicorn httpx pymysql python-dotenv \
            transformers bitsandbytes datasets peft \
            pillow pymupdf ollama
```

### 2. 환경변수 설정

`.env` 파일을 열고 실제 값으로 교체합니다.

```env
# HuggingFace
HF_TOKEN=hf_your_token
HF_DATASET_REPO=yunhwa/law_instruct
HF_MODEL_REPO=yunhwa/law_alpaca
HF_GGUF_REPO=yunhwa/gemma3-4b-legal-gguf

# Ollama
OLLAMA_BASE_URL=http://127.0.0.1:11434
OLLAMA_MODEL=law_gemma4.1          # ← 실제 Ollama 모델명으로 변경

# MySQL
DB_HOST=localhost
DB_PORT=3306
DB_NAME=legal_ai
DB_USER=root
DB_PASSWORD=your_password

# 서버
API_HOST=0.0.0.0
API_PORT=8000
NEXT_PUBLIC_API_URL=http://localhost:8000
```

### 3. Ollama 모델 준비

```bash
# 이미 생성한 Ollama 모델 확인
ollama list

# 모델이 없을 경우 GGUF에서 생성 (notebooks/04 참조)
ollama create law_gemma4.1 -f Modelfile
```

### 4. FastAPI 서버 실행

```bash
# vibe_claude/ 디렉터리에서 실행
uvicorn src.serving.main:app --reload --host 0.0.0.0 --port 8000
```

서버 시작 시 MySQL DB(`legal_ai`)와 `analysis_log` 테이블이 자동 생성됩니다.  
MySQL 연결에 실패해도 서버는 DB 없이 정상 실행됩니다.

### 5. Next.js 프론트엔드 실행

```bash
cd web
npm install
npm run dev
```

브라우저에서 `http://localhost:3000` 접속

---

## API 엔드포인트

| 메서드 | 경로 | 설명 |
|---|---|---|
| POST | `/ask-chatbot` | 법률 질문 → Ollama 모델 답변 |
| POST | `/analyze-document` | PDF/이미지 업로드 → OCR + 정규화 |
| GET | `/history` | 분석 기록 조회 (`?limit=50&domain=민사법`) |
| GET | `/health` | 서버 상태 확인 |

### 요청/응답 예시

```http
POST /ask-chatbot
{
  "question": "계약 해지 시 손해배상 청구가 가능한가요?",
  "domain": "민사법"
}

→ {
  "answer": "민법 제390조에 따라 채무불이행 시 손해배상을 청구할 수 있습니다...",
  "log_id": 42
}
```

---

## 데이터 파이프라인

### 알파카 포맷

```json
{
  "instruction": "계약 해지 시 손해배상 청구 가능한가요?",
  "input": "민사법",
  "output": "민법 제390조에 따라..."
}
```

### 처리 흐름

```
AIHub 원본 JSON (data/raw/)
    └─ notebooks/01: question/answer 추출 → 알파카 변환 → HF 업로드
           └─ notebooks/02: Gemma3-4b QLoRA 파인튜닝 (4-bit 양자화)
                  └─ notebooks/03: 모델 병합 → 추론 테스트 → HF 업로드
                         └─ notebooks/04: GGUF 변환 → Ollama 등록
```

### 학습 데이터 샘플 (`alpaca_legal_400.json`)

분야별 100개, 총 400개로 구성된 파인튜닝 검증용 데이터셋

| 도메인 | 샘플 수 |
|---|---|
| 민사법 | 100 |
| 형사법 | 100 |
| 행정법 | 100 |
| 지식재산권법 | 100 |

---

## 챗봇 UI 주요 기능

- **도메인 선택 버튼** — 민사법 / 형사법 / 행정법 / 지식재산권법 탭 클릭 후 질문
- **문서 업로드** — PDF, JPG, PNG 업로드 → OCR → 정규화 결과 반환
- **대화 기록** — 좌측 사이드바에 최근 대화 목록 표시
- **다크 테마** — 네이비 계열 다크 UI

---

## 모델 파인튜닝 설정 (RTX 2000 Ada 기준)

```python
# QLoRA 4-bit 양자화
load_in_4bit=True
bnb_4bit_compute_dtype=torch.float16
bnb_4bit_use_double_quant=True

# LoRA
r=16, lora_alpha=32, lora_dropout=0.05
target_modules=["q_proj", "v_proj"]

# 학습
num_train_epochs=3
per_device_train_batch_size=2
max_seq_length=2048
```

### 추론 프롬프트 형식

```
<start_of_turn>user
{질문}

[입력]
{도메인}<end_of_turn>
<start_of_turn>model
```

---

## DB 스키마

```sql
CREATE TABLE analysis_log (
    id         INT AUTO_INCREMENT PRIMARY KEY,
    filename   VARCHAR(255)  COMMENT '업로드 파일명 (없으면 NULL)',
    question   TEXT          COMMENT '사용자 질문',
    answer     LONGTEXT      COMMENT '모델 답변',
    model      VARCHAR(100)  COMMENT '사용 모델명',
    domain     VARCHAR(50)   COMMENT '법률 도메인',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```

---

## 개발 진행 현황

| Phase | 내용 | 상태 |
|---|---|---|
| 1 | AIHub 데이터 전처리 → 알파카 변환 → HF 업로드 | 완료 |
| 2 | Gemma3-4b QLoRA 파인튜닝 | 완료 |
| 3 | 모델 병합 → GGUF 변환 → Ollama 등록 | 완료 |
| 4 | FastAPI 서빙 + MySQL DB 연동 | 완료 |
| 5 | OCR / 문서 분석 파이프라인 | 완료 |
| 6 | Next.js 챗봇 UI (도메인 선택, 다크 테마) | 완료 |
| 7 | MCP 서버 (국가법령 API, 판례 API) | 예정 |
| 8 | MCP ↔ FastAPI ↔ Next.js 통합 연동 | 예정 |

---

## 주의사항

- `.env` 파일은 절대 커밋하지 말 것 (`.gitignore` 등록 필요)
- `.venv/` 폴더는 수정하지 말 것 — 패키지는 `pip`으로만 관리
- CUDA 메모리 부족 시 `max_new_tokens`를 줄여서 실행 (기본값: 512)
- OCR 결과는 이미지 품질에 따라 정확도가 달라질 수 있음
- 모든 법률 답변은 **참고용**이며, 실제 법률 판단은 전문가에게 문의하세요
- AIHub 데이터는 비상업적 연구 목적으로만 사용 가능
