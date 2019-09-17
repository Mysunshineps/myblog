package com.cy.pj.entity;
import java.io.Serializable;

import lombok.Data;

@Data
//用户表
public class User implements Serializable{
	private static final long serialVersionUID = 3691120159791053492L;
	
	private Integer id;
	private String username;//用户名
	private String password;
	private String repassword;
	private String gender;
	private String salt;//盐值
	private String headUrl;//头像
	private String homeUrl;//主页图片
	private String email;//邮件
}
