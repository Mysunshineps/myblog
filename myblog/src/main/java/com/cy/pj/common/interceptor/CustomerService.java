package com.cy.pj.common.interceptor;

import java.util.Set;

/**
 * @Description:
 * @Author:         psq
 * @CreateDate:     2021/2/22 14:21
 * @Version:        1.0
 */
public interface CustomerService {

    /**
     * 获取独立部署的客户编号
     * 若为null 则为sass 模式
     * 解析域名获取客户标识
     * @return
     */
    String getIndependentCustomerNo();

    /**
     * 获取所有 启用的客户标识集合
     * @return
     */
    Set<String> getCustomerNos();

    /**
     * 获取客户信息
     * @return
     */
    Customer getCustomer();

    /**
     *
     * @param customerNo
     * @return
     */
    Customer getCustomer(String customerNo);

    /**
     * 获取客户所有配置
     * @param customerNo
     * @return
     */
    CustomerConfig getConfig(String customerNo);

    /**
     * 获取客户所有配置
     * @return
     */
    CustomerConfig getConfig();

    /**
     * 获取当前客户标识
     * @return
     */
    String getCustomerNo();

    /**
     * 检查客户标识是否存在启用
     * @param no
     * @return
     */
    boolean checkCustomerNo(String no);

    /**
     * 设置客户标识
     * @param no
     */
    void setCustomerNo(String no);

    /**
     * 清除内存客户标识
     */
    void clearCustomerNos();

    /**
     * 设置 SASS 模式默认标识
     */
    boolean setSassDefaultNo();

    /**
     * 清除 SASS 模式默认标识
     */
    void revertSassDefaultNo();


}
