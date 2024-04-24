package com.taotao.cloud.order.application.statemachine.cola.order.service;

import com.alibaba.cola.statemachine.Action;
import com.alibaba.cola.statemachine.Condition;
import com.alibaba.cola.statemachine.StateMachine;
import com.alibaba.cola.statemachine.StateMachineFactory;
import com.taotao.cloud.order.application.statemachine.cola.config.StateMachineRegister;
import com.taotao.cloud.order.application.statemachine.cola.entity.Order;
import com.taotao.cloud.order.application.statemachine.cola.enums.OrderEvent;
import com.taotao.cloud.order.application.statemachine.cola.enums.OrderStatus;
import com.taotao.cloud.order.application.statemachine.cola.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

	private final OrderRepository orderRepository;

	/**
	 * 订单暂存
	 */
	public void save(Order order) {
		StateMachine<OrderStatus, OrderEvent, Order> stateMachine = StateMachineFactory.get(StateMachineRegister.STATE_MACHINE_ID);
		// 触发状态机
		OrderStatus status = order.getStatus() == null ? OrderStatus.DRAFT : order.getStatus();
		stateMachine.fireEvent(status, OrderEvent.SAVE, order);
		// 打印状态流转
		stateMachine.showStateMachine();
	}

	/**
	 * 订单提交
	 */
	public void submit(Order order) {
		StateMachine<OrderStatus, OrderEvent, Order> stateMachine = StateMachineFactory.get(StateMachineRegister.STATE_MACHINE_ID);
		stateMachine.fireEvent(order.getStatus(), OrderEvent.SUBMIT, order);
	}

	/**
	 * 订单确认
	 */
	public void confirm(Order order) {
		StateMachine<OrderStatus, OrderEvent, Order> stateMachine = StateMachineFactory.get(StateMachineRegister.STATE_MACHINE_ID);
		stateMachine.fireEvent(order.getStatus(), OrderEvent.SEAL_SUCCEED, order);
	}

	public Action<OrderStatus, OrderEvent, Order> saveAction() {
		return (from, to, event, order) -> this.saveOrderStatus(order, to);
	}

	public Action<OrderStatus, OrderEvent, Order> submitAction() {
		return (from, to, event, order) -> this.saveOrderStatus(order, to);
	}

	public Action<OrderStatus, OrderEvent, Order> sealSucceedAction() {
		return (from, to, event, order) -> this.saveOrderStatus(order, to);
	}

	private void saveOrderStatus(Order order, OrderStatus toStatus) {
		order.setStatus(toStatus);
		orderRepository.save(order);


	}

	public Condition<Order> submitCondition() {
		return order -> true;
	}


	public Condition<Order> sealSucceedCondition() {
		return order -> OrderStatus.WAIT_SIGN_SEAL.equals(order.getStatus());
	}


}
