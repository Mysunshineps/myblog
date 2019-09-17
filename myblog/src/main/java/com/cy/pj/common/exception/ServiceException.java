package com.cy.pj.common.exception;

public class ServiceException extends RuntimeException{

	private static final long serialVersionUID = -5598865415547474216L;
	public ServiceException() {
		super();
	}
	
	public ServiceException(String message){
		super(message);
	}
	public ServiceException(Throwable cause){
		super(cause);
	}
	
}
