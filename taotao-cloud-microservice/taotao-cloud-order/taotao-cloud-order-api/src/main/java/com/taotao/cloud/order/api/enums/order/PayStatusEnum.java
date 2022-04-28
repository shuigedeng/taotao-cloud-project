package com.taotao.cloud.order.api.enums.order;

/**
 * 订单状态枚举
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-28 09:22:31
 */
public enum PayStatusEnum {

	/**
	 * 支付状态
	 */
	UNPAID("待付款"),
	PAID("已付款"),
	CANCEL("已取消");

	private final String description;

	PayStatusEnum(String description) {
		this.description = description;
	}

	public String description() {
		return this.description;
	}


}
