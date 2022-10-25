<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set value="${pageContext.request.contextPath }" var="contextPath" ></c:set>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<style>
#container{
background-color: white;
min-height: 1200px;
}
td{
text-align: center;
}
</style>
<body>
<div id="container">
<!-- 	메인페이지
<img src='resources/img/찹쌀떡.jpg' alt='sibal'/> -->
<!-- <div contenteditable="true" style="width: 700px; height: 700px; background-color: white;" id="div1" ></div>

<button type="button" onclick="wow();" >버튼</button> -->

<!-- <div id="editor" contenteditable="true">
</div>

<div id="console"></div> 
-->


<c:choose>
	<c:when test="${not empty boardList}">
		<div >
	
			<h1> <a style="text-decoration: none; color: #888;" href="${contextPath }/board/getBoardList">Board</a> </h1>
			
			<table border="1" style="width:100%">
				<tr>
					<td> 글번호 </td>
					<td> 아이디 </td>
					<td> 제목 </td>
					<td> 게시일 </td>
					<td> 조회수 </td>
					<td> 추천수 </td>
				</tr>
			

						<c:forEach items="${boardList }" varStatus="i" var="boardList">
							<tr>
								<td style="width: 10%"> 	${boardList.boardNum} </td>
								<td style="width: 15%"> 	<a href='javascript:void(0);' onclick="guestBook('${boardList.boardID }')" style="text-decoration: none; color: #A4A4A4;"> ${boardList.boardID } </a> </td>
								<td style="width: 45%"> 	
									<c:choose>
										<c:when test="${boardList.commentCount > 0 }">
											<a href="${contextPath }/board/getBoardContent.do?boardNum=${boardList.boardNum }" style="text-decoration: none;">
											${boardList.boardTitle } &nbsp; <span style="color: #F7819F"> ${boardList.commentCount } </span>
											</a> 
										</c:when>
										<c:otherwise>
											<a href="${contextPath }/board/getBoardContent.do?boardNum=${boardList.boardNum }" style="text-decoration: none;">
											${boardList.boardTitle }
											</a> 
										</c:otherwise>
									</c:choose>
								</td>
								<td style="width: 20%;"> 	${boardList.boardDate } </td>
								<td style="width: 5%;"> 	${boardList.boardCount } </td>
								<td style="width: 5%;"> 	<span style="color: #F7819F" >${boardList.upCount }</span> </td>
							</tr>
						</c:forEach>	

			</table>
		</div>
	</c:when>
	<c:otherwise>
		<div style="min-height: 1200px;">
			<h1>게시물이 없습니다.</h1>
		</div>			
	</c:otherwise>
</c:choose>



<c:choose>
	<c:when test="${not empty freeBoardList}">
		<div >
	
			<h1> <a style="text-decoration: none; color: #888;" href="${contextPath }/freeBoard/getBoardList">FreeBoard</a> </h1>
			
			<table border="1" style="width:100%">
				<tr>
					<td> 글번호 </td>
					<td> 아이디 </td>
					<td> 제목 </td>
					<td> 게시일 </td>
					<td> 조회수 </td>
				</tr>
			
	
						<c:forEach items="${freeBoardList }" varStatus="i" var="boardList">
							<tr>
								<td style="width: 10%"> 	${boardList.boardNum} </td>
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
<script src="https://code.jquery.com/jquery-latest.min.js"></script>
<script type="text/javascript">

function guestBook(id) {
	
	if(confirm(id+"님의 방명록으로 가기")){
		location.href='${contextPath}/member/guestBookForm.do?id='+id;
	}
}

/* function wow() {
	var div1 = $('#div1').children();
	var div2 = $('#div1 > div').children();
	var div3 = $('#div1').innerText;
	var div4 = $('#div1').innerHTML;

	console.log(div3);
	console.log(div4);
} */


/*   var input = document.getElementById('editor');

input.onkeyup = function() {
  console.log(input.textContent)
  document.getElementById('console').innerHTML = input.innerText.replace(/(\n|\r\n)/g, '<br>'); 
}  
 */

</script>
</html>