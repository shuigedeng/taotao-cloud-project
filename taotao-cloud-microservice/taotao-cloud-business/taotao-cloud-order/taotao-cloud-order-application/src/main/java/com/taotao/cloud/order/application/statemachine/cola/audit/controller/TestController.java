package com.taotao.cloud.order.application.statemachine.cola.audit.controller;

import com.taotao.cloud.order.application.statemachine.cola.audit.pojo.param.AuditParam;
import com.taotao.cloud.order.application.statemachine.cola.audit.service.AuditService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @date 2023/7/12 15:50
 */
@RestController
@RequestMapping("/test")
@Slf4j
public class TestController {

	@Autowired
	private AuditService auditService;

	@PostMapping("/audit")
	public void audit(@RequestBody @Validated AuditParam auditParam) {
		AuditContext auditContext = new AuditContext();
		BeanUtils.copyProperties(auditParam, auditContext);
		auditService.audit(auditContext);
	}

	@GetMapping("/uml")
	public String uml() {
		return auditService.uml();
	}
}
