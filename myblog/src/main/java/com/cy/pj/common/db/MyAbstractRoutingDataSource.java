package com.cy.pj.common.db;

import com.cy.pj.common.exception.NoSuchCustomerException;
import com.cy.pj.common.custom.Customer;
import com.cy.pj.common.custom.CustomerApi;
import com.cy.pj.dao.enums.Constants;
import com.cy.pj.entity.DataSourceType;
import com.cy.pj.sys.utils.SpringUtils;
import com.cy.pj.thread.threadlocal.CustomerThreadLocal;
import org.apache.commons.lang3.StringUtils;
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
    /**
     * 数据源的集合
     */
    public static final Map<Object, Object> DATASOURCES = new ConcurrentHashMap<>(8);

    @Override
    protected Object determineCurrentLookupKey() {
        //dataSourceType :1代表主库写，2代表从库读
        Integer dataSourceType = CustomerThreadLocal.getDataSourceType()==null? 1:CustomerThreadLocal.getDataSourceType();
        String customerNo = CustomerApi.getCustomerNo();
        if (!DATASOURCES.containsKey(customerNo+ Constants.UNDERLINE + dataSourceType)){
            initDataSource(customerNo,dataSourceType);
        }
        return customerNo+Constants.UNDERLINE+dataSourceType;
    }

    /**
     * 初始化数据源
     * @param customerNo
     */
    private synchronized void initDataSource(String customerNo,Integer dataSourceType) {
        // 校验该客户标识
        boolean b = CustomerApi.getInstance().checkCustomerNo(customerNo);
        if (!b){
            // 该客户不存在或未启用
            throw new NoSuchCustomerException();
        }
        if (DATASOURCES.containsKey(customerNo + Constants.UNDERLINE + dataSourceType)) {
            return;
        }
        // 获取该客户标识基础信息
        Customer customer = CustomerApi.getInstance().getCustomer(customerNo);
        MoreDBConfig dbConfig = SpringUtils.getBean(MoreDBConfig.class);
        if (dataSourceType.equals(DataSourceType.SALVE.getType()) && StringUtils.isNotBlank(customer.getSpringDatasourceSlaveUrl())){
            dbConfig.initCustomerSalveDB(customer);
        }else {
            dbConfig.initCustomerMasterDB(customer);
        }
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
