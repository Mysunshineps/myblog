package com.cy.pj.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class UserLoginLogController {
	
	/* 1.返回主页 */
	@RequestMapping("doIndexUI")
	private String doIndexUI() {
		return "index";
	}
}
