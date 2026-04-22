# CLAUDE.md — 법률 QA 챗봇 프로젝트

## 프로젝트 개요

AIHub 법률 데이터셋을 알파카 포맷으로 변환하고, Gemma3-4b 모델을 파인튜닝하여
법률 질문에 답변하는 챗봇 서비스를 구축하는 프로젝트.

**목표 스택**: 데이터 전처리 → 파인튜닝 → FastAPI 서빙 → Next.js 챗봇 UI → MCP 연동

---

## 기술 스택

| 레이어 | 기술 |
|---|---|
| 데이터 전처리 | Python, pandas, HuggingFace datasets |
| 파인튜닝 | Gemma3-4b, QLoRA, BitsAndBytes (4bit) |
| 모델 서빙 | FastAPI, uvicorn |
| 프론트엔드 | Next.js 15, TypeScript, Tailwind CSS |
| MCP 도구 | 국가법령정보 Open API, 대법원 판례 API |
| 환경 | Python venv (.venv), .env 환경변수 관리 |

---

## 프로젝트 구조 (목표)

```
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
│   │   └── alpaca_legal.json
│   └── sample/                   # 검증용 샘플
│
├── notebooks/
│   └── 01_data_preprocessing.ipynb   # 기존 전처리 노트북 (확장)
│
├── src/
│   ├── preprocess/
│   │   └── to_alpaca.py          # 알파카 변환 스크립트
│   ├── training/
│   │   └── finetune.py           # Gemma3-4b QLoRA 파인튜닝
│   └── serving/
│       ├── main.py               # FastAPI 앱 엔트리포인트
│       ├── model.py              # 모델 로드 및 추론
│       └── schemas.py            # Pydantic 요청/응답 스키마
│
├── mcp/
│   ├── law_search_server.py      # 국가법령정보 MCP 서버
│   └── precedent_server.py       # 대법원 판례 MCP 서버
│
├── web/                          # Next.js 챗봇 프론트엔드
│   ├── app/
│   │   ├── page.tsx              # 메인 챗봇 페이지
│   │   └── api/
│   │       └── chat/
│   │           └── route.ts      # FastAPI 프록시 라우트
│   ├── components/
│   │   ├── ChatWindow.tsx
│   │   ├── MessageBubble.tsx
│   │   └── LawReference.tsx      # 판례/법령 인용 카드
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
HF_DATASET_REPO=yunhwa/legal_qa
HF_MODEL_REPO=yunhwa/gemma3-4b-legal

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

## FastAPI 서빙

### 엔드포인트

```
POST /chat
  Body: { "question": "...", "domain": "민법" }
  Response: { "answer": "...", "references": [...] }

GET  /health
  Response: { "status": "ok", "model": "gemma3-4b-legal" }
```

### 모델 설정 (RTX 2000 Ada 기준)

```python
# BitsAndBytesConfig 4bit 양자화
load_in_4bit=True
bnb_4bit_compute_dtype=torch.float16
bnb_4bit_use_double_quant=True
```

---

## MCP 서버

### 1. 국가법령정보 MCP (`law_search_server.py`)

```
도구명: search_law
입력: { "query": "손해배상", "law_name": "민법" }
출력: { "article": "제390조", "content": "..." }
API: https://www.law.go.kr/DRF/lawSearch.do
```

### 2. 대법원 판례 MCP (`precedent_server.py`)

```
도구명: search_precedent
입력: { "query": "계약 해지 손해배상" }
출력: { "case_no": "2023다12345", "summary": "..." }
```

### Claude Code에서 MCP 등록 방법

```bash
claude mcp add law-search python mcp/law_search_server.py
claude mcp add precedent python mcp/precedent_server.py
```

---

## Next.js 챗봇 UI

### 핵심 컴포넌트

- `ChatWindow`: 메시지 히스토리 + 입력창
- `MessageBubble`: 사용자/AI 말풍선 구분
- `LawReference`: 관련 법령/판례 인용 카드 (MCP 결과 렌더링)

### API 연동 흐름

```
사용자 입력
  → Next.js /api/chat (route.ts)
  → FastAPI POST /chat
  → Gemma3-4b 추론 + MCP 법령 검색
  → 응답 스트리밍 반환
```

---

## 개발 명령어

```bash
# 가상환경 활성화
.venv\Scripts\activate          # Windows
source .venv/bin/activate       # Mac/Linux

# 패키지 설치
pip install fastapi uvicorn transformers bitsandbytes datasets peft

# FastAPI 실행
uvicorn src.serving.main:app --reload --host 0.0.0.0 --port 8000

# Next.js 실행
cd web && npm run dev

# MCP 서버 테스트
python mcp/law_search_server.py
```

---

## 개발 우선순위

```
Phase 1  데이터 전처리 → 알파카 변환 → HF 업로드
Phase 2  Gemma3-4b QLoRA 파인튜닝
Phase 3  FastAPI 서빙 구축
Phase 4  MCP 서버 (국가법령 API) 제작
Phase 5  Next.js 챗봇 UI 개발
Phase 6  MCP ↔ Next.js ↔ FastAPI 통합 연동
```

---

## 주의사항

- `.venv/` 폴더는 수정하지 말 것, 패키지는 pip으로만 관리
- `.env` 파일은 절대 커밋하지 말 것 (`.gitignore`에 추가)
- 모델 추론 시 CUDA 메모리 부족 시 `max_new_tokens` 줄이기 (기본값: 512)
- AIHub 데이터는 비상업적 연구 목적으로만 사용
- 법률 답변은 "참고용"임을 UI에서 반드시 명시할 것