/*
 * Copyright (c) 2019-2029, Dreamlu 卢春梦 (596392912@qq.com & www.dreamlu.net).
 * <p>
 * Licensed under the GNU LESSER GENERAL PUBLIC LICENSE 3.0;
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.gnu.org/licenses/lgpl.html
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.caffeine.configuration;

import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.CaffeineSpec;
import com.taotao.cloud.caffeine.manager.CaffeineAutoCacheManager;
import com.taotao.cloud.caffeine.properties.CaffeineProperties;
import com.taotao.cloud.caffeine.repository.CaffeineRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.cache.CacheAutoConfiguration;
import org.springframework.boot.autoconfigure.cache.CacheManagerCustomizer;
import org.springframework.boot.autoconfigure.cache.CacheManagerCustomizers;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.lang.Nullable;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 * Caffeine auto cache configuration.
 *
 * @author shuigedeng
 * @version 2022.07
 * @since 2022-07-03 09:33:45
 */
@EnableCaching
@ConditionalOnClass({Caffeine.class, CaffeineCacheManager.class})
@AutoConfiguration(before = CacheAutoConfiguration.class)
@EnableConfigurationProperties({RedisProperties.class, CaffeineProperties.class ,CacheProperties.class})
@ConditionalOnProperty(prefix = CaffeineProperties.PREFIX, name = "enabled", havingValue = "true")
public class CaffeineCacheAutoConfiguration {

	@Bean
	public CaffeineRepository caffeineRepository() {
		return new CaffeineRepository();
	}

	@Bean
	@ConditionalOnMissingBean
	public CacheManagerCustomizers cacheManagerCustomizers(
		ObjectProvider<CacheManagerCustomizer<?>> customizers) {
		return new CacheManagerCustomizers(
			customizers.orderedStream().collect(Collectors.toList()));
	}

	@ConditionalOnMissingBean
	@Bean("cacheResolver")
	public CacheManager cacheManager(CacheProperties cacheProperties,
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

	private static CaffeineAutoCacheManager createCacheManager(CacheProperties cacheProperties,
		ObjectProvider<Caffeine<Object, Object>> caffeine,
		ObjectProvider<CaffeineSpec> caffeineSpec,
		ObjectProvider<CacheLoader<Object, Object>> cacheLoader) {
		CaffeineAutoCacheManager cacheManager = new CaffeineAutoCacheManager();
		setCacheBuilder(cacheProperties, caffeineSpec.getIfAvailable(), caffeine.getIfAvailable(),
			cacheManager);
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
