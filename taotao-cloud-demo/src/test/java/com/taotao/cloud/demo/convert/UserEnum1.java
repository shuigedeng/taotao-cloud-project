package com.taotao.cloud.demo.convert;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 用户枚举
 */
public enum UserEnum1 {
	ADMIN("admin"),

	TEST("test");

	@JsonValue
	private String value;

	UserEnum1(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
