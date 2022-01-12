package com.taotao.cloud.web.error;


/**
 * ApiGlobalError
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-01-12 08:57:33
 */
public class ApiGlobalError {

	private final String code;
	private final String message;

	public ApiGlobalError(String code, String message) {
		this.code = code;
		this.message = message;
	}

	public String getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}
}
