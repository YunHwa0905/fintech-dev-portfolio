# -*- coding: utf-8 -*-
"""
MySQL DB 연동 모듈
- analysis_log 테이블에 분석 결과 저장
- 저장 항목: 파일명, 질문, 답변, 사용 모델, 도메인, 생성 시각
"""
import os
import logging
from datetime import datetime
from typing import Optional
from contextlib import contextmanager

import pymysql
from pymysql.cursors import DictCursor
from dotenv import load_dotenv

load_dotenv()
logger = logging.getLogger(__name__)

# ── DB 연결 설정 ──────────────────────────────────────────
DB_CONFIG = {
    "host":     os.getenv("DB_HOST",     "localhost"),
    "port":     int(os.getenv("DB_PORT", "3306")),
    "database": os.getenv("DB_NAME",     "legal_ai"),
    "user":     os.getenv("DB_USER",     "root"),
    "password": os.getenv("DB_PASSWORD", ""),
    "charset":  "utf8mb4",
    "cursorclass": DictCursor,
    "autocommit": True,
}

# ── CREATE TABLE DDL ──────────────────────────────────────
CREATE_TABLE_SQL = """
CREATE TABLE IF NOT EXISTS analysis_log (
    id          INT          AUTO_INCREMENT PRIMARY KEY,
    filename    VARCHAR(255) DEFAULT NULL         COMMENT '업로드 파일명 (없으면 NULL)',
    question    TEXT         NOT NULL             COMMENT '사용자 질문',
    answer      LONGTEXT     NOT NULL             COMMENT '모델 답변',
    model       VARCHAR(100) NOT NULL             COMMENT '사용 모델명',
    domain      VARCHAR(50)  DEFAULT NULL         COMMENT '법률 도메인 (민사법 등)',
    created_at  DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '생성 시각'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
"""


# ── 연결 컨텍스트 매니저 ──────────────────────────────────
@contextmanager
def get_connection():
    conn = pymysql.connect(**DB_CONFIG)
    try:
        yield conn
    except Exception as e:
        conn.rollback()
        raise e
    finally:
        conn.close()


# ── DB 및 테이블 초기화 ───────────────────────────────────
def init_db():
    """
    DB가 없으면 생성, 테이블이 없으면 생성.
    서버 시작 시 1회 호출.
    """
    # 1) DB 자체 생성 (없을 수도 있으므로 database 없이 먼저 접속)
    base_cfg = {k: v for k, v in DB_CONFIG.items()
                if k not in ("database", "cursorclass")}
    base_cfg["cursorclass"] = DictCursor

    try:
        conn = pymysql.connect(**base_cfg)
        with conn.cursor() as cur:
            cur.execute(
                f"CREATE DATABASE IF NOT EXISTS `{DB_CONFIG['database']}` "
                f"CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;"
            )
        conn.close()
        logger.info("DB '%s' 준비 완료", DB_CONFIG["database"])
    except Exception as e:
        logger.error("DB 생성 실패: %s", e)
        raise

    # 2) 테이블 생성
    with get_connection() as conn:
        with conn.cursor() as cur:
            cur.execute(CREATE_TABLE_SQL)
    logger.info("analysis_log 테이블 준비 완료")


# ── 분석 결과 저장 ────────────────────────────────────────
def save_analysis(
    question: str,
    answer:   str,
    model:    str,
    filename: Optional[str] = None,
    domain:   Optional[str] = None,
) -> int:
    """
    분석 결과를 analysis_log 테이블에 저장.
    Returns: 삽입된 row의 id
    """
    sql = """
        INSERT INTO analysis_log (filename, question, answer, model, domain)
        VALUES (%s, %s, %s, %s, %s)
    """
    with get_connection() as conn:
        with conn.cursor() as cur:
            cur.execute(sql, (filename, question, answer, model, domain))
            inserted_id = cur.lastrowid

    logger.info("저장 완료 id=%d | model=%s | domain=%s", inserted_id, model, domain)
    return inserted_id


# ── 히스토리 조회 ─────────────────────────────────────────
def get_history(limit: int = 50, domain: Optional[str] = None) -> list[dict]:
    """
    최근 분석 기록 조회.
    - limit: 최대 반환 건수
    - domain: 특정 도메인 필터 (None이면 전체)
    """
    if domain:
        sql = """
            SELECT id, filename, question, answer, model, domain, created_at
            FROM analysis_log
            WHERE domain = %s
            ORDER BY created_at DESC
            LIMIT %s
        """
        params = (domain, limit)
    else:
        sql = """
            SELECT id, filename, question, answer, model, domain, created_at
            FROM analysis_log
            ORDER BY created_at DESC
            LIMIT %s
        """
        params = (limit,)

    with get_connection() as conn:
        with conn.cursor() as cur:
            cur.execute(sql, params)
            rows = cur.fetchall()

    # datetime → 문자열 변환 (JSON 직렬화용)
    for row in rows:
        if isinstance(row.get("created_at"), datetime):
            row["created_at"] = row["created_at"].strftime("%Y-%m-%d %H:%M:%S")

    return rows


# ── 단건 조회 ─────────────────────────────────────────────
def get_analysis_by_id(log_id: int) -> Optional[dict]:
    sql = "SELECT * FROM analysis_log WHERE id = %s"
    with get_connection() as conn:
        with conn.cursor() as cur:
            cur.execute(sql, (log_id,))
            row = cur.fetchone()
    if row and isinstance(row.get("created_at"), datetime):
        row["created_at"] = row["created_at"].strftime("%Y-%m-%d %H:%M:%S")
    return row
