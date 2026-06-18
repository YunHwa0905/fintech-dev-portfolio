<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<!DOCTYPE html>
<html>
<head>
<title>가비아 DB 조회 결과</title>
<style>
    body { font-family: sans-serif; text-align: center; padding-top: 50px; }
    .box { border: 2px solid #007bff; display: inline-block; padding: 20px; border-radius: 10px; }
    h2 { color: #007bff; }
</style>
</head>
<body>
    <div class="box">
        <h2>지역별 최대 판매량 조회</h2>
        <hr>
        <p><strong>결과:</strong> ${msg}</p>
        <br>
        <button onclick="history.back()">뒤로가기</button>
    </div>
</body>
</html>