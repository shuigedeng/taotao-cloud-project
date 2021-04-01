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
package com.taotao.cloud.redis.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

import java.util.List;

/**
 * CacheManagerProperties
 *
 * @author dengtao
 * @version 1.0.0
 * @since 2020/4/30 10:16
 */
@Data
@RefreshScope
@ConfigurationProperties(prefix = CacheManagerProperties.BASE_REDIS_CACHE_MANAGER_PREFIX)
public class CacheManagerProperties {

	public static final String BASE_REDIS_CACHE_MANAGER_PREFIX = "taotao.cloud.redis.cache.manager";

	private List<CacheConfig> configs;

	@Data
	public static class CacheConfig {

		/**
		 * cache key
		 */
		private String key;
		/**
		 * 过期时间，sec
		 */
		private long second = 60;
	}
}
