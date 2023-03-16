package com.taotao.cloud.rpc.common.enums;

public enum PackageType {
	REQUEST_PACK(726571, "req"),
	RESPONSE_PACK(726573, "res");

	private final int code;
	private final String message;

	PackageType(int code, String message) {
		this.code = code;
		this.message = message;
	}

	public int getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}
}
