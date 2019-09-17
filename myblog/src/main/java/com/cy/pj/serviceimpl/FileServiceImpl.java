package com.cy.pj.serviceimpl;

import java.awt.image.BufferedImage;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cy.pj.dao.FileDao;
import com.cy.pj.service.FileService;
@Service
public class FileServiceImpl implements FileService {
	//本地路径
	private String localFileDir="D:/jt-image/images/";
	//云服务器路径
	//private String localFileDir="/usr/local/src/image/";
	@Autowired
	private FileDao fileDao;
	@Override
	public String upLoad(MultipartFile uploadFile) {
		if(uploadFile ==null) {
			return "old";
		}
		System.err.println(uploadFile);
		String filename = uploadFile.getOriginalFilename();
		
		filename = filename.toLowerCase();
		
		if(!filename.matches("^.+\\.(jpg|png|gif)$")) {
			return "图片格式错误，请上传正确的图片";
		}
		
		try {
			BufferedImage bufferedImage = ImageIO.read(uploadFile.getInputStream());
			int height = bufferedImage.getHeight();
			int width = bufferedImage.getWidth();
			if(height==0||width==0) {
				return "图片内容错误，请上传正确的图片";
			}
			
			String dateDir = new SimpleDateFormat("yyyy/MM/dd").format(new Date());
			//准备文件上传路径拼接
			String localdir = localFileDir+dateDir;
			File dirFile = new File(localdir);
			if (!dirFile.exists()) {
				dirFile.mkdirs();
			}
			
			String uuid = UUID.randomUUID().toString().replace("-", "");
			
			String fileType = filename.substring(filename.lastIndexOf("."));
			
			String realFileName = uuid+fileType;
			
			String realPath = localdir+"/"+realFileName;
			File realFilePath = new File(realPath);
			uploadFile.transferTo(realFilePath);
			System.out.println(realFilePath);
			String str = "http://image.blog.com/"+dateDir+"/"+realFileName;	//本地路径-->利用nginx方向代理到本地路径(修改电脑hosts文件)
			//String str = "http://www.web.com/"+dateDir+"/"+realFileName;//云服务器上的路径
			System.out.println(str);
			return str;
		} catch (Exception e) {
			e.printStackTrace();
			return "上传失败";
		}
		
		
	}

}
