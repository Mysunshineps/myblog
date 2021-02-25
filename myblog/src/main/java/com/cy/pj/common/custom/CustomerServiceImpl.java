package com.cy.pj.common.custom;

import com.alibaba.fastjson.JSON;
import com.cy.pj.common.exception.NoSuchCustomerException;
import com.cy.pj.dao.CustomerConfigMapper;
import com.cy.pj.dao.CustomerMapper;
import com.cy.pj.dao.enums.Constants;
import com.cy.pj.redis.RedisKey;
import com.cy.pj.redis.StringRedisService;
import com.cy.pj.thread.threadlocal.CustomerThreadLocal;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Description:    获取客户配置实现类
 * @Author:         psq
 * @CreateDate:     2021/2/22 14:21
 * @Version:        1.0
 */
@Service("CustomerServiceImpl")
public class CustomerServiceImpl implements CustomerService {
    private static Logger logger = LoggerFactory.getLogger(CustomerServiceImpl.class);

    /**
     * 启用的客户编码集合
     */
    private static final Set<String> CUSTOMER_NOS = Sets.newHashSet();
//    private static final Map<String,PlatformConfig> PLATFORM_CONFIGS = Maps.newHashMap();

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private CustomerConfigMapper customerConfigMapper;

    @Autowired
    private StringRedisService stringRedisService;


    /**
     * 非sass 模式 默认为空
     * 独立部署的 客户编号
     */
    @Value("${sass.independent.deployment.no:#{null}}")
    private String independentCustomerNo;

    /**
     * 获取独立部署的客户编号
     * @return
     */
    @Override
    public String getIndependentCustomerNo(){
        return independentCustomerNo;
    }

    /**
     * 设置客户标识
     * @param no
     */
    @Override
    public void setCustomerNo(String no) {
        CustomerThreadLocal.setCustomerNo(no);
    }

    /**
     * 检查客户标识是否存在启用
     * @param no
     * @return
     */
    @Override
    public boolean checkCustomerNo(String no) {
        if (StringUtils.isBlank(no)){
            return false;
        }
        Set<String> customerNos = getCustomerNos();
        if (customerNos.contains(no)){
            return true;
        }
        return false;
    }

    /**
     * 获取启用的客户标识集合
     * @return
     */
    @Override
    public Set<String> getCustomerNos(){
        if (!CUSTOMER_NOS.isEmpty()){
            return CUSTOMER_NOS;
        }
        setSassDefaultNo();
        try {
            String json = stringRedisService.getString(RedisKey.customer.NOS);
            if (json != null){
                CUSTOMER_NOS.addAll(JSON.parseArray(json,String.class));
                return CUSTOMER_NOS;
            }
            Customer customer = new Customer();
            customer.setStatus(1);
            List<Customer> select = customerMapper.select(customer);
            CUSTOMER_NOS.addAll(select.stream().map(x -> x.getCustomerNo()).collect(Collectors.toSet()));
            stringRedisService.set(RedisKey.customer.NOS, JSON.toJSONString(CUSTOMER_NOS), Constants.SecondsTime.TWENTY_THREE_HOUR);
        } catch (Exception e) {
            logger.error("get all customerNos error",e);
        }finally {
            revertSassDefaultNo();
        }
        return CUSTOMER_NOS;
    }

    /**
     * 获取当前客户
     * @return
     */
    @Override
    public Customer getCustomer() {
        return getCustomer(getCustomerNo());
    }

    /**
     * 获取标识客户
     * @return
     */
    @Override
    public Customer getCustomer(String customerNo) {
        setSassDefaultNo();
        Customer customer = null;
        try {
            String key = RedisKey.customer.BASICS+customerNo;
            String json = stringRedisService.getString(key);
            if (json != null){
                return JSON.parseObject(json,Customer.class);
            }
            customer = new Customer();
            customer.setCustomerNo(customerNo);
            customer = customerMapper.selectOne(customer);
            if (customer == null){
                // 无此客户
                throw new NoSuchCustomerException("no such customer: "+customerNo);
            }
            stringRedisService.set(key, JSON.toJSONString(customer),Constants.SecondsTime.TWENTY_THREE_HOUR);
        }finally {
            revertSassDefaultNo();
        }
        return customer;
    }



