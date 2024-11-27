package com.taotao.cloud.order.biz.statemachine.spring;

public enum OrderEvent {
	// 开始审核
	APPROVE_START,
	// 审核通过
	APPROVE_SUCCESS,
	// 审核失败
	APPROVE_FAILED,
	//操作放款
	LOAN;


}
