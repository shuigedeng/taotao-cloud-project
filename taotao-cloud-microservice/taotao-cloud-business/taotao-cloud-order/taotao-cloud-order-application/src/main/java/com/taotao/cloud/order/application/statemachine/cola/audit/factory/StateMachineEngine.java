package com.taotao.cloud.order.application.statemachine.cola.audit.factory;

import com.alibaba.cola.statemachine.StateMachine;
import com.alibaba.cola.statemachine.StateMachineFactory;
import com.taotao.cloud.order.application.statemachine.cola.audit.pojo.enums.StateMachineEnum;
import com.taotao.cloud.order.application.statemachine.cola.audit.pojo.state.AuditState;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @date 2023/7/12 16:58
 */
@Component
public class StateMachineEngine implements InitializingBean {

	private Map<String, StateMachineStrategy> stateMachineMap;

	@Autowired
	private ApplicationContext applicationContext;

	/**
	 * 根据枚举获取状态机实例key
	 *
	 * @param stateMachineEnum stateMachineEnum
	 * @return String
	 */
	private String getMachine(StateMachineEnum stateMachineEnum) {
		return stateMachineMap.get(stateMachineEnum.getCode()).getMachineType();
	}

	/**
	 * 根据枚举获取状态机示例，并根据当前状态、事件、上下文，进行状态流转
	 *
	 * @param stateMachineEnum stateMachineEnum
	 * @param auditState       auditState
	 * @param auditEvent       auditEvent
	 * @param auditContext     auditContext
	 * @return AuditState
	 */
	public AuditState fire(StateMachineEnum stateMachineEnum, AuditState auditState,
						   AuditEvent auditEvent, AuditContext auditContext) {
		StateMachine<AuditState, AuditEvent, AuditContext> stateMachine = StateMachineFactory.get(getMachine(stateMachineEnum));
		return stateMachine.fireEvent(auditState, auditEvent, auditContext);
	}

	/**
	 * 根据枚举获取状态机示例的状态DSL UML图
	 *
	 * @param stateMachineEnum stateMachineEnum
	 * @return String
	 */
	public String generateUml(StateMachineEnum stateMachineEnum) {
		StateMachine<AuditState, AuditEvent, AuditContext> stateMachine = StateMachineFactory.get(getMachine(stateMachineEnum));
		return stateMachine.generatePlantUML();
	}

	/**
	 * 获取所有实现了接口的状态机
	 */
	@Override
	public void afterPropertiesSet() {
		Map<String, StateMachineStrategy> beansOfType = applicationContext.getBeansOfType(StateMachineStrategy.class);
		stateMachineMap = Optional.of(beansOfType)
			.map(beansOfTypeMap -> beansOfTypeMap.values().stream()
				.filter(stateMachineHandler -> StringUtils.hasLength(stateMachineHandler.getMachineType()))
				.collect(Collectors.toMap(StateMachineStrategy::getMachineType, Function.identity())))
			.orElse(new HashMap<>(8));
	}
}
