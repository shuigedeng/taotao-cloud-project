/*
 * Copyright (c) 2020-2030 ZHENGGENGWEI(码匠君)<herodotus@aliyun.com>
 *
 * Dante Engine Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Dante Engine 采用APACHE LICENSE 2.0开源协议，您在使用过程中，需要注意以下几点：
 *
 * 1.请不要删除和修改根目录下的LICENSE文件。
 * 2.请不要删除和修改 Dante Engine 源码头部的版权声明。
 * 3.请保留源码和相关描述文件的项目出处，作者声明等。
 * 4.分发源码时候，请注明软件出处 https://gitee.com/herodotus/dante-engine
 * 5.在修改包名，模块名称，项目代码等时，请注明软件出处 https://gitee.com/herodotus/dante-engine
 * 6.若您的项目无法满足以上几点，可申请商业授权
 */

package com.taotao.cloud.jetcache.enhance;

import com.alicp.jetcache.Cache;
import com.taotao.cloud.common.utils.common.JsonUtils;
import java.util.concurrent.Callable;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.support.AbstractValueAdaptingCache;
import org.springframework.lang.Nullable;

/**
 * <p>Description: 基于 JetCache 的 Spring Cache 扩展 </p>
 *
 * @author : gengwei.zheng
 * @date : 2022/7/23 11:11
 */
public class JetCacheSpringCache extends AbstractValueAdaptingCache {

	private static final Logger log = LoggerFactory.getLogger(JetCacheSpringCache.class);

	private final String cacheName;
	private final Cache<Object, Object> cache;

	public JetCacheSpringCache(String cacheName, Cache<Object, Object> cache,
		boolean allowNullValues) {
		super(allowNullValues);
		this.cacheName = cacheName;
		this.cache = cache;
	}

	@Override
	public String getName() {
		return this.cacheName;
	}

	@Override
	public final Cache<Object, Object> getNativeCache() {
		return this.cache;
	}

	@Override
	@Nullable
	protected Object lookup(Object key) {
		Object value = cache.get(key);
		if (ObjectUtils.isNotEmpty(value)) {
			log.trace("[Herodotus] |- CACHE - Lookup data in herodotus cache, value is : [{}]",
				JsonUtils.toJson(value));
			return value;
		}

		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	@Nullable
	public <T> T get(Object key, Callable<T> valueLoader) {

		log.trace("[Herodotus] |- CACHE - Get data in herodotus cache, key: {}", key);

		return (T) fromStoreValue(cache.computeIfAbsent(key, k -> {
			try {
				return toStoreValue(valueLoader.call());
			} catch (Throwable ex) {
				throw new ValueRetrievalException(key, valueLoader, ex);
			}
		}));
	}

	@Override
	@Nullable
	public void put(Object key, @Nullable Object value) {
		log.trace("[Herodotus] |- CACHE - Put data in herodotus cache, key: {}", key);
		cache.put(key, this.toStoreValue(value));
	}


	@Override
	@Nullable
	public ValueWrapper putIfAbsent(Object key, @Nullable Object value) {
		log.trace("[Herodotus] |- CACHE - PutIfPresent data in herodotus cache, key: {}", key);
		Object existing = cache.putIfAbsent(key, toStoreValue(value));
		return toValueWrapper(existing);
	}

	@Override
	public void evict(Object key) {
		log.trace("[Herodotus] |- CACHE - Evict data in herodotus cache, key: {}", key);
		cache.remove(key);
	}

	@Override
	public boolean evictIfPresent(Object key) {
		log.trace("[Herodotus] |- CACHE - EvictIfPresent data in herodotus cache, key: {}", key);
		return cache.remove(key);
	}

	@Override
	public void clear() {
		log.trace("[Herodotus] |- CACHE - Clear data in herodotus cache.");
		cache.close();
	}
}
