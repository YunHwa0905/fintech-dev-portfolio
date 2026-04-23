# -*- coding: utf-8 -*-
import pytesseract
from PIL import Image, ImageOps, ImageFilter
from pdf2image import convert_from_path
import os

# Tesseract 설치 경로 (본인의 설치 경로 확인 필수!)
pytesseract.pytesseract.tesseract_cmd = r'C:\Program Files\Tesseract-OCR\tesseract.exe'

def extract_text_from_image(image_path):
    try:
        image = Image.open(image_path)
        
        # 1. 전처리: 대비(Contrast)를 대폭 높여 글자를 선명하게 만듦
        from PIL import ImageEnhance
        enhancer = ImageEnhance.Contrast(image)
        image = enhancer.enhance(2.0) # 대비 2배 증폭
        image = image.convert('L')    # 흑백 변환
        
        # 2. Tesseract 옵션 세밀화
        # lang='kor'만 지정하여 영문 혼선을 방지 (법령은 한글 위주이므로)
        # --psm 6: 단일 텍스트 블록으로 간주하여 줄단위 인식을 강화
        text = pytesseract.image_to_string(
            image, 
            lang='kor', 
            config='--oem 3 --psm 3'
        )
        return text.strip()
    except Exception as e:
        return f"Tesseract OCR 에러: {str(e)}"

def process_pdf(pdf_path):
    """PDF의 각 페이지를 이미지로 변환하여 OCR 수행"""
    images = convert_from_path(pdf_path)
    full_text = []
    
    for i, image in enumerate(images):
        temp_path = f"temp_page_{i}.jpg"
        image.save(temp_path, "JPEG")
        text = extract_text_from_image(temp_path)
        full_text.append(f"--- Page {i+1} ---\n{text}")
        if os.path.exists(temp_path):
            os.remove(temp_path)
            
    return "\n".join(full_text)