package com.taotao.cloud.order.biz.statemachine.spring;

import org.springframework.statemachine.persist.StateMachinePersister;

/**
 * BizOrderStatusServiceImpl
 *
 * @author shuigedeng
 * @version 2026.02
 * @since 2025-12-19 09:30:45
 */
@Service
public class BizOrderStatusServiceImpl implements BizOrderStatusService {

    @Resource
    private OrderMapper orderMapper;

    @Resource
    private StateMachine<OrderStatusEnum, OrderEvent> orderStateMachine;

    @Resource
    private StateMachinePersister<OrderStatusEnum, OrderEvent, BizOrder> persister;


    /**
     * @param orderId 订单id
     * @param event 事件类型
     */
    @Override
    public void eventHandler( Long orderId, OrderEvent event ) {
        BizOrder order = orderMapper.getOrderById(orderId);
        Assert.notNull(order, "订单不存在");
        // 自定义状态机参数对象(可以在此对象中定义后续需要用到的字段参数，状态配置那里如果需要做业务逻辑判断)
        StateMachineParam param = new StateMachineParam();
        param.setBizOrder(order);
        Message message = MessageBuilder.withPayload(event).build();
        if (!sendEvent(message, param)) {
            throw new ApplicationBizException("订单状态流转异常");
        }
    }


    /**
     * 发送订单状态转换事件 这里不要使用synchronized锁方法，效率比较低， 分布式系统优先采用分布式锁，下单锁userId，订单状态流转锁orderId根据业务考虑使用什么。
     */
    private synchronized boolean sendEvent( Message<OrderEvent> message, StateMachineParam param ) {
        boolean result = false;
        try {
            orderStateMachine.start();
            //尝试恢复状态机状态
            persister.restore(orderStateMachine, param.getBizOrder());
            orderStateMachine.getExtendedState().getVariables().put("param", param);
            result = orderStateMachine.sendEvent(message);
            //持久化状态机状态
            persister.persist(orderStateMachine, param.getBizOrder());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            orderStateMachine.stop();
        }
        return result;
    }

}
