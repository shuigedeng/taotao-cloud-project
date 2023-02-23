package com.taotao.cloud.order.api.enums.order;

/**
 * 发货状态枚举
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-28 09:21:55
 */
public enum DeliverStatusEnum {

	/**
	 * 发货状态
	 */
	UNDELIVERED("未发货"),
	DELIVERED("已发货"),
	RECEIVED("已收货");

	private final String description;

	DeliverStatusEnum(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

}
