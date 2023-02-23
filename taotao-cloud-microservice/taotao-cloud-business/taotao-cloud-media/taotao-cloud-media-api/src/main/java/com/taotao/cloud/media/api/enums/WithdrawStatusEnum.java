package com.taotao.cloud.media.api.enums;

/**
 * 提现申请状态枚举类
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-25 16:37:42
 */
public enum WithdrawStatusEnum {
	/**
	 * 申请中
	 */
	APPLY("申请中"),
	/**
	 * 审核成功即提现成功
	 */
	VIA_AUDITING("审核通过"),
	/**
	 * 审核未通过
	 */
	FAIL_AUDITING("审核未通过");

	private final String description;

	public String description() {
		return description;
	}

	WithdrawStatusEnum(String description) {
		this.description = description;
	}

}
