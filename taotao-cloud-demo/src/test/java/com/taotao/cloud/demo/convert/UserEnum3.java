package com.taotao.cloud.demo.convert;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 用户枚举
 */
public enum UserEnum3 {
	ADMIN(1),

	TEST(2);

	@JsonValue
	private Integer value;

	UserEnum3(Integer value) {
		this.value = value;
	}

	@JsonCreator
	public static UserEnum3 of(Integer value) {
		if (value == null) {
			return null;
		}
		for (UserEnum3 userEnum2: UserEnum3.values()) {
			if (userEnum2.value.equals(value)) {
				return userEnum2;
			}
		}
		return null;
	}

	public Integer getValue() {
		return value;
	}
}
