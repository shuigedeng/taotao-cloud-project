package com.taotao.cloud.cache.jetcache.enhance;

import com.taotao.cloud.common.constant.SymbolConstants;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.lang.Nullable;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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
