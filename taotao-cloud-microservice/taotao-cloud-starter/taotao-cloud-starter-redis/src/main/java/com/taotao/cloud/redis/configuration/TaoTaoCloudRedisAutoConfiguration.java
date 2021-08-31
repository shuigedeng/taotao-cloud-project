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
import com.taotao.cloud.core.lock.DistributedLock;
import com.taotao.cloud.redis.lock.RedissonDistributedLock;
import com.taotao.cloud.redis.properties.RedisLockProperties;
import com.taotao.cloud.redis.repository.RedisRepository;
import org.redisson.api.RedissonClient;
import org.redisson.spring.data.connection.RedissonConnectionFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * redis 配置类
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2020/4/30 10:13
 */
@Configuration
public class TaoTaoCloudRedisAutoConfiguration implements InitializingBean {

	private final RedissonClient redissonClient;

	public TaoTaoCloudRedisAutoConfiguration(RedissonClient redissonClient) {
		this.redissonClient = redissonClient;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		LogUtil.started(TaoTaoCloudRedisAutoConfiguration.class, StarterName.REDIS_STARTER);
	}

	@Bean
	public RedisConnectionFactory redissonConnectionFactory(RedissonClient redisson) {
		LogUtil.started(RedisConnectionFactory.class, StarterName.REDIS_STARTER);
		return new RedissonConnectionFactory(redisson);
	}

	@Bean
	public RedisSerializer<String> redisKeySerializer() {
		LogUtil.started(RedisSerializer.class, StarterName.REDIS_STARTER);
		return RedisSerializer.string();
	}

	@Bean
	public RedisSerializer<Object> redisValueSerializer() {
		LogUtil.started(RedisSerializer.class, StarterName.REDIS_STARTER);
		return RedisSerializer.json();
	}

	@Bean
	@ConditionalOnClass(RedisOperations.class)
	public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
		LogUtil.started(RedisTemplate.class, StarterName.REDIS_STARTER);

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
		LogUtil.started(RedisRepository.class, StarterName.REDIS_STARTER);

		return new RedisRepository(redisTemplate, false);
	}

	@Bean("stringRedisTemplate")
	public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory factory) {
		LogUtil.started(StringRedisTemplate.class, StarterName.REDIS_STARTER);

		StringRedisTemplate template = new StringRedisTemplate();
		template.setConnectionFactory(factory);
		return template;
	}

	@Bean
	@ConditionalOnBean(RedissonClient.class)
	@ConditionalOnProperty(prefix = RedisLockProperties.PREFIX, name = "enabled", havingValue = "true")
	public DistributedLock redissonDistributedLock() {
		LogUtil.started(DistributedLock.class, StarterName.REDIS_STARTER);

		return new RedissonDistributedLock(redissonClient);
	}
}
