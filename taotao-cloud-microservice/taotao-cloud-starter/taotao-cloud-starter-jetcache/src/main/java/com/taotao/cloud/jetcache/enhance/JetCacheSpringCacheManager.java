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

import static com.taotao.cloud.common.constant.StrPool.COLON;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.lang.Nullable;

/**
 * 基于 JetCache 的 Spring Cache Manager 扩展
 *
 * @author shuigedeng
 * @version 2022.07
 * @since 2022-07-25 08:54:44
 */
public class JetCacheSpringCacheManager implements CacheManager {

	private static final Logger log = LoggerFactory.getLogger(JetCacheSpringCacheManager.class);

	private boolean dynamic = true;
	private boolean allowNullValues = true;
	private boolean desensitization = true;

	private final Map<String, Cache> cacheMap = new ConcurrentHashMap<>(16);

	private final JetCacheCreateCacheFactory jetCacheCreateCacheFactory;

	public JetCacheSpringCacheManager(JetCacheCreateCacheFactory jetCacheCreateCacheFactory) {
		this.jetCacheCreateCacheFactory = jetCacheCreateCacheFactory;
	}

	public JetCacheSpringCacheManager(JetCacheCreateCacheFactory jetCacheCreateCacheFactory,
		String... cacheNames) {
		this.jetCacheCreateCacheFactory = jetCacheCreateCacheFactory;
		setCacheNames(Arrays.asList(cacheNames));
	}

	public void setAllowNullValues(boolean allowNullValues) {
		this.allowNullValues = allowNullValues;
	}

	public void setDesensitization(boolean desensitization) {
		this.desensitization = desensitization;
	}

	public boolean isAllowNullValues() {
		return allowNullValues;
	}

	public boolean isDesensitization() {
		return desensitization;
	}

	private void setCacheNames(@Nullable Collection<String> cacheNames) {
		if (cacheNames != null) {
			for (String name : cacheNames) {
				this.cacheMap.put(name, createJetCache(name));
			}
			this.dynamic = false;
		} else {
			this.dynamic = true;
		}
	}

	protected Cache createJetCache(String name) {
		com.alicp.jetcache.Cache<Object, Object> cache = jetCacheCreateCacheFactory.create(name);
		log.debug("CACHE -  cache [{}] is CREATED.", name);
		return new JetCacheSpringCache(name, cache, allowNullValues, desensitization);
	}

	private String availableCacheName(String name) {
		if (StringUtils.endsWith(name, COLON)) {
			return name;
		} else {
			return name + COLON;
		}
	}


	@Override
	@Nullable
	public Cache getCache(String name) {
		String usedName = availableCacheName(name);
		return this.cacheMap.computeIfAbsent(usedName, cacheName ->
			this.dynamic ? createJetCache(cacheName) : null);
	}

	@Override
	public Collection<String> getCacheNames() {
		return Collections.unmodifiableSet(this.cacheMap.keySet());
	}
}
