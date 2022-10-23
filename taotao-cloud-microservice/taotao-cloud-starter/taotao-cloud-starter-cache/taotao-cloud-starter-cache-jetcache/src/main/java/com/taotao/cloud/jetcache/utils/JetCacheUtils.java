package com.taotao.cloud.jetcache.utils;

import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.CacheType;
import com.taotao.cloud.jetcache.enhance.JetCacheCreateCacheFactory;
import org.apache.commons.lang3.ObjectUtils;

import java.time.Duration;

/**
 * <p>Description: JetCache 单例工具类 </p>
 */
public class JetCacheUtils {

	private static volatile JetCacheUtils instance;
	private JetCacheCreateCacheFactory jetCacheCreateCacheFactory;

	private JetCacheUtils() {

	}

	private void init(JetCacheCreateCacheFactory jetCacheCreateCacheFactory) {
		this.jetCacheCreateCacheFactory = jetCacheCreateCacheFactory;
	}

	private JetCacheCreateCacheFactory getJetCacheCreateCacheFactory() {
		return jetCacheCreateCacheFactory;
	}

	public static JetCacheUtils getInstance() {
		if (ObjectUtils.isEmpty(instance)) {
			synchronized (JetCacheUtils.class) {
				if (ObjectUtils.isEmpty(instance)) {
					instance = new JetCacheUtils();
				}
			}
		}
		return instance;
	}

	public static void setJetCacheCreateCacheFactory(
		JetCacheCreateCacheFactory jetCacheCreateCacheFactory) {
		getInstance().init(jetCacheCreateCacheFactory);
	}

	public static <K, V> Cache<K, V> create(String name, Duration expire) {
		return create(name, expire, true);
	}

	public static <K, V> Cache<K, V> create(String name, Duration expire, Boolean cacheNullValue) {
		return create(name, expire, cacheNullValue, null);
	}

	public static <K, V> Cache<K, V> create(String name, Duration expire, Boolean cacheNullValue,
											Boolean syncLocal) {
		return create(name, CacheType.BOTH, expire, cacheNullValue, syncLocal);
	}

	public static <K, V> Cache<K, V> create(String name, CacheType cacheType) {
		return create(name, cacheType, null);
	}

	public static <K, V> Cache<K, V> create(String name, CacheType cacheType, Duration expire) {
		return create(name, cacheType, expire, true);
	}

	public static <K, V> Cache<K, V> create(String name, CacheType cacheType, Duration expire,
											Boolean cacheNullValue) {
		return create(name, cacheType, expire, cacheNullValue, null);
	}

	public static <K, V> Cache<K, V> create(String name, CacheType cacheType, Duration expire,
											Boolean cacheNullValue, Boolean syncLocal) {
		return getInstance().getJetCacheCreateCacheFactory()
			.create(name, cacheType, expire, cacheNullValue, syncLocal);
	}
}
