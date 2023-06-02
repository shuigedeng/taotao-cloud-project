package com.taotao.cloud.demo.convert;

import org.dromara.hutoolcore.util.StrUtil;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 用户枚举
 */
public enum UserEnum2 {
	ADMIN("admin"),

	TEST("test");

	@JsonValue
	private String value;

	UserEnum2(String value) {
		this.value = value;
	}

	@JsonCreator
	public static UserEnum2 of(String value) {
		if (StrUtil.isBlank(value)) {
			return null;
		}
		for (UserEnum2 userEnum2: UserEnum2.values()) {
			if (userEnum2.value.equals(value)) {
				return userEnum2;
			}
		}
		return null;
	}

	public String getValue() {
		return value;
	}
}
