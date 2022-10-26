package com.taotao.cloud.limit.ratelimiter;

import com.taotao.cloud.limit.ext.LimitProperties;
import org.redisson.api.RedissonClient;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * 速度限制器自动配置
 *
 * @author shuigedeng
 * @version 2022.09
 * @since 2022-10-26 08:56:50
 */
@EnableConfigurationProperties({LimitProperties.class})
@ConditionalOnProperty(prefix = LimitProperties.PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
@AutoConfiguration(after = RedisAutoConfiguration.class)
public class RateLimiterAutoConfiguration {

	@Bean
	public BizKeyProvider bizKeyProvider() {
		return new BizKeyProvider();
	}

	@Bean
	public RateLimiterService rateLimiterService(BizKeyProvider bizKeyProvider) {
		return new RateLimiterService(bizKeyProvider);
	}

	@Bean
	public RateLimitAspectHandler rateLimitAspectHandler(RedissonClient client,
														 RateLimiterService rateLimiterService) {
		return new RateLimitAspectHandler(client, rateLimiterService);
	}

}
