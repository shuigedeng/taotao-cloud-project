package com.taotao.cloud.sys.biz.entity.verification.entity.enums;


/**
 * 验证码资源枚举
 */
public enum VerificationSourceEnum {
	/**
	 * 滑块
	 */
	SLIDER("滑块"),
	/**
	 * 验证码源
	 */
	RESOURCE("验证码源");

	private final String description;

	VerificationSourceEnum(String des) {
		this.description = des;
	}
}
