package com.taotao.cloud.monitor.kuding.config.conditions;

import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

public abstract class PropertiesEnabledCondition extends SpringBootCondition {

	protected final String propName;

	protected final boolean matchIfMissing;

	public PropertiesEnabledCondition(String propName, boolean matchIfMissing) {
		this.propName = propName;
		this.matchIfMissing = matchIfMissing;
	}

	@Override
	public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata) {
		Boolean enabled = context.getEnvironment().getProperty(propName, Boolean.class);
		if ((enabled == null && matchIfMissing) || (enabled != null && enabled))
			return ConditionOutcome.match("开启通知");
		return ConditionOutcome.noMatch("不开启通知");
	}

}
