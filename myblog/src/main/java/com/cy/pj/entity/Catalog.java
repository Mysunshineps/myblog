package com.cy.pj.entity;

import java.io.Serializable;

import lombok.Data;

@Data
//种类表
public class Catalog implements Serializable {
	private static final long serialVersionUID = -3299198678120169580L;
	
	private Integer id;
	private String name;//种类名
	private String keywords;//关键字
	private String desc;//描述
	private Integer type;//先不用管，多余的
}
