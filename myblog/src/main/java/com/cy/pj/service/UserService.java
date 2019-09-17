package com.cy.pj.service;

import java.util.List;

import com.cy.pj.entity.Contents;
import com.cy.pj.entity.User;

public interface UserService {
	
		//插入注册信息
		int doinsertObjects(User entity);
	
		//根据查询用户信息
		public User doFindUser(String username);
		
		//根据id修改用户信息
		public Integer doUpdateObject(User entity);
		
		User findUserById(Integer id);
		List<Contents> doFindAllContents();
		
		//头像上传功能
		int addImage(String username, String sqlPath);

		String doinsertCollect(Integer userId, Integer contentsId);

		String doCheck(Integer userId, Integer contentsId);

		List<Contents> doFindAllCollect(Integer userId);

}
