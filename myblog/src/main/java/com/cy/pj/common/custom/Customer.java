package com.cy.pj.common.custom;

import com.alibaba.fastjson.JSON;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
* @Description:    客户实体类
* @Author:         psq
* @CreateDate:     2021/2/22 14:21
* @Version:        1.0
*/
@Table(name = "customer")
public class Customer implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 客户标识
     */
    @Column(name = "customer_no")
    private String customerNo;

    /**
     * 客户名称
     */
    @Column(name = "customer_name")
    private String customerName;

    /**
     * 数据库url
     */
    @Column(name = "datasource_url")
    private String datasourceUrl;

    /**
     * 数据库账号
     */
    @Column(name = "datasource_username")
    private String datasourceUsername;

    /**
     * 数据库 密码
     */
    @Column(name = "datasource_password")
    private String datasourcePassword;

    @Column(name = "spring_datasource_slave_url")
    private String springDatasourceSlaveUrl;


    @Column(name = "spring_datasource_slave_username")
    private String springDatasourceSlaveUsername;

    @Column(name = "spring_datasource_slave_password")
    private String springDatasourceSlavePassword;

    /**
     * 默认0 状态：1：启用，0：禁用
     */
    @Column(name = "status")
    private Integer status;

    /**
     * 备注
     */
    @Column(name = "remarks")
    private String remarks;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private Date updateTime;

    @Override
    public String toString(){
        return JSON.toJSONString(this);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCustomerNo() {
        return customerNo;
    }

    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getDatasourceUrl() {
        return datasourceUrl;
    }

    public void setDatasourceUrl(String datasourceUrl) {
        this.datasourceUrl = datasourceUrl;
    }

    public String getDatasourceUsername() {
        return datasourceUsername;
    }

    public void setDatasourceUsername(String datasourceUsername) {
        this.datasourceUsername = datasourceUsername;
    }

    public String getDatasourcePassword() {
        return datasourcePassword;
    }

    public void setDatasourcePassword(String datasourcePassword) {
        this.datasourcePassword = datasourcePassword;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getSpringDatasourceSlaveUrl() {
        return springDatasourceSlaveUrl;
    }

    public void setSpringDatasourceSlaveUrl(String springDatasourceSlaveUrl) {
        this.springDatasourceSlaveUrl = springDatasourceSlaveUrl;
    }

    public String getSpringDatasourceSlaveUsername() {
        return springDatasourceSlaveUsername;
    }

    public void setSpringDatasourceSlaveUsername(String springDatasourceSlaveUsername) {
        this.springDatasourceSlaveUsername = springDatasourceSlaveUsername;
    }

    public String getSpringDatasourceSlavePassword() {
        return springDatasourceSlavePassword;
    }

    public void setSpringDatasourceSlavePassword(String springDatasourceSlavePassword) {
        this.springDatasourceSlavePassword = springDatasourceSlavePassword;
    }
}
