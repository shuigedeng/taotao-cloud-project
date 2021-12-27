package com.taotao.cloud.stock.biz.common.util.exception;

/**
 * 自定义异常
 *
 * @author haoxin
 * @date 2021-02-02
 **/
public class XTException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
    private String msg;
    private int code = 500;
    
    public XTException(String msg) {
		super(msg);
		this.msg = msg;
	}
	
	public XTException(String msg, Throwable e) {
		super(msg, e);
		this.msg = msg;
	}
	
	public XTException(String msg, int code) {
		super(msg);
		this.msg = msg;
		this.code = code;
	}
	
	public XTException(String msg, int code, Throwable e) {
		super(msg, e);
		this.msg = msg;
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}
	
	
}
