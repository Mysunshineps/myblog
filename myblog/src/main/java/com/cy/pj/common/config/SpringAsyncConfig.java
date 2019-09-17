package com.cy.pj.common.config;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
//@Configuration
public class SpringAsyncConfig implements AsyncConfigurer{
	//@Value注解自动从配置文件读取数据注入给属性
	@Value("${executor.corePoolSize}")
	private int corePoolSize=10;
	@Value("${executor.maximumPoolSize}")
	private int maximumPoolSize=20;
	@Value("${executor.keepAliveTime}")
	private int keepAliveTime=30;
	@Value("${executor.queueCapacity}")
	private int queueCapacity=20;
	private ThreadFactory threadFactory=
			new ThreadFactory() {
		private AtomicLong number=new AtomicLong(100);
		@Override
		public Thread newThread(Runnable r) {
			return new Thread(r,"async-thread-"+number.getAndIncrement());
		}
	};
	//JDK 自带的池(Tomcat默认也是使用这个池)
	@Bean("asyncThreadPool")
	public Executor newThreadPoolExecutor() {
		ThreadPoolExecutor pool=
		new ThreadPoolExecutor(corePoolSize,
				maximumPoolSize,
				keepAliveTime, 
				TimeUnit.SECONDS, 
				new LinkedBlockingQueue<>(queueCapacity),
				threadFactory);
		return pool;
	}
	@Override
	public Executor getAsyncExecutor() {
		System.out.println("corePoolSize="+corePoolSize);
		System.out.println("==getAsyncExecutor()==");
        ThreadPoolTaskExecutor pool=
        new ThreadPoolTaskExecutor();
        pool.setCorePoolSize(corePoolSize);
        pool.setMaxPoolSize(maximumPoolSize);
        pool.setKeepAliveSeconds(keepAliveTime);
        pool.setQueueCapacity(queueCapacity);
        pool.initialize();
		return pool;
	}
	
}






