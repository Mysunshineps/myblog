package com.cy.pj.common.config;
import com.cy.pj.common.interceptor.CustomerInterceptor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SpringWebConfig 
       implements WebMvcConfigurer{
	/**
	 * 配置spring mvc拦截器
	 */
	@Override
	public void addInterceptors(
	   InterceptorRegistry registry) {
	   System.out.println("==add CustomerInterceptor==");
	   registry.addInterceptor(new CustomerInterceptor())
	   .addPathPatterns("/admin/**");
	}

	 @SuppressWarnings({ "rawtypes", "unchecked" })
	 @Bean
	 public FilterRegistrationBean newFilterRegistrationBean() {
		 FilterRegistrationBean fBean=
		 new FilterRegistrationBean();
		 fBean.setFilter(new DelegatingFilterProxy("shiroFilterFactory"));
		 fBean.addUrlPatterns("/*");
		 return fBean;
	 }
}
