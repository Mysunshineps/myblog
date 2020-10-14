package com.cy.pj.redis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
* @Description:    redis 属性配置
*/
@Component
public class RedisProperties {

    /**
     * redis IP
     */
    @Value("${spring.redis.host:}")
    private String host;

    /**
     * redis 端口号（默认6379）
     */
    @Value("${spring.redis.port:#{6379}}")
    private Integer port;

    /**
     * redis 库 默认0
     */
    @Value("${spring.redis.database:#{0}}")
    private Integer database;

    /**
     * redis密码 默认空
     */
    @Value("${spring.redis.password:}")
    private String password;

    /**
     * redis集群节点
     */
    @Value("${spring.redis.cluster.nodes:}")
    private String nodes;

    /**
     * redis 超时时间（默认 500 毫秒）
     */
    @Value("${spring.redis.timeout:#{500}}")
    private Long timeOut;

    /**
     * redis池 最大链接数
     */
    @Value("${spring.redis.jedis.pool.max-total:#{500}}")
    private Integer poolMaxTotal;

    /**
     * redis 最大活动链接数
     */
    @Value("${spring.redis..max-redirects:#{300}}")
    private Integer maxRedirects;

    /**
     * 连接池中的最小空闲连接
     */
    @Value("${spring.redis.jedis.pool.min-idle:#{5}}")
    private Integer poolMinIdle;

    /**
     * 连接池最大空闲连接数（使用负值表示没有限制）
     */
    @Value("${spring.redis.jedis.pool.max-idle:#{8}}")
    private Integer poolMaxIdle;

    /**
     * 连接池最大阻塞等待时间（使用负值表示没有限制）
     */
    @Value("${spring.redis.jedis.pool.max-wait:#{-1}}")
    private Long poolMaxWait;

    /**
     * 在获取连接的时候检查有效性, 默认false
     */
    @Value("${spring.redis.jedis.pool.test-on-borrow:#{false}}")
    private Boolean testOnBorrow;

    /**
     * 在空闲时检查有效性, 默认true
     */
    @Value("${spring.redis.jedis.pool.test-on-return:#{true}}")
    private Boolean testOnReturn;

    /**
     * 空闲链接检测线程检测周期。如果为负值，表示不运行检测线程。（单位：毫秒，默认为-layer）
     */
    @Value("${spring.redis.jedis.pool.time-between-eviction-runs-millis:#{6000}}")
    private Long timeBetweenEvictionRunsMillis;

    /**
     * 空闲链接检测线程一次运行检测多少条链接
     */
    @Value("${spring.redis.jedis.pool.num-tests-per-eviction-run:#{10}}")
    private Integer numTestsPerEvictionRun;

    /**
     * 配置一个连接在池中最小生存的时间，单位是毫秒
     */
    @Value("${spring.redis.jedis.pool.min-evictable-idle-time-millis:#{3000}}")
    private Long minEvictableIdleTimeMillis;

    public Integer getMaxRedirects() {
        return maxRedirects;
    }

    public void setMaxRedirects(Integer maxRedirects) {
        this.maxRedirects = maxRedirects;
    }

    public Long getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(Long timeOut) {
        this.timeOut = timeOut;
    }

    public Integer getPoolMaxTotal() {
        return poolMaxTotal;
    }

    public void setPoolMaxTotal(Integer poolMaxTotal) {
        this.poolMaxTotal = poolMaxTotal;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public Integer getDatabase() {
        return database;
    }

    public void setDatabase(Integer database) {
        this.database = database;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNodes() {
        return nodes;
    }

    public void setNodes(String nodes) {
        this.nodes = nodes;
    }

    public Integer getPoolMinIdle() {
        return poolMinIdle;
    }

    public void setPoolMinIdle(Integer poolMinIdle) {
        this.poolMinIdle = poolMinIdle;
    }

    public Integer getPoolMaxIdle() {
        return poolMaxIdle;
    }

    public void setPoolMaxIdle(Integer poolMaxIdle) {
        this.poolMaxIdle = poolMaxIdle;
    }

    public Long getPoolMaxWait() {
        return poolMaxWait;
    }

    public void setPoolMaxWait(Long poolMaxWait) {
        this.poolMaxWait = poolMaxWait;
    }

    public Boolean getTestOnBorrow() {
        return testOnBorrow;
    }

    public void setTestOnBorrow(Boolean testOnBorrow) {
        this.testOnBorrow = testOnBorrow;
    }

    public Boolean getTestOnReturn() {
        return testOnReturn;
    }

    public void setTestOnReturn(Boolean testOnReturn) {
        this.testOnReturn = testOnReturn;
    }

    public Long getTimeBetweenEvictionRunsMillis() {
        return timeBetweenEvictionRunsMillis;
    }

    public void setTimeBetweenEvictionRunsMillis(Long timeBetweenEvictionRunsMillis) {
        this.timeBetweenEvictionRunsMillis = timeBetweenEvictionRunsMillis;
    }

    public Integer getNumTestsPerEvictionRun() {
        return numTestsPerEvictionRun;
    }

    public void setNumTestsPerEvictionRun(Integer numTestsPerEvictionRun) {
        this.numTestsPerEvictionRun = numTestsPerEvictionRun;
    }

    public Long getMinEvictableIdleTimeMillis() {
        return minEvictableIdleTimeMillis;
    }

    public void setMinEvictableIdleTimeMillis(Long minEvictableIdleTimeMillis) {
        this.minEvictableIdleTimeMillis = minEvictableIdleTimeMillis;
    }
}