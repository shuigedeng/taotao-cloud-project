package com.taotao.cloud.mq.stream.framework.trigger.enums;

/**
 * 队列枚举
 */
public enum DelayQueueEnums {

	/**
	 * 促销活动
	 */
	PROMOTION("促销活动"),
	ORDER("订单延迟");

	private final String description;

	DelayQueueEnums(String description) {
		this.description = description;
	}
}
