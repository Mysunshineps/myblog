package com.cy.pj.common.exception;

import java.io.PrintStream;
import java.io.PrintWriter;

/**
* @Description:    无此客户异常
* @Author:         psq
* @CreateDate:     2021/2/22 10:58
* @Version:        1.0
*/
public class NoSuchCustomerException extends RuntimeException{
    public NoSuchCustomerException() {
        super("no such customer: ");
    }

    public NoSuchCustomerException(String message) {
        super(message);
    }

    public NoSuchCustomerException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoSuchCustomerException(Throwable cause) {
        super(cause);
    }

    protected NoSuchCustomerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }

    @Override
    public String getLocalizedMessage() {
        return super.getLocalizedMessage();
    }

    @Override
    public synchronized Throwable getCause() {
        return super.getCause();
    }

    @Override
    public synchronized Throwable initCause(Throwable cause) {
        return super.initCause(cause);
    }

    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    public void printStackTrace() {
        super.printStackTrace();
    }

    @Override
    public void printStackTrace(PrintStream s) {
        super.printStackTrace(s);
    }

    @Override
    public void printStackTrace(PrintWriter s) {
        super.printStackTrace(s);
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return super.fillInStackTrace();
    }

    @Override
    public StackTraceElement[] getStackTrace() {
        return super.getStackTrace();
    }

    @Override
    public void setStackTrace(StackTraceElement[] stackTrace) {
        super.setStackTrace(stackTrace);
    }
}
