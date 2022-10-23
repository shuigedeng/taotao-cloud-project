package com.taotao.cloud.jetcache.enhance;

import com.alicp.jetcache.Cache;
import com.alicp.jetcache.CacheManager;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.template.QuickConfig;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import java.time.Duration;

/**
 * <p>Description: JetCache 手动创建Cache 工厂 </p>
 *
 * @author : gengwei.zheng
 * @date : 2022/7/23 10:49
 */
public class JetCacheCreateCacheFactory {

	private final CacheManager cacheManager;

	public JetCacheCreateCacheFactory(CacheManager cacheManager) {
		this.cacheManager = cacheManager;
	}

	public <K, V> Cache<K, V> create(String name) {
		return create(name, Duration.ofHours(2L));
	}

	public <K, V> Cache<K, V> create(String name, Duration expire) {
		return create(name, expire, true);
	}

	public <K, V> Cache<K, V> create(String name, Duration expire, Boolean cacheNullValue) {
		return create(name, expire, cacheNullValue, null);
	}

	public <K, V> Cache<K, V> create(String name, Duration expire, Boolean cacheNullValue,
									 Boolean syncLocal) {
		return create(name, CacheType.BOTH, expire, cacheNullValue, syncLocal);
	}

	public <K, V> Cache<K, V> create(String name, CacheType cacheType) {
		return create(name, cacheType, null);
	}

	public <K, V> Cache<K, V> create(String name, CacheType cacheType, Duration expire) {
		return create(name, cacheType, expire, true);
	}

	public <K, V> Cache<K, V> create(String name, CacheType cacheType, Duration expire,
									 Boolean cacheNullValue) {
		return create(name, cacheType, expire, cacheNullValue, null);
	}

	public <K, V> Cache<K, V> create(String name, CacheType cacheType, Duration expire,
									 Boolean cacheNullValue, Boolean syncLocal) {
		return create(null, name, cacheType, expire, cacheNullValue, syncLocal);
	}

	public <K, V> Cache<K, V> create(String area, String name, CacheType cacheType, Duration expire,
									 Boolean cacheNullValue, Boolean syncLocal) {
		return create(area, name, cacheType, expire, cacheNullValue, syncLocal, null);
	}

	public <K, V> Cache<K, V> create(String area, String name, CacheType cacheType, Duration expire,
									 Boolean cacheNullValue, Boolean syncLocal, Duration localExpire) {
		return create(area, name, cacheType, expire, cacheNullValue, syncLocal, localExpire, null);
	}

	public <K, V> Cache<K, V> create(String area, String name, CacheType cacheType, Duration expire,
									 Boolean cacheNullValue, Boolean syncLocal, Duration localExpire, Integer localLimit) {
		return create(area, name, cacheType, expire, cacheNullValue, syncLocal, localExpire,
			localLimit, false);
	}

	public <K, V> Cache<K, V> create(String area, String name, CacheType cacheType, Duration expire,
									 Boolean cacheNullValue, Boolean syncLocal, Duration localExpire, Integer localLimit,
									 Boolean useAreaInPrefix) {
		return create(area, name, cacheType, expire, cacheNullValue, syncLocal, localExpire,
			localLimit, useAreaInPrefix, false, null);
	}

	public <K, V> Cache<K, V> create(String area, String name, CacheType cacheType, Duration expire,
									 Boolean cacheNullValue, Boolean syncLocal, Duration localExpire, Integer localLimit,
									 Boolean useAreaInPrefix, Boolean penetrationProtect, Duration penetrationProtectTimeout) {
		QuickConfig.Builder builder = StringUtils.isEmpty(area) ? QuickConfig.newBuilder(name)
			: QuickConfig.newBuilder(area, name);
		builder.cacheType(cacheType);
		builder.expire(expire);
		if (cacheType == CacheType.BOTH) {
			builder.syncLocal(syncLocal);
		}
		builder.localExpire(localExpire);
		builder.localLimit(localLimit);
		builder.cacheNullValue(cacheNullValue);
		builder.useAreaInPrefix(useAreaInPrefix);
		if (ObjectUtils.isNotEmpty(penetrationProtect)) {
			builder.penetrationProtect(penetrationProtect);
			if (BooleanUtils.isTrue(penetrationProtect) && ObjectUtils.isNotEmpty(
				penetrationProtectTimeout)) {
				builder.penetrationProtectTimeout(penetrationProtectTimeout);
			}
		}

		QuickConfig quickConfig = builder.build();
		return create(quickConfig);
	}


	@SuppressWarnings("unchecked")
	private <K, V> Cache<K, V> create(QuickConfig quickConfig) {
		return cacheManager.getOrCreateCache(quickConfig);
	}
}
