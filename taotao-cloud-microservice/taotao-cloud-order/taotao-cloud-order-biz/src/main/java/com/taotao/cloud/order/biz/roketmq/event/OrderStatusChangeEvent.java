package com.taotao.cloud.order.biz.roketmq.event;


import com.taotao.cloud.order.api.dto.order.OrderMessage;

/**
 * 订单状态改变事件
 */
public interface OrderStatusChangeEvent {

    /**
     * 订单改变
     * @param orderMessage 订单消息
     */
    void orderChange(OrderMessage orderMessage);
}
