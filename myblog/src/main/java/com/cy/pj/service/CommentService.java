package com.cy.pj.service;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.cy.pj.entity.Comment;


public interface CommentService {

	int doInsertComment(Comment entity);

	List<String> doCommentedById(Integer cid);


}
