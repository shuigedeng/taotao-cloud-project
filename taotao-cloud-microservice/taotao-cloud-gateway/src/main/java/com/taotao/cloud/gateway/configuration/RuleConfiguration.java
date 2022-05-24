package com.taotao.cloud.gateway.configuration;

import com.taotao.cloud.gateway.service.IRuleCacheService;
import com.taotao.cloud.gateway.service.impl.RuleCacheServiceImpl;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

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
