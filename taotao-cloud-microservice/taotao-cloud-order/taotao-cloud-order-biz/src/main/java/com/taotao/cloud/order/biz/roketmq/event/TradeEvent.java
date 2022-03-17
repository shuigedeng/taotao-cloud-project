package com.taotao.cloud.order.biz.roketmq.event;


import com.taotao.cloud.order.api.dto.cart.TradeDTO;

/**
 * 订单创建消息
 */
public interface TradeEvent {

	/**
	 * 订单创建
	 *
	 * @param tradeDTO 交易
	 */
	void orderCreate(TradeDTO tradeDTO);

}
