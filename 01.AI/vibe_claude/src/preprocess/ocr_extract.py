# -*- coding: utf-8 -*-
import os
import base64
from io import BytesIO
from PIL import Image
import ollama
from pdf2image import convert_from_path

# 설정 (CLAUDE.md 환경변수 기준)
OCR_MODEL = "llama3.2-vision:11b"

def encode_image_to_base64(image):
    """PIL 이미지를 base64 문자열로 변환합니다."""
    buffered = BytesIO()
    image.save(buffered, format="JPEG")
    return base64.b64encode(buffered.getvalue()).decode('utf-8')

def extract_text_from_image(image_path):
    """이미지 파일에서 텍스트를 추출합니다."""
    try:
        response = ollama.chat(
            model=OCR_MODEL,
            messages=[{
                'role': 'user',
                'content': '이 문서에 포함된 모든 텍스트를 읽어서 그대로 출력해줘. 표나 조문 번호는 형식을 유지해줘.',
                'images': [image_path]
            }]
        )
        return response['message']['content']
    except Exception as e:
        return f"OCR 에러: {str(e)}"

def process_pdf(pdf_path):
    """PDF의 각 페이지를 이미지로 변환하여 OCR을 수행합니다."""
    images = convert_from_path(pdf_path)
    full_text = []
    
    for i, image in enumerate(images):
        # 임시 이미지 저장 후 분석
        temp_path = f"temp_page_{i}.jpg"
        image.save(temp_path, "JPEG")
        text = extract_text_from_image(temp_path)
        full_text.append(f"--- Page {i+1} ---\n{text}")
        os.remove(temp_path) # 임시 파일 삭제
        
    return "\n".join(full_text)

if __name__ == "__main__":
    # 테스트 실행
    test_file = "test_document.jpg" # 실제 파일 경로로 수정 필요
    if os.path.exists(test_file):
        if test_file.lower().endswith('.pdf'):
            result = process_pdf(test_file)
        else:
            result = extract_text_from_image(test_file)
        print(result)