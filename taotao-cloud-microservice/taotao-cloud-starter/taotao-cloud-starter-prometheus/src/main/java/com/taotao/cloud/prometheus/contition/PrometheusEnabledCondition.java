package com.taotao.cloud.prometheus.contition;

import org.springframework.core.annotation.Order;

@Order(0)
public class PrometheusEnabledCondition extends PropertiesEnabledCondition {

	public PrometheusEnabledCondition() {
		super("prometheus.enabled", true);
	}

}
