package com.taotao.cloud.gateway.rule;

import com.taotao.cloud.gateway.service.IRuleCacheService;
import com.taotao.cloud.gateway.service.impl.RuleCacheServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * 规则配置
 */
@Component
public class RuleConfiguration {

	@Bean
	public IRuleCacheService ruleCacheService() {
		return new RuleCacheServiceImpl();
	}
}
