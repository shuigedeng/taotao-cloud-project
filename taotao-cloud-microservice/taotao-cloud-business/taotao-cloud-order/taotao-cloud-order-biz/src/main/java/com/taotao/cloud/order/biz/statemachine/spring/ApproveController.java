package com.taotao.cloud.order.biz.statemachine.spring;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ApproveController
 *
 * @author shuigedeng
 * @version 2026.01
 * @since 2025-12-19 09:30:45
 */
@RestController
@RequiredArgsConstructor
public class ApproveController {

    private final OrderStatusService orderStatusService;


    /**
     * 前端调用start方法将订单状态改为审核中，并自动持久化到数据库
     *
     * @param orderId 订单id
     */
    @PostMapping("/start")
    public void start( Long orderId ) {
        orderStatusService.eventHandler(orderId, OrderEvent.APPROVE_START);
    }

    /**
     * 前端调用start方法将订单状态改为审核成功，并自动持久化到数据库
     *
     * @param orderId 订单id
     */
    @PostMapping("/approveSuccess")
    public void approveSuccess( Long orderId ) {
        orderStatusService.eventHandler(orderId, OrderEvent.APPROVE_SUCCESS);
    }

    /**
     * 前端调用start方法将订单状态改为审核失败，并自动持久化到数据库
     *
     * @param orderId 订单id
     */
    @PostMapping("/approveFailed")
    public void approveFailed( Long orderId ) {
        orderStatusService.eventHandler(orderId, OrderEvent.APPROVE_FAILED);
    }

}
