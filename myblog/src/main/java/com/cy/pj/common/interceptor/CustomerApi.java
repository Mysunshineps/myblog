package com.cy.pj.common.interceptor;

import com.cy.pj.common.exception.NoSuchCustomerException;
import com.cy.pj.sys.utils.SpringUtils;

/**
* @Description:    客户配置API
 *  可通过此类获取客户配置信息，不可在类加载过程中调用此类方法
 * @Author:         psq
 * @CreateDate:     2021/2/22 14:21
* @Version:        1.0
*/
public class CustomerApi {
    private static volatile CustomerService instance = null;
    private CustomerApi(){}
    public static CustomerService getInstance(){
        synchronized (CustomerApi.class){
            if (instance == null){
                instance = SpringUtils.getBean("CustomerServiceImpl", CustomerService.class);
            }
        }
        return instance;
    }

    /**
     * 获取当前线程副本的客户编码
     * @return
     */
    public static String getCustomerNo(){
        return getInstance().getCustomerNo();
    }

    /**
     * 设置客户编码到当前线程副本
     * @param no
     */
    public static void setCustomerNo(String no){
        getInstance().setCustomerNo(no);
    }

    /**
     * 校验该客户是否被启用 并设置到 线程副本
     * 无此客户抛出异常 NoSuchCustomerException
     * @param no
     */
    public static void checkCustomerNo(String no){
        boolean flag = getInstance().checkCustomerNo(no);
        if (!flag){
            throw new NoSuchCustomerException();
        }
        CustomerApi.setCustomerNo(no);
    }

    /**
     * 获取独立部署的客户编号
     * 若为null 则为sass 模式
     * 解析域名获取客户标识
     * @return
     */
    public static String getIndependentCustomerNo(){
        return getInstance().getIndependentCustomerNo();
    }
}
