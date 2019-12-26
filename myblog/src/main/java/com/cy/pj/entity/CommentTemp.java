package com.cy.pj.entity;

import lombok.Data;

import java.io.Serializable;

@Data
//评论表
public class CommentTemp implements Serializable {
	private static final long serialVersionUID = -1776983448704619573L;

    /**
     * comments主键id
     */
	private Integer coid;
    /**
     * contents表主键,关联字段
     */
    private Integer cid;
    /**
     * 评论生成时的GMT unix时间戳-->后来为了方便改成date类型了
     */
    private String created;

    /**
     *  user的主键ID
     */
    private Long userId;

    /**
     * 内容文字
     */
    private String content;

    /**
     * 内容标题
     */
    private String title;
}
