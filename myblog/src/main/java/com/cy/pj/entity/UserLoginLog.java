package com.cy.pj.entity;

import java.io.Serializable;
import java.sql.Date;

import lombok.Data;
@Data

//登录日志
public class UserLoginLog implements Serializable{
	private static final long serialVersionUID = -8285190045720847942L;
	
	private Integer id;//日志主键
	private Integer userId;//关联user表
	private Date date;//登录日期
	private String ip;//登入IP
	private String action;//产生的动作
	private String data;//产生的数据
}
