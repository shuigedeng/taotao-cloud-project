/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
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

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.CaffeineSpec;
import com.google.common.collect.Maps;
import com.taotao.cloud.common.constant.StarterName;
import com.taotao.cloud.common.constant.StrPool;
import com.taotao.cloud.common.utils.log.LogUtil;
import com.taotao.cloud.redis.properties.CacheProperties;
import com.taotao.cloud.redis.serializer.RedisObjectSerializer;
import java.lang.reflect.Field;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.cache.CacheManagerCustomizer;
import org.springframework.boot.autoconfigure.cache.CacheManagerCustomizers;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.convert.DurationStyle;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.lang.Nullable;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

/**
 * TaoTaoCloudCacheAutoConfiguration
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-07 21:17:09
 */
@AutoConfiguration(after = RedisAutoConfiguration.class)
@EnableCaching
@EnableConfigurationProperties({CacheProperties.class})
@ConditionalOnProperty(prefix = CacheProperties.PREFIX, name = "enabled", havingValue = "true")
public class CacheManagerAutoConfiguration implements InitializingBean {

	@Override
	public void afterPropertiesSet() throws Exception {
		LogUtil.started(CacheManagerAutoConfiguration.class, StarterName.REDIS_STARTER);
	}

	private final CacheProperties cacheProperties;

	public CacheManagerAutoConfiguration(CacheProperties cacheProperties) {
		this.cacheProperties = cacheProperties;
	}

	@Bean
	public KeyGenerator keyGenerator() {
		return (target, method, objects) -> {
			StringBuilder sb = new StringBuilder();
			sb.append(target.getClass().getName());
			sb.append(StrPool.COLON);
			sb.append(method.getName());
			for (Object obj : objects) {
				if (obj != null) {
					sb.append(StrPool.COLON);
					sb.append(obj);
				}
			}
			return sb.toString();
		};
	}

	@Primary
	@Bean(name = "redisCacheManager")
	@ConditionalOnProperty(prefix = CacheProperties.PREFIX, name = "type", havingValue = "REDIS")
	public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
		RedisCacheConfiguration defConfig = getDefConf();
		defConfig.entryTtl(cacheProperties.getDef().getTimeToLive());

		Map<String, CacheProperties.Cache> configs = cacheProperties.getConfigs();
		Map<String, RedisCacheConfiguration> map = Maps.newHashMap();

		//自定义的缓存过期时间配置
		Optional
			.ofNullable(configs)
			.ifPresent(config ->
				config.forEach((key, cache) -> {
					RedisCacheConfiguration cfg = handleRedisCacheConfiguration(cache, defConfig);
					map.put(key, cfg);
				})
			);

