package com.cy.pj.entity;

import java.io.Serializable;
import java.sql.Date;

import lombok.Data;
@Data
//评论表
public class Comment implements Serializable {
	private static final long serialVersionUID = -1776983448704619573L;
	
	/*
	 * private Integer id; private String name;//评论人名 private Integer
	 * contentsId;//关联contents文章表 private String content;//评论内容 private Date
	 * created;//评论时间 private String email;//评论人的email
	 */	
	
	 /**
     * comment表主键
     */
    private Integer coid;
    /**
     * contents表主键,关联字段
     */
    private Integer cid;
    /**
     * 评论生成时的GMT unix时间戳-->后来为了方便改成date类型了
     */
    private Date created;
    /**
     * 评论作者
     */
    private String author;
    /**
     * 评论所属用户id
     */
    private String authorId;
    /**
     * 评论所属内容作者id
     */
    private Integer ownerId;
    /**
     * 评论者邮件
     */
    private String mail;
    /**
     * 评论者网址
     */
    private String url;
    /**
     * 评论者ip地址
     *
     */
    private String ip;
    /**
     * 评论者客户端
     */
    private String agent;
    /**
     * 评论类型
     */
    private String type;
    /**
     * 评论状态
     */
    private String status;
    /**
     * 父级评论
     */
    private Integer parent;
    /**
     * 评论内容
     */
    private String content;
	
	
}
