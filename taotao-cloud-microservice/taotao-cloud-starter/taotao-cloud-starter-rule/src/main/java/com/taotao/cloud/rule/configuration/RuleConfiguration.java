package com.taotao.cloud.rule.configuration;

import com.taotao.cloud.rule.service.IRuleCacheService;
import com.taotao.cloud.rule.service.impl.RuleCacheServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 规则配置
 */
@Configuration
public class RuleConfiguration {

	@Bean
	public IRuleCacheService ruleCacheService() {
		return new RuleCacheServiceImpl();
	}
}
