package com.taotao.cloud.order.biz.statemachine.spring;

public enum OrderStatusEnum {

	// 待审核
	APPROVE_PENDING,
	// 审核中
	APPROVE_ING,
	// 审核失败
	APPROVE_FAILED,
	// 审核成功
	APPROVE_SUCCESS,
	// 放款成功
	LOAN_SUCCESS,
	// 部分放款成功
	PARTIALLY_LOAN_SUCCESS;

}
