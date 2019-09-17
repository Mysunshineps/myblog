package com.cy.pj.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.cy.pj.service.FileService;

@Controller
@RequestMapping("/file")
public class FileController {
	@Autowired
	private FileService fileService;
	
	//file/uload
	
	@RequestMapping(value = "/upload",method = RequestMethod.POST)
	@ResponseBody
	public String fileUpLoad(MultipartFile uploadFile) {
		String str=fileService.upLoad(uploadFile);
		System.out.println("str:"+str);
		return str;
	}
	
}
