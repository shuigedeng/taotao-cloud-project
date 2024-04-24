package com.taotao.cloud.order.application.statemachine.cola.order.config;

import com.alibaba.cola.statemachine.StateMachine;
import com.alibaba.cola.statemachine.builder.StateMachineBuilder;
import com.alibaba.cola.statemachine.builder.StateMachineBuilderFactory;
import com.taotao.cloud.order.application.statemachine.cola.order.entity.Order;
import com.taotao.cloud.order.application.statemachine.cola.order.enums.OrderEvent;
import com.taotao.cloud.order.application.statemachine.cola.order.enums.OrderStatus;
import com.taotao.cloud.order.application.statemachine.cola.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 初始化状态机
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class StateMachineRegister {
	public static final String STATE_MACHINE_ID = "orderStateMachineId";

	private final OrderService orderService;

	/**
	 * 构建状态机实例
	 */
	@Bean
	public StateMachine<OrderStatus, OrderEvent, Order> stateMachine() {
		// 第一步：生成一个状态机builder
		StateMachineBuilder<OrderStatus, OrderEvent, Order> stateMachineBuilder = StateMachineBuilderFactory.create();

		// 第二步：设置一个状态转移类型的builder，并设置from\to\on\when\perform
		// 订单暂存触发事件(源状态和目标状态一致，我们可以用内部流转表示)
		stateMachineBuilder.internalTransition()
			.within(OrderStatus.DRAFT)
			.on(OrderEvent.SAVE)
			.perform(orderService.saveAction());


		// 提交操作触发事件(用于多个流转的构建器)
		stateMachineBuilder.externalTransitions()
			.fromAmong(OrderStatus.DRAFT, OrderStatus.APPROVAL_FAIL)
			.to(OrderStatus.AUDITING)
			.on(OrderEvent.SUBMIT)
			.when(orderService.submitCondition())
			.perform(orderService.submitAction());

		// 确认操作触发事件（用于一个流转的构建器）
		stateMachineBuilder.externalTransition()
			.from(OrderStatus.WAIT_SIGN_SEAL)
			.to(OrderStatus.WAIT_CONFIRM)
			.on(OrderEvent.SEAL_SUCCEED)
			.when(orderService.sealSucceedCondition())
			.perform(orderService.sealSucceedAction());

		// 设置状态机的id，并在StateMachineFactory中的stateMachineMap进行注册
		return stateMachineBuilder.build(STATE_MACHINE_ID);

	}


}
