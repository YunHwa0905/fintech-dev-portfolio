# GEMINI.md (LLM 모델서빙전용)
## 대화및언어설정
-**언어:**모든답변은반드시**한국어**로작성해줘.
-**톤:**친절하고전문적인어조로설명해줘.
-**코드설명:**코드는영문으로작성하되, 주석과로직설명은한국어로상세하게달아줘.
## 2. 환경기준
-Python 3.12.10
## 3. 프로젝트구조
/codeset
├── 코드파일명.py
├── 코드파일명.ipynb
├── dataset/
│ └── 데이터셋
└── CLAUD.md(GEMINI.md)
## 4. 핵심아키텍처
-데이터준비(ALPACA포맷)
-모델로딩: 서버시작시1회수행
-추론요청: API 호출시수행
-Stateless 구조유지
## 5. CORS 설정(필수)
-모든외부요청허용
설정기준:
-allow_origins: ["*"]
-allow_methods: ["GET", "POST", "PUT", "DELETE"]
-allow_credentials: True
-allow_headers: ["*"]
## 6. 에러처리
-try-except 사용
-오류발생시JSON 반환
형식:
-success: false
-message: error 내용
## 7. 기본코딩스타일
-파이썬코드초반에는패키지설치하는부분꼭추가해주고
-변수명: camelCase (카멜케이스)
-LIST 안에반복문이나IF문쓰지말고
-반복문: for iin range(0, len..)
-조건문:Lif
-함수: def 함수명( 파라미터): 단""" 함수설명