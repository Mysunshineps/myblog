package com.cy.pj.service;

import java.util.List;
import java.util.Map;

import com.cy.pj.entity.CommentTemp;
import org.apache.ibatis.annotations.Mapper;

import com.cy.pj.entity.Comment;


public interface CommentService {

	int doInsertComment(Comment entity);

	List<Map<String,Object>> doCommentedById(Integer cid);

	List<CommentTemp> findCommentsByUserId(Integer userId);

	int deleteComment(Integer coid);
}