		return RedisCacheManager
			.builder(redisConnectionFactory)
			.cacheDefaults(defConfig)
			.withInitialCacheConfigurations(map)
			.build();
	}

	//@Bean("caffeineCacheManager")
	//@ConditionalOnProperty(prefix = CacheProperties.PREFIX, name = "type", havingValue = "CAFFEINE")
	//public CacheManager caffeineCacheManager() {
	//	LogUtil.started(CaffeineCacheManager.class, StarterNameConstant.REDIS_STARTER);
	//
	//	CaffeineCacheManager cacheManager = new CaffeineCacheManager();
	//
	//	Caffeine caffeine = Caffeine
	//		.newBuilder()
	//		.recordStats()
	//		.initialCapacity(500)
	//		.expireAfterWrite(cacheProperties.getDef().getTimeToLive())
	//		.maximumSize(cacheProperties.getDef().getMaxSize());
	//
	//	cacheManager.setAllowNullValues(cacheProperties.getDef().isCacheNullValues());
	//	cacheManager.setCaffeine(caffeine);
	//
	//	//配置了这里，就必须事先在配置文件中指定key 缓存才生效
	//    Map<String, CacheProperties.Cache> configs = cacheProperties.getConfigs();
	//    Optional.ofNullable(configs).ifPresent((config)->{
	//        cacheManager.setCacheNames(config.keySet());
	//    });
	//	return cacheManager;
	//}

	/**
	 * Caffeine auto cache configuration.
	 *
	 * @author shuigedeng
	 * @version 2022.04
	 * @since 2022-05-20 17:32:35
	 */
	@AutoConfiguration(beforeName = "org.springframework.boot.autoconfigure.cache.CaffeineCacheConfiguration")
	@EnableConfigurationProperties({
		org.springframework.boot.autoconfigure.cache.CacheProperties.class})
	@ConditionalOnMissingBean(CacheManager.class)
	public static class CaffeineAutoCacheConfiguration {

		@Bean
		@ConditionalOnMissingBean
		public CacheManagerCustomizers cacheManagerCustomizers(
			ObjectProvider<CacheManagerCustomizer<?>> customizers) {
			return new CacheManagerCustomizers(
				customizers.orderedStream().collect(Collectors.toList()));
		}

		@Bean("caffeineCacheManager")
		@ConditionalOnProperty(prefix = CacheProperties.PREFIX, name = "type", havingValue = "CAFFEINE")
		public CacheManager cacheManager(
			org.springframework.boot.autoconfigure.cache.CacheProperties cacheProperties,
			CacheManagerCustomizers customizers,
			ObjectProvider<Caffeine<Object, Object>> caffeine,
			ObjectProvider<CaffeineSpec> caffeineSpec,
			ObjectProvider<CacheLoader<Object, Object>> cacheLoader) {
			CaffeineAutoCacheManager cacheManager = createCacheManager(cacheProperties, caffeine,
				caffeineSpec, cacheLoader);
			List<String> cacheNames = cacheProperties.getCacheNames();
			if (!CollectionUtils.isEmpty(cacheNames)) {
				cacheManager.setCacheNames(cacheNames);
			}
			return customizers.customize(cacheManager);
		}

		private static CaffeineAutoCacheManager createCacheManager(
			org.springframework.boot.autoconfigure.cache.CacheProperties cacheProperties,
			ObjectProvider<Caffeine<Object, Object>> caffeine,
			ObjectProvider<CaffeineSpec> caffeineSpec,
			ObjectProvider<CacheLoader<Object, Object>> cacheLoader) {
			CaffeineAutoCacheManager cacheManager = new CaffeineAutoCacheManager();
			setCacheBuilder(cacheProperties, caffeineSpec.getIfAvailable(),
				caffeine.getIfAvailable(), cacheManager);
			cacheLoader.ifAvailable(cacheManager::setCacheLoader);
			return cacheManager;
		}

		private static void setCacheBuilder(
			org.springframework.boot.autoconfigure.cache.CacheProperties cacheProperties,
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

//	@AutoConfiguration
//	@EnableMethodCache(basePackages = "com.taotao.cloud")
//	@EnableCreateCacheAnnotation
//	@PropertySource(factory = YamlPropertySourceFactory.class, value = "classpath:jetcache.yml")
//	public class JetCacheAutoConfiguration {
//
//		@Bean
//		@ConditionalOnProperty(prefix = CustomCacheProperties.PREFIX, name = "type", havingValue = "JETCACHE")
//		public SpringConfigProvider springConfigProvider() {
//			return new SpringConfigProvider();
//		}
//
//		@Bean
//		@ConditionalOnProperty(prefix = CustomCacheProperties.PREFIX, name = "type", havingValue = "JETCACHE")
//		public GlobalCacheConfig config() {
//			Map localBuilders = new HashMap();
//			EmbeddedCacheBuilder localBuilder = LinkedHashMapCacheBuilder
//				.createLinkedHashMapCacheBuilder()
//				.keyConvertor(FastjsonKeyConvertor.INSTANCE);
//			localBuilders.put(CacheConsts.DEFAULT_AREA, localBuilder);
//
//			Map remoteBuilders = new HashMap(6);
//			RedisSpringDataCacheBuilder<?> redisSpringDataCacheBuilder = RedisSpringDataCacheBuilder.createBuilder()
//				.keyConvertor(FastjsonKeyConvertor.INSTANCE)
//				.valueEncoder(JavaValueEncoder.INSTANCE)
//				.valueDecoder(JavaValueDecoder.INSTANCE);
//			remoteBuilders.put(CacheConsts.DEFAULT_AREA, redisSpringDataCacheBuilder);
//
//			GlobalCacheConfig globalCacheConfig = new GlobalCacheConfig();
//			// globalCacheConfig.setConfigProvider(configProvider);//for jetcache <=2.5
//			globalCacheConfig.setLocalCacheBuilders(localBuilders);
//			globalCacheConfig.setRemoteCacheBuilders(remoteBuilders);
//			globalCacheConfig.setStatIntervalMinutes(15);
//			globalCacheConfig.setAreaInCacheName(false);
//
//			return globalCacheConfig;
//		}
//	}

	/**
	 * caffeine 缓存自动配置超时时间
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
			throw new IllegalArgumentException(
				"caffeine not support customization Caffeine bean，you can customize CaffeineSpec bean.");
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
		 * Build a common Caffeine Cache instance for the specified cache name, using the common
		 * Caffeine configuration specified on this cache manager.
		 *
		 * @param name the name of the cache
		 * @return the native Caffeine Cache instance
		 * @see #createCaffeineCache
		 */
		@Override
		protected com.github.benmanes.caffeine.cache.Cache<Object, Object> createNativeCaffeineCache(
			String name) {
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

	private RedisCacheConfiguration getDefConf() {
		RedisCacheConfiguration def = RedisCacheConfiguration
			.defaultCacheConfig()
			.disableCachingNullValues()
			.serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(
				new StringRedisSerializer()))
			.serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(
				new RedisObjectSerializer()));
		return handleRedisCacheConfiguration(cacheProperties.getDef(), def);
	}

	private RedisCacheConfiguration handleRedisCacheConfiguration(
		CacheProperties.Cache redisProperties, RedisCacheConfiguration config) {
		if (Objects.isNull(redisProperties)) {
			return config;
		}
		if (redisProperties.getTimeToLive() != null) {
			config = config.entryTtl(redisProperties.getTimeToLive());
		}
		if (redisProperties.getKeyPrefix() != null) {
			config = config.computePrefixWith(cacheName -> redisProperties.getKeyPrefix().concat(
				StrPool.COLON).concat(cacheName).concat(StrPool.COLON));
		} else {
			config = config.computePrefixWith(cacheName -> cacheName.concat(StrPool.COLON));
		}
		if (!redisProperties.isCacheNullValues()) {
			config = config.disableCachingNullValues();
		}
		if (!redisProperties.isUseKeyPrefix()) {
			config = config.disableKeyPrefix();
		}

		return config;
	}
}
