package com.cy.pj.common.config;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
@Configuration
//告诉spring从配置文件中读取前缀为executor配置然后
//赋值给对象中的属性(需要供set方法)
@ConfigurationProperties("executor")
@Setter
@Slf4j
public class SpringExecutorConfig implements AsyncConfigurer{
	private int corePoolSize=10;
	private int maximumPoolSize=20;
	private int keepAliveTime=30;
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
        //设置拒绝执行的处理
        pool.setRejectedExecutionHandler(new RejectedExecutionHandler() {
			@Override
			public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
				log.error("队列满了,拒绝执行");
			}
		});
        pool.initialize();
		return pool;
	}
}






