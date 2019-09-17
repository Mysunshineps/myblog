package com.cy.pj.service;

import java.util.List;

import com.cy.pj.entity.Contents;

public interface ContentsService {
	Contents findContentById(Integer cid);
	
	List<Contents> findContents(Integer id);
	
	Contents findPreContent(Integer cid);
	Contents findNextContent(Integer cid);
}
