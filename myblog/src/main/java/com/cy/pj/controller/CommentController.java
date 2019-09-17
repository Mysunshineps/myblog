package com.cy.pj.controller;


import java.util.List;

import javax.validation.Valid;

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
		List<String> list= commentService.doCommentedById(cid);
		System.err.println("查询的评论历史数据："+list);
		return new JsonResult(list);
	}
	
	
	
	
}
