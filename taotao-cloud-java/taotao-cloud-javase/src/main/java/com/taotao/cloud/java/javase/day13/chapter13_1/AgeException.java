package com.taotao.cloud.java.javase.day13.chapter13_1;
/**
 * 自定义异常
 * (1)继承Exception 或子类
 * (2)添加构造方法
 * @author wgy
 *
 */
public class AgeException extends RuntimeException{

	public AgeException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public AgeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public AgeException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public AgeException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public AgeException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}
	
}
