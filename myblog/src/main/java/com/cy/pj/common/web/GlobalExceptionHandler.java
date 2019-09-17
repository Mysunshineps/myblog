package com.cy.pj.common.web;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cy.pj.common.vo.JsonResult;


/*全局异常处理类*/
@ControllerAdvice//全局异常处理注解（是spring MVC中的)，描述的类是全局异常处理类
				 //可以在此类中定义多个异常处理方法
public class GlobalExceptionHandler {
	
	
	//JDK中的自带的日志API
	
	//使用@ExceptionHandler注解的方法是异常处理方法
	@ExceptionHandler(RuntimeException.class)
	@ResponseBody
	public JsonResult doHandleRuntimeException(RuntimeException e) {
		e.printStackTrace();//也可以写日志
		return new JsonResult(e);//封装异常信息，JsonResult(e)对应的是JsonResult(Throwable e){}
		
	}
	
}
