package com.cy.pj.common.db;

import com.alibaba.druid.pool.DruidDataSource;
import com.cy.pj.common.interceptor.Customer;
import com.cy.pj.sys.utils.SpringUtils;
import com.github.pagehelper.PageInterceptor;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @Description:    数据库配置(默认是MySql)
 * @Author:         psq
 * @CreateDate:     2021/2/22 14:21
 * @Version:        1.0
 */
@Configuration
public class MoreDBConfig {

    private Logger logger = LoggerFactory.getLogger(MoreDBConfig.class);

    @Value("${spring.datasource.url}")
    private String dbUrl;

    @Value("${spring.datasource.slave.url:}")
    private String slaveDbUrl;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.slave.username:}")
    private String slaveUsername;

    @Value("${spring.datasource.password}")
    private String password;

    @Value("${spring.datasource.slave.password:}")
    private String slavePassword;

    @Value("${spring.datasource.driverClassName}")
    private String driverClassName;

    @Value("${spring.datasource.initialSize}")
    private int initialSize;

    @Value("${spring.datasource.minIdle}")
    private int minIdle;

    @Value("${spring.datasource.maxActive}")
    private int maxActive;

    @Value("${spring.datasource.maxWait}")
    private int maxWait;

    @Value("${spring.datasource.timeBetweenEvictionRunsMillis}")
    private int timeBetweenEvictionRunsMillis;

    @Value("${spring.datasource.minEvictableIdleTimeMillis}")
    private int minEvictableIdleTimeMillis;

    @Value("${spring.datasource.validationQuery}")
    private String validationQuery;

    @Value("${spring.datasource.testWhileIdle}")
    private boolean testWhileIdle;

    @Value("${spring.datasource.testOnBorrow}")
    private boolean testOnBorrow;

    @Value("${spring.datasource.testOnReturn}")
    private boolean testOnReturn;

    @Value("${spring.datasource.poolPreparedStatements}")
    private boolean poolPreparedStatements;

    @Value("${spring.datasource.maxPoolPreparedStatementPerConnectionSize}")
    private int maxPoolPreparedStatementPerConnectionSize;

    @Value("${spring.datasource.filters}")
    private String filters;

    @Value("${spring.datasource.connectionProperties}")
    private String connectionProperties;

    @Value("${mybatis.type-aliases-package}")
    private String typeAliasesPackage;

    @Value("${mybatis.mapper-locations}")
    private String mapperLocation;

    /**
     * 非sass 模式 默认为空
     * 独立部署的 客户编号
     */
    @Value("${sass.independent.deployment.no:#{null}}")
    private String independentCustomerNo;

    private DataSource masterDataSource = null;

