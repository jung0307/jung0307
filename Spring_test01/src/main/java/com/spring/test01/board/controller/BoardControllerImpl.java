package com.spring.test01.board.controller;

import java.awt.Image;
import java.io.File;
import java.net.http.HttpHeaders;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.test01.board.service.BoardService;
import com.spring.test01.board.vo.BoardVO;
import com.spring.test01.board.vo.CommentVO;
import com.spring.test01.board.vo.GoodAndBadVO;
import com.spring.test01.board.vo.ImageVO;
import com.spring.test01.common.paging.Paging;

@RestController("boardController")
public class BoardControllerImpl implements BoardController{

	@Autowired
	BoardService boardService;
	@Autowired
	BoardVO boardVO;
	
	ImageVO imageVO;
	
	List<BoardVO> boardList;
	List<ImageVO> imageFileList;
	List<CommentVO> commentList;
	List<GoodAndBadVO> goodAndBadList;
	
	private static String ARTICLE_IMAGE_REPO_PATH = "c:\\spring\\img";
	
	/* File settingFile = new File(ARTICLE_IMAGE_REPO_PATH); */
	
	@Override
	@RequestMapping(value = "/board/getBoardList" , method = RequestMethod.GET)
	public ModelAndView getBoardList(@RequestParam(required = false) String pagingNum ,@RequestParam(required = false) String searchSection,
			@RequestParam(required = false)String searchText,HttpServletRequest req, HttpServletResponse resp) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("getBoardList ??????");
		
		Map<String , Object> pagingMap = new HashMap<String , Object>();
		
		 // ?????? ?????? ???????????? list
		
		int countBoardList = 0; // ??? ????????? ???
		int count = 0;
		int pagingNumInt = 1; // ?????? pagingNum ???
		Paging paging = new Paging();
		
		// ?????????
		int pagingBlock = 5; // ??? ????????? ????????? ????????? ?????? ???
		int pagingSize = 30; // ??? ????????? ????????? ????????? ???
		
//		System.out.println("searchSection : "+searchSection);
		
		
		// searchSection??? ?????? ???????????? map??? ?????????
			if(searchSection!=null) {
				if(searchSection.equals("boardID")) {
					System.out.println("searchSection : " + searchSection);
					pagingMap.put("boardID", searchSection);
				}else if(searchSection.equals("boardTitle")) {
					System.out.println("searchSection : " + searchSection);
					pagingMap.put("boardTitle", searchSection);
				}else if(searchSection.equals("boardContent")) {
					System.out.println("searchSection : " + searchSection);
					pagingMap.put("boardContent", searchSection);
				}
			}
			
			System.out.println("searchSection : "+searchSection);
		

		// ???????????? ?????? ??????
		if(searchText!=null && !searchText.equals("")) {
			System.out.println("searchSection : "+searchSection);
			System.out.println("searchText : "+searchText);
			pagingMap.put("searchText", searchText.trim());
//			pagingMap.put("searchSection", searchSection.trim());
			countBoardList = boardService.getCountAllBoardListBySearchText(pagingMap);
			System.out.println("countBoardList : " + countBoardList);
		}
		// ???????????? ?????? ??????
		else {
			System.out.println("searchText ??????");
			searchText = "";
			countBoardList = boardService.getCountAllBoardList(); // max(boardNum)
			System.out.println("countBoardList : " + countBoardList);
		}
		
//		countBoardList = boardService.getCountAllBoardList(); // max(boardNum)
		
		int totalBlock = (int) Math.ceil(((double)countBoardList / pagingSize));
		System.out.println("(countBoardList / pagingSize) : "+((double)countBoardList / pagingSize));
		if(pagingNum!=null) {
			 pagingNumInt = Integer.parseInt(pagingNum);
		}
		System.out.println("pagingMap : "+pagingNumInt);
		int startNum = (pagingSize * (pagingNumInt - 1)) + 1; // db?????? ????????? ??? ???????????? ??????
		int endNum = (pagingSize * pagingNumInt); // ????????????
		
