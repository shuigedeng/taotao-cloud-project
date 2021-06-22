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
import com.taotao.cloud.redis.properties.CacheManagerProperties;
import com.taotao.cloud.redis.properties.CustomCacheProperties;
import com.taotao.cloud.redis.redis.RedisOps;
import com.taotao.cloud.redis.repository.CacheOps;
import com.taotao.cloud.redis.repository.CachePlusOps;
import com.taotao.cloud.redis.repository.RedisRepository;
import com.taotao.cloud.redis.repository.impl.RedisOpsImpl;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
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
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * redis 配置类
 *
 * @author dengtao
 * @version 1.0.0
 * @since 2020/4/30 10:13
 */
@Configuration
@EnableCaching
@AllArgsConstructor
public class TaoTaoCloudRedisConfiguration implements InitializingBean {

	private final CustomCacheProperties cacheProperties;

	@Override
	public void afterPropertiesSet() throws Exception {
		LogUtil.info("[TAOTAO CLOUD][" + StarterNameConstant.TAOTAO_CLOUD_REDIS_STARTER + "]"
			+ "redis模块已启动");
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
		return new RedisRepository(redisTemplate);
	}

	@Bean
	@ConditionalOnMissingBean
	public RedisOps getRedisOps(@Qualifier("redisTemplate") RedisTemplate<String, Object> redisTemplate, StringRedisTemplate stringRedisTemplate) {
		return new RedisOps(redisTemplate, stringRedisTemplate, cacheProperties.getCacheNullVal());
	}

	/**
	 * redis 持久库
	 *
	 * @param redisOps the redis template
	 * @return the redis repository
	 */
	@Bean
	@ConditionalOnMissingBean
	public CacheOps cacheOps(RedisOps redisOps) {
		return new RedisOpsImpl(redisOps);
	}

	/**
	 * redis 增强持久库
	 *
	 * @param redisOps the redis template
	 * @return the redis repository
	 */
	@Bean
	@ConditionalOnMissingBean
	public CachePlusOps cachePlusOps(RedisOps redisOps) {
		return new RedisOpsImpl(redisOps);
	}


	@Primary
	@Bean(name = "cacheManager")
	public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory,
		CacheManagerProperties cacheManagerProperties) {
		RedisCacheConfiguration difConf = getDefConf().entryTtl(Duration.ofHours(1));

		//自定义的缓存过期时间配置
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
			.serializeKeysWith(RedisSerializationContext.SerializationPair
				.fromSerializer(RedisSerializer.string()))
			.serializeValuesWith(RedisSerializationContext.SerializationPair
				.fromSerializer(RedisSerializer.json()));
	}

}