    /**
     * 获取标识客户配置
     * @param customerNo
     * @return
     */
    @Override
    public CustomerConfig getConfig(String customerNo) {
        setSassDefaultNo();
        CustomerConfig config = null;
        try {
            String key = RedisKey.customer.CONFIG + customerNo;
            String json = stringRedisService.getString(key);
            if (json != null){
                return JSON.parseObject(json,CustomerConfig.class);
            }
            config = new CustomerConfig();
            config.setCustomerNo(customerNo);
            config = customerConfigMapper.selectOne(config);
            if (config == null){
                throw new NoSuchCustomerException("this customer no config: "+customerNo);
            }
            stringRedisService.set(key, JSON.toJSONString(config),Constants.SecondsTime.TWENTY_THREE_HOUR);
        } finally {
            revertSassDefaultNo();
        }
        return config;
    }

    /**
     * 获取当前客户配置
     * @return
     */
    @Override
    public CustomerConfig getConfig() {
        return getConfig(getCustomerNo());
    }

    /**
     * 获取当前客户标识
     * @return
     */
    @Override
    public String getCustomerNo() {
        String customerNo = this.getIndependentCustomerNo();
        if (customerNo != null){
            return customerNo;
        }
        customerNo = CustomerThreadLocal.getCustomerNo();
        if (customerNo == null){
            throw new NoSuchCustomerException("no customerNo");
        }
        return customerNo;
    }

    /**
     * 获取当前客户启用哪些三方平台设置
     * 多级缓存
     * @return
     */
//    @Override
//    public PlatformConfig getPlatformConfig() {
//        return getPlatformConfig(getCustomerNo());
//    }
//
//    @Override
//    public PlatformConfig getPlatformConfig(String customerNo){
//        PlatformConfig config = PLATFORM_CONFIGS.get(customerNo);
//        if (config != null){
//            return config;
//        }
//        String json = (String)stringRedisService.get(customerNo,RedisKey.customer.BASICS_PLATFORM);
//        if (json != null){
//            config = JSON.parseObject(json,PlatformConfig.class);
//            PLATFORM_CONFIGS.put(customerNo,config);
//            return config;
//        }
//        Customer customer = getCustomer(customerNo);
//        config = new PlatformConfig();
//        config.setCrm(customer.getStoreCrm());
//        config.setElem(customer.getStoreElem());
//        config.setErp(customer.getStoreErp());
//        config.setJD(customer.getStoreJd());
//        config.setYouzan(customer.getStoreYouzan());
//        config.setMeituan(customer.getStoreMeituan());
//        stringRedisService.setNoCus(customerNo,RedisKey.customer.BASICS_PLATFORM, JSON.toJSONString(config),Constants.SecondsTime.TWENTY_THREE_HOUR);
//        PLATFORM_CONFIGS.put(customerNo,config);
//        return config;
//    }


    /**
     * 获取当前客户Erp 配置
     * 切换客户后缓存不变
     * 禁止多客户线程调用此方法
     * 多级缓存
     * @return
     */
//    @Override
//    public ErpProperties getErpConfig() {
//        ErpProperties erpProperties = (ErpProperties) CustomerThreadLocal.getInfo(LocalName.CONFIG_ERP);
//        if (erpProperties != null){
//            return erpProperties;
//        }
//        String json = stringRedisService.getString(RedisKey.customer.CONFIG_ERP);
//        if (json != null){
//            erpProperties = JSON.parseObject(json, ErpProperties.class);
//            CustomerThreadLocal.setInfo(LocalName.CONFIG_ERP,erpProperties);
//            return erpProperties;
//        }
//        CustomerConfig config =getConfig();
//        erpProperties = new ErpProperties();
//        erpProperties.setAppKey(config.getErpAppKey());
//        erpProperties.setRegisterKey(config.getErpRegisterKey());
//        erpProperties.setWebService(config.getErpWebService());
//        stringRedisService.set(RedisKey.customer.CONFIG_ERP, JSON.toJSONString(erpProperties), Constants.SecondsTime.TWENTY_THREE_HOUR);
//        CustomerThreadLocal.setInfo(LocalName.CONFIG_ERP,erpProperties);
//        return erpProperties;
//    }


