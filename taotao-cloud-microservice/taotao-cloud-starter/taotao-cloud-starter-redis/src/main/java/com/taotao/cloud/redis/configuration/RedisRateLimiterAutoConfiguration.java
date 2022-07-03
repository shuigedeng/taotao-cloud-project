package com.taotao.cloud.redis.configuration;

import com.taotao.cloud.common.constant.StarterName;
import com.taotao.cloud.common.utils.log.LogUtil;
import com.taotao.cloud.redis.ratelimiter.RedisRateLimiterAspect;
import com.taotao.cloud.redis.ratelimiter.RedisRateLimiterClient;
import java.util.List;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.scripting.support.ResourceScriptSource;

/**
 * 速度限制器自动配置
 *
 * @author shuigedeng
 * @version 2022.07
 * @since 2022-07-03 09:24:19
 */
@AutoConfiguration
@ConditionalOnProperty(prefix = "taotao.cloud.redis.rate-limiter.enable", value = "true", matchIfMissing = true)
public class RedisRateLimiterAutoConfiguration implements InitializingBean {

	@Override
	public void afterPropertiesSet() throws Exception {
		LogUtil.started(RedisRateLimiterAutoConfiguration.class, StarterName.REDIS_STARTER);
	}

	@SuppressWarnings("unchecked")
	private RedisScript<List<Long>> redisRateLimiterScript() {
		DefaultRedisScript redisScript = new DefaultRedisScript<>();
		redisScript.setScriptSource(new ResourceScriptSource(
			new ClassPathResource("META-INF/scripts/taotao_rate_limiter.lua")));
		redisScript.setResultType(List.class);
		return redisScript;
	}

	@Bean
	@ConditionalOnMissingBean
	public RedisRateLimiterClient redisRateLimiter(StringRedisTemplate redisTemplate,
		Environment environment) {
		RedisScript<List<Long>> redisRateLimiterScript = redisRateLimiterScript();
		return new RedisRateLimiterClient(redisTemplate, redisRateLimiterScript, environment);
	}

	@Bean
	@ConditionalOnMissingBean
	public RedisRateLimiterAspect redisRateLimiterAspect(RedisRateLimiterClient rateLimiterClient) {
		return new RedisRateLimiterAspect(rateLimiterClient);
	}
}
