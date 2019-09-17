package com.cy.pj.entity;


import java.io.Serializable;
import java.sql.Date;

import lombok.Data;
@Data
//文章表
public class Contents implements Serializable{
	private static final long serialVersionUID = 3173973172720071187L;
	
	/**
     * 文章的主键编号
     */
    private Integer cid;
    /**
     * 内容标题
     */
    private String title;
    /**
     * 标题图片
     */
    private String titlePic;
    /**
     * 内容缩略名
     */
    private String slug;
    /**
     * 内容生成时的GMT unix时间戳
     */
    private String created;
    /**
     * 内容更改时的GMT unix时间戳
     */
    private Integer modified;
    /**
     * 内容文字
     */
    private String content;
    /**
     * 内容所属用户id
     */
    private Integer userId;
    /**
     * 内容类别
     */
    private String type;
    /**
     * 内容状态
     */
    private String status;
    /**
     * 标签列表
     */
    private String tags;
    /**
     * 分类种类
     */
    private String catelogName;
    /**
     * 点击次数
     */
    private Integer hits;
    /**
     * 内容所属评论数
     */
    private Integer commentsNum;
    /**
     * 是否允许评论
     */
    private Integer allowComment;
    /**
     * 是否允许ping
     */
    private Integer allowPing;
    /**
     * 允许出现在集合中
     */
    private Integer allowFeed;
}