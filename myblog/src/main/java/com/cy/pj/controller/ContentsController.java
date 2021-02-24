package com.cy.pj.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cy.pj.entity.Contents;
import com.cy.pj.service.ContentsService;

@Controller
@RequestMapping("/admin")
public class ContentsController {
	@Autowired
	private ContentsService contentsService;
	@RequestMapping("doLoadContentUI") 
	public String doLoadContentUI() {
		return "single_edit"; 
	}
	@RequestMapping("doContentUI") 
	@ResponseBody
	public Contents doContentUI(Integer cid) {
		Contents con = contentsService.findContentById(cid); 
		return con; 
	}

	@RequestMapping("doPreContents")
	@ResponseBody 
	public Contents doPreContents(Integer cid) { 
		Contents con =
			contentsService.findPreContent(cid); 
			return con; 
	}

	@RequestMapping("doNextContents")
	@ResponseBody 
	public Contents doNextContents(Integer cid) { 
		Contents con =
			contentsService.findNextContent(cid); 
			return con; 
	}
	
}
