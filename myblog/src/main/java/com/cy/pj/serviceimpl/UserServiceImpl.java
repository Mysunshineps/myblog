package com.cy.pj.serviceimpl;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.cy.pj.common.exception.ServiceException;
import com.cy.pj.dao.ContentsDao;
import com.cy.pj.dao.UserDao;
import com.cy.pj.entity.Contents;
import com.cy.pj.entity.User;
import com.cy.pj.service.UserService;

@Service
public class UserServiceImpl implements UserService{

	@Autowired
	private UserDao userDao;
	@Autowired
	private ContentsDao contentsDao;
	
	@Override
	public int doinsertObjects(User entity) {
		System.out.println(entity);
		if(entity==null)
			throw new ServiceException("保存对象不能为空");
		if(StringUtils.isEmpty(entity.getUsername()))
			throw new ServiceException("用户名不能为空");
		if(StringUtils.isEmpty(entity.getPassword()))
			throw new ServiceException("密码不能为空");
		if(!entity.getPassword().equals(entity.getRepassword())) 
			throw new ServiceException("两次输入的密码不一致,请重新输入");
		String salt=UUID.randomUUID().toString();
		SimpleHash sh=new SimpleHash("MD5",entity.getPassword(),salt, 1);
		entity.setPassword(sh.toHex());
		entity.setSalt(salt);
		int rows=userDao.insertObjects(entity);
		return rows;
	}

	@Override
	public User doFindUser(String username) {
		//1.参数的合法性验证
		if(username==null)
			throw new IllegalArgumentException("用户名不能为空");
		//2.进行查询用户信息
		User user = userDao.findUserByUserName(username);
		System.out.println();
		System.out.println(username);
		if(user==null) {
			throw new ServiceException("该用户信息可能已经不存在,请尝试重新登录");
		}
		
		return user;
	}


	//根据用户id修改用户信息
	@Override
	public Integer doUpdateObject(User entity) {
		//1.参数的合法性验证
		if(entity.getUsername()==null&&entity.getEmail()==null&&entity.getGender()==null
			&&entity.getHeadUrl()==null&&entity.getHomeUrl()==null&&entity.getPassword()==null&&entity.getRepassword()==null)
			throw new IllegalArgumentException("用户信息的任意一项不能为空");
		User user = (User)SecurityUtils.getSubject().getPrincipal();
		SimpleHash sh=new SimpleHash("MD5",entity.getPassword(),user.getSalt(),1);
				if(!user.getPassword().equals(sh.toHex()))
				throw new IllegalArgumentException("原密码不正确");
				//3.对新密码进行加密
				String salt=UUID.randomUUID().toString();
				sh=new SimpleHash("MD5",entity.getRepassword(),salt, 1);
				
				//4.将新密码加密以后的结果更新到数据库
				
				entity.setPassword(sh.toHex());
				
				entity.setSalt(salt);
				System.out.println("2.UserServiceImpl"+entity);
				int rows;
				if(entity.getHeadUrl().equals("old")&&entity.getHomeUrl().equals("old")) {
					rows = userDao.doNoupdateHome(entity);
				}else {
					rows=userDao.doUpdateObject(entity);
				}
				if(rows==0)
				throw new ServiceException("修改失败");
				return rows;
	}
	
	@Override
	public User findUserById(Integer id) {
		return userDao.findUserById(id);
	}
	@Override
	public List<Contents> doFindAllContents() {
		return contentsDao.findContents();
	}
	
	
	//头像上传功能
	@Override
	public int addImage(String username, String sqlPath) {
		int rows = userDao.addImage(username, sqlPath);
		return rows;
	}
	
	//收藏功能
	@Override
	public String doinsertCollect(Integer userId, Integer contentsId) {
		int rows = userDao.docheck(userId,contentsId);
		if(rows==0) {
			int row = userDao.insertCollect(userId,contentsId);
			if(row==0) {
				return "收藏失败";
			}else {
				return "收藏成功";
			}
		}else {
			int row = userDao.doCancel(userId,contentsId);
			if(row == 0) {
				return "取消收藏失败";
			}else {
				return "已取消收藏";
			}
		}
		
	}

	@Override
	public String doCheck(Integer userId, Integer contentsId) {
		int rows = userDao.docheck(userId,contentsId);
		if(rows == 0) {
			return "收藏";
		}
		return "取消收藏";
	}

	@Override
	public List<Contents> doFindAllCollect(Integer userId) {
		List<Integer> contentIds = contentsDao.findContentsByuserId(userId);
		System.err.println("contentIds:"+contentIds);
		return contentsDao.findCollects(contentIds);
	}


}











