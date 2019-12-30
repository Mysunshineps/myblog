package com.cy.pj.controller;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import com.cy.pj.entity.CommentTemp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cy.pj.common.vo.JsonResult;
import com.cy.pj.entity.Comment;
import com.cy.pj.service.CommentService;


@Controller
@RequestMapping("/comment/")
public class CommentController {
	@Autowired
	private CommentService commentService;
	
	@RequestMapping("doInsertComment")
	@ResponseBody
	public JsonResult doInsertComment(@Valid Comment entity) {
		commentService.doInsertComment(entity);
		return new JsonResult("评论成功，即将跳转到主页");
	}
	
	
	@RequestMapping("doCommentedById")
	@ResponseBody
	public JsonResult doCommentedById(Integer cid) {
		List<Map<String,Object>> list= commentService.doCommentedById(cid);
		List<String> commentList = new ArrayList<>();
		for (int i=0; i<list.size();i++){
			Map<String, Object> map = list.get(i);
			commentList.add(map.get("username")+"["+map.get("created")+"]："+map.get("content"));
		}
		return new JsonResult(commentList);
	}

	@RequestMapping("doFindAllComment")
	@ResponseBody
	public List<CommentTemp> doFindAllComment(Integer userId) {
		List<CommentTemp> commentTempList = commentService.findCommentsByUserId(userId);
		return commentTempList;
	}

	@RequestMapping("deleteComment")
	@ResponseBody
	public JsonResult deleteComment(Integer coid) {
		int i = commentService.deleteComment(coid);
		if (i > 0){
			return new JsonResult();
		}else {
			return new JsonResult("error");
		}
	}
	
	
}