		pagingMap.put("startNum", startNum);
		pagingMap.put("endNum", endNum);
		
		
		System.out.println("pagingBlock :  "+pagingBlock);
		System.out.println("pagingNumInt :  "+pagingNumInt);
		System.out.println("pagingSize :  "+pagingSize);
		System.out.println("totalBlock :  "+totalBlock);
		System.out.println("countBoardList :  "+countBoardList);
		System.out.println("startNum :  "+startNum);
		System.out.println("endNum :  "+endNum);
		

		String path = "board";
		
//		int pagingBlock,int pagingNum, int totalBlock, int totalNum
		String pagingMessage = paging.pagination(pagingBlock, pagingNumInt, totalBlock, countBoardList,searchSection,searchText,req,path);
//		String pagingMessage = paging.pagination(5, 1, 30, 533, req);
		ModelAndView mav = new ModelAndView("board");
//		boardList = boardService.getBoardList();
		boardList = boardService.getBoardList(pagingMap);
		commentList = boardService.getCountCommentGroupByBoardNum();  // ?????? ?????? ???????????? list
		
		
		// ?????? ??? ????????????
		for(int i = 0; i < boardList.size(); i++) {
//			System.out.println("iiiii boardList ???");
//			System.out.println(boardList.get(i).getBoardNum());
			for(int j = 0; j < commentList.size(); j++) {
//				System.out.println("jjjjj commentList???");
//				System.out.println(commentList.get(j).getBoardNum());
				if(boardList.get(i).getBoardNum() == commentList.get(j).getBoardNum()) {	
					boardList.get(i).setCommentCount(commentList.get(j).getCommentCount());
//					System.out.println("boardList??? CommentCount?????? ??????");
					
				}
				
			}
			
		}
		
