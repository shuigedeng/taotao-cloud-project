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

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.CaffeineSpec;
import com.taotao.cloud.common.constant.StarterNameConstant;
import com.taotao.cloud.common.utils.JsonUtil;
import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.core.lock.DistributedLock;
import com.taotao.cloud.redis.lock.RedissonDistributedLock;
import com.taotao.cloud.redis.properties.RedisLockProperties;
import com.taotao.cloud.redis.ratelimiter.RedisRateLimiterAspect;
import com.taotao.cloud.redis.ratelimiter.RedisRateLimiterClient;
import com.taotao.cloud.redis.repository.RedisRepository;
import java.lang.reflect.Field;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import org.redisson.api.RedissonClient;
import org.redisson.spring.data.connection.RedissonConnectionFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.cache.CacheManagerCustomizer;
import org.springframework.boot.autoconfigure.cache.CacheManagerCustomizers;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.convert.DurationStyle;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.connection.RedisConnectionFactory;
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
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

/**
 * TaoTaoCloudRedisAutoConfiguration
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-07 21:17:02
 */
@Configuration
public class RedisAutoConfiguration implements InitializingBean {

	private final RedissonClient redissonClient;

	public RedisAutoConfiguration(RedissonClient redissonClient) {
		this.redissonClient = redissonClient;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		LogUtil.started(RedisAutoConfiguration.class, StarterNameConstant.REDIS_STARTER);
	}

	@Bean
	public RedisConnectionFactory redissonConnectionFactory(RedissonClient redisson) {
		LogUtil.started(RedisConnectionFactory.class, StarterNameConstant.REDIS_STARTER);
		return new RedissonConnectionFactory(redisson);
	}

	@Bean
	public RedisSerializer<String> redisKeySerializer() {
		LogUtil.started(RedisSerializer.class, StarterNameConstant.REDIS_STARTER);
		return RedisSerializer.string();
	}

	@Bean
	public RedisSerializer<Object> redisValueSerializer() {
		LogUtil.started(RedisSerializer.class, StarterNameConstant.REDIS_STARTER);
		return RedisSerializer.json();
	}

	@Bean
	@ConditionalOnClass(RedisOperations.class)
	public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
		LogUtil.started(RedisTemplate.class, StarterNameConstant.REDIS_STARTER);

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
		LogUtil.started(RedisRepository.class, StarterNameConstant.REDIS_STARTER);

