package com.taotao.cloud.order.biz;

import com.alibaba.cola.statemachine.Action;
import com.alibaba.cola.statemachine.Condition;
import com.alibaba.cola.statemachine.StateMachine;
import com.alibaba.cola.statemachine.builder.StateMachineBuilder;
import com.alibaba.cola.statemachine.builder.StateMachineBuilderFactory;
import com.shdatalink.statemachin.entity.Order;
import com.shdatalink.statemachin.enums.OrderEvent;
import com.shdatalink.statemachin.enums.OrderStatus;
import com.shdatalink.statemachin.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


/**
 * ColaStatemachinDemoApplicationTests
 *
 * @author shuigedeng
 * @version 2026.01
 * @since 2025-12-19 09:30:45
 */
@SpringBootTest
class ColaStatemachinDemoApplicationTests {

    private static final String MACHINE_ID = "orderStatusMachine";

    @Autowired
    private OrderService orderService;

    @Test
    public void orderSave() {
        Order order = new Order();
        order.setSerialNo("OD2021011");
        order.setCreateBy("admin");
        orderService.save(order);
    }

    @Test
    public void orderSubmit() {
        Order order = new Order();
        order.setId(7);
        order.setSerialNo("OD2021011");
        order.setCreateBy("admin");
        order.setStatus(OrderStatus.COMPLETED);
        orderService.submit(order);
    }

    @Test
    public void orderConfirm() {
        Order order = new Order();
        order.setId(7);
        order.setSerialNo("OD2021011");
        order.setCreateBy("admin");
        order.setStatus(OrderStatus.WAIT_SIGN_SEAL);
        orderService.confirm(order);
    }


}
