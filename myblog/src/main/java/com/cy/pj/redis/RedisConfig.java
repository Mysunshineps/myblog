package com.cy.pj.redis;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.*;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.session.data.redis.config.ConfigureRedisAction;
import redis.clients.jedis.JedisPoolConfig;

import java.lang.reflect.Method;

/**
 * 2018/4/5.
 */
@Configuration
@EnableCaching
public class RedisConfig extends CachingConfigurerSupport {
    @Autowired
    private RedisProperties redisProperties;


    /**
     * redis 链接工厂类
     * @return
     */
    @Bean
    @Primary
    public RedisConnectionFactory redisConnectionFactory() {
        // Jedis 链接池 配置参数
        JedisPoolConfig config=new JedisPoolConfig();
        config.setMaxIdle(redisProperties.getPoolMaxIdle());
        config.setMaxTotal(redisProperties.getPoolMaxTotal());
        config.setMinIdle(redisProperties.getPoolMinIdle());
        // 空闲链接检测线程检测周期。如果为负值，表示不运行检测线程。（单位：毫秒，默认为-layer）
        config.setTimeBetweenEvictionRunsMillis(redisProperties.getTimeBetweenEvictionRunsMillis());
        // 配置一个连接在池中最小生存的时间，单位是毫秒
        config.setMinEvictableIdleTimeMillis(redisProperties.getMinEvictableIdleTimeMillis());
        // 空闲链接检测线程一次运行检测多少条链接
        config.setNumTestsPerEvictionRun(redisProperties.getNumTestsPerEvictionRun());
        config.setTestOnReturn(redisProperties.getTestOnReturn());
        config.setTestOnBorrow(redisProperties.getTestOnBorrow());

        JedisClientConfiguration.JedisClientConfigurationBuilder builder= JedisClientConfiguration.builder();
        builder.usePooling().poolConfig(config);

        if (StringUtils.isBlank(redisProperties.getNodes())){
            return new JedisConnectionFactory(getRedisStandaloneConfiguration(),builder.build());
        }else {
            return new JedisConnectionFactory(getRedisClusterConfig(),builder.build());
        }

    }

    /**
     * 集群模式
     * redis 链接配置
     * @return
     */
    private RedisClusterConfiguration getRedisClusterConfig(){
        RedisClusterConfiguration redisClusterConfiguration = new RedisClusterConfiguration();
        redisClusterConfiguration.setPassword(redisProperties.getPassword());
//        if (StringUtils.isNotBlank(redisProperties.getHost())){
//            redisClusterConfiguration.clusterNode(redisProperties.getHost(),redisProperties.getPort());
//        }
        if (StringUtils.isNotBlank(redisProperties.getNodes())){
            String[] split = redisProperties.getNodes().split(",");
            for (String s : split) {
                String[] node = s.split(":");
                redisClusterConfiguration.addClusterNode(new RedisNode(node[0].trim(),Integer.parseInt(node[1].trim())));
            }
        }
        // 最大活动链接数
        redisClusterConfiguration.setMaxRedirects(redisProperties.getMaxRedirects());
        if (StringUtils.isNotBlank(redisProperties.getPassword())){
            redisClusterConfiguration.setPassword(RedisPassword.of(redisProperties.getPassword()));
        }
        return redisClusterConfiguration;
    }

    /**
     * 非集群模式 redis 链接配置
     * @return
     */
    private RedisStandaloneConfiguration getRedisStandaloneConfiguration(){
        RedisStandaloneConfiguration configuration=new RedisStandaloneConfiguration();
        configuration.setHostName(redisProperties.getHost());
        configuration.setDatabase(redisProperties.getDatabase());
        configuration.setPort(redisProperties.getPort());
        configuration.setPassword(RedisPassword.of(redisProperties.getPassword()));
        return configuration;
    }

    /**
     * https://www.cnblogs.com/coderzl/p/7512644.html
     * 1、开启redis的Keyspace notifications功能,重启
     * notify-keyspace-events Ex
     *
     * 2、关闭Spring-session中对CONFIG的操作
     * @return
     */
    @Bean
    public static ConfigureRedisAction configureRedisAction() {
        return ConfigureRedisAction.NO_OP;
    }

    /**
     * 自定义key. 这个可以不用
     * 此方法将会根据类名+方法名+所有参数的值生成唯一的一个key,即使//@Cacheable中的value属性一样，key也会不一样。
     */
    @Bean
    public KeyGenerator simpleKeyGenerator(){
        return new KeyGenerator() {
            @Override
            public Object generate(Object target, Method method, Object... params) {
                StringBuilder sb = new StringBuilder();
                sb.append(target.getClass().getName());
                sb.append(method.getName());
                if (null != params){
                    for (Object obj : params) {
                        sb.append(obj.toString());
                    }
                }
                return sb.toString();
            }
        };

    }

    @Bean
    public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory factory) {

        StringRedisTemplate template = new StringRedisTemplate(factory);
        //定义key序列化方式
        //RedisSerializer<String> redisSerializer = new StringRedisSerializer();//Long类型会出现异常信息;需要我们上面的自定义key生成策略，一般没必要
        //定义value的序列化方式
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);

        // template.setKeySerializer(redisSerializer);
        template.setValueSerializer(jackson2JsonRedisSerializer);
        template.setHashValueSerializer(jackson2JsonRedisSerializer);
        template.afterPropertiesSet();
        return template;
    }

    /**
     * 为spring session添加springSessionRedisTaskExecutor线程池。
     * @return
     */
    @Bean
    public ThreadPoolTaskExecutor springSessionRedisTaskExecutor(){
        ThreadPoolTaskExecutor springSessionRedisTaskExecutor = new ThreadPoolTaskExecutor();
        springSessionRedisTaskExecutor.setCorePoolSize(8);
        springSessionRedisTaskExecutor.setMaxPoolSize(16);
        springSessionRedisTaskExecutor.setKeepAliveSeconds(10);
        springSessionRedisTaskExecutor.setQueueCapacity(1000);
        springSessionRedisTaskExecutor.setThreadNamePrefix("Spring session redis executor thread: ");
        return springSessionRedisTaskExecutor;
    }
}