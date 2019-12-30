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


	@RequestMapping(value = "/upload/headImage",method = RequestMethod.POST)
	@ResponseBody
	public String fileUpLoadHeadImage(MultipartFile uploadFile) {
		String str=fileService.upLoad(uploadFile);
		if (str.equals("error")){
			return "200";
		}else {
			return str;
		}
	}

	@RequestMapping(value = "/upload/homeImage",method = RequestMethod.POST)
	@ResponseBody
	public String fileUpLoadHomeImage(MultipartFile uploadFile) {
		String str=fileService.upLoad(uploadFile);
		if (str.equals("error")){
			return "200";
		}else {
			return str;
		}
	}
}
