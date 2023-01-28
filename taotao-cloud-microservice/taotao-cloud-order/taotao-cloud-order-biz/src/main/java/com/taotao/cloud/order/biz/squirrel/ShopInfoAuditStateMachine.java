package com.taotao.cloud.order.biz.squirrel;

import org.springframework.context.ApplicationContext;
import org.squirrelframework.foundation.fsm.annotation.State;
import org.squirrelframework.foundation.fsm.annotation.StateMachineParameters;
import org.squirrelframework.foundation.fsm.annotation.States;
import org.squirrelframework.foundation.fsm.annotation.Transit;
import org.squirrelframework.foundation.fsm.annotation.Transitions;
import org.squirrelframework.foundation.fsm.impl.AbstractUntypedStateMachine;

/**
* 店铺审核状态机
*/
@States({
@State(name = "audit"),
@State(name = "agree"),
@State(name = "reject")
})
@Transitions({
@Transit(from = "audit", to = "agree", on = "AGREE", callMethod = "agree"),
@Transit(from = "audit", to = "reject", on = "REJECT", callMethod = "reject"),
@Transit(from = "reject", to = "audit", on = "SUBMIT", callMethod = "submit"),
@Transit(from = "agree", to = "audit", on = "SUBMIT", callMethod = "submit"),
@Transit(from = "audit", to = "audit", on = "SUBMIT", callMethod = "submit"),
})
@StateMachineParameters(stateType=ShopInfoAuditStatusEnum.class, eventType=ShopInfoAuditEvent.class, contextType=ShopInfoAuditStatusUpdateParam.class)
public class ShopInfoAuditStateMachine extends AbstractUntypedStateMachine {

	private ApplicationContext applicationContext;

	public ShopInfoAuditStateMachine() {
	}

	public ShopInfoAuditStateMachine(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	// 审核通过业务逻辑
	public void agree(ShopInfoAuditStatusEnum fromState, ShopInfoAuditStatusEnum toState,
		ShopInfoAuditEvent event, ShopInfoAuditStatusUpdateParam param) {
		this.agree(fromState, toState, event, param);
	}

	// 审核驳回业务逻辑
	public void reject(ShopInfoAuditStatusEnum fromState, ShopInfoAuditStatusEnum toState,
		ShopInfoAuditEvent event, ShopInfoAuditStatusUpdateParam param) {
		this.reject(fromState, toState, event, param);
	}

	// 提交业务逻辑
	public void submit(ShopInfoAuditStatusEnum fromState, ShopInfoAuditStatusEnum toState,
		ShopInfoAuditEvent event, ShopInfoAuditStatusUpdateParam param) {
		this.submit(fromState, toState, event, param);
	}

	public  void main(String[] args, ShopInfoAuditStatusUpdateParam param) {
		StateMachineEngine stateMachineEngine = getApplicationContext().getBean(StateMachineEngine.class);
// 审核通过调case
		stateMachineEngine.fire(ShopInfoAuditStateMachine.class,ShopInfoAuditStatusEnum.audit,ShopInfoAuditEvent.AGREE,param);
// 审核驳回case
		stateMachineEngine.fire(ShopInfoAuditStateMachine.class,ShopInfoAuditStatusEnum.audit,ShopInfoAuditEvent.REJECT,param);
	}
}
