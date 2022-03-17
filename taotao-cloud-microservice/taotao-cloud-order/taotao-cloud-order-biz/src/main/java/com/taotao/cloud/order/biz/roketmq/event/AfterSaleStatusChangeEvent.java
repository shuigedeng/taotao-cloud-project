package com.taotao.cloud.order.biz.roketmq.event;


import com.taotao.cloud.order.biz.entity.aftersale.AfterSale;

/**
 * 售后单改变状态
 */
public interface AfterSaleStatusChangeEvent {

	/**
	 * 售后单改变
	 *
	 * @param afterSale 售后
	 */
	void afterSaleStatusChange(AfterSale afterSale);
}
