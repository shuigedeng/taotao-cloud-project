package com.taotao.cloud.order.api.enums.order;

/**
 * 订单出库状态枚举
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-28 09:22:16
 */
public enum OrderOutStatusEnum {

	/**
	 * 等待出库
	 */
	WAIT("等待出库"),

	/**
	 * 出库成功
	 */
	SUCCESS("出库成功"),

	/**
	 * 出库失败
	 */
	FAIL("出库失败");

	private final String description;

	OrderOutStatusEnum(String description) {
		this.description = description;
	}

	public String description() {
		return this.description;
	}


}
