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

package com.taotao.cloud.cache.caffeine.repository;

import cn.hutool.core.util.StrUtil;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.taotao.cloud.cache.caffeine.model.CacheHashKey;
import com.taotao.cloud.cache.caffeine.model.CacheKey;
import com.taotao.cloud.common.constant.StrPool;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.lang.NonNull;


/**
 * 基于 Caffeine 实现的内存缓存， 主要用于开发、测试、演示环境
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:46:04
 */
public class CaffeineRepository {

	/**
	 * 最大数量
	 */
	private static final long DEF_MAX_SIZE = 1_000;

	/**
	 * 为什么不直接用 Cache<String, Object> ？ 因为想针对每一个key单独设置过期时间
	 */
	private final Cache<String, Cache<String, Object>> cacheMap = Caffeine.newBuilder()
		.maximumSize(DEF_MAX_SIZE)
		.build();

	public Long del(@NonNull CacheKey... keys) {
		for (CacheKey key : keys) {
			cacheMap.invalidate(key.getKey());
		}
		return (long) keys.length;
	}

	public Long del(String... keys) {
		for (String key : keys) {
			cacheMap.invalidate(key);
		}
		return (long) keys.length;
	}

	public void set(@NonNull CacheKey key, Object value, boolean... cacheNullValues) {
		if (value == null) {
			return;
		}
		Caffeine<Object, Object> builder = Caffeine.newBuilder()
			.maximumSize(DEF_MAX_SIZE);
		if (key.getExpire() != null) {
			builder.expireAfterWrite(key.getExpire());
		}
		Cache<String, Object> cache = builder.build();
		cache.put(key.getKey(), value);
		cacheMap.put(key.getKey(), cache);
	}


	@SuppressWarnings("unchecked")
	public <T> T get(@NonNull CacheKey key, boolean... cacheNullValues) {
		Cache<String, Object> ifPresent = cacheMap.getIfPresent(key.getKey());
		if (ifPresent == null) {
			return null;
		}
		return (T) ifPresent.getIfPresent(key.getKey());
	}

