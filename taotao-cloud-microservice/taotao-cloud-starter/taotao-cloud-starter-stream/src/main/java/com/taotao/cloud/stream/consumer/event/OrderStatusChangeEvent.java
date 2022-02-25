package com.taotao.cloud.stream.consumer.event;

import cn.lili.modules.order.order.entity.dto.OrderMessage;

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
