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

import com.taotao.cloud.common.base.Callable;
import com.taotao.cloud.common.base.PropertyCache;
import lombok.val;
import org.apache.logging.log4j.util.Strings;

/**
 * PropertyUtil
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2020/6/2 16:38
 **/
public class PropertyUtil {

	public static String NULL = "<?NULL?>";

	public static void eachProperty(Callable.Action3<String, String, Object> call) {
		for (val key : System.getProperties().stringPropertyNames()) {
			call.invoke("properties", key, System.getProperty(key));
		}
		for (val kv : System.getenv().entrySet()) {
			call.invoke("env", kv.getKey(), kv.getValue());
		}
	}

	private static <T> T getProperty(String key, T defaultvalue) {
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

	public static <T> T getEnvProperty(String key, T defaultvalue) {
		String value = System.getenv(key);
		if (value == null) {
			return defaultvalue;
		} else {
			return (T) BeanUtil.convert(value, defaultvalue.getClass());
		}
	}


	public static <T> T getSystemProperty(String key, T defaultvalue) {
		String value = System.getProperty(key);
		if (value == null) {
			return defaultvalue;
		} else {
			return (T) BeanUtil.convert(value, defaultvalue.getClass());
		}
	}

	public static void setDefaultInitProperty(Class cls, String module, String key,
		String propertyValue) {
		setDefaultInitProperty(cls, module, key, propertyValue, "");
	}

	public static void setDefaultInitProperty(Class cls, String module, String key,
		String propertyValue, String message) {
		if (Strings.isEmpty(PropertyUtil.getPropertyCache(key, ""))) {
			if (!Strings.isEmpty(propertyValue)) {
				System.setProperty(key, propertyValue);
				PropertyCache.Default.tryUpdateCache(key, propertyValue);

				LogUtil
					.info(module + " set default init property key: {0}, value: {1}, message: {2}",
						key, propertyValue, message);
			}
		} else {
			if (Strings.isEmpty(getSystemProperty(key, ""))) {
				System.setProperty(key, PropertyUtil.getPropertyCache(key, ""));
			}
		}
	}

	public static <T> T getPropertyCache(String key, T defaultvalue) {
		return PropertyCache.Default.get(key, defaultvalue);
	}

}
