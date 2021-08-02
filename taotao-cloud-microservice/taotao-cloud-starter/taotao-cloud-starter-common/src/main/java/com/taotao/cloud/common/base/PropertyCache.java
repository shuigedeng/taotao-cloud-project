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
package com.taotao.cloud.common.base;

import com.taotao.cloud.common.enums.EventEnum;
import com.taotao.cloud.common.utils.BeanUtil;
import com.taotao.cloud.common.utils.PropertyUtil;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;

/**
 * 属性缓存层
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2021/6/22 17:09
 **/
@Order(200000000)
public class PropertyCache implements CommandLineRunner {

	public static PropertyCache Default = new PropertyCache();
	private final ConcurrentHashMap<String, Object> cache = new ConcurrentHashMap<>();
	private boolean isStart = false;

	@Override
	public void run(String... args) throws Exception {
		//启动结束后开始缓存
		clear();
		isStart = true;
	}

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

	public void tryUpdateCache(String key, Object value) {
		if (isStart == false) {
			return;
		}
		if (cache.containsKey(key)) {
			if (value == null) {
				cache.put(key, PropertyUtil.NULL);
			} else {
				cache.put(key, value);
			}
		}
		Pubsub.Default.pub(EventEnum.PropertyCacheUpdateEvent.toString(), new HashMap(1) {
			{
				put(key, value);
			}
		});
	}

	public void listenUpdateCache(String name, Callable.Action1<HashMap<String, Object>> action) {
		Pubsub.Default.sub(EventEnum.PropertyCacheUpdateEvent, new Pubsub.Sub(name, action));
	}

	public void clear() {
		cache.clear();
	}
}
