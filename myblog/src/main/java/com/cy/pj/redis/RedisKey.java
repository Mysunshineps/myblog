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

        /**
         * 客户七牛云 配置
         */
        String CONFIG_QINIU = ".customer.config.qiniu";

        /**
         * 启用的客户编号集合
         */
        String NOS = ".customer.nos";
    }

    interface contents{
        String CONTENTS_INFO = "contents_cid_";

    }

    interface collects{
        String COLLECTS = "collects_contentId_";

    }

}