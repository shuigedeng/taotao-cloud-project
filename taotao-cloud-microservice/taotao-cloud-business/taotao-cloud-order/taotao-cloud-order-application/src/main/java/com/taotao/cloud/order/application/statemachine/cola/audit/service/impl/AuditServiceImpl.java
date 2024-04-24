package com.taotao.cloud.order.application.statemachine.cola.audit.service.impl;

import com.taotao.cloud.order.application.statemachine.cola.audit.dao.AuditDao;
import com.taotao.cloud.order.application.statemachine.cola.audit.pojo.dto.AuditDTO;
import com.taotao.cloud.order.application.statemachine.cola.audit.pojo.enums.StateMachineEnum;
import com.taotao.cloud.order.application.statemachine.cola.audit.pojo.event.AuditEvent;
import com.taotao.cloud.order.application.statemachine.cola.audit.pojo.state.AuditState;
import com.taotao.cloud.order.application.statemachine.cola.audit.service.AuditService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @date 2023/7/12 15:56
 */
@Service
@Slf4j
public class AuditServiceImpl implements AuditService {

	@Autowired
	private AuditDao auditDao;

	@Autowired
	private StateMachineEngine stateMachineEngine;

	@Override
	public void audit(AuditContext auditContext) {
		Long id = auditContext.getId();
		AuditDTO auditDTO = auditDao.selectById(id);
		String auditState = auditDTO.getAuditState();
		Integer auditEvent = auditContext.getAuditEvent();
		// 获取当前状态和事件
		AuditState nowState = AuditState.getEnumsByCode(auditState);
		AuditEvent nowEvent = AuditEvent.getEnumsByCode(auditEvent);
		// 执行状态机
		stateMachineEngine.fire(StateMachineEnum.TEST_MACHINE, nowState, nowEvent, auditContext);
	}

	@Override
	public String uml() {
		return stateMachineEngine.generateUml(StateMachineEnum.TEST_MACHINE);
	}
}
