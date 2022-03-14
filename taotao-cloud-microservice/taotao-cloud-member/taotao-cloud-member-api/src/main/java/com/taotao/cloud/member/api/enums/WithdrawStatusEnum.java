package com.taotao.cloud.member.api.enums;

/**
 * 提现申请状态枚举类
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

	private String description;

	public String description() {
		return description;
	}

	WithdrawStatusEnum(String description) {
		this.description = description;
	}

}
