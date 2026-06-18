#!/usr/bin/env python
# coding: utf-8

# In[1]:


import os

import httpx
import uvicorn
from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
from pydantic import BaseModel

OLLAMA_BASE = os.environ.get("OLLAMA_HOST", "http://127.0.0.1:11434").rstrip("/")
OLLAMA_MODEL = os.environ.get("OLLAMA_MODEL", "gemma3:4b")
DEFAULT_SYSTEM_MESSAGE = "당신은 문제를 정확하게 답변하는 AI입니다."


# In[2]:


async def generate_gemma_answer(
    user_message: str,
    system_message: str = DEFAULT_SYSTEM_MESSAGE,
    max_new_tokens: int = 512,
    temperature: float = 0.2,
    top_p: float = 0.95,
    top_k: int = 50,
) -> str:
    messages = []
    if system_message:
        messages.append({"role": "system", "content": system_message})
    messages.append({"role": "user", "content": user_message})

    payload = {
        "model": OLLAMA_MODEL,
        "messages": messages,
        "stream": False,
        "options": {
            "temperature": temperature,
            "top_p": top_p,
            "top_k": top_k,
            "num_predict": max_new_tokens,
        },
    }
    async with httpx.AsyncClient(timeout=600.0) as client:
        r = await client.post(f"{OLLAMA_BASE}/api/chat", json=payload)
        r.raise_for_status()
        data = r.json()
    content = (data.get("message") or {}).get("content") or ""
    return content.strip()


app = FastAPI(title="ML API")


# In[3]:


app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["GET", "POST", "PUT", "DELETE"],
    allow_headers=["*"],
)


class InDataset(BaseModel):
    question: str


@app.post("/predict", status_code=200)
async def predict_tf(x: InDataset):
    response = await generate_gemma_answer(x.question, DEFAULT_SYSTEM_MESSAGE)
    return {"prediction": response}


@app.get("/")
async def root():
    return {"message": "online"}


# In[ ]:


#import nest_asyncio
#nest_asyncio.apply()
host = os.environ.get("HOST", "0.0.0.0")
port = int(os.environ.get("PORT", "9999"))
uvicorn.run(app, host=host, port=port, log_level="info")


# In[ ]:




