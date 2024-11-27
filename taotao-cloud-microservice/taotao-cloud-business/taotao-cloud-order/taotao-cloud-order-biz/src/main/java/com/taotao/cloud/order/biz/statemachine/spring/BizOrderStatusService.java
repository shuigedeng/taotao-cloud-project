package com.taotao.cloud.order.biz.statemachine.spring;

public interface BizOrderStatusService {

    /**
     *
     * 通用状态变更处理器
     * @param incomingId
     * @param event
     */
    void eventHandler(Long orderId, OrderEvent event);

}
