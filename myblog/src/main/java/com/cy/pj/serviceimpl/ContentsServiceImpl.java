package com.cy.pj.serviceimpl;

import java.util.List;

import com.cy.pj.common.custom.CustomerApi;
import com.cy.pj.dao.enums.Constants;
import com.cy.pj.redis.RedisKey;
import com.cy.pj.redis.StringRedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cy.pj.dao.ContentsDao;
import com.cy.pj.entity.Contents;
import com.cy.pj.service.ContentsService;

@Service
public class ContentsServiceImpl implements ContentsService{
	@Autowired
	private ContentsDao contentsDao;

	@Autowired
	private StringRedisService stringRedisService;

	@Override
	public Contents findContentById(Integer cid) {
		Contents contents = new Contents();
		try {
			String redisKey = RedisKey.contents.CONTENTS_INFO + CustomerApi.getCustomerNo()+ Constants.UNDERLINE +cid;
			contents = stringRedisService.getContent(redisKey);
			if (null != contents){
				return contents;
			}
			contents = contentsDao.findContentById(cid);
			stringRedisService.set(redisKey,contents,10*1000L);
		} catch (Exception e) {
			e.printStackTrace();
			contents = contentsDao.findContentById(cid);
		}
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