		// ?????? ??? ????????????
		goodAndBadList = boardService.selectAllContentUpAndDown();
		for(int i =0; i<boardList.size(); i++) {
//			System.out.println("iiiii boardList ???");
//			System.out.println(boardList.get(i).getBoardNum());
			for(int j = 0; j < goodAndBadList.size(); j++) {
//				System.out.println("jjjjj commentList???");
//				System.out.println(goodAndBadList.get(j).getBoardNum());
				if(boardList.get(i).getBoardNum() == goodAndBadList.get(j).getBoardNum()) {
					boardList.get(i).setUpCount(goodAndBadList.get(j).getUp());
//					System.out.println("boardList??? up?????? ??????");
				}
			}
		}
		
		
//		countBoardList = boardService.getCountBoardList(); count(*)
//		System.out.println(boardList.get(0).getBoardCount());
//		System.out.println(boardList.get(0).getBoardDate());
		mav.addObject("boardList", boardList);
//		mav.addObject("commentList", commentList);
		mav.addObject("countBoardList", countBoardList);
		mav.addObject("startNum", startNum);
		mav.addObject("endNum", endNum);
		mav.addObject("pagingNum", pagingNum);
		mav.addObject("pagingMessage", pagingMessage);
		
		
		return mav;
	}
	
	@Override
	@RequestMapping(value = "/board/getBoardContent.do" , method = RequestMethod.GET)
	public ModelAndView getBoardContent(@RequestParam("boardNum") int boardNum, HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		// TODO Auto-generated method stub
		System.out.println("getBoardContent ??????");
		System.out.println(boardNum);
		ModelAndView mav = new ModelAndView("boardContentView");
		boardService.gainBoardCount(boardNum);
		boardVO = boardService.getBoardContent(boardNum);
		imageFileList = boardService.getImageFileList(boardNum);
		
		
		int allCountUp = 0;
		int allCountDown = 0;
		allCountUp = boardService.selectUp(boardNum);
		allCountDown = boardService.selectDown(boardNum);
		
		System.out.println("allCountUp : " + allCountUp);
		System.out.println("allCountDown : " + allCountDown);
		
		for(ImageVO s : imageFileList) {
			System.out.println("imageFileList : "+ s.getContentNum());
			System.out.println("imageFileList : "+ s.getImageFileName());
			System.out.println("imageFileList : "+ s.getImageFileNo());
			System.out.println("imageFileList : "+ s.getRegDate());
		}
		mav.addObject(boardVO);
		mav.addObject("imageFileList",imageFileList);
		mav.addObject("allCountUp", allCountUp);
		mav.addObject("allCountDown", allCountDown);
		return mav;
	}
	
	@Override
	@RequestMapping(value = "/board/writeBoardContent.do" , method = RequestMethod.GET)
	public ModelAndView writeBoardContent(@RequestParam("boardID") String boardID, HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		// TODO Auto-generated method stub
		System.out.println("writeBoardContent ??????");
		System.out.println(boardID);
		
		ModelAndView mav = new ModelAndView("boardContentForm");
 		return mav;
	}
	
	@Override
	@RequestMapping(value = "/board/uploadBoardContent.do")
	public ResponseEntity uploadBoardContent(MultipartHttpServletRequest mReq, HttpServletResponse resp) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("uploadBoardContent ??????");
		
		mReq.setCharacterEncoding("utf-8");
		
		String message;
		ResponseEntity resEnt;
		org.springframework.http.HttpHeaders responseHeaders = new org.springframework.http.HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=utf-8");
		
		Map<String , Object> boardContentMap = new HashMap();
		List<String> fileList = upload(mReq);
		List<ImageVO> imageFileList = new ArrayList<ImageVO>();
		
		int boardNum = boardService.getCountBoardList()+1;
		String boardID = null;
		System.out.println("boardNum : "+boardNum);

		Enumeration enu = mReq.getParameterNames();
		while(enu.hasMoreElements()) {
			String name = (String) enu.nextElement();
			String value = mReq.getParameter(name);
			System.out.println("name : " + name + "  value : " + value );
			if(name.equals("boardID")) {
				boardID = value;
			}
			if(name.equals("boardContent")) {
				String value1 = value.replace("<div>", "<p>");
				String value2 = value1.replace("</div>", "</p>");
				value = value2;
			}
			boardContentMap.put(name, value);
		}
		
		if(fileList!=null) {
			int i = 1;
			for(String s : fileList) {
				imageVO = new ImageVO();
				imageVO.setImageFileName(s);
				imageVO.setContentNum(boardNum);
				imageVO.setImageFileNo(i++);
				System.out.println("getImageFileName ?????? ?????? "+imageVO.getImageFileName());
				System.out.println("getContentNum ?????? ?????? "+imageVO.getContentNum());
				System.out.println("getImageFileNo ?????? ?????? "+imageVO.getImageFileNo());
				imageFileList.add(imageVO);
			}
		}
		
		boardContentMap.put("imageFileList", imageFileList);
		boardContentMap.put("boardNum", boardNum);
		
		try {
			
			  boardService.addBoardContent(boardContentMap);
			  boardService.addImageFile(boardContentMap);

		}catch (Exception e) {
			// TODO: handle exception
			System.out.println("addBoardContent : addImageFile ?????? ");
			e.printStackTrace();
		}
		
		try {
			if(fileList!=null && fileList.size() !=0) {
				File destDir = new File(ARTICLE_IMAGE_REPO_PATH + "\\" + boardNum);
				for(String fileName : fileList) {
					File srcFile = new File(ARTICLE_IMAGE_REPO_PATH + "\\" + "temp" + "\\" + fileName);
					if(srcFile.exists()) {
						FileUtils.moveFileToDirectory(srcFile, destDir, true);						
					}
				}
			}
			message = "<script>";
			message = message + " alert('????????? ??????????????????'); ";
			message = message + " location.href='" + mReq.getContextPath()+"/board/getBoardList';";
			message = message + " </script> ";
			
			resEnt = new ResponseEntity(message, responseHeaders, HttpStatus.CREATED);
			
			
		}catch (Exception e) {
			// TODO: handle exception
			
			if(fileList!=null && fileList.size() !=0) {
				for(String fileName : fileList) {
					File srcFile = new File(ARTICLE_IMAGE_REPO_PATH + "\\" + "temp" + "\\" + fileName);
					srcFile.delete();
				}
			}
			message = "<script>";
			message = message + " alert('?????? ??????! ?????? ??????????????????'); ";
			message = message + " location.href='" + mReq.getContextPath()+"/board/writeBoardContent.do?boardID="+boardID+"';";
			message = message + " </script> ";
			
			resEnt = new ResponseEntity(message, responseHeaders, HttpStatus.CREATED);
			
			e.printStackTrace();
		}

		return resEnt;
	}
	

	/* consumes = "application/json;" */
	@Override
	@ResponseBody
	@RequestMapping(value = "/board/rewriteBoardContentForm.do" , method = RequestMethod.POST)
	public ModelAndView rewriteBoardContentForm(@ModelAttribute("boradVO") BoardVO boardVO,HttpServletRequest req, HttpServletResponse resp) throws Exception {
		// TODO Auto-generated method stub
		req.setCharacterEncoding("utf-8");
		resp.setContentType("text/html;charset=utf-8");
		System.out.println("rewriteBoardContentForm ??????");
		ModelAndView mav = new ModelAndView();
		mav.setViewName("boardRewriteContentForm");
		mav.addObject("boardVO", boardVO);
//		System.out.println("getBoardContent : " + boardVO.getBoardContent());
//		System.out.println("getBoardCount : " +boardVO.getBoardCount());
//		System.out.println("getBoardDate : " +boardVO.getBoardDate());
//		System.out.println("getBoardID : " +boardVO.getBoardID());
//		System.out.println("getBoardNum : " +boardVO.getBoardNum());
//		System.out.println("getBoardTitle : " +boardVO.getBoardTitle());
//		System.out.println("getFileName : " +boardVO.getFileName());
		
		return mav;
	}
	
	@Override
	@RequestMapping(value = "/board/rewriteBoardContent.do" , method = RequestMethod.POST)
	public ResponseEntity rewriteBoardContent(MultipartHttpServletRequest mReq, HttpServletResponse resp, @RequestParam("boardNum_") int boardNum_)
			throws Exception {
		// TODO Auto-generated method stub
		System.out.println("rewriteBoardContent ??????");
		
		mReq.setCharacterEncoding("utf-8");
		
		String message;
		int updateCount = 0;
		ResponseEntity resEnt;
		org.springframework.http.HttpHeaders responseHeaders = new org.springframework.http.HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=utf-8");
		
		Map<String , Object> boardContentMap = new HashMap();
		File dir = new File(ARTICLE_IMAGE_REPO_PATH+"\\"+boardNum_);
		if(dir.exists()) {
			FileUtils.cleanDirectory(dir);
			System.out.println(boardNum_+"dir ??????");
		}
		List<String> fileList = upload(mReq);
		List<ImageVO> imageFileList = new ArrayList<ImageVO>();
		
		int boardNum = boardService.getCountBoardList()+1;
		String boardID = null;
		System.out.println("boardNum : "+boardNum);

		Enumeration enu = mReq.getParameterNames();
		while(enu.hasMoreElements()) {
			String name = (String) enu.nextElement();
			String value = mReq.getParameter(name);
			System.out.println("name : " + name + "  value : " + value );
			if(name.equals("boardID")) {
				boardID = value;
			}
			if(name.equals("boardContent")) {
				String value1 = value.replace("<div>", "<p>");
				String value2 = value1.replace("</div>", "</p>");
				value = value2;
			}
			boardContentMap.put(name, value);
		}
		
		if(fileList!=null) {
			int i = 1;
			for(String s : fileList) {
				imageVO = new ImageVO();
				imageVO.setImageFileName(s);
				imageVO.setContentNum(boardNum);
				imageVO.setImageFileNo(i++);
				System.out.println("getImageFileName ?????? ?????? "+imageVO.getImageFileName());
				System.out.println("getContentNum ?????? ?????? "+imageVO.getContentNum());
				System.out.println("getImageFileNo ?????? ?????? "+imageVO.getImageFileNo());
				imageFileList.add(imageVO);
			}
		}
		
//		boardContentMap.put("imageFileList", imageFileList);
//		boardContentMap.put("boardNum", boardNum);
		
		try {
			
			  updateCount = boardService.rewriteBoardContent(boardContentMap);
//			  boardService.addImageFile(boardContentMap);

		}catch (Exception e) {
			// TODO: handle exception
			System.out.println("rewriteBoardContent : rewriteBoardContent ?????? ");
			e.printStackTrace();
		}
		
		try {
			if(fileList!=null && fileList.size() !=0 && updateCount!=0) {
//				File destDir = new File(ARTICLE_IMAGE_REPO_PATH + "\\" + boardNum);
				for(String fileName : fileList) {
					File srcFile = new File(ARTICLE_IMAGE_REPO_PATH + "\\" + "temp" + "\\" + fileName);
					if(srcFile.exists()) {
						FileUtils.moveFileToDirectory(srcFile, dir, true);						
					}
				}
			}
			message = "<script>";
			message = message + " alert('?????? ??????!'); ";
			message = message + " location.href='" + mReq.getContextPath()+"/board/getBoardList';";
			message = message + " </script> ";
			
			resEnt = new ResponseEntity(message, responseHeaders, HttpStatus.CREATED);
			
			
		}catch (Exception e) {
			// TODO: handle exception
			
			if(fileList!=null && fileList.size() !=0) {
				for(String fileName : fileList) {
					File srcFile = new File(ARTICLE_IMAGE_REPO_PATH + "\\" + "temp" + "\\" + fileName);
					srcFile.delete();
				}
			}
			message = "<script>";
			message = message + " alert('?????? ??????! ?????? ??????????????????'); ";
			message = message + " location.href='" + mReq.getContextPath()+"/board/getBoardList';";
			message = message + " </script> ";
			
			resEnt = new ResponseEntity(message, responseHeaders, HttpStatus.CREATED);
			
			e.printStackTrace();
		}

		return resEnt;
	}
	
	@Override
	@RequestMapping(value = "/board/removeBoardContent.do" , method = RequestMethod.GET)
	public ResponseEntity removeBoardContent(HttpServletRequest req, HttpServletResponse resp, @RequestParam("boardNum") int boardNum,
		@RequestParam("boardID") String boardID , @RequestParam(required = false) String role) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("removeBoardContent ??????");
		int deleteCount = 0;
		String message;
		
		Map boardContentMap = new HashMap();
		boardContentMap.put("boardID", boardID);
		boardContentMap.put("boardNum", boardNum);
		org.springframework.http.HttpHeaders responseHeaders = new org.springframework.http.HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=utf-8");
		
		
		
		System.out.println("boardID : "+boardID + " boardNum : "+boardNum + " role : " + role);
		
		if(role.equals("admin")) {
			System.out.println("admin delete");
			deleteCount =  boardService.removeBoardContentByAdmin(boardContentMap);
		}else {
			deleteCount = boardService.removeBoardContent(boardContentMap);
		}
		
		
		
		
		if(deleteCount!=0) {
			
			int count = 0;
			count = boardService.removeGoodAndBad(boardNum);
			if(count!=0) {
				System.out.println("goodAndBad ????????????");
			}else {
				System.out.println("goodAndBad ????????????");
			}
			
			File dir = new File(ARTICLE_IMAGE_REPO_PATH + "\\" + boardNum);
			if(dir.isDirectory()) {
				FileUtils.cleanDirectory(dir);
				dir.delete();
				System.out.println("dir ??????");
			}
			
			message = "<script> ";
			message = message + " alert('?????? ??????!'); ";
			message = message + " location.href='" + req.getContextPath()+"/board/getBoardList';";
			message = message + " </script> ";
			
			return new ResponseEntity(message,responseHeaders,HttpStatus.OK);
		}else {
			message = "<script> ";
			message = message + " alert('?????? ??????!'); ";
			message = message + " location.href='" + req.getContextPath()+"/board/getBoardList';";
			message = message + " </script> ";
			
			return new ResponseEntity(message,responseHeaders,HttpStatus.OK);
		}
	}
	
	@Override
	@ResponseBody
	@RequestMapping(value = "/board/addComment.do" , method = RequestMethod.POST , produces = "application/text; charset=UTF-8")
	public String addComment(@RequestBody CommentVO commentVO, HttpServletRequest req, HttpServletResponse resp) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("addComment ??????");
		int count = 0;
		
		Map<String,Object> commentMap = new HashMap<String, Object>();
		
