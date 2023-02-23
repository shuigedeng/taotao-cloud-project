package com.taotao.cloud.order.api.enums.trade;

/**
 * 退款方式
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-28 09:23:33
 */
public enum AfterSaleRefundWayEnum {

	/**
	 * "原路退回"
	 */
	ORIGINAL("原路退回"),
	/**
	 * "线下支付"
	 */
	OFFLINE("线下支付");

	private final String description;

	AfterSaleRefundWayEnum(String description) {
		this.description = description;
	}

	public String description() {
		return description;
	}

}
