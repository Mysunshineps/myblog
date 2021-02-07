package com.cy.pj.common.vo;

import java.io.Serializable;
/**
 * 封装控制层要返回给客户端的数据
 */
public class JsonResult implements Serializable{
	private static final long serialVersionUID = 5641672536130824041L;
	/**
	 * 状态码;1代表ok，0代表error
	 */
	private int state=1;
	/**
	 * 状态信息
	 */
	private String message="ok";
	/**
	 * 返回数据
	 */
	private Object data;
	
	public JsonResult() {
	}

	public JsonResult(String message) {
		this.message = message;
	}

	/**
	 * 一般查询时调用，封装查询结果
	 */
	public JsonResult(Object data) {
		this.data = data;
	}

	/**
	 * 出现异常时调用
	 */
	public JsonResult(Throwable e){
		this.state=0;
		this.message=e.getMessage();
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "JsonResult [state=" + state + ", message=" + message + ", data=" + data + "]";
	}
	
	
	
}
