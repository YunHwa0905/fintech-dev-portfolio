<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<title>사용자 목록</title>
<style>
table {
	width: 80%;
	border-collapse: collapse;
	margin-top: 20px;
}

th, td {
	border: 1px solid #ddd;
	padding: 8px;
	text-align: center;
}

th {
	background-color: #f4f4f4;
}
</style>
</head>
<body>
	<h2>전체 사용자 목록 (JSP 방식)</h2>
	<table>
		<thead>
			<tr>
				<th>ID</th>
				<th>이름</th>
				<th>성별</th>
				<th>도시</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="user" items="${userList}">
				<tr>
					<td>${user.userId}</td>
					<td>${user.name}</td>
					<td>${user.gender}</td>
					<td>${user.city}</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<div class="pagination-container">
		<ul class="pagination">
			<c:if test="${pageMaker.prev}">
				<li><a href="userList.do?page=${pageMaker.startPage -1}">&laquo;
						이전</a></li>
			</c:if>
			<c:forEach var="idx" begin="${pageMaker.startPage}"
				end="${pageMaker.endPage}">
				<li
					<c:out value="${pageMaker.cri.page == idx ? 'class=active' : ''}"/>>
					<a href="userList.do?page=${idx}">${idx}</a>
				</li>
			</c:forEach>
			<c:if test="${pageMaker.next}">
				<li><a href="userList.do?page=${pageMaker.endPage + 1}">다음
						&raquo;</a></li>
			</c:if>
		</ul>
	</div>
</body>
</html>