package com.taotao.cloud.rule.configuration;

import com.taotao.cloud.rule.service.IRuleCacheService;
import com.taotao.cloud.rule.service.impl.RuleCacheServiceImpl;
import org.springframework.context.annotation.Bean;

/**
 * 规则配置
 */
public class RuleConfiguration {

	@Bean
	public IRuleCacheService ruleCacheService() {
		return new RuleCacheServiceImpl();
	}
}
