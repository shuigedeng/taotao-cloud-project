package com.taotao.cloud.schedule.gloriaapi.common.vo;

/**
 * 错误码
 * @author Carlos  carlos_love_gloria@163.com
 * @since 2022/3/28
 * @version 1.0.0
 */
public enum ResultCode implements IErrorCode{
	
	SUCCESS(20000,"操作成功"),

	SESSION_INVALID(40000,"会话失效，请刷新登录页面，重新登录"),
	VERIFICATION_FREQUENCY(40001,"请勿频繁刷新验证码"),
	VERIFICATION_ERROR(40002,"验证码错误"),
	VERIFICATION_TIMEOUT(40003,"验证码已过期"),
	
	ACCOUNT_INVALID(40101,"用户名或密码错误"),
	ACCOUNT_NO_PRIVILEGE(40102,"用户没有登录权限"),
	LOGIN_FREQUENCY(40103,"用户频繁登录，请稍后再试"),
	
	TOKEN_MISSING(40200,"用户未登录"),
	TOKEN_INVALID(40201,"令牌无效，请重新登录"),
	TOKEN_EXPIRE(40203,"令牌过期，请重新登录"),
	TOKEN_TIMEOUT(40204,"用户长时间未操作，已自动注销，请重新登录"),
	
	REQUEST_ERROR(40300,"请求参数有误"),
	NOT_FOUND(40400,"请求资源未找到"),
	
	SYSTEM_ERROR(50000,"系统未知错误");
	
	
	private final long code;
	private final String message;
	
	ResultCode(long code, String message) {
		this.code = code;
		this.message = message;
	}

	public long getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}
	
	
}
