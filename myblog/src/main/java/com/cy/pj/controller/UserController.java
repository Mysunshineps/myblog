package com.cy.pj.controller;

import java.io.File;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.json.JSONTokener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.cy.pj.common.vo.JsonResult;
import com.cy.pj.entity.Contents;
import com.cy.pj.entity.User;
import com.cy.pj.service.UserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/user/")
public class UserController {
	
	@Autowired
	private UserService userService;
	@RequestMapping("doLogin")
	@ResponseBody
	//用户名权限控制
	public JsonResult doLogin(String username,
			String password) {
		//System.err.println("1.doLogin"+username+password);
		//1.获取Subject对象
		Subject subject=SecurityUtils.getSubject();
		//2.提交用户信息
		UsernamePasswordToken token=new UsernamePasswordToken();
		token.setUsername(username);
		token.setPassword(password.toCharArray());
		subject.login(token);//提交给SecurityManager
		//log.info("subject-----------"+subject);
		return new JsonResult("登陆成功!");
	}
	
	//初始化检查是否收藏，并显示到浏览器上
	@RequestMapping("docheck")
	@ResponseBody
	public JsonResult doCheck(Integer userId,Integer contentsId) {
		String check = userService.doCheck(userId,contentsId);
		return new JsonResult(check);
	}
	
	//收藏文章
	@RequestMapping("doCollect")
	@ResponseBody
	public JsonResult doCollect(Integer userId,Integer contentsId) {
		System.out.println("userId:"+userId+" contentsId:"+contentsId);
		String str = userService.doinsertCollect(userId,contentsId);
		System.out.println(str);
		return new JsonResult(str);
	}
	
	@RequestMapping("doRegister")
	@ResponseBody
	public JsonResult doRegister(@Valid User entity) {
		//System.out.println(entity);
		userService.doinsertObjects(entity);
		return new JsonResult("注册成功!");
	}
	
	    //根据用户名查询用户信息并返回
		@RequestMapping("doFindUser")
		@ResponseBody
		public JsonResult doFindUser(String username) {
				String  newStingString= new JSONTokener(username).nextValue().toString();
				System.out.println(newStingString);
				return new JsonResult(userService.doFindUser(username));
			}
		
	    //修改用户信息
		@RequestMapping("doUpdateObject")
		@ResponseBody	
		public JsonResult doUpdateObject(User entity) {
				userService.doUpdateObject(entity);
				System.out.println("头像："+entity.getHeadUrl());
				return new JsonResult("修改成功!");
			}
		
			
			
			
		@RequestMapping("doLoadIndexUI")
			public String doLoadIndexUI() {
				return "index";	
			}
		
		@RequestMapping("doFindUserId")
		@ResponseBody
			public User doFindUserId(Integer id) {
				return userService.findUserById(id);
			}
		
		@RequestMapping("doFindAllContents")
		@ResponseBody
		public List<Contents> doFindAllContents() {	
			//System.err.println(userService.findContents(id).toString());
			return userService.doFindAllContents();
			}
		
		@RequestMapping("doFindAllCollect")
		@ResponseBody
		public List<Contents> doFindAllCollect(Integer userId) {	
			return userService.doFindAllCollect(userId);
			}
		
	
}
