<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set value="${pageContext.request.contextPath }" var="contextPath" ></c:set>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<style>

#loginForm{
	float: right;
	position: relative;
	top: 40px; 
	margin-right: 15px;
	
}
</style>
<!-- <h1> header </h1> -->

<div style="position: absolute; width: 80%;">
<div style="display: inline-block; margin-left: 10px;">
	<!-- <img src="resources/img/header.png" style="width: 170px; height: 170px;"> -->
	 <a href="${contextPath }/"> <img src="resources/img/header3-removebg-preview.png" style="width: 200px; height: 200px;">  </a>
</div>


<c:choose>
	<c:when test="${logIn == 'on' }">
		<div id="loginForm">
			<c:import url="/member/welcomeIndex.do"></c:import>
		</div>
	</c:when>
	<c:otherwise>
		<div id="loginForm" >
			<c:import url="/member/loginMemberForm.do"></c:import>
		</div>
	</c:otherwise>
</c:choose>

</div>


</body>
</html>