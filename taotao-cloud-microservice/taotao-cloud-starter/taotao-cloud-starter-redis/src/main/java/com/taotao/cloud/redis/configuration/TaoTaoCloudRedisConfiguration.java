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

import com.taotao.cloud.common.constant.StarterNameConstant;
import com.taotao.cloud.common.utils.JsonUtil;
import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.redis.lock.RedissonDistributedLock;
import com.taotao.cloud.redis.properties.CacheManagerProperties;
import com.taotao.cloud.redis.properties.RedisLockProperties;
import com.taotao.cloud.redis.repository.RedisRepository;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import org.redisson.api.RedissonClient;
import org.redisson.spring.data.connection.RedissonConnectionFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * redis 配置类
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2020/4/30 10:13
 */
@EnableCaching
@Configuration
public class TaoTaoCloudRedisConfiguration implements InitializingBean {

	private final RedissonClient redissonClient;

	public TaoTaoCloudRedisConfiguration(RedissonClient redissonClient) {
		this.redissonClient = redissonClient;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		LogUtil.info("[TAOTAO CLOUD][" + StarterNameConstant.TAOTAO_CLOUD_REDIS_STARTER + "]"
			+ "redis模块已启动");
	}

	@Bean
	public RedissonConnectionFactory redissonConnectionFactory(RedissonClient redisson) {
		return new RedissonConnectionFactory(redisson);
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

	@Bean
	@ConditionalOnBean(RedissonClient.class)
	@ConditionalOnProperty(prefix = RedisLockProperties.PREFIX, name = "enabled", havingValue = "true")
	public RedissonDistributedLock redissonDistributedLock() {
		return new RedissonDistributedLock(redissonClient);
	}

	@Bean
	public KeyGenerator keyGenerator() {
		return (target, method, objects) -> {
			StringBuilder sb = new StringBuilder();
			sb.append(target.getClass().getName());
			sb.append(":").append(method.getName()).append(":");
			for (Object obj : objects) {
				sb.append(obj.toString());
			}
			return sb.toString();
		};
	}

	@Primary
	@Bean(name = "cacheManager")
	public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory,
		CacheManagerProperties cacheManagerProperties) {
		RedisCacheConfiguration difConf = getDefConf().entryTtl(Duration.ofHours(1));

		//自定义的缓存过期时间配置¬
		int configSize = cacheManagerProperties.getConfigs() == null ? 0
			: cacheManagerProperties.getConfigs().size();

		Map<String, RedisCacheConfiguration> redisCacheConfigurationMap = new HashMap<>(configSize);
		if (configSize > 0) {
			cacheManagerProperties.getConfigs().forEach(e -> {
				RedisCacheConfiguration conf = getDefConf()
					.entryTtl(Duration.ofSeconds(e.getSecond()));
				redisCacheConfigurationMap.put(e.getKey(), conf);
			});
		}

		return RedisCacheManager
			.builder(redisConnectionFactory)
			.cacheDefaults(difConf)
			.withInitialCacheConfigurations(redisCacheConfigurationMap)
			.build();
	}

	private RedisCacheConfiguration getDefConf() {
		return RedisCacheConfiguration
			.defaultCacheConfig()
			.disableCachingNullValues()
			.computePrefixWith(cacheName -> "cache".concat(":").concat(cacheName).concat(":"))
			.serializeKeysWith(RedisSerializationContext.SerializationPair
				.fromSerializer(RedisSerializer.string()))
			.serializeValuesWith(RedisSerializationContext.SerializationPair
				.fromSerializer(RedisSerializer.json()));
	}

}