//		System.out.println("commentVO.getBoardNum() : "+commentVO.getBoardNum());
//		System.out.println("commentVO.getCommentContent() : "+commentVO.getCommentContent());
//		System.out.println("commentVO.getCommentDate() : "+commentVO.getCommentDate());
//		System.out.println("commentVO.getCommentGroup() : "+commentVO.getCommentGroup());
//		System.out.println("commentVO.getCommentHir() : "+commentVO.getCommentHir());
//		System.out.println("commentVO.getCommentID() : "+commentVO.getCommentID());
		
		
		
		count = boardService.addComment(commentVO);
		if(count!=0) {
			System.out.println("?????? ?????? ??????!");
		}else {
			System.out.println("?????? ?????? ??????");
		}
		commentList = boardService.getCommentList(commentVO.getBoardNum());
		
		for(CommentVO c : commentList) {
			System.out.println("commentNum : "+ c.getCommentNum());
		}
		
		commentMap.put("commentList", commentList);
		
		ObjectMapper mapper = new ObjectMapper();
		String jsonMap = mapper.writeValueAsString(commentMap);
		System.out.println(jsonMap);
		return jsonMap;
		
	}
	
	@Override
	@ResponseBody
	@RequestMapping(value = "/board/addComment2.do" , method = RequestMethod.POST , produces = "application/text; charset=UTF-8")
	public String addComment2(@RequestBody CommentVO commentVO, HttpServletRequest req, HttpServletResponse resp) throws Exception {
		// TODO Auto-generated method stub
		
		System.out.println("addComment2 ??????");
		int count = 0;
		
		Map<String,Object> commentMap = new HashMap<String, Object>();
		
		System.out.println("commentVO.getBoardNum() : "+commentVO.getBoardNum());
		System.out.println("commentVO.getCommentContent() : "+commentVO.getCommentContent());
		System.out.println("commentVO.getCommentDate() : "+commentVO.getCommentDate());
		System.out.println("commentVO.getCommentGroup() : "+commentVO.getCommentGroup());
		System.out.println("commentVO.getCommentHir() : "+commentVO.getCommentHir());
		System.out.println("commentVO.getCommentID() : "+commentVO.getCommentID());
		
		
		
		count = boardService.addComment(commentVO);
		if(count!=0) {
			System.out.println("??????2 ?????? ??????!");
		}else {
			System.out.println("??????2 ?????? ??????");
		}
		commentList = boardService.getCommentList(commentVO.getBoardNum());
		
		for(CommentVO c : commentList) {
			System.out.println("commentGroup : "+ c.getCommentGroup());
			System.out.println("commentHir : "+ c.getCommentHir());
		}
		
		commentMap.put("commentList", commentList);
		
		ObjectMapper mapper = new ObjectMapper();
		String jsonMap = mapper.writeValueAsString(commentMap);
		System.out.println(jsonMap);
		return jsonMap;

	}
	
	@Override
	@ResponseBody
	@RequestMapping(value = "/board/getCommentList.do" , method = RequestMethod.GET , produces = "application/text; charset=UTF-8")
	public String getCommentList(@RequestParam("boardNum") int boardNum, HttpServletRequest req, HttpServletResponse resp) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("getCommentList ??????");
		Map<String, Object> commentMap = new HashMap<String, Object>();
		
		commentList = boardService.getCommentList(boardNum);
		commentMap.put("commentList", commentList);
		
		ObjectMapper mapper = new ObjectMapper();
		String jsonMap = mapper.writeValueAsString(commentMap);
		
		return jsonMap;
	}
	
	@Override
	@RequestMapping(value = "/board/removeComment.do" , method = RequestMethod.POST , produces = "application/text; charset=utf-8")
	@ResponseBody
	public String removeComment(@RequestBody CommentVO commentVO, HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		// TODO Auto-generated method stub
		
		System.out.println("removeComment ??????");
		
		Map commentMap = new HashMap();
		
		int count = 0;
		int commentNum = commentVO.getCommentNum();
		int boardNum = commentVO.getBoardNum();
		System.out.println("commentContent : " + commentVO.getCommentContent());
		System.out.println("commentNum : " + commentNum);
		System.out.println("boardNum : " + boardNum);
		count = boardService.removeComment(commentNum);
		
		if(count!=0) {
			System.out.println("?????? ?????? ??????!");
			commentList = boardService.getCommentList(boardNum);
			commentMap.put("commentList", commentList);
			ObjectMapper mapper = new ObjectMapper();
			String jsonMap = mapper.writeValueAsString(commentMap);
			
			return jsonMap;
		}else {
			System.out.println("?????? ?????? ??????!");
			return null;
		}

	}
	
	@Override
	@RequestMapping(value = "/board/goodAndBad.do" , method = RequestMethod.POST)
	@ResponseBody
	public String goodAndBad(@RequestBody GoodAndBadVO goodAndBadVO, HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		// TODO Auto-generated method stub
		System.out.println("goodAndBad ??????");
		System.out.println("goodAndBadVO.getBoardNum() : "+goodAndBadVO.getBoardNum());
		System.out.println("goodAndBadVO.getId() : "+goodAndBadVO.getId());
		System.out.println("goodAndBadVO.getUp() : "+goodAndBadVO.getUp());
		System.out.println("goodAndBadVO.getDown() : "+goodAndBadVO.getDown());
		
		int up = goodAndBadVO.getUp();
		int down = goodAndBadVO.getDown();
		
		int countId = 0;
		int countByUp = 0;
		int countByDown = 0;
		
		Map<String,Object> goodAndBadMap = new HashMap<String, Object>();
		goodAndBadMap.put("boardNum", goodAndBadVO.getBoardNum());
		goodAndBadMap.put("id", goodAndBadVO.getId());
		
		countId = boardService.selectId(goodAndBadMap);
		// db??? ???????????? ?????? ?????? ?????? ???
		if(countId==0) {
			System.out.println("db??? ???????????? ?????? ?????? ?????? ???");
			boardService.insertUpAndDown(goodAndBadMap);
		}
		
		countByUp = boardService.selectCountUpById(goodAndBadMap);
		countByDown = boardService.selectCountDownById(goodAndBadMap);
		
		System.out.println("countByUp : " + countByUp);
		System.out.println("countByDown : " + countByDown);
		
		// up click 
		if(up == 1 && down == 0) {
			System.out.println("up click");
			// up??? ???????????? down??? ????????? ???(0,0)
			if(countByUp==0 && countByDown==0) {
				// up 1 ??????
				System.out.println("up??? ???????????? down??? ????????? ???(0,0)");
				goodAndBadMap.put("up", 1);
				goodAndBadMap.put("down", 0);
				boardService.updateUpDown(goodAndBadMap);
			}
			// up??? ?????? ?????? down??? ????????? ???(1,0)
			else if(countByUp!=0 && countByDown==0) {
				
				System.out.println("up??? ?????? ?????? down??? ????????? ???(1,0)");
				goodAndBadMap.put("up", 1);
				goodAndBadMap.put("down", 0);
				boardService.updateUpDown(goodAndBadMap);
			}
			// up??? ?????? ?????? down??? ?????? ?????? ??? - ?????? ?????? (1,1)
			
			// up??? ???????????? down??? ?????? ?????? ???(0,1)
			else if(countByUp==0 && countByDown!=0) {
				System.out.println("up??? ???????????? down??? ?????? ?????? ???(0,1)");
				goodAndBadMap.put("up", 0);
				goodAndBadMap.put("down", 0);
				boardService.updateUpDown(goodAndBadMap);
			}
		}
		// down click
		else if(up == 0 && down == 1) {
			System.out.println("down click");
			
			// up??? ???????????? down??? ????????? ???(0,0)
			if(countByUp==0 && countByDown==0) {
				System.out.println("up??? ???????????? down??? ????????? ???(0,0)");
				goodAndBadMap.put("up", 0);
				goodAndBadMap.put("down", 1);
				boardService.updateUpDown(goodAndBadMap);
			}
			// up??? ?????? ?????? down??? ????????? ???(1,0)
			else if(countByUp!=0 && countByDown==0) {
				System.out.println("up??? ?????? ?????? down??? ????????? ???(1,0)");
				goodAndBadMap.put("up", 0);
				goodAndBadMap.put("down", 0);
				boardService.updateUpDown(goodAndBadMap);
			}
			// up??? ?????? ?????? down??? ?????? ?????? ??? - ?????? ?????? (1,1)
			
			// up??? ???????????? down??? ?????? ?????? ???(0,1)
			else if(countByUp==0 && countByDown!=0) {
				System.out.println("up??? ???????????? down??? ?????? ?????? ???(0,1)");
				goodAndBadMap.put("up", 0);
				goodAndBadMap.put("down", 1);
				boardService.updateUpDown(goodAndBadMap);
			}
		}
		
		int allCountUp = boardService.selectUp(goodAndBadVO.getBoardNum());
		int allCountDown = boardService.selectDown(goodAndBadVO.getBoardNum());
		
		System.out.println("allCountUp : "+ allCountUp);
		System.out.println("allCountDown : "+ allCountDown);
		
		goodAndBadMap.clear();
		goodAndBadMap.put("allCountUp", allCountUp);
		goodAndBadMap.put("allCountDown", allCountDown);
		
		ObjectMapper mapper = new ObjectMapper();
		String jsonMap = mapper.writeValueAsString(goodAndBadMap);
		
		return jsonMap;
		
	}
	
	
	private List<String> upload(MultipartHttpServletRequest mReq) throws Exception{
		List<String> fileList = new ArrayList<String>();
		Iterator<String> fileNames = mReq.getFileNames();
		
		File temp = new File(ARTICLE_IMAGE_REPO_PATH+"\\"+"temp");
		
		if(!temp.exists()) {
			temp.mkdirs();
			System.out.println("temp ??????");
		}else {
			if(!temp.isDirectory()) {
				temp.delete();
				temp.mkdirs();
				System.out.println("temp ????????? ???????????? ?????? ??? ?????? ??????");
			}else if(temp.isFile()) {
				temp.delete();
				temp.mkdirs();
				System.out.println("temp ?????? ?????? ??? ?????? ??????");
			}
			/* System.out.println("temp ?????? ??????"); */
		}
		
		while(fileNames.hasNext()) {
			String fileName = fileNames.next(); // input file name ex file1...file2...
			MultipartFile mFile = mReq.getFile(fileName);
			String originalFileName = mFile.getOriginalFilename();
//			System.out.println(originalFileName);
			fileList.add(originalFileName);
			File file = new File(ARTICLE_IMAGE_REPO_PATH+"\\"+fileName);
			if(mFile.getSize()!=0) {
				if(!file.exists()) {
					if(file.getParentFile().mkdirs()) {
						file.createNewFile();
					}
				}
			}
			
			mFile.transferTo(new File(ARTICLE_IMAGE_REPO_PATH+"\\"+"temp"+"\\"+originalFileName));
		}
		return fileList;
	}
}
