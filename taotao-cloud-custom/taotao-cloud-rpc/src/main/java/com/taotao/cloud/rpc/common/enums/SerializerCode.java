package com.taotao.cloud.rpc.common.enums;

public enum SerializerCode {
	KRYO(0),// KRYO 序列化 方式
	JSON(1), // JSON 序列化方式
	HESSIAN(2); // HESSIAN 序列化方式
	private final int code;

	SerializerCode(int code) {
		this.code = code;
	}

	public int getCode() {
		return code;
	}
}