    /**
     * 获取当前客户CRM 配置
     * 切换客户后缓存不变
     * 禁止多客户线程调用此方法
     * 多级缓存
     * @return
     */
//    @Override
//    public CrmProperties getCrmConfig() {
//        CrmProperties properties = (CrmProperties) CustomerThreadLocal.getInfo(LocalName.CONFIG_CRM);
//        if (properties != null){
//            return properties;
//        }
//        String json = stringRedisService.getString(RedisKey.customer.CONFIG_CRM);
//        if (json != null){
//            properties = JSON.parseObject(json,CrmProperties.class);
//            CustomerThreadLocal.setInfo(LocalName.CONFIG_CRM,properties);
//            return properties;
//        }
//        CustomerConfig config =getConfig();
//        properties = new CrmProperties();
//        properties.setAppKey(config.getCrmAppKey());
//        properties.setRegisterKey(config.getCrmRegisterKey());
//        properties.setWebService(config.getCrmWebService());
//        properties.setAesKey(config.getCrmAesKey());
//        stringRedisService.set(RedisKey.customer.CONFIG_CRM, JSON.toJSONString(properties), Constants.SecondsTime.TWENTY_THREE_HOUR);
//        CustomerThreadLocal.setInfo(LocalName.CONFIG_CRM,properties);
//        return properties;
//    }

    /**
     * 获取当前客户七牛云 配置
     * 切换客户后缓存不变
     * 禁止多客户线程调用此方法
     * @return
     */
//    @Override
//    public QiniuConfiguration getQinuConfig(){
//        QiniuConfiguration properties = (QiniuConfiguration)CustomerThreadLocal.getInfo(LocalName.CONFIG_QINIU);
//        if (properties != null){
//            return properties;
//        }
//        String json = stringRedisService.getString(RedisKey.customer.CONFIG_QINIU);
//        if (json != null){
//            properties = JSON.parseObject(json,QiniuConfiguration.class);
//            CustomerThreadLocal.setInfo(LocalName.CONFIG_QINIU,properties);
//            return properties;
//        }
//        CustomerConfig config =getConfig();
//        properties = new QiniuConfiguration();
//        properties.setAccessKey(config.getQiniuAccessKey());
//        properties.setBucket(config.getQiniuBucket());
//        properties.setBucketUrl(config.getQiniuBucketUrl());
//        properties.setSecretKey(config.getQiniuSecretKey());
//        stringRedisService.set(RedisKey.customer.CONFIG_QINIU, JSON.toJSONString(properties), Constants.SecondsTime.TWENTY_THREE_HOUR);
//        CustomerThreadLocal.setInfo(LocalName.CONFIG_QINIU,properties);
//        return properties;
//    }

    /**
     * 获取客户京东 配置
     * 切换客户后缓存不变
     * 禁止多客户线程调用此方法
     * 多级缓存
     * @return
     */
//    @Override
//    public JDConfiguration getJdConfig() {
//        JDConfiguration properties = (JDConfiguration) CustomerThreadLocal.getInfo(LocalName.CONFIG_JD);
//        if (properties != null){
//            return properties;
//        }
//        String json = stringRedisService.getString(RedisKey.customer.CONFIG_JD);
//        if (json != null){
//            properties = JSON.parseObject(json,JDConfiguration.class);
//            CustomerThreadLocal.setInfo(LocalName.CONFIG_JD,properties);
//            return properties;
//        }
//        CustomerConfig config =getConfig();
//        properties = new JDConfiguration();
//        properties.setAppKey(config.getJdAppKey());
//        properties.setSecret(config.getJdSecret());
//        stringRedisService.set(RedisKey.customer.CONFIG_JD, JSON.toJSONString(properties), Constants.SecondsTime.TWENTY_THREE_HOUR);
//        CustomerThreadLocal.setInfo(LocalName.CONFIG_JD,properties);
//        return properties;
//    }

