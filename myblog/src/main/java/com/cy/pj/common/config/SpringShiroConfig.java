package com.cy.pj.common.config;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.cache.MemoryConstrainedCacheManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cy.pj.sys.service.realm.ShiroUserRealm;

@Configuration
public class SpringShiroConfig {

	@Bean("securityManager")
	public SecurityManager newSecurityManager(
			ShiroUserRealm realm,
			CacheManager cacheManager) {
		DefaultWebSecurityManager sManager=
		new DefaultWebSecurityManager();
		sManager.setRealm(realm);
		sManager.setCacheManager(cacheManager);
		sManager.setRememberMeManager(newRememberMeManager());
		sManager.setSessionManager(newSessionManager());
		return sManager;
	}
	/**创建一个Bean对象,通过此对象创建一个过滤器工厂,
	 * 通过此工厂创建ShiroFilter对象,最后通过过滤器对象
	 * 对请求数据进行过滤*/
	@Bean("shiroFilterFactory")
	@Autowired
	public ShiroFilterFactoryBean newShiroFilterFactoryBean(
			SecurityManager securityManager) {
		ShiroFilterFactoryBean fBean=
	    new ShiroFilterFactoryBean();
		fBean.setSecurityManager(securityManager);
	    //设置进行认证的url
		fBean.setLoginUrl("/dologin");
		Map<String,String> fm=new LinkedHashMap<>();
	    //设置允许匿名访问的资源
	    fm.put("/css/**", "anon");
	    fm.put("/bower_components/**", "anon");
	    fm.put("/fonts/**", "anon");
	    fm.put("/images/**", "anon");
	    fm.put("/js/**", "anon");
	    fm.put("/logo/**", "anon");
	    fm.put("/sass/**", "anon");
	    fm.put("/doReg", "anon");
	    fm.put("/doSingle", "anon");
	    fm.put("/Index", "anon");
	    fm.put("/user/doLogin", "anon");
	    fm.put("/user/doRegister", "anon");
	    fm.put("/user/doSignOut", "anon");
	    fm.put("/user/doProFile", "anon");
	    fm.put("/user/doFindUser", "anon");
	    fm.put("/user/doFindUserId", "anon");
	    fm.put("/comment/doInsertComment", "anon");
	    fm.put("/user/doLoadIndexUI", "anon");
	    
	    //设置必须认证才可以访问的资源
	    fm.put("/**", "user");
		fBean.setFilterChainDefinitionMap(fm);
		return fBean;
	}
	//=========配置授权=======================
	//1.配置一个Shiro 中 Bean对象的生命周期管理器
	@Bean("lifecycleBeanPostProcessor")
	public LifecycleBeanPostProcessor newLifecycleBeanPostProcessor() {
		return new LifecycleBeanPostProcessor();
	}
	/**为目标对象(方法上有@RequiresPermissions注解的)
	 * 创建代理对象,通过代理对象调用通知(Advice)实现
	 * 授权检测*/
//	@DependsOn("lifecycleBeanPostProcessor")
//	@Bean
//	public DefaultAdvisorAutoProxyCreator newProxyCreator() {
//		return new DefaultAdvisorAutoProxyCreator();
//	}
	/**
	 * 此对象负责定义切入点以及通知增强.
	 * @param securityManager
	 * @return
	 */
	@Bean
	public AuthorizationAttributeSourceAdvisor newAdvisor(
			SecurityManager securityManager) {
		AuthorizationAttributeSourceAdvisor advisor=
			new AuthorizationAttributeSourceAdvisor();
		advisor.setSecurityManager(securityManager);
		return advisor;
	}
	//=======================
	//配置shiro缓存对象
	@Bean
	public CacheManager newCacheManager(){
	  return new MemoryConstrainedCacheManager();
    }
	//配置cookie对象
	public CookieRememberMeManager newRememberMeManager() {
		 CookieRememberMeManager cManager=
		 new CookieRememberMeManager();
		 SimpleCookie c=new SimpleCookie("rememberMe");
		 c.setMaxAge(10*60);
		 cManager.setCookie(c);
		 return cManager;
	 }
	//默认 Session时长设置
	 public DefaultWebSessionManager newSessionManager() {
		 DefaultWebSessionManager sManager=
				 new DefaultWebSessionManager();
		 sManager.setGlobalSessionTimeout(60*60*1000);
		 return sManager;
	 }
	
}








