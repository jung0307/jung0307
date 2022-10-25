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
<style>
#container{
background-color: white;
}
tr{
text-align: center;
}
td{
border: 1px;
}
.td_header{
border-bottom-color: gray;
}
/* .showComment_tr{
background-color: #D0F5A9;
} */
</style>
<body>
<div id="container">
	<c:choose>
		<c:when test="${not empty boardVO}">
			<table border="1" style="width:100%; border-collapse: collapse;" id="table_">
				<tr style="border-bottom: soild; border-bottom-color: #cccccc; border-bottom-width: 0.5px;">
					<td style="width:15%" class="td_header">${boardVO.boardID }</td>
					<td style="width:55%" class="td_header">${boardVO.boardTitle }</td>
					<td style="width:20%" class="td_header">${boardVO.boardDate }</td>
					<td style="width:10%" class="td_header">${boardVO.boardCount }</td>
				</tr>
				<tr>
					<td colspan="4" style="text-align: left; padding-left: 30px; padding-right: 30px; padding-top: 30px; line-height:0;">
						<div style="min-height: 1000px;">
							<div style="display: block; margin-bottom: 200px; position: relative;">
								${boardVO.boardContent }
							</div>
<%-- 							<c:choose>
								<c:when test="${empty imageFileList }">
									<h1>시발</h1>
								</c:when>
								<c:otherwise>
									<c:forEach var="imageFileList" items="${imageFileList }" varStatus="i">
											<div style="display: block;">
												<img alt="drawing of a cat"
												src="${contextPath }/download.do?boardNum=${boardVO.boardNum}&imageFileName=${imageFileList.imageFileName}"
												>
											</div>
										</c:forEach>			 				
								</c:otherwise>
							</c:choose> --%>
						</div>
					</td>		
				</tr>
				<tr style="border-bottom: soild; border-bottom-color: #cccccc; border-bottom-width: 0.5px;">
					
						<td style="float: right; padding: 15px;"> 
							<div class="btn-group" role="group" aria-label="Basic example">
								 <button type="button" class="btn btn-secondary" id="rewriteBTN" onclick="rewrite();">rewrite</button>
								 <button type="button" class="btn btn-secondary" id="removeBTN" onclick="remove();">delete</button>
							</div>
						</td>
					
					<td colspan="4" style="padding: 15px;">
						<button type="button" style="float: right;" class="btn btn-secondary" id="homeBTN" onclick="home();" >Home</button>
					</td>				
				</tr>						

					<tr>
						<td colspan="4" style="text-align: left; padding-left: 30px; padding-right: 30px; padding-top: 30px; line-height:0;">
							<div style="min-height: 200px;">
								<div class="form-group">
									<label for="exampleTextarea" class="form-label mt-4" style="margin-bottom: 15px;"> guest
									<input type="password" id = "commentPwd" style="width: 100px; margin-left: 5px; display: inline-block;" class="form-control" name="commentPwd" required="required" placeholder="password">
									</label>
									<button type="button" class="btn btn-primary" style="margin-left: 15px; float: right;" onclick="commentSubmit()">submit</button>
									<textarea class="form-control" id="exampleTextarea" rows="3" name="commentContent"></textarea>
								 </div>
							</div>							
						</td>
					</tr>					

<!-- 			<tr>
						<td colspan="4" style="padding-left: 30px; padding-right: 30px; padding-top: 30px;">
							<div class="card border-primary  mb-3" style="width: auto;">
							  <div class="card-header">
							  	<div style="float: left;">
							  		이름
							  	</div>
							  	<div style="float: right;">
									 <button type="button" class="btn btn-secondary" id="" onclick="">delete</button>
							  	</div>
							  </div>
							  <div class="card-body">
							    <h4 class="card-title">Secondary card title</h4>
							    <p class="card-text">Some quick example text to build on the card title and make up the bulk of the card's content.</p>
							</div>
						</td>							
				 </tr>	 -->
			</table>
		</c:when>
		<c:otherwise>
			<script type="text/javascript">
				alert('view게시판 오류!');
				location.href='${contextPath}/freeBoard/getBoardList.do';
			</script>
		</c:otherwise>
	</c:choose>
