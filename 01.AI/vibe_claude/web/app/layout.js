import './globals.css'

export const metadata = {
  title: 'LegalAI — AI 법률 분석 비서',
  description: '법률 문서 분석, 법령 검색, 판례 조회를 AI로 빠르게',
}

export default function RootLayout({ children }) {
  return (
    <html lang="ko">
      <body className="antialiased h-full">{children}</body>
    </html>
  )
}
