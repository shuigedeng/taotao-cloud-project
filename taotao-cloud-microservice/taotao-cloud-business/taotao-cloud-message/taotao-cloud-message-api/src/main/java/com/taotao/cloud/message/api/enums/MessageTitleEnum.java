package com.taotao.cloud.message.api.enums;

/**
 * 消息标题
 */
public enum MessageTitleEnum {

	/**
	 * 您有新的订单，请您关注
	 */
	NEW_ORDER("您有新的订单，请您关注"),
	/**
	 * 您有订单被支付，请您及时进行发货处理
	 */
	PAY_ORDER("您有订单被支付，请您及时进行发货处理");

	private final String description;

	MessageTitleEnum(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

}
