package com.cy.pj.common.custom;

import com.alibaba.fastjson.JSON;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @Description:    客户配置实体类
 * @Author:         psq
 * @CreateDate:     2021/2/22 14:25
 * @Version:        1.0
 */
@Table(name = "customer_config")
public class CustomerConfig implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 客户标识
     */
    @Column(name = "customer_no")
    private String customerNo;

    /**
     * 七牛云配置：访问key
     */
    @Column(name = "qiniu_access_key")
    private String qiniuAccessKey;

    /**
     * 七牛云配置：密钥
     */
    @Column(name = "qiniu_secret_key")
    private String qiniuSecretKey;

    /**
     * 七牛云配置：标识
     */
    @Column(name = "qiniu_bucket")
    private String qiniuBucket;

    /**
     * 七牛云配置：前缀url
     */
    @Column(name = "qiniu_bucket_url")
    private String qiniuBucketUrl;

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

    public String getQiniuAccessKey() {
        return qiniuAccessKey;
    }

    public void setQiniuAccessKey(String qiniuAccessKey) {
        this.qiniuAccessKey = qiniuAccessKey;
    }

    public String getQiniuSecretKey() {
        return qiniuSecretKey;
    }

    public void setQiniuSecretKey(String qiniuSecretKey) {
        this.qiniuSecretKey = qiniuSecretKey;
    }

    public String getQiniuBucket() {
        return qiniuBucket;
    }

    public void setQiniuBucket(String qiniuBucket) {
        this.qiniuBucket = qiniuBucket;
    }

    public String getQiniuBucketUrl() {
        return qiniuBucketUrl;
    }

    public void setQiniuBucketUrl(String qiniuBucketUrl) {
        this.qiniuBucketUrl = qiniuBucketUrl;
    }
}
