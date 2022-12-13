package com.taotao.cloud.sa.just.biz.just;

import me.zhyd.oauth.cache.AuthStateCache;

/**
 * <p>
 * 自定义缓存实现
 * </p>
 */
public class MyAuthStateCache implements AuthStateCache {

	/**
	 * 存入缓存
	 *
	 * @param key   缓存key
	 * @param value 缓存内容
	 */
	@Override
	public void cache(String key, String value) {
		// TODO: 自定义存入缓存
	}

	/**
	 * 存入缓存
	 *
	 * @param key     缓存key
	 * @param value   缓存内容
	 * @param timeout 指定缓存过期时间（毫秒）
	 */
	@Override
	public void cache(String key, String value, long timeout) {
		// TODO: 自定义存入缓存
	}

	/**
	 * 获取缓存内容
	 *
	 * @param key 缓存key
	 * @return 缓存内容
	 */
	@Override
	public String get(String key) {
		// TODO: 自定义获取缓存内容
		return null;
	}

	/**
	 * 是否存在key，如果对应key的value值已过期，也返回false
	 *
	 * @param key 缓存key
	 * @return true：存在key，并且value没过期；false：key不存在或者已过期
	 */
	@Override
	public boolean containsKey(String key) {
		// TODO: 自定义判断key是否存在
		return false;
	}
}
