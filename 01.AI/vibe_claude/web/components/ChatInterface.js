"use client";
import React, { useState, useRef, useEffect, useCallback } from 'react';
import {
  Send, Paperclip, Loader2, X, Scale, User, Bot,
  Plus, MessageSquare, TriangleAlert, ChevronRight,
  FileText, BookOpen, Gavel
} from 'lucide-react';
import axios from 'axios';

const WELCOME = {
  role: 'bot',
  text: '안녕하세요! **AI 법률 분석 비서**입니다.\n\n다음과 같은 도움을 드릴 수 있습니다:\n• 법률 질문 답변\n• 법률 문서 / 이미지 분석\n• 법령 조문 및 판례 검색\n\n궁금한 점을 자유롭게 질문하거나 분석이 필요한 문서를 업로드해 주세요.',
};

const RECENT = ['계약서 해지 손해배상 문의', '임대차 보증금 반환 분쟁', '형사 고소장 작성 방법'];

function TypingDots() {
  return (
    <div className="dot-bounce flex items-center gap-1 py-0.5">
      <span className="w-2 h-2 bg-indigo-400 rounded-full" />
      <span className="w-2 h-2 bg-indigo-400 rounded-full" />
      <span className="w-2 h-2 bg-indigo-400 rounded-full" />
    </div>
  );
}

function BotAvatar() {
  return (
    <div className="w-8 h-8 rounded-full bg-indigo-900/60 border border-indigo-700/50 flex items-center justify-center shrink-0 mt-0.5">
      <Scale size={15} className="text-indigo-400" />
    </div>
  );
}

function UserAvatar() {
  return (
    <div className="w-8 h-8 rounded-full bg-slate-700 border border-slate-600 flex items-center justify-center shrink-0 mt-0.5">
      <User size={15} className="text-slate-300" />
    </div>
  );
}

