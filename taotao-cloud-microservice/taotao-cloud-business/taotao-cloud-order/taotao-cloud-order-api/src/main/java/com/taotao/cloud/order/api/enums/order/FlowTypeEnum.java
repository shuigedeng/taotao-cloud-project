package com.taotao.cloud.order.api.enums.order;


/**
 * 流水类型枚举
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-28 09:22:01
 */
public enum FlowTypeEnum {

	/**
	 * 流水类型
	 */
	PAY("支付"),
	REFUND("退款");

	private final String description;

	FlowTypeEnum(String description) {
		this.description = description;
	}

	public String description() {
		return description;
	}
}
