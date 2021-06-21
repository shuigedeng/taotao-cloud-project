package com.taotao.cloud.common.base;

import com.taotao.cloud.common.enums.EventEnum;
import com.taotao.cloud.common.utils.BeanUtil;
import com.taotao.cloud.common.utils.PropertyUtil;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import lombok.val;
import lombok.var;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;

/**
 * @author: chejiangyi
 * @version: 2019-08-10 13:00 属性缓存层
 **/
@Order(200000000)
public class PropertyCache implements CommandLineRunner {

	public static PropertyCache Default = new PropertyCache();
	private ConcurrentHashMap<String, Object> cache = new ConcurrentHashMap<>();
	private boolean isStart = false;

	@Override
	public void run(String... args) throws Exception {
		//启动结束后开始缓存
		clear();
		isStart = true;
	}

	public <T> T get(String key, T defaultValue) {
		if (!isStart) {
			val v = PropertyUtil.getProperty(key);
			if (v == null) {
				return defaultValue;
			} else {
				return (T) BeanUtil.convert(v, defaultValue.getClass());
			}
		}
		var value = cache.get(key);
		if (value == null) {
			val v = PropertyUtil.getProperty(key);
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