export default function ChatInterface() {
  const [messages, setMessages] = useState([WELCOME]);
  const [input, setInput] = useState('');
  const [file, setFile] = useState(null);
  const [filePreview, setFilePreview] = useState(null);
  const [loading, setLoading] = useState(false);
  const fileInputRef = useRef(null);
  const scrollRef = useRef(null);
  const textareaRef = useRef(null);

  useEffect(() => {
    if (scrollRef.current) {
      scrollRef.current.scrollTop = scrollRef.current.scrollHeight;
    }
  }, [messages, loading]);

  // Auto-resize textarea
  useEffect(() => {
    const el = textareaRef.current;
    if (!el) return;
    el.style.height = 'auto';
    el.style.height = Math.min(el.scrollHeight, 128) + 'px';
  }, [input]);

  const handleSend = useCallback(async () => {
    if ((!input.trim() && !file) || loading) return;

    const userText = input.trim();
    setMessages(prev => [...prev, {
      role: 'user',
      text: userText || '📄 문서 분석을 요청합니다.',
    }]);
    setInput('');
    setLoading(true);

    const formData = new FormData();
    if (file) formData.append('file', file);
    formData.append('question', userText);

    try {
      const endpoint = file ? '/analyze-document' : '/ask-chatbot';
      const payload = file ? formData : { question: userText };
      const res = await axios.post(`http://localhost:8000${endpoint}`, payload);
      setMessages(prev => [...prev, {
        role: 'bot',
        text: file
          ? `📋 **문서 분석 결과**\n\n${res.data.ocr_text}`
          : res.data.answer,
      }]);
    } catch {
      setMessages(prev => [...prev, {
        role: 'bot',
        text: '⚠️ 오류가 발생했습니다.\n서버가 실행 중인지 확인해 주세요.',
      }]);
    } finally {
      setFile(null);
      setFilePreview(null);
      setLoading(false);
    }
  }, [input, file, loading]);

  const handleFileChange = (e) => {
    const f = e.target.files[0];
    if (!f) return;
    setFile(f);
    const reader = new FileReader();
    reader.onloadend = () => setFilePreview(reader.result);
    reader.readAsDataURL(f);
    e.target.value = '';
  };

  const handleKeyDown = (e) => {
    if (e.key === 'Enter' && !e.shiftKey) {
      e.preventDefault();
      handleSend();
    }
  };

  const resetChat = () => {
    setMessages([WELCOME]);
    setInput('');
    setFile(null);
    setFilePreview(null);
  };

  return (
    <div className="flex h-screen bg-[#0b0f1a] text-slate-100 overflow-hidden">

      {/* ── Sidebar ── */}
      <aside className="w-60 flex flex-col bg-[#0d1220] border-r border-slate-800/70 shrink-0">

        {/* Logo */}
        <div className="px-4 py-5 border-b border-slate-800/70">
          <div className="flex items-center gap-3">
            <div className="w-9 h-9 rounded-xl bg-indigo-600 flex items-center justify-center shadow-lg shadow-indigo-600/30">
              <Scale size={19} className="text-white" />
            </div>
            <div className="leading-tight">
              <p className="text-sm font-bold text-white tracking-tight">LegalAI</p>
              <p className="text-[10px] text-slate-500 mt-0.5">법률 분석 비서</p>
            </div>
          </div>
        </div>

        {/* New Chat */}
        <div className="px-3 pt-3 pb-1">
          <button
            onClick={resetChat}
            className="w-full flex items-center gap-2 px-3 py-2.5 rounded-xl text-sm text-slate-400 hover:text-white hover:bg-slate-800/80 transition-all group"
          >
            <Plus size={15} className="text-slate-600 group-hover:text-indigo-400 transition-colors shrink-0" />
            새 대화 시작
          </button>
        </div>

        {/* Recent chats */}
        <div className="flex-1 overflow-y-auto px-3 pb-3 custom-scroll">
          <p className="text-[10px] font-semibold text-slate-700 uppercase tracking-widest px-2 py-3">
            최근 대화
          </p>
          <div className="space-y-0.5">
            {RECENT.map((item, i) => (
              <button
                key={i}
                className="w-full text-left flex items-center gap-2.5 px-3 py-2 rounded-lg text-xs text-slate-500 hover:text-slate-200 hover:bg-slate-800/60 transition-all group"
              >
                <MessageSquare size={12} className="shrink-0 text-slate-700 group-hover:text-indigo-500 transition-colors" />
                <span className="truncate">{item}</span>
              </button>
            ))}
          </div>
        </div>

        {/* Quick links */}
        <div className="px-3 pb-2 space-y-0.5 border-t border-slate-800/70 pt-2">
          {[
            { icon: BookOpen, label: '법령 검색' },
            { icon: Gavel, label: '판례 조회' },
            { icon: FileText, label: '문서 분석' },
          ].map(({ icon: Icon, label }) => (
            <button
              key={label}
              className="w-full flex items-center gap-2.5 px-3 py-2 rounded-lg text-xs text-slate-600 hover:text-slate-300 hover:bg-slate-800/50 transition-all"
            >
              <Icon size={13} className="shrink-0" />
              {label}
            </button>
          ))}
        </div>

        {/* Model info */}
        <div className="px-3 pb-4 pt-1">
          <div className="flex items-center gap-2.5 px-3 py-2.5 rounded-xl bg-slate-800/50 border border-slate-700/50">
            <div className="w-2 h-2 bg-emerald-500 rounded-full animate-pulse shrink-0" />
            <div className="min-w-0 flex-1">
              <p className="text-[11px] text-slate-300 font-medium truncate">Gemma3-4b-Legal</p>
              <p className="text-[10px] text-slate-600">QLoRA 파인튜닝</p>
            </div>
          </div>
        </div>
      </aside>

      {/* ── Main ── */}
      <div className="flex-1 flex flex-col min-w-0">

        {/* Header */}
        <header className="h-14 border-b border-slate-800/70 flex items-center justify-between px-6 bg-[#0b0f1a]/80 backdrop-blur-md shrink-0">
          <div className="flex items-center gap-3">
            <div className="w-8 h-8 rounded-lg bg-indigo-900/50 border border-indigo-700/40 flex items-center justify-center">
              <Bot size={16} className="text-indigo-400" />
            </div>
            <div className="leading-tight">
              <p className="text-sm font-semibold text-slate-100">AI 법률 분석 비서</p>
              <p className="text-[10px] text-slate-600">법률 질문 · 문서 분석 · 판례 검색</p>
            </div>
          </div>
          <div className="flex items-center gap-2 px-3 py-1.5 rounded-full bg-slate-800/80 border border-slate-700/60">
            <span className="w-1.5 h-1.5 bg-emerald-500 rounded-full animate-pulse" />
            <span className="text-[11px] text-slate-400 font-medium">Online</span>
          </div>
        </header>

        {/* Messages */}
        <div
          ref={scrollRef}
          className="flex-1 overflow-y-auto px-6 py-6 space-y-5 custom-scroll"
        >
          {messages.map((m, i) => (
            <div
              key={i}
              className={`flex gap-3 msg-animate ${m.role === 'user' ? 'justify-end' : 'justify-start'}`}
            >
              {m.role === 'bot' && <BotAvatar />}
              <div
                className={`max-w-[75%] text-sm leading-relaxed whitespace-pre-wrap rounded-2xl px-4 py-3 ${
                  m.role === 'user'
                    ? 'bg-indigo-600 text-white rounded-br-sm shadow-lg shadow-indigo-900/40'
                    : 'bg-slate-800/80 text-slate-200 rounded-bl-sm border border-slate-700/60'
                }`}
              >
                {m.text}
              </div>
              {m.role === 'user' && <UserAvatar />}
            </div>
          ))}

          {/* Loading bubble */}
          {loading && (
            <div className="flex gap-3 justify-start msg-animate">
              <BotAvatar />
              <div className="bg-slate-800/80 border border-slate-700/60 rounded-2xl rounded-bl-sm px-5 py-3.5 flex items-center gap-3">
                <TypingDots />
                <span className="text-xs text-slate-500">분석 중...</span>
              </div>
            </div>
          )}
        </div>

        {/* ── Input area ── */}
        <div className="px-4 pb-4 pt-3 border-t border-slate-800/70 bg-[#0b0f1a]/60 backdrop-blur-sm shrink-0">

          {/* File preview */}
          {filePreview && (
            <div className="mb-3 inline-flex items-center gap-2.5 px-3 py-2 bg-slate-800/80 rounded-xl border border-slate-700/60">
              <img
                src={filePreview}
                alt="preview"
                className="h-9 w-9 object-cover rounded-lg border border-slate-600"
              />
              <div className="min-w-0">
                <p className="text-xs text-slate-300 font-medium truncate max-w-[140px]">{file?.name}</p>
                <p className="text-[10px] text-slate-600 mt-0.5">
                  {(file?.size / 1024).toFixed(0)} KB
                </p>
              </div>
              <button
                onClick={() => { setFile(null); setFilePreview(null); }}
                className="ml-1 p-1 rounded-lg text-slate-600 hover:text-slate-300 hover:bg-slate-700 transition-colors"
              >
                <X size={14} />
              </button>
            </div>
          )}

          {/* Input box */}
          <div className="flex items-end gap-2 bg-slate-800/70 rounded-2xl px-2 py-2 border border-slate-700/60 focus-within:border-indigo-600/60 focus-within:bg-slate-800 transition-all">
            <button
              onClick={() => fileInputRef.current?.click()}
              title="파일 첨부 (이미지, PDF)"
              className="p-2 text-slate-600 hover:text-indigo-400 hover:bg-slate-700/70 rounded-xl transition-all shrink-0"
            >
              <Paperclip size={19} />
            </button>
            <input
              type="file"
              ref={fileInputRef}
              onChange={handleFileChange}
              className="hidden"
              accept="image/*,.pdf"
            />
            <textarea
              ref={textareaRef}
              value={input}
              onChange={(e) => setInput(e.target.value)}
              onKeyDown={handleKeyDown}
              placeholder={
                file
                  ? '이미지에 대해 무엇이 궁금하신가요?'
                  : '법률 질문을 입력하세요... (Enter: 전송 / Shift+Enter: 줄바꿈)'
              }
              rows={1}
              className="flex-1 bg-transparent text-slate-200 text-sm placeholder-slate-600 resize-none focus:outline-none py-1.5 leading-relaxed"
              style={{ minHeight: '28px', maxHeight: '128px' }}
            />
            <button
              onClick={handleSend}
              disabled={loading || (!input.trim() && !file)}
              className="p-2.5 bg-indigo-600 hover:bg-indigo-500 active:bg-indigo-700 disabled:bg-slate-700/80 disabled:text-slate-600 text-white rounded-xl transition-all shrink-0 shadow-md shadow-indigo-900/40 disabled:shadow-none"
            >
              {loading
                ? <Loader2 size={18} className="animate-spin" />
                : <Send size={18} />
              }
            </button>
          </div>

          {/* Disclaimer */}
          <div className="flex items-center justify-center gap-1.5 mt-2">
            <TriangleAlert size={10} className="text-slate-700" />
            <p className="text-[10px] text-slate-700">
              AI 분석 결과는 법적 효력이 없으며 전문 법률 상담을 대체하지 않습니다.
            </p>
          </div>
        </div>
      </div>
    </div>
  );
}
