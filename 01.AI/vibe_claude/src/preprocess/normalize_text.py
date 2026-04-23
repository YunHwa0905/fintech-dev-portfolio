import re

def basic_normalize(text: str) -> str:
    """기계적인 노이즈 제거 및 기본 가독성 확보"""
    # 1. 과도한 공백 및 줄바꿈 정리
    text = re.sub(r'\s+', ' ', text).strip()
    
    # 2. 법령 특유의 패턴(제N조) 가독성 처리
    text = re.sub(r'\[?\s?(제\s?\d+\s?조)\s?\]?', r'\n[\1] ', text)
    
    # 3. 괄호 안팎 공백 정리
    text = re.sub(r'\s?\(\s?', ' (', text)
    text = re.sub(r'\s?\)\s?', ') ', text)
    
    return text.strip()