    /**
     * 获取 客户美团 配置
     * 切换客户后缓存不变
     * 禁止多客户线程调用此方法
     * 多级缓存
     * @return
     */
//    @Override
//    public MeiTuanConfiguration getMeituanConfig() {
//        // 优先从 线程副本取信息
//        MeiTuanConfiguration properties = (MeiTuanConfiguration) CustomerThreadLocal.getInfo(LocalName.CONFIG_MEITUAN);
//        if (properties != null){
//            return properties;
//        }
//        // 其次从redis 缓存中获取信息
//        String json = stringRedisService.getString(RedisKey.customer.CONFIG_MEITUAN);
//        if (json != null){
//            properties  = JSON.parseObject(json,MeiTuanConfiguration.class);
//            CustomerThreadLocal.setInfo(LocalName.CONFIG_MEITUAN,properties);
//            return properties;
//        }
//        CustomerConfig config =getConfig();
//        properties = new MeiTuanConfiguration();
//        properties.setAppKey(config.getMeituanAppKey());
//        properties.setSecret(config.getMeituanSecret());
//        stringRedisService.set(RedisKey.customer.CONFIG_MEITUAN, JSON.toJSONString(properties), Constants.SecondsTime.TWENTY_THREE_HOUR);
//        CustomerThreadLocal.setInfo(LocalName.CONFIG_MEITUAN,properties);
//        return properties;
//    }

    /**
     * 获取客户饿了么配置信息
     * 切换客户后缓存不变
     * 禁止多客户线程调用此方法
     * 多级缓存
     * @return
     */
//    @Override
//    public ElemConfiguration getElemConfig() {
//        ElemConfiguration properties = (ElemConfiguration) CustomerThreadLocal.getInfo(LocalName.CONFIG_ELEM);
//        if (properties != null){
//            return properties;
//        }
//        String json = stringRedisService.getString(RedisKey.customer.CONFIG_ELEM);
//        if (json != null){
//            properties =  JSON.parseObject(json,ElemConfiguration.class);
//            CustomerThreadLocal.setInfo(LocalName.CONFIG_ELEM,properties);
//            return properties;
//        }
//        CustomerConfig config =getConfig();
//        properties = new ElemConfiguration();
//        properties.setAppkey(config.getElemAppKey());
//        properties.setSecret(config.getElemSecret());
//        stringRedisService.set(RedisKey.customer.CONFIG_ELEM, JSON.toJSONString(properties), Constants.SecondsTime.TWENTY_THREE_HOUR);
//        CustomerThreadLocal.setInfo(LocalName.CONFIG_ELEM,properties);
//        return properties;
//
//    }

    /**
     * 获取有赞 配置
     * 切换客户后缓存不变
     * 禁止多客户线程调用此方法
     * 多级缓存
     * @return
     */
//    @Override
//    public YouzanConfiguration getYouzanConfig() {
//        YouzanConfiguration properties = (YouzanConfiguration) CustomerThreadLocal.getInfo(LocalName.CONFIG_YOUZAN);
//        if (properties != null){
//            return properties;
//        }
//        String json = stringRedisService.getString(RedisKey.customer.CONFIG_YOUZAN);
//        if (json != null){
//            properties =  JSON.parseObject(json,YouzanConfiguration.class);
//            CustomerThreadLocal.setInfo(LocalName.CONFIG_YOUZAN,properties);
//            return properties;
//        }
//        CustomerConfig config =getConfig();
//        properties = new YouzanConfiguration();
//        properties.setAppKey(config.getYouzanAppKey());
//        properties.setSecret(config.getYouzanSecret());
//        properties.setShopId(Long.parseLong(config.getYouzanShopId()));
//        stringRedisService.set(RedisKey.customer.CONFIG_YOUZAN, JSON.toJSONString(properties), Constants.SecondsTime.TWENTY_THREE_HOUR);
//        CustomerThreadLocal.setInfo(LocalName.CONFIG_YOUZAN,properties);
//        return properties;
//    }

