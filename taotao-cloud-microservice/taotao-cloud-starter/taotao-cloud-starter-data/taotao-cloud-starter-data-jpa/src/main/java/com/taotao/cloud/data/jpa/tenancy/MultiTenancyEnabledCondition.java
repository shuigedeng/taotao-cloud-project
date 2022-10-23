package com.taotao.cloud.data.jpa.tenancy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * <p>Description: Couchdb 注入开启条件 </p>
 */
public class MultiTenancyEnabledCondition implements Condition {

	private static final Logger log = LoggerFactory.getLogger(MultiTenancyEnabledCondition.class);

	@SuppressWarnings("NullableProblems")
	@Override
	public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata metadata) {
		String property = conditionContext.getEnvironment()
			.getProperty("taotao.cloud.jpa.multi-tenancy.enabled");
		boolean result = Boolean.getBoolean(property);
		log.debug("[Herodotus] |- Condition [Multi Tenancy Enabled] value is [{}]", result);
		return result;
	}
}
