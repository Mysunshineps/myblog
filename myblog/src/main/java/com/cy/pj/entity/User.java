package com.cy.pj.entity;
import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Table;

//用户表
@Table(name = "user")
public class User implements Serializable{
	private static final long serialVersionUID = 3691120159791053492L;
	
	private Integer id;
	private String username;//用户名
	private String password;
	private String repassword;
	private String gender;
	private String salt;//盐值

	@Column(name = "head_url")
	private String headUrl;//头像

	@Column(name = "home_url")
	private String homeUrl;//主页图片
	private String email;//邮件

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRepassword() {
		return repassword;
	}

	public void setRepassword(String repassword) {
		this.repassword = repassword;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public String getHeadUrl() {
		return headUrl;
	}

	public void setHeadUrl(String headUrl) {
		this.headUrl = headUrl;
	}

	public String getHomeUrl() {
		return homeUrl;
	}

	public void setHomeUrl(String homeUrl) {
		this.homeUrl = homeUrl;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
