package com.taotao.cloud.prometheus.config.conditions;

import org.springframework.core.annotation.Order;

@Order(10)
public class OnServiceMonitorNoticeCondition extends PropertiesEnabledCondition {

	public OnServiceMonitorNoticeCondition() {
		super("prometheus.service-monitor.enabled", false);
	}

}
