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
package com.taotao.cloud.redis.component;

import com.taotao.cloud.common.constant.StarterNameConstant;
import com.taotao.cloud.redis.properties.CacheManagerProperties;
import com.taotao.cloud.redis.repository.RedisRepository;
import com.taotao.cloud.redis.serializer.RedisObjectSerializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * redis 配置类
 *
 * @author dengtao
 * @date 2020/4/30 10:13
 * @since v1.0
 */
@Slf4j
@EnableCaching
public class RedisComponent implements InitializingBean {

	@Override
	public void afterPropertiesSet() throws Exception {
		log.info("[TAOTAO CLOUD][" + StarterNameConstant.TAOTAO_CLOUD_REDIS_STARTER + "]" + "redis模块已启动");
	}

	@Bean
	public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
		RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(factory);

		RedisSerializer<String> stringSerializer = new StringRedisSerializer();
		RedisSerializer<Object> redisObjectSerializer = new RedisObjectSerializer();

		redisTemplate.setKeySerializer(stringSerializer);
		redisTemplate.setHashKeySerializer(stringSerializer);
		redisTemplate.setValueSerializer(redisObjectSerializer);
		redisTemplate.afterPropertiesSet();

		return redisTemplate;
	}

	@Bean
	public RedisRepository redisRepository(RedisTemplate<String, Object> redisTemplate) {
		return new RedisRepository(redisTemplate);
	}

	@Primary
	@Bean(name = "cacheManager")
	public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory,
									 CacheManagerProperties cacheManagerProperties) {
		RedisCacheConfiguration difConf = getDefConf().entryTtl(Duration.ofHours(1));

		//自定义的缓存过期时间配置
		int configSize = cacheManagerProperties.getConfigs() == null ? 0 : cacheManagerProperties.getConfigs().size();
		Map<String, RedisCacheConfiguration> redisCacheConfigurationMap = new HashMap<>(configSize);
		if (configSize > 0) {
			cacheManagerProperties.getConfigs().forEach(e -> {
				RedisCacheConfiguration conf = getDefConf().entryTtl(Duration.ofSeconds(e.getSecond()));
				redisCacheConfigurationMap.put(e.getKey(), conf);
			});
		}

		return RedisCacheManager.builder(redisConnectionFactory)
			.cacheDefaults(difConf)
			.withInitialCacheConfigurations(redisCacheConfigurationMap)
			.build();
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

	private RedisCacheConfiguration getDefConf() {
		return RedisCacheConfiguration.defaultCacheConfig()
			.disableCachingNullValues()
			.computePrefixWith(cacheName -> "cache".concat(":").concat(cacheName).concat(":"))
			.serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
			.serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new RedisObjectSerializer()));
	}

}
