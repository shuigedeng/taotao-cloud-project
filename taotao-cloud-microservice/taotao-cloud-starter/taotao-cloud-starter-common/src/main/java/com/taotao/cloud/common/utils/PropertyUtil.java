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
package com.taotao.cloud.common.utils;

import com.taotao.cloud.common.model.Callable;
import com.taotao.cloud.common.model.PropertyCache;
import java.util.Map;
import java.util.Objects;

/**
 * PropertyUtil
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 20:51:35
 */
public class PropertyUtil {

	/**
	 * NULL
	 */
	public static String NULL = "<?NULL?>";

	/**
	 * eachProperty
	 *
	 * @param call call
	 * @author shuigedeng
	 * @since 2021-09-02 20:51:44
	 */
	public static void eachProperty(Callable.Action3<String, String, Object> call) {
		for (String key : System.getProperties().stringPropertyNames()) {
			call.invoke("properties", key, System.getProperty(key));
		}

		for (Map.Entry<String, String> kv : System.getenv().entrySet()) {
			call.invoke("env", kv.getKey(), kv.getValue());
		}
	}

	/**
	 * getProperty
	 *
	 * @param key          key
	 * @param defaultvalue defaultvalue
	 * @param <T>          T
	 * @return T
	 * @author shuigedeng
	 * @since 2021-09-02 20:51:48
	 */
	public static <T> T getProperty(String key, T defaultvalue) {
		String value = System.getProperty(key);
		if (value == null) {
			value = System.getenv(key);
		}
		if (value == null && ContextUtil.getApplicationContext() != null) {
			value = ContextUtil.getApplicationContext().getEnvironment().getProperty(key);
		}
		if (value == null) {
			return defaultvalue;
		}
		return (T) BeanUtil.convert(value, defaultvalue.getClass());
	}

	/**
	 * getProperty
	 *
	 * @param key key
	 * @return {@link java.lang.String }
	 * @author shuigedeng
	 * @since 2021-09-02 20:51:57
	 */
	public static String getProperty(String key) {
		String value = System.getProperty(key);
		if (value == null) {
			value = System.getenv(key);
		}

		if (value == null && ContextUtil.getApplicationContext() != null) {
			value = ContextUtil.getApplicationContext().getEnvironment().getProperty(key);
		}

		return value;
	}

	/**
	 * getEnvProperty
	 *
	 * @param key          key
	 * @param defaultvalue defaultvalue
	 * @param <T>          T
	 * @return T
	 * @author shuigedeng
	 * @since 2021-09-02 20:52:00
	 */
	public static <T> T getEnvProperty(String key, T defaultvalue) {
		String value = System.getenv(key);
		if (value == null) {
			return defaultvalue;
		} else {
			return (T) BeanUtil.convert(value, defaultvalue.getClass());
		}
	}

	/**
	 * getSystemProperty
	 *
	 * @param key          key
	 * @param defaultvalue defaultvalue
	 * @param <T>          T
	 * @return T
	 * @author shuigedeng
	 * @since 2021-09-02 20:52:08
	 */
	public static <T> T getSystemProperty(String key, T defaultvalue) {
		String value = System.getProperty(key);
		if (value == null) {
			return defaultvalue;
		} else {
			return (T) BeanUtil.convert(value, defaultvalue.getClass());
		}
	}

	/**
	 * setDefaultInitProperty
	 *
	 * @param cls           cls
	 * @param module        module
	 * @param key           key
	 * @param propertyValue propertyValue
	 * @author shuigedeng
	 * @since 2021-09-02 20:52:16
	 */
	public static void setDefaultInitProperty(Class<?> cls, String module, String key,
		String propertyValue) {
		setDefaultInitProperty(cls, module, key, propertyValue, "");
	}

	/**
	 * setDefaultInitProperty
	 *
	 * @param cls           cls
	 * @param module        module
	 * @param key           key
	 * @param propertyValue propertyValue
	 * @param message       message
	 * @author shuigedeng
	 * @since 2021-09-02 20:52:19
	 */
	public static void setDefaultInitProperty(Class<?> cls, String module, String key,
		String propertyValue, String message) {
		if (StringUtil.isEmpty(PropertyUtil.getPropertyCache(key, ""))) {
			if (!StringUtil.isEmpty(propertyValue)) {
				System.setProperty(key, propertyValue);
				PropertyCache propertyCache = ContextUtil.getBean(PropertyCache.class, false);
				if (Objects.nonNull(propertyCache)) {
					propertyCache.tryUpdateCache(key, propertyValue);
				}

				LogUtil.info(" set default init property key: {}, value: {}, message: {}",
					key, propertyValue, message);
			}
		} else {
			if (StringUtil.isEmpty(getSystemProperty(key, ""))) {
				System.setProperty(key,
					Objects.requireNonNull(PropertyUtil.getPropertyCache(key, "")));
			}
		}
	}

	public static void setProperty(String key, String propertyValue, String message) {
		System.setProperty(key, propertyValue);
		LogUtil.info(" set default init property key: {}, value: {}, message: {}",
			key, propertyValue, message);
	}

	/**
	 * getPropertyCache
	 *
	 * @param key          key
	 * @param defaultvalue defaultvalue
	 * @param <T>          T
	 * @return T
	 * @author shuigedeng
	 * @since 2021-09-02 20:52:22
	 */
	public static <T> T getPropertyCache(String key, T defaultvalue) {
		PropertyCache propertyCache = ContextUtil.getBean(PropertyCache.class, false);
		if (Objects.nonNull(propertyCache)) {
			return propertyCache.get(key, defaultvalue);
		}
		return null;
	}

}
