package com.taotao.cloud.web.configuration;

import com.taotao.cloud.web.limit.LimitAspect;
import com.taotao.cloud.web.properties.LimitProperties;
import com.taotao.cloud.web.properties.XssProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * 限流自动注入配置
 */
public class LimitConfiguration {

	@Bean
	@ConditionalOnProperty(prefix = LimitProperties.PREFIX, name = "enabled", havingValue = "true")
	public LimitAspect limitAspect(RedisTemplate<String, String> limitRedisTemplate) {
		return new LimitAspect(limitRedisTemplate);
	}
}
