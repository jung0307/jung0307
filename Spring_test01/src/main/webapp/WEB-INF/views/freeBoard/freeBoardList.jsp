<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set value="${pageContext.request.contextPath }" var="contextPath" ></c:set>
<!DOCTYPE html>
<html>
<head>
<style type="text/css">
#container{
/* 	margin: 20px; */
	background-color: white;
}
tr{
text-align: center;
}
td{
border: 1px;
}

</style>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>

<div id="container" >
<c:choose>
	<c:when test="${not empty boardList}">
		<div style="min-height: 1200px;">
	
			<a href="${contextPath }/freeBoard/getBoardList" style="text-decoration: none;"> <h1>FreeBoard</h1> </a>
			
			<table border="1" style="width:100%">
				<tr>
					<td> 글번호 </td>
					<td> 아이디 </td>
					<td> 제목 </td>
					<td> 게시일 </td>
					<td> 조회수 </td>
				</tr>
			
				<c:choose>
					<c:when test="${empty pagingNum || pagingNum == 1}">
						<c:forEach items="${boardList }" varStatus="i" var="boardList">
							<tr>
								<td style="width: 10%"> 	${countBoardList -i.index} </td>
								<td style="width: 15%"> 	${boardList.boardID } </td>
								<td style="width: 45%"> 	
									<c:choose>
										<c:when test="${boardList.commentCount > 0 }">
											<a href="${contextPath }/freeBoard/getBoardContent.do?boardNum=${boardList.boardNum }" style="text-decoration: none;">
											${boardList.boardTitle } &nbsp; <span style="color: #F7819F"> ${boardList.commentCount } </span>
											</a> 
										</c:when>
										<c:otherwise>
											<a href="${contextPath }/freeBoard/getBoardContent.do?boardNum=${boardList.boardNum }" style="text-decoration: none;">
											${boardList.boardTitle }
											</a> 
										</c:otherwise>
									</c:choose>
								</td>
								<td style="width: 20%"> 	${boardList.boardDate } </td>
								<td style="width: 10%"> 	${boardList.boardCount } </td>
							</tr>
						</c:forEach>					
					</c:when>
					<c:otherwise>
						<c:forEach items="${boardList }" varStatus="i" var="boardList">
							<tr>
								<td style="width: 10%"> 	${countBoardList -i.index - (startNum-1 )} </td>
								<td style="width: 15%"> 	${boardList.boardID } </td>
								<td style="width: 45%"> 	
									<c:choose>
										<c:when test="${boardList.commentCount > 0 }">
											<a href="${contextPath }/freeBoard/getBoardContent.do?boardNum=${boardList.boardNum }" style="text-decoration: none;">
											${boardList.boardTitle } &nbsp; <span style="color: #F7819F"> ${boardList.commentCount } </span>
											</a> 
										</c:when>
										<c:otherwise>
											<a href="${contextPath }/freeBoard/getBoardContent.do?boardNum=${boardList.boardNum }" style="text-decoration: none;">
											${boardList.boardTitle }
											</a> 
										</c:otherwise>
									</c:choose>
								</td>
								<td style="width: 20%"> 	${boardList.boardDate } </td>
								<td style="width: 10%"> 	${boardList.boardCount } </td>
							</tr>
						</c:forEach>						
					</c:otherwise>
				</c:choose>
				

					<tr>
						<td colspan="5" style="padding:15px;"> <button class="btn btn-secondary" style="float: right;" onclick="writeBoardContent();" > 글쓰기 </button> </td>
					</tr>					

				<c:if test="${not empty pagingMessage }">
					<tr>
						<td colspan="5" style="padding: 30px;" >
						${pagingMessage }
						</td>
					</tr>
				</c:if>
			</table>
		</div>
	</c:when>
	<c:otherwise>
		<div style="min-height: 1200px;">
			<h1>게시물이 없습니다.</h1>
		</div>			
	</c:otherwise>
</c:choose>
</div>
</body>
<script type="text/javascript">
	function writeBoardContent() {
		location.href='${contextPath}/freeBoard/writeBoardContent.do';
		/* alert('a'); */
	}
	
/* 	function guestBook(id) {
		
		if(confirm(id+"님의 방명록으로 가기")){
			location.href='${contextPath}/member/guestBookForm.do?id='+id;
		}else{
			alert('tq');
		}
	} */
</script>
</html>