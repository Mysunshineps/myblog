package com.cy.pj.redis;

/**
 * redis key
 */
public interface RedisKey {

    interface customer{

        String CUSTOMER = ".customer.";
        /**
         * 客户基础信息
         * 存放于sass 库
         */
        String BASICS = ".customer.basics.";
        /**
         * 客户配置信息
         * 存放于sass 库
         */
        String CONFIG = ".customer.config.";
//        /**
//         * 客户启用三方平台设置
//         */
//        String BASICS_PLATFORM = ".customer.basics.platform";
//        /**
//         * 客户美团配置
//         */
//        String CONFIG_MEITUAN = ".customer.config.meituan";
//        /**
//         * 客户京东配置
//         */
//        String CONFIG_JD = ".customer.config.jd";
//        /**
//         * 客户饿了么配置
//         */
//        String CONFIG_ELEM = ".customer.config.elem";
//        /**
//         * 客户有赞配置
//         */
//        String CONFIG_YOUZAN = ".customer.config.youzan";
//        /**
//         * 客户CRM 配置
//         */
//        String CONFIG_CRM = ".customer.config.crm";
//        /**
//         * 客户ERP 配置
//         */
//        String CONFIG_ERP = ".customer.config.erp";
        /**
         * 客户七牛云 配置
         */
        String CONFIG_QINIU = ".customer.config.qiniu";
//        /**
//         * 客户 有赞 token
//         */
//        String TOKEN_YOUZAN = ".customer.token.youzan";
//        /**
//         * 客户 饿了么 token
//         */
//        String TOKEN_ELEM = ".customer.token.elem";
//        /**
//         * 客户 京东 token
//         */
//        String TOKEN_JD = ".customer.token.jd";
        /**
         * 启用的客户编号集合
         */
        String NOS = ".customer.nos";
    }

    String CONTENTS = "contents.";

    interface collects{
        String COLLECTS = "collects_contentId_";
    }

}