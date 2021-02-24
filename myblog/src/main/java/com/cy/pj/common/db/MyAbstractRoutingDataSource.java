package com.cy.pj.common.db;

import com.cy.pj.common.exception.NoSuchCustomerException;
import com.cy.pj.common.interceptor.Customer;
import com.cy.pj.common.interceptor.CustomerApi;
import com.cy.pj.sys.utils.SpringUtils;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description:    数据源路由
 * @Author:         psq
 * @CreateDate:     2021/2/22 14:21
 * @Version:        1.0
 */
public class MyAbstractRoutingDataSource extends AbstractRoutingDataSource {
    // 通过此常量可动态初始化数据源
    private static final Map<Object, Object> DATASOURCES = new ConcurrentHashMap<>(8);
    @Override
    protected Object determineCurrentLookupKey() {
        String customerNo = CustomerApi.getCustomerNo();
        if (!DATASOURCES.containsKey(customerNo)){
            initDataSource(customerNo);
        }
        return customerNo;
    }

    /**
     * 初始化数据源
     * @param customerNo
     */
    private synchronized void initDataSource(String customerNo) {
        // 校验该客户标识
        boolean b = CustomerApi.getInstance().checkCustomerNo(customerNo);
        if (!b){
            // 该客户不存在或未启用
            throw new NoSuchCustomerException();
        }
        if (DATASOURCES.containsKey(customerNo)){
            return;
        }
        // 获取该客户标识基础信息
        Customer customer = CustomerApi.getInstance().getCustomer(customerNo);
        MoreDBConfig dbConfig = SpringUtils.getBean(MoreDBConfig.class);
        dbConfig.initCustomerDB(customer);
        // 每次初始化新的数据源需调用此方法 将数据源加入 池
        afterPropertiesSet();
    }

    /**
     * 设置 数据源
     * @param key
     * @param value
     */
    protected static void putDataSource(Object key,Object value){
        DATASOURCES.put(key,value);
    }

    /**
     * 获取 数据源集合
     * @return
     */
    protected static Map<Object, Object> getDatasources(){
        return DATASOURCES;
    }

}
