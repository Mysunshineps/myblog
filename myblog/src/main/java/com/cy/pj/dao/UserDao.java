package com.cy.pj.dao;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.cy.pj.entity.User;

import java.util.Map;

@Mapper
public interface UserDao {

	//将注册信息插入数据库
	int insertObjects(User entity);

	int ifExistUserName(String userName);

	//查询信息
	User findUserByUserName(String username);

	//修改用户信息
	Integer doUpdateObject(User entity);

	User findUserById(Integer id);

	Integer addImage(@Param("username")String username,
							@Param("headUrl")String sqlPath);

	//修改用户信息，但不修改头像及其主页图片
	int doNoupdateHome(User entity);

	//判断用户文章表是否有此数据
	int docheck(Integer userId, Integer contentsId);

	//根据上面的，若没有则插入数据-->收藏成功
	int insertCollect(Integer userId, Integer contentsId);

	//根据上面的，若有则删除-->取消收藏
	int doCancel(Integer userId, Integer contentsId);

	void uploadImage(Map<String, String> params);

}
