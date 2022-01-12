package com.taotao.cloud.web.error;

/**
 * ApiParameterError 
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-01-12 09:05:51
 */
public class ApiParameterError {

	private final String code;
	private final String parameter;
	private final String message;
	private final Object rejectedValue;

	public ApiParameterError(String code, String parameter, String message, Object rejectedValue) {
		this.code = code;
		this.parameter = parameter;
		this.message = message;
		this.rejectedValue = rejectedValue;
	}

	public String getCode() {
		return code;
	}

	public String getParameter() {
		return parameter;
	}

	public String getMessage() {
		return message;
	}

	public Object getRejectedValue() {
		return rejectedValue;
	}
}
