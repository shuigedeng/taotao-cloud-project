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
package com.taotao.cloud.core.model;

import com.taotao.cloud.common.utils.BeanUtil;
import com.taotao.cloud.core.enums.EventEnum;
import com.taotao.cloud.core.utils.PropertyUtil;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;

/**
 * 属性缓存层
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 20:37:32
 */
@Order(200000000)
public class PropertyCache implements CommandLineRunner {

	private Pubsub pubsub;

	public PropertyCache(Pubsub pubsub) {
		this.pubsub = pubsub;
	}

	/**
	 * cache
	 */
	private final ConcurrentHashMap<String, Object> cache = new ConcurrentHashMap<>();
	/**
	 * isStart
	 */
	private boolean isStart = false;

	@Override
	public void run(String... args) throws Exception {
		//启动结束后开始缓存
		clear();
		isStart = true;
	}

	/**
	 * get
	 *
	 * @param key          key
	 * @param defaultValue defaultValue
	 * @param <T>          T
	 * @return T
	 * @author shuigedeng
	 * @since 2021-09-02 20:37:54
	 */
	public <T> T get(String key, T defaultValue) {
		if (!isStart) {
			String v = PropertyUtil.getProperty(key);
			if (v == null) {
				return defaultValue;
			} else {
				return (T) BeanUtil.convert(v, defaultValue.getClass());
			}
		}

		Object value = cache.get(key);
		if (value == null) {
			String v = PropertyUtil.getProperty(key);
			if (v != null) {
				cache.put(key, v);
			} else {
				cache.put(key, PropertyUtil.NULL);
			}
		}

		value = cache.get(key);
		if (PropertyUtil.NULL.equals(value)) {
			return defaultValue;
		} else {
			return (T) BeanUtil.convert(value, defaultValue.getClass());
		}
	}

	/**
	 * tryUpdateCache
	 *
	 * @param key   key
	 * @param value value
	 * @author shuigedeng
	 * @since 2021-09-02 20:38:10
	 */
	public void tryUpdateCache(String key, Object value) {
		if (!isStart) {
			return;
		}

		if (cache.containsKey(key)) {
			if (value == null) {
				cache.put(key, PropertyUtil.NULL);
			} else {
				cache.put(key, value);
			}
		}

		pubsub.pub(EventEnum.PropertyCacheUpdateEvent.toString(), new HashMap(1) {
			{
				put(key, value);
			}
		});
	}

	/**
	 * listenUpdateCache
	 *
	 * @param name   name
	 * @param action action
	 * @author shuigedeng
	 * @since 2021-09-02 20:38:14
	 */
	public void listenUpdateCache(String name, Callable.Action1<HashMap<String, Object>> action) {
		pubsub.sub(EventEnum.PropertyCacheUpdateEvent, new Pubsub.Sub(name, action));
	}

	/**
	 * clear
	 *
	 * @author shuigedeng
	 * @since 2021-09-02 20:38:20
	 */
	public void clear() {
		cache.clear();
	}


}
