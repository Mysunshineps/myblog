package com.cy.pj.common.interceptor;

import com.cy.pj.entity.DataSourceType;
import com.cy.pj.thread.threadlocal.CustomerThreadLocal;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @Description:    重写 getOrder 保证本切面优先级高于事务切面优先级，
 *  在启动类加上@EnableTransactionManagement(order = 10) 启动事务管理
 * @Author:         psq
 * @CreateDate:     2021/2/25 15:19
 * @Version:        1.0
 */
@Aspect
@Component
public class ReadWriteInterceptor implements Ordered {

    @Override
    public int getOrder() {
        return 0;
    }

    @Pointcut("execution(* com.cy.pj.serviceimpl..*.select*(..))")
    public void readSelect(){}

    @Around(value = "readSelect()")
    private Object readSelect(ProceedingJoinPoint joinPoint) throws Throwable{
        return setRead(joinPoint);
    }

    /**
     * 执行方法
     * @param joinPoint
     * @return
     */
    private Object setRead(ProceedingJoinPoint joinPoint) throws Throwable{
        try{
            Signature sig = joinPoint.getSignature();
            MethodSignature msig = null;
            msig = (MethodSignature) sig;
            Object target = joinPoint.getTarget();
            Method currentMethod = target.getClass().getMethod(msig.getName(), msig.getParameterTypes());
            String name = currentMethod.getName();
            System.out.println("只读方法名："+target.getClass().getName()+"."+name);
            CustomerThreadLocal.setDataSourceType(DataSourceType.SALVE.getType());
            return joinPoint.proceed();
        }finally {
            //移除 主从库设置一方面为了避免内存泄漏，更重要的是避免对后续在本线程上执行的操作产生影响
            CustomerThreadLocal.removeDataSourceType();
        }
    }
}