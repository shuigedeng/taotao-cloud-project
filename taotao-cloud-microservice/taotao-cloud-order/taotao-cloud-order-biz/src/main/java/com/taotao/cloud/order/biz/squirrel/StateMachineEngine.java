package com.taotao.cloud.order.biz.squirrel;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.transaction.annotation.Transactional;
import org.squirrelframework.foundation.fsm.StateMachine;
import org.squirrelframework.foundation.fsm.StateMachineBuilder;
import org.squirrelframework.foundation.fsm.StateMachineBuilderFactory;
import org.squirrelframework.foundation.fsm.UntypedStateMachine;
import org.squirrelframework.foundation.fsm.UntypedStateMachineBuilder;

/**
* StateMachineBuilder实例
*/
public class StateMachineEngine <T extends UntypedStateMachine, S, E, C> implements
	ApplicationContextAware {


	private ApplicationContext applicationContext;


	private static Map<String, UntypedStateMachineBuilder> builderMap = new HashMap<String, UntypedStateMachineBuilder>();

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}


	@Transactional
	public void fire(Class<T> machine, S state, E event, C context) {
		StateMachineBuilder stateMachineBuilder = this.getStateMachineBuilder(machine);
		StateMachine stateMachine = stateMachineBuilder.newStateMachine(state, applicationContext);
		stateMachine.fire(event, context);
	}


	private StateMachineBuilder getStateMachineBuilder(Class<T> stateMachine) {
		UntypedStateMachineBuilder stateMachineBuilder = builderMap.get(stateMachine.getName());
		if (stateMachineBuilder == null) {
			stateMachineBuilder = StateMachineBuilderFactory.create(stateMachine,
				ApplicationContext.class);
			builderMap.put(stateMachine.getName(), stateMachineBuilder);
		}
		return stateMachineBuilder;
	}
}