    /**
     * 获取有赞token
     * 切换客户后缓存不变
     * 禁止多客户线程调用此方法
     * 多级缓存
     * @return
     */
//    @Override
//    public String getYouzanToken() {
//        String token = (String) CustomerThreadLocal.getInfo(LocalName.TOKEN_YOUZAN);
//        if (StringUtils.isNotBlank(token)){
//            return token;
//        }
//        token = stringRedisService.getString(RedisKey.customer.TOKEN_YOUZAN);
//        if (StringUtils.isNotBlank(token)){
//            CustomerThreadLocal.setInfo(LocalName.TOKEN_YOUZAN,token);
//            return token;
//        }
//        token = tokenService.getYouzanToken();
//        stringRedisService.set(RedisKey.customer.TOKEN_YOUZAN,token, Constants.SecondsTime.TWENTY_THREE_HOUR);
//        CustomerThreadLocal.setInfo(LocalName.TOKEN_YOUZAN,token);
//        return token;
//    }

    /**
     * 获取饿了么token
     * 切换客户后缓存不变
     * 禁止多客户线程调用此方法
     * 多级缓存
     * @return
     */
//    @Override
//    public String getElemToken() {
//        String token = (String) CustomerThreadLocal.getInfo(LocalName.TOKEN_ELEM);
//        if (StringUtils.isNotBlank(token)){
//            return token;
//        }
//        token = stringRedisService.getString(RedisKey.customer.TOKEN_ELEM);
//        if (StringUtils.isNotBlank(token)){
//            CustomerThreadLocal.setInfo(LocalName.TOKEN_ELEM,token);
//            return token;
//        }
//        token = tokenService.getElemToken();
//        stringRedisService.set(RedisKey.customer.TOKEN_ELEM,token, Constants.SecondsTime.TWENTY_THREE_HOUR);
//        CustomerThreadLocal.setInfo(LocalName.TOKEN_ELEM,token);
//        return token;
//    }

    /**
     * 获取京东token
     * 切换客户后缓存不变
     * 禁止多客户线程调用此方法
     * 多级缓存
     * @return
     */
//    @Override
//    public String getJdToken() {
//        String token = (String) CustomerThreadLocal.getInfo(LocalName.TOKEN_JD);
//        if (StringUtils.isNotBlank(token)){
//            return token;
//        }
//        token = stringRedisService.getString(RedisKey.customer.TOKEN_JD);
//        if (StringUtils.isNotBlank(token)){
//            CustomerThreadLocal.setInfo(LocalName.TOKEN_JD,token);
//            return token;
//        }
//        token = tokenService.getJdToken();
//        stringRedisService.set(RedisKey.customer.TOKEN_JD,token, Constants.SecondsTime.TWENTY_THREE_HOUR);
//        CustomerThreadLocal.setInfo(LocalName.TOKEN_JD,token);
//        return token;
//    }

    /**
     * 清除内存客户标识
     */
    @Override
    public void clearCustomerNos(){
        CUSTOMER_NOS.clear();
//        PLATFORM_CONFIGS.clear();
    }


    /**
     * 设置 SASS 模式默认标识
     */
    @Override
    public boolean setSassDefaultNo() {
        if (StringUtils.isBlank(getIndependentCustomerNo())){
            // 非独立部署
            // 设置sass 库标识 查询客户都切换至sass 库
            CustomerThreadLocal.setDefaultCustomerNo();
            return true;
        }
        return false;
    }

    /**
     * 清除 SASS 模式默认标识
     */
    @Override
    public void revertSassDefaultNo() {
        if (StringUtils.isBlank(getIndependentCustomerNo())){
            // 非独立部署
            // 还原客户标识
            CustomerThreadLocal.revertCustomerNo();
        }
    }
}