//    @Value("${owenMybatis.config.location}")
//    private String configLocation;

    @PostConstruct
    public void  init() {
        try {
            masterDataSource = masterDataSource();
            SpringUtils.setBeanForName("masterDataSource",masterDataSource);
            MyAbstractRoutingDataSource.putDataSource(StringUtils.isBlank(independentCustomerNo) ? "blog_saas" : independentCustomerNo,masterDataSource);

            AbstractRoutingDataSource routingDataSource = routingDataSource();
            SpringUtils.setBeanForClass(AbstractRoutingDataSource.class,routingDataSource);

            SqlSessionFactory sqlSessionFactory = sqlSessionFactory(routingDataSource);
            SpringUtils.setBeanForClass(SqlSessionFactory.class,sqlSessionFactory);

            DataSourceTransactionManager dataSourceTransactionManager = dataSourceTransactionManager(routingDataSource);
            SpringUtils.setBeanForClass(DataSourceTransactionManager.class,dataSourceTransactionManager);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化客户数据源
     * @param customer
     */
    public void initCustomerDB(Customer customer){
        logger.info("Ready to initialize data source: "+customer.getCustomerNo());
        DruidDataSource datasource = new DruidDataSource();
        datasource.setUrl(customer.getDatasourceUrl());
        datasource.setUsername(customer.getDatasourceUsername());
        datasource.setPassword(customer.getDatasourcePassword());
        datasource.setDriverClassName(driverClassName);
        DataSource dataSource = getDataSource(datasource);
        SpringUtils.setBeanForName("masterDataSource"+"_"+customer.getCustomerNo(),dataSource);
        MyAbstractRoutingDataSource.putDataSource(customer.getCustomerNo(),dataSource);
    }



    /**
     * 主数据库设置
     * @return
     */
    public DataSource masterDataSource(){
        DruidDataSource datasource = new DruidDataSource();
        datasource.setUrl(this.dbUrl);
        datasource.setUsername(username);
        datasource.setPassword(password);
        datasource.setDriverClassName(driverClassName);
        //configuration
        return getDataSource(datasource);
    }


    /**
     * 从数据库设置
     * @return
     */
    public DataSource salveDataSource(){
        DruidDataSource datasource = new DruidDataSource();
        datasource.setUrl(slaveDbUrl);
        datasource.setUsername(slaveUsername);
        datasource.setPassword(slavePassword);
        datasource.setDriverClassName(driverClassName);
        //configuration
        return getDataSource(datasource);
    }

    /**
     * 获取配置
     * @return
     */
    private DataSource getDataSource( DruidDataSource datasource){
        datasource.setInitialSize(initialSize);
        datasource.setMinIdle(minIdle);
        datasource.setMaxActive(maxActive);
        datasource.setMaxWait(maxWait);
        datasource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
        datasource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
        datasource.setValidationQuery(validationQuery);
        datasource.setTestWhileIdle(testWhileIdle);
        datasource.setTestOnBorrow(testOnBorrow);
        datasource.setTestOnReturn(testOnReturn);
        datasource.setPoolPreparedStatements(poolPreparedStatements);
        datasource.setMaxPoolPreparedStatementPerConnectionSize(maxPoolPreparedStatementPerConnectionSize);
        try {
            datasource.setFilters(filters);
        } catch (SQLException e) {
            logger.error("druid configuration initialization filter", e);
        }
        datasource.setConnectionProperties(connectionProperties);

        return datasource;
    }

    /**
     * 设置数据源路由，通过该类中的determineCurrentLookupKey决定使用哪个数据源
     */
    @Bean
    public AbstractRoutingDataSource routingDataSource() {
        MyAbstractRoutingDataSource proxy = new MyAbstractRoutingDataSource();
        //targetDataSources 数据源集合 以key 判断数据源
        proxy.setDefaultTargetDataSource(masterDataSource);
        proxy.setTargetDataSources(MyAbstractRoutingDataSource.getDatasources());
        return proxy;
    }

    /**
     * 多数据源需要自己设置sqlSessionFactory
     */
    @Bean
    public SqlSessionFactory sqlSessionFactory(AbstractRoutingDataSource routingDataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(routingDataSource);
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        // 实体类对应的位置
        bean.setTypeAliasesPackage(typeAliasesPackage);
        // mybatis的XML的配置
        bean.setMapperLocations(resolver.getResources(mapperLocation));
//        bean.setConfigLocation(resolver.getResource(configLocation));
        //分页插件
        Interceptor interceptor = new PageInterceptor();
        Properties properties = new Properties();
        properties.setProperty("helperDialect", "Mysql");
        properties.setProperty("reasonable", "true");
        properties.setProperty("supportMethodsArguments","true");
        properties.setProperty("params","count=countSql;pageNum=pageNumKey;pageSize=pageSizeKey");
        interceptor.setProperties(properties);
        bean.setPlugins(new Interceptor[] {interceptor});

        return bean.getObject();
    }

    /**
     * 设置事务，事务需要知道当前使用的是哪个数据源才能进行事务处理
     */
    @Bean
    public DataSourceTransactionManager dataSourceTransactionManager(AbstractRoutingDataSource routingDataSource) {
        return new DataSourceTransactionManager(routingDataSource);
    }



}
