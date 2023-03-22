package com.taotao.cloud.log.api.api.enums;

import java.util.Arrays;
import java.util.Optional;
import lombok.Getter;

/**
 * 通用枚举
 *
 * @since 2022/02/23
 */
public enum BooleanEnum {

	/**
	 * 否
	 */
	FALSE(0, "false"),

	/**
	 * 是
	 */
	TRUE(1, "true");

	BooleanEnum(Integer code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	@Getter
	private final Integer code;

	@Getter
	private final String msg;

	public static Optional<BooleanEnum> findEnum(Integer code) {
		return Arrays.stream(BooleanEnum.values())
			.filter(booleanEnum -> booleanEnum.code.equals(code))
			.findFirst();
	}
}
