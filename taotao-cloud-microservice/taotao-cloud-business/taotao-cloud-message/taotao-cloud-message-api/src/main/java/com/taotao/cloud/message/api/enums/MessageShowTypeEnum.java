package com.taotao.cloud.message.api.enums;

/**
 * 消息展示类型
 */
public enum MessageShowTypeEnum {

	//订单
	ORDER("订单"),
	//售后单
	AFTER_SALE("售后订单"),
	//站内信
	NOTICE("站内信");

	private final String description;

	MessageShowTypeEnum(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}


}
