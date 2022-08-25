package com.taotao.cloud.monitor.kuding.config.conditions;

import org.springframework.core.annotation.Order;

@Order(10)
public class OnExceptionNoticeContition extends PropertiesEnabledCondition {

	public OnExceptionNoticeContition() {
		super("prometheus.exceptionnotice.enabled", true);
	}

}
