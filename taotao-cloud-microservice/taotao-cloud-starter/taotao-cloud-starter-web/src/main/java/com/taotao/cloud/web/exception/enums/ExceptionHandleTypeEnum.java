package com.taotao.cloud.web.exception.enums;


/**
 * 异常处理类型
 */
public enum ExceptionHandleTypeEnum {

	/**
	 * 异常处理通知类型 说明
	 */
	LOGGER("不通知"), DING_TALK("通过钉钉通知"), MAIL("邮件通知");

	private final String text;

	public String getText() {
		return text;
	}

	ExceptionHandleTypeEnum(String text) {
		this.text = text;
	}
}
