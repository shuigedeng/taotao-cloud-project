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

import com.github.benmanes.caffeine.cache.Caffeine;
import com.google.common.collect.Maps;
import com.taotao.cloud.common.constant.StrPool;
import com.taotao.cloud.redis.properties.CustomCacheProperties;
import com.taotao.cloud.redis.properties.RedisLockProperties;
import com.taotao.cloud.redis.serializer.RedisObjectSerializer;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
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

/**
 * redis 配置类
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2020/4/30 10:13
 */
@EnableCaching
@ConditionalOnProperty(prefix = CustomCacheProperties.PREFIX, name = "enabled", havingValue = "true")
public class TaoTaoCloudCacheAutoConfiguration implements InitializingBean {

	private final CustomCacheProperties cacheProperties;

	public TaoTaoCloudCacheAutoConfiguration(
		CustomCacheProperties cacheProperties) {
		this.cacheProperties = cacheProperties;
	}

	@Override
	public void afterPropertiesSet() throws Exception {

	}

	/**
	 * key 的生成
	 */
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
					sb.append(obj.toString());
				}
			}
			return sb.toString();
		};
	}

	@Bean(name = "redisCacheManager")
	@Primary
	@ConditionalOnProperty(prefix = CustomCacheProperties.PREFIX, name = "type", havingValue = "REDIS")
	public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
		RedisCacheConfiguration defConfig = getDefConf();
		defConfig.entryTtl(cacheProperties.getDef().getTimeToLive());

		Map<String, CustomCacheProperties.Cache> configs = cacheProperties.getConfigs();
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
		CustomCacheProperties.Cache redisProperties, RedisCacheConfiguration config) {
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

	@Bean("caffeineCacheManager")
	@ConditionalOnProperty(prefix = CustomCacheProperties.PREFIX, name = "type", havingValue = "CAFFEINE")
	public CacheManager caffeineCacheManager() {
		CaffeineCacheManager cacheManager = new CaffeineCacheManager();

		Caffeine caffeine = Caffeine
			.newBuilder()
			.recordStats()
			.initialCapacity(500)
			.expireAfterWrite(cacheProperties.getDef().getTimeToLive())
			.maximumSize(cacheProperties.getDef().getMaxSize());

		cacheManager.setAllowNullValues(cacheProperties.getDef().isCacheNullValues());
		cacheManager.setCaffeine(caffeine);

		//配置了这里，就必须事先在配置文件中指定key 缓存才生效
//        Map<String, CustomCacheProperties.Redis> configs = cacheProperties.getConfigs();
//        Optional.ofNullable(configs).ifPresent((config)->{
//            cacheManager.setCacheNames(config.keySet());
//        });
		return cacheManager;
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
}
