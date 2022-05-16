package com.taotao.cloud.order.biz.roketmq.event;


import com.taotao.cloud.order.api.dto.order.OrderMessage;

/**
 * 订单状态改变事件
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-05-16 17:33:57
 */
public interface OrderStatusChangeEvent {

	/**
	 * 订单改变
	 *
	 * @param orderMessage 订单消息
	 * @since 2022-05-16 17:33:57
	 */
	void orderChange(OrderMessage orderMessage);
}
