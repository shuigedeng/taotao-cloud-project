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

import com.taotao.cloud.common.constant.SymbolConstants;
import java.time.Duration;
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
 * <p>Description: 基于 JetCache 的 Spring Cache Manager 扩展 </p>
 *
 * @author : gengwei.zheng
 * @date : 2022/7/23 14:06
 */
public class JetCacheSpringCacheManager implements CacheManager {

	private static final Logger log = LoggerFactory.getLogger(JetCacheSpringCacheManager.class);

	private boolean dynamic = true;
	private boolean allowNullValues = true;

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

	public boolean isAllowNullValues() {
		return allowNullValues;
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
		log.debug("[Herodotus] |- CACHE - Herodotus cache [{}] is CREATED.", name);
		return new JetCacheSpringCache(name, cache, allowNullValues);
	}

	protected Cache createJetCache(String name, Duration expire) {
		com.alicp.jetcache.Cache<Object, Object> cache = jetCacheCreateCacheFactory.create(name,
			expire, allowNullValues, true);
		log.debug("[Herodotus] |- CACHE - Herodotus cache [{}] with expire is CREATED.", name);
		return new JetCacheSpringCache(name, cache, allowNullValues);
	}

	private String availableCacheName(String name) {
		if (StringUtils.endsWith(name, SymbolConstants.COLON)) {
			return name;
		} else {
			return name + SymbolConstants.COLON;
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
