package com.taotao.cloud.sys.biz.tools.soap.exception;

/**
 * 
 * 功能:不支持的 portName<br/>
 */
public class UnSupportPortNameException extends RuntimeException{

	public UnSupportPortNameException(String message){
		super(message);
	}
}
