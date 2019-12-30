package com.cy.pj.serviceimpl;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import com.cy.pj.dao.ContentsDao;
import com.cy.pj.entity.Contents;
import com.cy.pj.service.ContentsService;

@Service
public class ContentsServiceImpl implements ContentsService{
	@Autowired
	private ContentsDao contentsDao;
	
	@Autowired
	private RedisTemplate redisTemplate;
	@Override
	public Contents findContentById(Integer cid) {
		Contents contents=contentsDao.findContentById(cid);
		return contents;
	}
	@Override
	public List<Contents> findContents(Integer id) {
		return null;
	}
	@Override
	public Contents findPreContent(Integer cid) {
		return contentsDao.findPreContent(cid);
	}
	@Override
	public Contents findNextContent(Integer cid) {
		return contentsDao.findNextContent(cid);
	}
	
}
