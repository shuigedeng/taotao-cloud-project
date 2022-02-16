/*
 * Copyright 2002-2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.taotao.cloud.redis.configuration;

import com.taotao.cloud.common.constant.StarterName;
import com.taotao.cloud.common.utils.JsonUtil;
import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.redis.ratelimiter.RedisRateLimiterAspect;
import com.taotao.cloud.redis.ratelimiter.RedisRateLimiterClient;
import com.taotao.cloud.redis.repository.RedisRepository;
import java.util.List;
import org.redisson.api.RedissonClient;
import org.redisson.spring.data.connection.RedissonConnectionFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisKeyExpiredEvent;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scripting.support.ResourceScriptSource;

/**
 * TaoTaoCloudRedisAutoConfiguration
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-07 21:17:02
 */
@Configuration
@ConditionalOnBean(RedissonClient.class)
@EnableConfigurationProperties({RedisProperties.class})
public class RedisAutoConfiguration implements InitializingBean {

	@Override
	public void afterPropertiesSet() throws Exception {
		LogUtil.started(RedisAutoConfiguration.class, StarterName.REDIS_STARTER);
	}

	@Bean
	public RedisConnectionFactory redissonConnectionFactory(RedissonClient redissonClient) {
		return new RedissonConnectionFactory(redissonClient);
	}

	@Bean
	public RedisSerializer<String> redisKeySerializer() {
		return RedisSerializer.string();
	}

	@Bean
	public RedisSerializer<Object> redisValueSerializer() {
		return RedisSerializer.json();
	}

	@Bean
	@ConditionalOnClass(RedisOperations.class)
	public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
		RedisTemplate<String, Object> template = new RedisTemplate<>();
		template.setConnectionFactory(factory);

		Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(
			Object.class);
		jackson2JsonRedisSerializer.setObjectMapper(JsonUtil.MAPPER);

		StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
		// key采用String的序列化方式
		template.setKeySerializer(stringRedisSerializer);
		// hash的key也采用String的序列化方式
		template.setHashKeySerializer(stringRedisSerializer);

		// value序列化方式采用jackson
		template.setValueSerializer(jackson2JsonRedisSerializer);
		// hash的value序列化方式采用jackson
		template.setHashValueSerializer(jackson2JsonRedisSerializer);
		template.afterPropertiesSet();
		return template;
	}

	@Bean
	public RedisRepository redisRepository(RedisTemplate<String, Object> redisTemplate) {
		return new RedisRepository(redisTemplate, false);
	}

	@Bean("stringRedisTemplate")
	public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory factory) {
		StringRedisTemplate template = new StringRedisTemplate();
		template.setConnectionFactory(factory);
		return template;
	}

	@Configuration
	@ConditionalOnProperty(value = "taotao.cloud.redis.key-expired-event.enable")
	public static class RedisKeyExpiredEventConfiguration {

		@Bean
		@ConditionalOnMissingBean
		public RedisMessageListenerContainer redisMessageListenerContainer(
			RedisConnectionFactory connectionFactory) {
			RedisMessageListenerContainer container = new RedisMessageListenerContainer();
			container.setConnectionFactory(connectionFactory);
			return container;
		}

		@Bean
		@ConditionalOnMissingBean
		public KeyExpirationEventMessageListener keyExpirationEventMessageListener(
			RedisMessageListenerContainer listenerContainer) {
			return new KeyExpirationEventMessageListener(listenerContainer);
		}

		@Async
		@EventListener
		public void onRedisKeyExpiredEvent(RedisKeyExpiredEvent<Object> event) {
			LogUtil.info(event.toString());
		}

	}

	@Configuration(proxyBeanMethods = false)
	@ConditionalOnProperty(value = "taotao.cloud.redis.rate-limiter.enable")
	public static class RateLimiterAutoConfiguration {

		private RedisScript<List<Long>> redisRateLimiterScript() {
			DefaultRedisScript redisScript = new DefaultRedisScript<>();
			redisScript.setScriptSource(new ResourceScriptSource(
				new ClassPathResource("META-INF/scripts/rate_limiter.lua")));
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
		public RedisRateLimiterAspect redisRateLimiterAspect(
			RedisRateLimiterClient rateLimiterClient) {
			return new RedisRateLimiterAspect(rateLimiterClient);
		}
	}
}
