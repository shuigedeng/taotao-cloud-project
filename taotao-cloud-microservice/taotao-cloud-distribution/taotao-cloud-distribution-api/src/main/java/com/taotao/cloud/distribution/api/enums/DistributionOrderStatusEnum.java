package com.taotao.cloud.distribution.api.enums;

/**
 * 分销员订单状态
 */
public enum DistributionOrderStatusEnum {
	//待结算（冻结）
	WAIT_BILL("待结算"),
	//待提现
	WAIT_CASH("待提现"),
	//已提现
	COMPLETE_CASH("提现完成"),
	//订单取消
	CANCEL("订单取消"),
	//订单取消
	REFUND("退款");

	private final String description;

	DistributionOrderStatusEnum(String description) {
		this.description = description;
	}
}
