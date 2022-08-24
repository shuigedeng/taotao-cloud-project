package com.taotao.cloud.limit.ratelimiter;

import org.redisson.api.RedissonClient;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AutoConfigureAfter(RedisAutoConfiguration.class)
public class RateLimiterAutoConfiguration {


	@Bean
	public RateLimiterService rateLimiterInfoProvider() {
		return new RateLimiterService();
	}

	@Bean
	public BizKeyProvider bizKeyProvider() {
		return new BizKeyProvider();
	}

	@Bean
	@ConditionalOnBean(RedissonClient.class)
	public RateLimitAspectHandler rateLimitAspectHandler(RedissonClient client,
		RateLimiterService lockInfoProvider) {
		return new RateLimitAspectHandler(client, lockInfoProvider);
	}

}
