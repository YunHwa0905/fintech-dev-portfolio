<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>주식 목록</title>
<style>
  @import url('https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@400;700;900&display=swap');

  * { box-sizing: border-box; margin: 0; padding: 0; }

  body {
    font-family: 'Noto Sans KR', sans-serif;
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    min-height: 100vh;
    padding: 30px;
  }

  .container {
    max-width: 1100px;
    margin: 0 auto;
  }

  .header {
    text-align: center;
    margin-bottom: 24px;
  }

  .header h2 {
    font-size: 32px;
    font-weight: 900;
    color: #ffffff;
    text-shadow: 0 2px 10px rgba(0,0,0,0.2);
    letter-spacing: -0.5px;
  }

  .header .subtitle {
    font-size: 13px;
    color: rgba(255,255,255,0.75);
    margin-top: 6px;
  }

  .badge {
    display: inline-block;
    background: #ff6b6b;
    color: white;
    font-size: 11px;
    font-weight: 700;
    padding: 3px 10px;
    border-radius: 20px;
    margin-left: 8px;
    animation: pulse 1.5s infinite;
    vertical-align: middle;
  }

  @keyframes pulse {
    0%, 100% { opacity: 1; }
    50% { opacity: 0.5; }
  }

  .table-wrapper {
    overflow-x: auto;
    border-radius: 16px;
    background: #ffffff;
    box-shadow: 0 20px 60px rgba(0,0,0,0.25);
  }

  table {
    width: 100%;
    border-collapse: collapse;
    font-size: 14px;
  }

  thead tr {
    background: linear-gradient(90deg, #667eea, #764ba2);
  }

  th {
    padding: 14px 18px;
    text-align: right;
    color: #ffffff;
    font-weight: 700;
    font-size: 13px;
    letter-spacing: 0.3px;
    white-space: nowrap;
  }

  th:first-child, td:first-child { text-align: left; }
  th:nth-child(2), td:nth-child(2) { text-align: center; }

  tbody tr {
    border-bottom: 1px solid #f3f4f6;
    transition: all 0.2s;
  }

  tbody tr:last-child { border-bottom: none; }

  tbody tr:hover {
    background: linear-gradient(90deg, #f0f4ff, #fdf0ff);
    transform: scale(1.002);
  }

  tbody tr:nth-child(even) {
    background-color: #fafbff;
  }

  tbody tr:nth-child(even):hover {
    background: linear-gradient(90deg, #f0f4ff, #fdf0ff);
  }

  td {
    padding: 12px 18px;
    text-align: right;
    white-space: nowrap;
  }

  .ticker {
    font-weight: 900;
    font-size: 14px;
    color: #ffffff;
    background: linear-gradient(135deg, #667eea, #764ba2);
    padding: 4px 10px;
    border-radius: 8px;
    display: inline-block;
  }

  .datetime {
    color: #9ca3af;
    font-size: 12px;
    font-weight: 400;
  }

  .price {
    color: #1f2937;
    font-weight: 600;
  }

  .volume {
    color: #3b82f6;
    font-weight: 600;
  }

  .amount {
    color: #10b981;
    font-weight: 700;
  }

  .empty {
    text-align: center;
    padding: 60px;
    color: #9ca3af;
    font-size: 16px;
  }

  .footer {
    text-align: center;
    margin-top: 16px;
    color: rgba(255,255,255,0.6);
    font-size: 12px;
  }
</style>
</head>
<body>
  <div class="container">

    <div class="header">
      <h2>📈 실시간 주식 현황 <span class="badge">LIVE</span></h2>
      <p class="subtitle">최신 시간대 기준 전종목 시세</p>
    </div>

    <div class="table-wrapper">
      <table>
        <thead>
          <tr>
            <th>종목코드</th>
            <th>기준시간</th>
            <th>시가</th>
            <th>고가</th>
            <th>저가</th>
            <th>종가</th>
            <th>거래량</th>
            <th>거래대금</th>
          </tr>
        </thead>
        <tbody>
          <c:choose>
            <c:when test="${empty stockList}">
              <tr><td colspan="8" class="empty">😅 데이터가 없습니다.</td></tr>
            </c:when>
            <c:otherwise>
              <c:forEach var="stock" items="${stockList}">
                <tr>
                  <td><span class="ticker">${stock.ticker}</span></td>
                  <td class="datetime">${stock.datetime}</td>
                  <td class="price"><fmt:formatNumber value="${stock.open}" pattern="#,###"/></td>
                  <td class="price"><fmt:formatNumber value="${stock.high}" pattern="#,###"/></td>
                  <td class="price"><fmt:formatNumber value="${stock.low}" pattern="#,###"/></td>
                  <td class="price"><fmt:formatNumber value="${stock.close}" pattern="#,###"/></td>
                  <td class="volume"><fmt:formatNumber value="${stock.volume}" pattern="#,###"/></td>
                  <td class="amount"><fmt:formatNumber value="${stock.amount}" pattern="#,###"/></td>
                </tr>
              </c:forEach>
            </c:otherwise>
          </c:choose>
        </tbody>
      </table>
    </div>

    <p class="footer">📊 데이터는 매시간 자동 수집됩니다</p>

  </div>
</body>
</html>