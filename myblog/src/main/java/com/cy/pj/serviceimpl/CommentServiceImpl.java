package com.cy.pj.serviceimpl;

import java.sql.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cy.pj.common.exception.ServiceException;
import com.cy.pj.dao.CommentDao;
import com.cy.pj.entity.Comment;
import com.cy.pj.service.CommentService;

@Service
public class CommentServiceImpl implements CommentService{
	
	@Autowired
	private CommentDao commentDao;

	
	//评论人（author）、时间（created）、评论内容（content）、评论人邮箱（mail）、评论人网址（url）
	//评论人（author）从前一个网页的cookie里取
	//评论内容（content）、评论人邮箱（mail）从前端取
	//时间（created）取当前系统时间
	@Override
	public int doInsertComment(Comment entity) {
		if(entity.getContent()==null)
			throw new ServiceException("评论内容不能为空");
		
		//获得当前时间
		long date = System.currentTimeMillis();
		entity.setCreated(new Date(date));
		int rows=commentDao.doInsertComment(entity);
		return rows;
	}


	@Override
	public List<String> doCommentedById(Integer cid) {
		List<String> list= commentDao.doCommentedById(cid);
		return list;
	}



}
