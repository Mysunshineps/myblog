package com.cy.pj.dao;

import java.util.List;
import java.util.Map;

import com.cy.pj.entity.CommentTemp;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.cy.pj.entity.Comment;

@Mapper
public interface CommentDao {

	//将评论信息插入数据库
	int doInsertComment(Comment entity);


	@Select("select c.content,c.created,u.username from comments c LEFT JOIN user u ON u.id = c.userId where c.cid = #{cid} order by c.created desc")
	List<Map<String,Object>> doCommentedById(Integer cid);

	List<CommentTemp> findCommentsByUserId(Integer userId);

	@Delete("delete from comments where coid = #{coid}")
	int deleteComment(Integer coid);
}
