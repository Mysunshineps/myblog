package com.cy.pj.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.cy.pj.entity.Comment;

@Mapper
public interface CommentDao {
	
	//将评论信息插入数据库
	public int doInsertComment(Comment entity);

	
	@Select("select content from comments where cid = #{cid}")
	public List<String> doCommentedById(Integer cid);
	
}
