package com.taotao.cloud.schedule.gloriaapi.common.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 全局响应对象
 * @author Carlos  carlos_love_gloria@163.com
 * @since 2022/3/28
 * @version 1.0.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommonResult<T> implements Serializable {
	private long code;
	private String message;
	private T data;
	
	protected CommonResult(IErrorCode errorCode) {
		this.code = errorCode.getCode();
		this.message = errorCode.getMessage();
		this.data = null;
	}

	public static  <T> CommonResult<T> success(){
		return new CommonResult<T>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), null);
	}

	public static  <T> CommonResult<T> success(T data){
		return new CommonResult<T>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), data);
	}

	public static CommonResult<Map<String, Object>> put(String key, Object value){
		CommonResult<Map<String, Object>> success = success(new HashMap<>());
		success.getData().put(key, value);
		return success;
	}

	public static CommonResult<String> failed(String message){
		return new CommonResult<String>(ResultCode.SYSTEM_ERROR.getCode(), message, null);
	}

	public static CommonResult<String> failed() {
		return failed(ResultCode.SYSTEM_ERROR.getMessage());
	}

	public static <T>CommonResult<T> failed(final long code, final String message) {
		return new CommonResult<T>(code, message, null);
	}

	public static <T>CommonResult<T> failed(IErrorCode errorCode){
		return failed(errorCode.getCode(), errorCode.getMessage());
	}
	
	public static <T> CommonResult<T> sessionInvalid(){
		return failed(ResultCode.SESSION_INVALID);
	}
	
	public static <T> CommonResult<T> verificationFrequency(){
		return failed(ResultCode.VERIFICATION_FREQUENCY);
	}
	
	public static <T> CommonResult<T> verificationError(){
		return failed(ResultCode.VERIFICATION_ERROR);
	}
	
	public static <T> CommonResult<T> verificationTimeout(){
		return failed(ResultCode.VERIFICATION_TIMEOUT);
	}
	
	public static <T> CommonResult<T> accountInvalid(){
		return failed(ResultCode.ACCOUNT_INVALID);
	}
	
	public static <T> CommonResult<T> loginFrequency(){
		return failed(ResultCode.LOGIN_FREQUENCY);
	}
	
	public static <T> CommonResult<T> accountNoPrivilege(){
		return failed(ResultCode.ACCOUNT_NO_PRIVILEGE);
	}
	
	public static <T> CommonResult<T> tokenMissing(){
		return failed(ResultCode.TOKEN_MISSING);
	}
	
	public static <T> CommonResult<T> tokenInvalid(){
		return failed(ResultCode.TOKEN_INVALID);
	}
	
	public static <T> CommonResult<T> tokenExpire(){
		return failed(ResultCode.TOKEN_EXPIRE);
	}
	
	public static <T> CommonResult<T> tokenTimeout(){
		return failed(ResultCode.TOKEN_TIMEOUT);
	}
	
	public static <T> CommonResult<T> requestError(){
		return failed(ResultCode.REQUEST_ERROR);
	}

	public static <T> CommonResult<T> requestError(String message){
		return failed(ResultCode.REQUEST_ERROR.getCode(), message);
	}

	public static <T> CommonResult<T> notFound(){
		return failed(ResultCode.NOT_FOUND);
	}


}
