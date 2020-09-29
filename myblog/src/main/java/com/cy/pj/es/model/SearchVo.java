package com.cy.pj.es.model;

import javax.persistence.Transient;

/**
 * @Description
 * @Author psq
 * @Date 2020/8/17/16:38
 */
public class SearchVo {

    //esId
    public Integer id;

    //保存的内容
    public String contents;

    @Transient
    private Integer fromNo;

    public SearchVo() {
    }

    public SearchVo(Integer id, String contents) {
        this.id = id;
        this.contents = contents;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public Integer getFromNo() {
        return fromNo;
    }

    public SearchVo setFromNo(Integer fromNo) {
        this.fromNo = fromNo;
        return this;
    }
}