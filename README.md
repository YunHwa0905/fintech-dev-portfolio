# 폴리텍 포트폴리오 2026

![Python](https://img.shields.io/badge/Python-3776AB?style=flat-square&logo=python&logoColor=white)
![PyTorch](https://img.shields.io/badge/PyTorch-EE4C2C?style=flat-square&logo=pytorch&logoColor=white)
![HuggingFace](https://img.shields.io/badge/HuggingFace-FFD21E?style=flat-square&logo=huggingface&logoColor=black)
![FastAPI](https://img.shields.io/badge/FastAPI-009688?style=flat-square&logo=fastapi&logoColor=white)
![Next.js](https://img.shields.io/badge/Next.js-000000?style=flat-square&logo=nextdotjs&logoColor=white)
![Node.js](https://img.shields.io/badge/Node.js-339933?style=flat-square&logo=nodedotjs&logoColor=white)
![Express](https://img.shields.io/badge/Express-000000?style=flat-square&logo=express&logoColor=white)
![Java](https://img.shields.io/badge/Java_11-007396?style=flat-square&logo=openjdk&logoColor=white)
![Spring](https://img.shields.io/badge/Spring_5.3-6DB33F?style=flat-square&logo=spring&logoColor=white)
![MyBatis](https://img.shields.io/badge/MyBatis-DC382D?style=flat-square&logo=redis&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL_8-4479A1?style=flat-square&logo=mysql&logoColor=white)
![Jupyter](https://img.shields.io/badge/Jupyter-F37626?style=flat-square&logo=jupyter&logoColor=white)
![Pandas](https://img.shields.io/badge/Pandas-150458?style=flat-square&logo=pandas&logoColor=white)
![NumPy](https://img.shields.io/badge/NumPy-013243?style=flat-square&logo=numpy&logoColor=white)

---

한국폴리텍대학 스마트금융 과정(2026)의 학습 과정을 담은 포트폴리오입니다.  
**AI/ML 파인튜닝**, **데이터 분석(pandas)**, **Node.js 웹 개발**, **Java Spring 백엔드** 네 트랙으로 구성되며,  
메인 프로젝트는 Gemma3-4b를 법률 데이터로 파인튜닝한 **AI 법률 QA 챗봇**입니다.

---

## 프로젝트 구조

```
fintech-dev-portfolio/
│
├── 01.AI/                          # AI/ML 트랙
│   ├── fine_tuning/                # Gemma3-4b 파인튜닝 실습 노트북
│   │   ├── 01.data_preprocessing.ipynb
│   │   ├── 02. Fine_tune.ipynb
│   │   └── 03. Load_And_Save.ipynb
│   │
│   ├── gguf/                       # GGUF 변환 & Ollama 서빙 실습
│   │   ├── app.py                  # FastAPI + Ollama 서빙 서버
│   │   ├── hkcode_gguf.py          # GGUF 변환 스크립트
│   │   └── gguf_Load_Save.ipynb
│   │
│   └── vibe_claude/                # ★ 메인 프로젝트: AI 법률 QA 챗봇
│       ├── notebooks/              # 데이터 전처리 → 파인튜닝 → GGUF 전 과정
│       ├── src/
│       │   ├── preprocess/         # OCR 추출 + 텍스트 정규화
│       │   └── serving/            # FastAPI 서버 (port 8000)
│       ├── web/                    # Next.js 15 챗봇 프론트엔드 (port 3000)
│       └── requirements.txt
│
├── 02.Node.Js/                     # Node.js 웹 개발 트랙 (주차별 실습)
│   ├── 0305/ ~ 0507/               # Express 기초, EJS, jQuery, CRUD
│   ├── 0521/ ~ 0529/               # 중간고사 / REST API / 뉴스 게시판
│   └── 0604/ ~ 0611/               # Chart.js / 실시간 주가 / DB 연동
│
├── 03.Java/                        # Java/Spring 트랙
│   ├── Practice/                   # Java 기초 (배열, 정렬, OOP)
│   ├── score_sort/                 # 성적 정렬 알고리즘
│   ├── RPS/                        # 가위바위보 게임 (MVC 패턴)
│   ├── hkcode/                     # Spring IoC/DI (POJO → 어노테이션 → JDBC)
│   └── test/                       # Spring MVC + MyBatis (주식/유저 CRUD + 페이징)
│
├── 04.Git_branch/                  # Git 브랜치 전략 실습
│   ├── calc.py
│   └── util.py
│
└── 05.data/                        # 데이터 분석 트랙 (pandas/numpy)
    ├── codeset/                    # 실습 노트북
    │   ├── 01.data_analysis.ipynb  # 로드/타입변환/조회/정렬/그룹바이/이동평균/join
    │   └── 계절성지수.ipynb          # 판매 데이터 계절성지수 실습
    └── dataset/                    # 실습용 데이터셋 (용량 이슈로 Git 미추적)
```

---

## 학습 내용

### 01. AI/ML

| 단계 | 주제 | 핵심 기술 |
|---|---|---|
| 데이터 전처리 | AIHub 법률 JSON → 알파카 포맷 변환 | pandas, HuggingFace datasets |
| 파인튜닝 | Gemma3-4b QLoRA 4-bit 양자화 학습 | transformers, PEFT, BitsAndBytes |
| 모델 저장/로드 | HuggingFace Hub 업로드 및 추론 테스트 | transformers, torch |
| GGUF 변환 | llama.cpp 활용 GGUF 포맷 변환 | llama.cpp |
| 모델 서빙 | Ollama 등록 + FastAPI REST API 서버 구축 | Ollama, FastAPI, uvicorn |
| OCR 파이프라인 | PDF/이미지 텍스트 추출 + 법령 패턴 정규화 | Ollama Vision, PyMuPDF, re |
| 프론트엔드 | 법률 도메인 선택 · 문서 업로드 · 챗봇 UI | Next.js 15, Tailwind CSS v4 |

### 02. Node.js

| 주차 | 학습 내용 |
|---|---|
| 03/05 ~ 03/26 | Express 서버 구성, HTML 라우팅, EJS 템플릿, 폼 처리 |
| 04/02 ~ 04/16 | jQuery Ajax, 별점 UI, 배열 기반 CRUD, 라디오 필터 |
| 04/30 ~ 05/07 | 조건부 렌더링, 함수 모듈화, 숫자 포맷, 상품 목록 |
| 05/21 ~ 05/29 | 중간고사 / 뉴스 게시판 REST API (목록/작성/수정/삭제) |
| 06/04 ~ 06/11 | Chart.js 시각화, 실시간 주가 조회(네이버 금융 API), MySQL DB 연동 |

### 03. Java / Spring

| 프로젝트 | 학습 내용 |
|---|---|
| Practice | 변수, 조건문, 반복문, 배열, 2차원 배열, OOP(클래스/메서드), 패키지 |
| score_sort | 버블 정렬, 선택 정렬 구현 |
| RPS | MVC 패턴, 서비스 레이어 분리, 게임 로직 설계 |
| hkcode | Spring IoC/DI (POJO → XML Bean → 어노테이션), Spring JDBC, MySQL 연동 |
| test | Spring MVC, MyBatis Mapper/XML, 페이징(Criteria/PageMaker), REST API, JSTL |

### 05. 데이터 분석 (pandas/numpy)

| 주제 | 학습 내용 |
|---|---|
| 데이터 로드/조회 | CSV 불러오기, 행/컬럼 조회, 데이터 타입 변환 |
| 데이터 조작 | 조건부 컬럼 생성(DB CASE WHEN 방식), 컬럼간 연산 |
| 정렬/집계 | sort_values, groupby 집계 |
| 시계열 | 이동평균(rolling), YEAR/WEEK 파생 컬럼, 계절성지수 산출 |
| 병합 | merge/join을 통한 마스터 데이터 결합 |

---

## 실행 방법

### AI 법률 QA 챗봇 (`01.AI/vibe_claude`)

**1. Python 환경 설정**
```bash
cd 01.AI/vibe_claude
.venv\Scripts\activate          # Windows

pip install fastapi uvicorn httpx pymysql python-dotenv \
            transformers bitsandbytes datasets peft \
            pillow pymupdf
```

**2. 환경변수 설정**  
`.env` 파일을 생성하고 아래 값을 입력합니다.
```env
HF_TOKEN=hf_your_token
OLLAMA_BASE_URL=http://127.0.0.1:11434
OLLAMA_MODEL=law_gemma
DB_HOST=localhost
DB_PORT=3306
DB_NAME=legal_ai
DB_USER=root
DB_PASSWORD=your_password
API_PORT=8000
NEXT_PUBLIC_API_URL=http://localhost:8000
```

**3. Ollama 모델 준비**
```bash
ollama list                          # 등록된 모델 확인
ollama create law_gemma -f Modelfile # 없을 경우 GGUF에서 생성
```

**4. FastAPI 서버 실행**
```bash
uvicorn src.serving.main:app --reload --host 0.0.0.0 --port 8000
```

**5. Next.js 프론트엔드 실행**
```bash
cd web
npm install
npm run dev
# → http://localhost:3000
```

---

### Node.js 실습 서버 (`02.Node.Js/{날짜}`)

```bash
cd 02.Node.Js/0611
npm install
node app.js
# → http://localhost:80
```

> MySQL이 필요한 경우 `app.js` 상단의 DB 연결 정보를 로컬 환경에 맞게 수정합니다.

---

### Java Spring 프로젝트 (`03.Java/hkcode` or `03.Java/test`)

```bash
# Eclipse / STS에서 Import → Existing Maven Projects
# Run on Server (Tomcat 9) 또는 Maven 빌드:
mvn clean package
# target/{프로젝트명}.war → Tomcat webapps 배포
```

> `src/main/resources/`의 DB 설정 파일을 로컬 환경에 맞게 수정해야 합니다.  
> (`db.properties`, `datasource.properties` — **절대 커밋하지 말 것**)

---

### 데이터 분석 실습 (`05.data`)

```bash
cd 05.data
.venv\Scripts\activate      # Windows
pip install pandas numpy jupyter
jupyter notebook codeset/01.data_analysis.ipynb
```

> `dataset/` 폴더는 용량 문제로 `.gitignore` 처리되어 있어 Git에는 포함되지 않습니다.

---

## 개발 환경

| 구분 | 도구 / 버전 |
|---|---|
| OS | Windows 11 |
| Python | 3.10+ |
| GPU | NVIDIA RTX 2000 Ada (GGUF/QLoRA 학습) |
| Node.js | 18+ |
| Java | OpenJDK 11 |
| IDE | VS Code · Eclipse 2024 |
| DB | MySQL 8.0 |
| 모델 런타임 | Ollama |
| 빌드 도구 | Maven 3 |
| 버전 관리 | Git |
