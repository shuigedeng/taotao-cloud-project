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

package com.taotao.cloud.jetcache.enhance;

import cn.hutool.crypto.SecureUtil;
import com.alicp.jetcache.Cache;
import com.taotao.cloud.common.utils.common.JsonUtils;
import java.util.concurrent.Callable;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.support.AbstractValueAdaptingCache;
import org.springframework.lang.Nullable;

/**
 * 基于 JetCache 的 Spring Cache 扩展
 *
 * @author shuigedeng
 * @version 2022.07
 * @since 2022-07-25 08:54:40
 */
public class JetCacheSpringCache extends AbstractValueAdaptingCache {

	private static final Logger log = LoggerFactory.getLogger(JetCacheSpringCache.class);

	private final String cacheName;
	private final Cache<Object, Object> cache;
	private final boolean desensitization;

	public JetCacheSpringCache(String cacheName, Cache<Object, Object> cache,
		boolean allowNullValues, boolean desensitization) {
		super(allowNullValues);
		this.cacheName = cacheName;
		this.cache = cache;
		this.desensitization = desensitization;
	}

	private String secure(Object key) {
		String original = String.valueOf(key);
		if (desensitization) {
			if (StringUtils.isNotBlank(original) && StringUtils.startsWith(original, "sql:")) {
				String recent = SecureUtil.md5(original);
				log.trace("CACHE - Secure the sql type key [{}] to [{}]", original,
					recent);
				return recent;
			}
		}
		return original;
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
		String secure = secure(key);

		Object value = cache.get(secure);
		if (ObjectUtils.isNotEmpty(value)) {
			log.trace("CACHE - Lookup data in  cache, value is : [{}]",
				JsonUtils.toJson(value));
			return value;
		}

		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	@Nullable
	public <T> T get(Object key, Callable<T> valueLoader) {
		String secure = secure(key);

		log.trace("CACHE - Get data in  cache, key: {}", secure);

		return (T) fromStoreValue(cache.computeIfAbsent(secure, k -> {
			try {
				return toStoreValue(valueLoader.call());
			} catch (Throwable ex) {
				throw new ValueRetrievalException(secure, valueLoader, ex);
			}
		}));
	}

	@Override
	@Nullable
	public void put(Object key, @Nullable Object value) {
		String secure = secure(key);
		log.trace("CACHE - Put data in  cache, key: {}", secure);
		cache.put(secure, this.toStoreValue(value));
	}


	@Override
	@Nullable
	public ValueWrapper putIfAbsent(Object key, @Nullable Object value) {
		String secure = secure(key);
		log.trace("CACHE - PutIfPresent data in  cache, key: {}", secure);
		Object existing = cache.putIfAbsent(secure, toStoreValue(value));
		return toValueWrapper(existing);
	}

	@Override
	public void evict(Object key) {
		String secure = secure(key);
		log.trace("CACHE - Evict data in  cache, key: {}", secure);
		cache.remove(secure);
	}

	@Override
	public boolean evictIfPresent(Object key) {
		String secure = secure(key);
		log.trace("CACHE - EvictIfPresent data in  cache, key: {}", secure);
		return cache.remove(secure);
	}

	@Override
	public void clear() {
		log.trace("CACHE - Clear data in  cache.");
		cache.close();
	}


}
