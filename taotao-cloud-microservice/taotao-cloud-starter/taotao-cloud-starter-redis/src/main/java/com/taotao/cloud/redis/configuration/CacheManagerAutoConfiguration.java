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
import com.taotao.cloud.common.constant.StarterName;
import com.taotao.cloud.common.constant.StrPool;
import com.taotao.cloud.common.utils.lang.StringUtil;
import com.taotao.cloud.common.utils.log.LogUtil;
import com.taotao.cloud.redis.properties.CacheProperties;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.cache.CacheManagerCustomizers;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.convert.DurationStyle;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.lang.Nullable;

/**
 * TaoTaoCloudCacheAutoConfiguration
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-07 21:17:09
 */
@EnableCaching
@AutoConfiguration(after = RedisAutoConfiguration.class)
@ConditionalOnProperty(prefix = CacheProperties.PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
public class CacheManagerAutoConfiguration implements InitializingBean {

	@Override
	public void afterPropertiesSet() throws Exception {
		LogUtil.started(CacheManagerAutoConfiguration.class, StarterName.REDIS_STARTER);
	}

	private final org.springframework.boot.autoconfigure.cache.CacheProperties cacheProperties;
	/**
	 * 序列化方式
	 */
	private final RedisSerializer<Object> redisSerializer;
	private final CacheManagerCustomizers customizerInvoker;
	@Nullable
	private final RedisCacheConfiguration redisCacheConfiguration;

	CacheManagerAutoConfiguration(RedisSerializer<Object> redisSerializer,
		org.springframework.boot.autoconfigure.cache.CacheProperties cacheProperties,
		CacheManagerCustomizers customizerInvoker,
		ObjectProvider<RedisCacheConfiguration> redisCacheConfiguration) {
		this.redisSerializer = redisSerializer;
		this.cacheProperties = cacheProperties;
		this.customizerInvoker = customizerInvoker;
		this.redisCacheConfiguration = redisCacheConfiguration.getIfAvailable();
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
	@ConditionalOnProperty(prefix = CacheProperties.PREFIX, name = "type", havingValue = "redis", matchIfMissing = true)
	public CacheManager cacheManager(
		ObjectProvider<RedisConnectionFactory> connectionFactoryObjectProvider) {
		RedisConnectionFactory connectionFactory = connectionFactoryObjectProvider.getIfAvailable();
		Objects.requireNonNull(connectionFactory, "Bean RedisConnectionFactory is null.");
		RedisCacheWriter redisCacheWriter = RedisCacheWriter.nonLockingRedisCacheWriter(
			connectionFactory);
		RedisCacheConfiguration cacheConfiguration = this.determineConfiguration();
		List<String> cacheNames = this.cacheProperties.getCacheNames();
		Map<String, RedisCacheConfiguration> initialCaches = new LinkedHashMap<>();
		if (!cacheNames.isEmpty()) {
			Map<String, RedisCacheConfiguration> cacheConfigMap = new LinkedHashMap<>(
				cacheNames.size());
			cacheNames.forEach(it -> cacheConfigMap.put(it, cacheConfiguration));
			initialCaches.putAll(cacheConfigMap);
		}
		boolean allowInFlightCacheCreation = true;
		boolean enableTransactions = false;
		RedisAutoCacheManager cacheManager = new RedisAutoCacheManager(redisCacheWriter,
			cacheConfiguration, initialCaches, allowInFlightCacheCreation);
		cacheManager.setTransactionAware(enableTransactions);
		return this.customizerInvoker.customize(cacheManager);

		//RedisCacheConfiguration defConfig = getDefConf();
		//defConfig.entryTtl(cacheProperties.getDef().getTimeToLive());
		//
		//Map<String, CacheProperties.Cache> configs = cacheProperties.getConfigs();
		//Map<String, RedisCacheConfiguration> map = Maps.newHashMap();
		//
		////自定义的缓存过期时间配置
		//Optional
		//	.ofNullable(configs)
		//	.ifPresent(config ->
		//		config.forEach((key, cache) -> {
		//			RedisCacheConfiguration cfg = handleRedisCacheConfiguration(cache, defConfig);
		//			map.put(key, cfg);
		//		})
		//	);
		//
		//return RedisCacheManager
		//	.builder(redisConnectionFactory)
		//	.cacheDefaults(defConfig)
		//	.withInitialCacheConfigurations(map)
		//	.build();
	}

	private RedisCacheConfiguration determineConfiguration() {
		if (this.redisCacheConfiguration != null) {
			return this.redisCacheConfiguration;
		} else {
			org.springframework.boot.autoconfigure.cache.CacheProperties.Redis redisProperties = this.cacheProperties.getRedis();
			RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig();
			config = config.serializeValuesWith(
				RedisSerializationContext.SerializationPair.fromSerializer(redisSerializer));
			if (redisProperties.getTimeToLive() != null) {
				config = config.entryTtl(redisProperties.getTimeToLive());
			}

			if (redisProperties.getKeyPrefix() != null) {
				config = config.prefixCacheNameWith(redisProperties.getKeyPrefix());
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

	/**
	 * redis cache 扩展cache name自动化配置
	 *
	 * @author L.cm
	 */
	public static class RedisAutoCacheManager extends RedisCacheManager {

		public RedisAutoCacheManager(RedisCacheWriter cacheWriter,
			RedisCacheConfiguration defaultCacheConfiguration,
			Map<String, RedisCacheConfiguration> initialCacheConfigurations,
			boolean allowInFlightCacheCreation) {
			super(cacheWriter, defaultCacheConfiguration, initialCacheConfigurations,
				allowInFlightCacheCreation);
		}

		@Override
		protected RedisCache createRedisCache(String name,
			@Nullable RedisCacheConfiguration cacheConfig) {
			if (StringUtil.isBlank(name) || !name.contains(StringPool.HASH)) {
				return super.createRedisCache(name, cacheConfig);
			}
			String[] cacheArray = name.split(StringPool.HASH);
			if (cacheArray.length < 2) {
				return super.createRedisCache(name, cacheConfig);
			}
			String cacheName = cacheArray[0];
			if (cacheConfig != null) {
				// 转换时间，支持时间单位例如：300ms，第二个参数是默认单位
				Duration duration = DurationStyle.detectAndParse(cacheArray[1], ChronoUnit.SECONDS);
				cacheConfig = cacheConfig.entryTtl(duration);
			}
			return super.createRedisCache(cacheName, cacheConfig);
		}

	}

//	@Configuration
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

	//private RedisCacheConfiguration getDefConf() {
	//	RedisCacheConfiguration def = RedisCacheConfiguration
	//		.defaultCacheConfig()
	//		.disableCachingNullValues()
	//		.serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(
	//			new StringRedisSerializer()))
	//		.serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(
	//			new RedisObjectSerializer()));
	//	return handleRedisCacheConfiguration(cacheProperties.getDef(), def);
	//}
	//
	//private RedisCacheConfiguration handleRedisCacheConfiguration(
	//	CacheProperties.Cache redisProperties, RedisCacheConfiguration config) {
	//	if (Objects.isNull(redisProperties)) {
	//		return config;
	//	}
	//	if (redisProperties.getTimeToLive() != null) {
	//		config = config.entryTtl(redisProperties.getTimeToLive());
	//	}
	//	if (redisProperties.getKeyPrefix() != null) {
	//		config = config.computePrefixWith(cacheName -> redisProperties.getKeyPrefix().concat(
	//			StrPool.COLON).concat(cacheName).concat(StrPool.COLON));
	//	} else {
	//		config = config.computePrefixWith(cacheName -> cacheName.concat(StrPool.COLON));
	//	}
	//	if (!redisProperties.isCacheNullValues()) {
	//		config = config.disableCachingNullValues();
	//	}
	//	if (!redisProperties.isUseKeyPrefix()) {
	//		config = config.disableKeyPrefix();
	//	}
	//
	//	return config;
	//}
}