</div>
 <div>
	<form id="hiddenForm" style="display: none;">
		<input type="hidden" name="boardID" value="${boardVO.boardID }">
		<input type="hidden" name="boardNum" value="${boardVO.boardNum }">
		<input type="hidden" name="boardTitle" value="${boardVO.boardTitle }">
		<input type="hidden" name="boardDate" value="${boardVO.boardDate }">
		<input type="hidden" name="boardCount" value="${boardVO.boardCount }">
		<input type="hidden" name="boardContent" value="${boardVO.boardContent }">
		<input type="hidden" name="fileName" value="">
		<input type="hidden" name="boardPwd" value="${boardVO.boardPwd }">
	</form>
</div> 
</body>
<script src="https://code.jquery.com/jquery-latest.min.js"></script>
<script type="text/javascript">

var form = document.getElementById('hiddenForm');
var rewriteBTN = document.getElementById('rewriteBTN');
var commentContent = document.getElementById('exampleTextarea');

var removeBTN = document.getElementById('removeBTN');
var commentPwd = document.getElementById('commentPwd');


function rewrite() {
	
	 var pwd = prompt('비밀번호를 입력하세요!');

	
   	if(pwd == "${boardVO.boardPwd}"){
		form.action = "${contextPath}/freeBoard/rewriteBoardContentForm.do";
		form.method = "post";
		form.submit();
	}else{
		alert('비밀번호가 틀립니다!');
	}    
}

function remove() {
	
	var pwd = prompt('비밀번호를 입력하세요!');
	location.href="${contextPath}/freeBoard/removeBoardContent.do?boardPwd="+pwd+"&boardNum=${boardVO.boardNum}&role=${memberVO.role}";
}

function home() {
	location.href='${contextPath}/freeBoard/getBoardList';
}

// 댓글 입력하면 insert하고 댓글 보여주기
function commentSubmit() {
	 
	if(commentPwd.value==""){
		alert('비밀번호를 입력하세요!');
		return false;
	}else if(commentContent.value==""){
		alert('내용을 입력하세요!');
		return false;
	}
	else{
		var commentVO = {
				commentID : "guest",
				commentContent : commentContent.value,
				commentDate : "",
				commentGroup : "",
				commentHir : "",
				commentNum : "",
				boardNum : "${boardVO.boardNum}",
				commentPwd  : commentPwd.value
		}
		
		$.ajax({
			
			type:"post",
			async : true,
			contentType:"application/json",
			url : "${contextPath}/freeBoard/addComment.do",
			data : JSON.stringify(commentVO),
			success: function (data,textStatus) {
	/* 			let cnt = 0;
				
				
				var jsonInfo = JSON.parse(data); */
				
				if($(".showComment_tr").length){
					/* alert('showComment_tr 확인'); */
					$(".showComment_tr").remove();
				}
				if($(".showComment_tr2").length){
					/* alert('showComment_tr2 확인'); */
					$(".showComment_tr2").remove();
				}
				if($(".showComment_tr2_").length){
					/* alert('showComment_tr2_ 확인'); */
					$(".showComment_tr2_").remove();
				}
				
				
				commentFunction(data); 
			},
			error: function (request, status, error) {
				alert("status : " + request.status + "\n" + "message:" + request.responseText + "\n" + "error:" + error);
			},
			complete: function () {
				
				commentContent.value = '';
				commentPwd.value = '';
				$('.showComment_tr2').attr('style' , 'display:none;');
				/* alert('댓글 완료'); */
			}
			
		});		
	}
	
}

// 대댓글 insert 하고 댓글 리스트 보여주기

