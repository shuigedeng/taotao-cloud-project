package com.taotao.cloud.order.biz.roketmq.event;


import com.taotao.cloud.order.api.dto.cart.TradeDTO;

/**
 * 订单创建消息
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-05-16 17:34:02
 */
public interface TradeEvent {

	/**
	 * 订单创建
	 *
	 * @param tradeDTO 交易
	 * @since 2022-05-16 17:34:02
	 */
	void orderCreate(TradeDTO tradeDTO);

}