		return new RedisRepository(redisTemplate, false);
	}

	@Bean("stringRedisTemplate")
	public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory factory) {
		LogUtil.started(StringRedisTemplate.class, StarterNameConstant.REDIS_STARTER);

		StringRedisTemplate template = new StringRedisTemplate();
		template.setConnectionFactory(factory);
		return template;
	}

	@Bean
	@ConditionalOnBean(RedissonClient.class)
	@ConditionalOnProperty(prefix = RedisLockProperties.PREFIX, name = "enabled", havingValue = "true")
	public DistributedLock redissonDistributedLock() {
		LogUtil.started(DistributedLock.class, StarterNameConstant.REDIS_STARTER);

		return new RedissonDistributedLock(redissonClient);
	}

	@ConditionalOnProperty(value = "taotao.cloud.redis.key-expired-event.enable")
	public class RedisKeyExpiredEventConfiguration {

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
	}

	@Configuration(proxyBeanMethods = false)
	@ConditionalOnProperty(value = "taotao.cloud.redis.rate-limiter.enable")
	public class RateLimiterAutoConfiguration {

		@SuppressWarnings("unchecked")
		private RedisScript<List<Long>> redisRateLimiterScript() {
			DefaultRedisScript redisScript = new DefaultRedisScript<>();
			redisScript.setScriptSource(new ResourceScriptSource(
				new ClassPathResource("META-INF/scripts/mica_rate_limiter.lua")));
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

	@Configuration(proxyBeanMethods = false)
	@EnableConfigurationProperties(CacheProperties.class)
	@ConditionalOnMissingBean(CacheManager.class)
	@ConditionalOnClass({Caffeine.class, CaffeineCacheManager.class})
	@AutoConfigureBefore(name = "org.springframework.boot.autoconfigure.cache.CaffeineCacheConfiguration")
	public class CaffeineAutoCacheConfiguration {

		@Bean
		@ConditionalOnMissingBean
		public CacheManagerCustomizers cacheManagerCustomizers(
			ObjectProvider<CacheManagerCustomizer<?>> customizers) {
			return new CacheManagerCustomizers(customizers.orderedStream().collect(Collectors.toList()));
		}

		@Bean
		public CacheManager cacheManager(CacheProperties cacheProperties,
			CacheManagerCustomizers customizers,
			ObjectProvider<Caffeine<Object, Object>> caffeine,
			ObjectProvider<CaffeineSpec> caffeineSpec,
			ObjectProvider<CacheLoader<Object, Object>> cacheLoader) {
			CaffeineAutoCacheManager cacheManager = createCacheManager(cacheProperties, caffeine, caffeineSpec, cacheLoader);
			List<String> cacheNames = cacheProperties.getCacheNames();
			if (!CollectionUtils.isEmpty(cacheNames)) {
				cacheManager.setCacheNames(cacheNames);
			}
			return customizers.customize(cacheManager);
		}

		private static CaffeineAutoCacheManager createCacheManager(CacheProperties cacheProperties,
			ObjectProvider<Caffeine<Object, Object>> caffeine, ObjectProvider<CaffeineSpec> caffeineSpec,
			ObjectProvider<CacheLoader<Object, Object>> cacheLoader) {
			CaffeineAutoCacheManager cacheManager = new CaffeineAutoCacheManager();
			setCacheBuilder(cacheProperties, caffeineSpec.getIfAvailable(), caffeine.getIfAvailable(), cacheManager);
			cacheLoader.ifAvailable(cacheManager::setCacheLoader);
			return cacheManager;
		}

		private static void setCacheBuilder(CacheProperties cacheProperties,
			@Nullable CaffeineSpec caffeineSpec,
			@Nullable Caffeine<Object, Object> caffeine,
			CaffeineCacheManager cacheManager) {
			String specification = cacheProperties.getCaffeine().getSpec();
			if (StringUtils.hasText(specification)) {
				cacheManager.setCacheSpecification(specification);
			} else if (caffeineSpec != null) {
				cacheManager.setCaffeineSpec(caffeineSpec);
			} else if (caffeine != null) {
				cacheManager.setCaffeine(caffeine);
			}
		}

	}

	/**
	 * caffeine 缓存自动配置超时时间
	 *
	 * @author L.cm
	 */
	public static class CaffeineAutoCacheManager extends CaffeineCacheManager {
		private static final Field CACHE_LOADER_FIELD;

		static {
			CACHE_LOADER_FIELD = Objects.requireNonNull(
				ReflectionUtils.findField(CaffeineCacheManager.class, "cacheLoader"));
			CACHE_LOADER_FIELD.setAccessible(true);
		}

		@Nullable
		private CaffeineSpec caffeineSpec = null;

		public CaffeineAutoCacheManager() {
			super();
		}

		public CaffeineAutoCacheManager(String... cacheNames) {
			super(cacheNames);
		}

		@Nullable
		@SuppressWarnings("unchecked")
		protected CacheLoader<Object, Object> getCacheLoader() {
			return (CacheLoader<Object, Object>) ReflectionUtils.getField(CACHE_LOADER_FIELD, this);
		}

		@Override
		public void setCaffeine(Caffeine<Object, Object> caffeine) {
			throw new IllegalArgumentException("mica-caffeine not support customization Caffeine bean，you can customize CaffeineSpec bean.");
		}

		@Override
		public void setCaffeineSpec(CaffeineSpec caffeineSpec) {
			super.setCaffeineSpec(caffeineSpec);
			this.caffeineSpec = caffeineSpec;
		}

		@Override
		public void setCacheSpecification(String cacheSpecification) {
			super.setCacheSpecification(cacheSpecification);
			this.caffeineSpec = CaffeineSpec.parse(cacheSpecification);
		}

		/**
		 * Build a common Caffeine Cache instance for the specified cache name,
		 * using the common Caffeine configuration specified on this cache manager.
		 *
		 * @param name the name of the cache
		 * @return the native Caffeine Cache instance
		 * @see #createCaffeineCache
		 */
		@Override
		protected com.github.benmanes.caffeine.cache.Cache<Object, Object> createNativeCaffeineCache(String name) {
			String[] cacheArray = name.split(StringPool.HASH);
			if (cacheArray.length < 2) {
				return super.createNativeCaffeineCache(name);
			}
			// 转换时间，支持时间单位例如：300ms，第二个参数是默认单位
			Duration duration = DurationStyle.detectAndParse(cacheArray[1], ChronoUnit.SECONDS);
			Caffeine<Object, Object> cacheBuilder;
			if (this.caffeineSpec != null) {
				cacheBuilder = Caffeine.from(caffeineSpec);
			} else {
				cacheBuilder = Caffeine.newBuilder();
			}
			CacheLoader<Object, Object> cacheLoader = getCacheLoader();
			if (cacheLoader == null) {
				return cacheBuilder.expireAfterAccess(duration).build();
			} else {
				return cacheBuilder.expireAfterAccess(duration).build(cacheLoader);
			}
		}

	}
}