function comment2Submit(commentNum,cnt){
	
	alert(commentNum);
	
	var commentContent2 = document.getElementById('exampleTextarea'+cnt);
	var commentPwd2 = document.getElementById('commentPwd2'+cnt);
	
	
	if(commentPwd2.value==""){
		alert('비밀번호를 입력하세요!');
		return false;
	}else if(commentContent2.value==""){
		alert('내용을 입력하세요!');
		return false;
	}else{
		var commentVO = {
				commentID : "guest",
				commentContent : commentContent2.value,
				commentDate : "",
				commentGroup : commentNum,
				commentHir : 1,
				commentNum : "",
				boardNum : "${boardVO.boardNum}",
				commentPwd : commentPwd2.value
			
			}
			
			
			$.ajax({
				type : "post",
				async : true,
				contentType : "application/json",
				url : "${contextPath}/freeBoard/addComment2.do",
				data : JSON.stringify(commentVO),
				success : function (data,textStatus) {
					
		/* 			alert('댓글2 성공 확인');
					let cnt = 0;
					
					
					var jsonInfo = JSON.parse(data); */
					
					if($(".showComment_tr").length){
						/* alert('showComment_tr 확인'); */
						$(".showComment_tr").remove();
					}
					if($(".showComment_tr2").length){
						/* alert('showComment_tr2 확인'); */
						$(".showComment_tr2").remove();
					}
					if($(".showComment_tr2_").length){
						/* alert('showComment_tr2_ 확인'); */
						$(".showComment_tr2_").remove();
					}
						
					/* (i > 0 ? '<img src="/img.jpg"/>': '') */
					
					commentFunction(data);
		  				
				},
				error: function (request, status, error) {
					/* alert("status : " + request.status + "\n" + "message:" + request.responseText + "\n" + "error:" + error); */
					alert('댓글 달 수 없어요');
				},
				complete : function () {
					commentContent2.value = '';
					commentPwd.value = '';
					$('.showComment_tr2').attr('style' , 'display:none;');
				}
			})				
	}
	
}




// 페이지가 로딩되면 댓글 보여주기
$(function showComment() {
	
	$.ajax({
		type:"GET",
		async : true,
		url : "${contextPath}/freeBoard/getCommentList.do?boardNum=${boardVO.boardNum}",
		success: function (data,textStatus) {

				
			commentFunction(data);
  			/*   $('.showComment_tr2').attr('style', "display:none;");  */
			
		},
		error: function (request, status, error) {
			alert("status : " + request.status + "\n" + "message:" + request.responseText + "\n" + "error:" + error);
		},
		complete: function () {
			alert('로딩 완료');
			$('.showComment_tr2').attr('style', "display:none;");
		}
		
	});	
	
})

// 댓글 클릭하면 대댓글 보여주고 감추기
function switchComment(cnt) {
	if($('#showComment_tr2_'+cnt).attr('style') == "display:none;"){
		
		/* alert(cnt+'none일 때'); */
		$('#showComment_tr2_'+cnt).attr('style', "display:''"); 
		
	}else{
		/* alert(cnt+'none이 아닐 때'); */
		$('#showComment_tr2_'+cnt).attr('style', "display:none;"); 
		}

}
// 댓글 삭제한 후 댓글 리스트 보여주기
function removeComment(id,commentNum,role) {
	
	var pwd = prompt('비밀번호를 입력하세요!');
		
		if(pwd!=null){
			var commentVO = {
					commentID : "${memberVO.id}",
					commentContent : "",
					commentDate : "",
					commentGroup : commentNum,
					commentHir : 1,
					commentNum : commentNum,
					boardNum : "${boardVO.boardNum}",
					commentPwd : pwd
				}
				
				
				$.ajax({
					type : "post",
					async : true,
					contentType : "application/json",
					url : "${contextPath}/freeBoard/removeComment.do",
					data : JSON.stringify(commentVO),
					success : function (data,textStatus) {
						
						commentFunction(data);
			  				
					},
					error: function (request, status, error) {
						alert("status : " + request.status + "\n" + "message:" + request.responseText + "\n" + "error:" + error);
					},
					complete : function () {
						$('.showComment_tr2').attr('style' , 'display:none;');
						/* alert('삭제 완료'); */
					}
				})	
		}else{
			alert('삭제 실패!');
		}
	
	
}


