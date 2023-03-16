package com.taotao.cloud.rpc.common.enums;


public enum ResponseCode {
	SUCCESS(200, "success"),
	FAILURE(500, "fail");

	private final int code;
	private final String message;

	private ResponseCode(int code, String message) {
		this.code = code;
		this.message = message;
	}

	public Integer getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}

}
