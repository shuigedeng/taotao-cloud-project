package com.taotao.cloud.order.biz.roketmq.event;


import com.taotao.cloud.order.biz.entity.aftersale.AfterSale;

/**
 * 售后单改变状态
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-05-16 17:33:52
 */
public interface AfterSaleStatusChangeEvent {

	/**
	 * 售后单改变
	 *
	 * @param afterSale 售后
	 * @since 2022-05-16 17:33:52
	 */
	void afterSaleStatusChange(AfterSale afterSale);
}
