package com.cy.pj.serviceimpl;

import java.net.ConnectException;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.cy.pj.es.model.SearchVo;
import com.cy.pj.es.service.EsSearchServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
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

	@Autowired
	private EsSearchServiceImpl esSearchService;

	@Override
	public Contents findContentById(Integer cid) {
		Contents contents = new Contents();
		try {
			SearchVo searchVo = esSearchService.selectContentById(cid + "");
			if (null != searchVo && StringUtils.isNotBlank(searchVo.getContents())){
				contents = JSON.parseObject(searchVo.getContents(), Contents.class);
			}else {
				contents=contentsDao.findContentById(cid);
				esSearchService.saveOrUpdate(new SearchVo(contents.getCid(), JSON.toJSONString(contents)));
			}
		} catch (Exception e) {
			contents=contentsDao.findContentById(cid);
			return contents;
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
