package com.cy.pj.controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class PageController {
	//登录界面
	@RequestMapping("dologin")
	public String dologin() {
		return "login";
	}
	//跳转到注册页面
	@RequestMapping("doregister")
	public String doregister() {
		return "register";
	}
	
	//跳转到主页面
	@RequestMapping("doindex")
	public String doindex() {
		return "index";
	}

	@RequestMapping("Index")
	public String index() {
		return "index";
	}
	
	//退出
	@RequestMapping("doSignOut")
	public String doSignOut() {
		return "login";
	}
	
	//个人设置
	@RequestMapping("doProFile")
	public String doProFile() {
		return "user_edit";
	}
	
	//内容
	@RequestMapping("doSingle")
	public String doSingle() {
		return "single_edit";
	}
	
	//跳转到评论
	@RequestMapping("doPingLun")
	public String doPingLun() {
		return "comment";
	}
	
	//跳转到高德地图定位
	@RequestMapping("doDiTu")
	public String doDiTu() {
		return "gaodeditu";
	}
	
	//跳转到动态页面
	@RequestMapping("doAction")
	public String doAciton() {
		return "action";
	}
	
	//从个人收藏返回到主页
	@RequestMapping("doPre")
	public String doPre() {
		return "index";
	}
}
