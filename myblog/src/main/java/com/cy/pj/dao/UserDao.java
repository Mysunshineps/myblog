package com.cy.pj.dao;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.cy.pj.entity.User;

@Mapper
public interface UserDao {
	
		//将注册信息插入数据库
		int insertObjects(User entity);
		
		//查询信息
		@Select("select *from user where username=#{username}")
		User findUserByUserName(String username);
		
		//修改用户信息
		public Integer doUpdateObject(User entity);
		
		
		@Select("select * from user where id=#{id}")
		User findUserById(Integer id);
		
		@Update("update user set head_url=#{headUrl} where username=#{username}")
		public Integer addImage(@Param("username")String username, 
					@Param("headUrl")String sqlPath);
		
		//修改用户信息，但不修改头像及其主页图片
		int doNoupdateHome(User entity);
		
		//判断用户文章表是否有此数据
		@Select("select count(*) from usercontents where user_id=#{userId} and contents_id=#{contentsId}")
		int docheck(Integer userId, Integer contentsId);
		
		//根据上面的，若没有则插入数据-->收藏成功
		@Insert("insert into usercontents (user_id,contents_id) values (#{userId},#{contentsId})")
		int insertCollect(Integer userId, Integer contentsId);
		
		//根据上面的，若有则删除-->取消收藏
		@Delete("delete from usercontents where user_id=#{userId} and contents_id=#{contentsId}")
		int doCancel(Integer userId, Integer contentsId);
}