	@SuppressWarnings("unchecked")
	public <T> T get(String key, boolean... cacheNullValues) {
		Cache<String, Object> ifPresent = cacheMap.getIfPresent(key);
		if (ifPresent == null) {
			return null;
		}
		return (T) ifPresent.getIfPresent(key);
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> find(@NonNull Collection<CacheKey> keys) {
		return keys.stream().map(k -> (T) get(k, false)).filter(Objects::nonNull)
			.collect(Collectors.toList());
	}

	@SuppressWarnings("unchecked")
	public <T> T get(@NonNull CacheKey key, Function<CacheKey, ? extends T> loader,
		boolean... cacheNullValues) {
		Cache<String, Object> cache = cacheMap.get(key.getKey(), (k) -> {
			Caffeine<Object, Object> builder = Caffeine.newBuilder()
				.maximumSize(DEF_MAX_SIZE);
			if (key.getExpire() != null) {
				builder.expireAfterWrite(key.getExpire());
			}
			Cache<String, Object> newCache = builder.build();
			newCache.get(k, (tk) -> loader.apply(new CacheKey(tk)));
			return newCache;
		});

		return (T) cache.getIfPresent(key.getKey());
	}


	public void flushDb() {
		cacheMap.invalidateAll();
	}


	public Boolean exists(@NonNull final CacheKey key) {
		Cache<String, Object> cache = cacheMap.getIfPresent(key.getKey());
		if (cache == null) {
			return false;
		}
		cache.cleanUp();
		return cache.estimatedSize() > 0;
	}


	public Long incr(@NonNull CacheKey key) {
		Long old = get(key, k -> 0L);
		Long newVal = old + 1;
		set(key, newVal);
		return newVal;
	}


	public Long getCounter(CacheKey key, Function<CacheKey, Long> loader) {
		return get(key);
	}


	public Long incrBy(@NonNull CacheKey key, long increment) {
		Long old = get(key, k -> 0L);
		Long newVal = old + increment;
		set(key, newVal);
		return newVal;
	}


	public Double incrByFloat(@NonNull CacheKey key, double increment) {
		Double old = get(key, k -> 0D);
		Double newVal = old + increment;
		set(key, newVal);
		return newVal;
	}


	public Long decr(@NonNull CacheKey key) {
		Long old = get(key, k -> 0L);
		Long newVal = old - 1;
		set(key, newVal);
		return newVal;
	}

	public Long decrBy(@NonNull CacheKey key, long decrement) {
		Long old = get(key, k -> 0L);
		Long newVal = old - decrement;
		set(key, newVal);
		return newVal;
	}
	// ---- 以下接口可能有问题，仅支持在开发环境使用

	/**
	 * KEYS * 匹配数据库中所有 key 。 KEYS h?llo 匹配 hello ， hallo 和 hxllo 等。 KEYS h*llo 匹配 hllo 和 heeeeello
	 * 等。 KEYS h[ae]llo 匹配 hello 和 hallo ，但不匹配 hillo
	 *
	 * @param pattern 表达式
	 * @return 集合
	 */
	public Set<String> keys(@NonNull String pattern) {
		if (StrUtil.isEmpty(pattern)) {
			return Collections.emptySet();
		}
		ConcurrentMap<String, Cache<String, Object>> map = cacheMap.asMap();
		Set<String> list = new HashSet<>();
		map.forEach((k, val) -> {
			// *
			if (StrPool.ASTERISK.equals(pattern)) {
				list.add(k);
				return;
			}
			// h?llo
			if (pattern.contains(StrPool.QUESTION_MARK)) {
				//待实现
				return;
			}
			// h*llo
			if (pattern.contains(StrPool.ASTERISK)) {
				//待实现
				return;
			}
			// h[ae]llo
			if (pattern.contains(StrPool.LEFT_SQ_BRACKET) && pattern.contains(
				StrPool.RIGHT_SQ_BRACKET)) {
				//待实现
				return;
			}
		});
		return list;
	}


	public Boolean expire(@NonNull CacheKey key) {
		return true;
	}


	public Boolean persist(@NonNull CacheKey key) {
		return true;
	}


	public String type(@NonNull CacheKey key) {
		return "caffeine";
	}


	public Long ttl(@NonNull CacheKey key) {
		return -1L;
	}


	public Long pTtl(@NonNull CacheKey key) {
		return -1L;
	}


	public void hSet(@NonNull CacheHashKey key, Object value, boolean... cacheNullValues) {
		this.set(key.tran(), value, cacheNullValues);
	}


	public <T> T hGet(@NonNull CacheHashKey key, boolean... cacheNullValues) {
		return get(key.tran(), cacheNullValues);
	}


	public <T> T hGet(@NonNull CacheHashKey key, Function<CacheHashKey, T> loader,
		boolean... cacheNullValues) {
		Function<CacheKey, T> ckLoader = k -> loader.apply(key);
		return get(key.tran(), ckLoader, cacheNullValues);
	}


	public Boolean hExists(@NonNull CacheHashKey cacheHashKey) {
		return exists(cacheHashKey.tran());
	}


	public Long hDel(@NonNull String key, Object... fields) {
		for (Object field : fields) {
			cacheMap.invalidate(StrUtil.join(StrUtil.COLON, key, field));
		}
		return (long) fields.length;
	}


	public Long hDel(@NonNull CacheHashKey cacheHashKey) {
		cacheMap.invalidate(cacheHashKey.tran().getKey());
		return 1L;
	}


	public Long hLen(@NonNull CacheHashKey key) {
		return 0L;
	}


	public Long hIncrBy(@NonNull CacheHashKey key, long increment) {
		return incrBy(key.tran(), increment);
	}


	public Double hIncrBy(@NonNull CacheHashKey key, double increment) {
		return incrByFloat(key.tran(), increment);
	}


	public Set<Object> hKeys(@NonNull CacheHashKey key) {
		return Collections.emptySet();
	}


	public List<Object> hVals(@NonNull CacheHashKey key) {
		return Collections.emptyList();
	}


	public <K, V> Map<K, V> hGetAll(CacheHashKey key) {
		return Collections.emptyMap();
	}


	public <K, V> Map<K, V> hGetAll(CacheHashKey key, Function<CacheHashKey, Map<K, V>> loader,
		boolean... cacheNullValues) {
		return Collections.emptyMap();
	}


	public Long sAdd(@NonNull CacheKey key, Object value) {
		return 0L;
	}


	public Long sRem(@NonNull CacheKey key, Object... members) {
		return 0L;
	}


	public Set<Object> sMembers(@NonNull CacheKey key) {
		return Collections.emptySet();
	}


	public <T> T sPop(@NonNull CacheKey key) {
		return null;
	}


	public Long sCard(@NonNull CacheKey key) {
		return 0L;
	}
}