function commentFunction(data) {
	
		var jsonInfo = JSON.parse(data);
		let cnt = 0;
		
		jsonInfo.commentList.forEach((item,idx)=>{
			
			if(item.commentHir == 0){
				/* alert(jsonInfo.commentList[key].commentHir) */
				$("#table_").append(
						
						"<tr class='showComment_tr' id='showComment_tr_"+ cnt +"' onclick='switchComment("+cnt+");'><td colspan='4' style='padding-left: 30px; padding-right: 30px; padding-top: 15px;'>"
						+"<div class='card border-primary  mb-3' style='width: auto;'>"
						+  "<div class='card-header'>"
						+  	"<div style='float: left; margin-right:15px;'>"
						+  		item.commentID
						+  	"</div>"
						+  	"<div style='float: left;'>"
						+  		item.commentDate
						+  	"</div>"
						+  	"<div style='float: right;'>"
						+		 "<button type='button' class='btn btn-secondary' id='' onclick=\"removeComment(\'"+item.commentID+"\',"+item.commentNum+",\'${memberVO.role}\');\">delete</button>"
						+  	"</div>"
						+  "</div>"
						+  "<div class='card-body' style='background-color: #FBFBEF;'>"
						+    "<p class='card-text' style='text-align: left;'>"+item.commentContent+"</p>"
						+"</div>"
						+"</td></tr>"
						
						+"<tr class='showComment_tr2' id='showComment_tr2_"+ cnt +"'><td colspan='4' style='padding-left: 30px; padding-right: 30px;'>"
						+"<div class='card border-secondary mb-3' style='width: auto;'>"
						+  "<div class='card-header'>"
						+  	"<div style='float: left;'>"
						+  		"guest"
						+       "<input type='password' id = 'commentPwd2"+cnt+"' style='width: 100px; margin-left: 10px; display: inline-block;' class='form-control' name='commentPwd' required='required' placeholder='password'>"
						+  	"</div>"
						+  	"<div style='float: right;'>"
						+		 "<button type='button' class='btn btn-primary' id='' onclick='comment2Submit("+item.commentNum+","+cnt+")'>submit</button>"
						+  	"</div>"
						+  "</div>"
						+  "<div class='card-body' style='background-color:#FBEFEF;'>"
/* 						+  "<label for='exampleTextarea' class='form-label mt-4' style='margin-bottom: 15px;'>"
						+  "</label>" */
						+  "<textarea class='form-control' id='exampleTextarea"+cnt+"' rows='3' name='commentContent'></textarea>"    
						+"</div>"
						+"</td></tr>"
						
				);
				cnt++
				    
			}else if(item.commentHir !=0){
				
				$("#table_").append(
						"<tr class='showComment_tr2_' id='showComment_tr2__"+ cnt +"'><td colspan='4' style='padding-left: 30px; padding-right: 30px;'>"
						+"<div class='card border-secondary mb-3' style='width: auto;'>"
						+  "<div class='card-header'>"
						+  	"<div style='float: left; margin-right:15px;'>"
						+  		item.commentID
						+  	"</div>"
						+  	"<div style='float: left;'>"
						+  		item.commentDate
						+  	"</div>"
						+  	"<div style='float: right;'>"
						+		 "<button type='button' class='btn btn-secondary' id='' onclick=\"removeComment(\'"+item.commentID+"\',"+item.commentNum+",\'${memberVO.role}\');\">delete</button>"
						+  	"</div>"
						+  "</div>"
						+  "<div class='card-body' style='background-color:#FBEFEF;'>"
						+ 	"<p class='card-text' style='text-align: left;'>"+item.commentContent+"</p>" 
						+"</div>"
						+"</td></tr>" 
						
				);
				cnt++
				
				
				}			 
			});

		

		
}
	


/*   function RewriteBoardContent(boardNum,boardID) {
	 alert(a); 
	 location.href='${contextPath}/freeBoard/rewriteBoardContentForm.do?boardID='+boardID+'&boardNum='+boardNum; 
	
}  */

/* $(function(){
	$("#rewriteBTN").click(function(){

		var boardVO = {
				boardID:"${boardVO.boardID}",
				boardNum:${boardVO.boardNum},
				boardTitle:"${boardVO.boardTitle}",
				boardDate:"${boardVO.boardDate}",
				boardCount:${boardVO.boardCount},
				boardContent:"",
				fileName:""
		};
		
		$.ajax({
			type:"POST",
			async: false,
			url:"${contextPath}/freeBoard/rewriteBoardContentForm.do",
			contentType:"application/json",
			data : JSON.stringify(boardVO),
			success:function (data,textStatus){
				alert('성공');

			},
		     error:function(request, status, error) {
		         alert("status : " + request.status + "\n" + "message:" + request.responseText + "\n" + "error:" + error);
		        },
 			complete:function(data,textStatus){
 				alert('완료');
 			}
		});		
	});
}); */

</script>
</html>