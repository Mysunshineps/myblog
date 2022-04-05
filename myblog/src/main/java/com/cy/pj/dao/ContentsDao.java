package com.cy.pj.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.cy.pj.entity.Contents;

@Mapper
public interface ContentsDao {
	
	
	Contents findContentById(Integer cid);

	List<Contents> findContents();	 
	
	Contents findPreContent(Integer cid);
	
	Contents findNextContent(Integer cid);
	
	List<Integer> selectIdsByUserId(Integer userId);

	List<Contents> selectCollectsByUseId(Integer userId);
	
}
