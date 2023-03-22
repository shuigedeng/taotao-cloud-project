package com.taotao.cloud.log.api.api.enums;

import java.util.Arrays;
import java.util.Optional;
import lombok.Getter;

/**
 * 短链域名类型 - 枚举
 *
 * @since 2022/05/03
 */
public enum ShortLinkDomainTypeEnum {

	ORIGIN(0, "原生"),

	CUSTOMER(1, "用户自建");

	ShortLinkDomainTypeEnum(Integer code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	@Getter
	private final Integer code;

	@Getter
	private final String msg;

	public static Optional<ShortLinkDomainTypeEnum> findEnum(Integer code) {
		return Arrays.stream(ShortLinkDomainTypeEnum.values())
			.filter(itemEnum -> itemEnum.code.equals(code))
			.findFirst();
	}

}
