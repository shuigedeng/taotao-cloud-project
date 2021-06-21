package com.taotao.cloud.common.utils;

import com.taotao.cloud.common.base.Callable;
import com.taotao.cloud.common.base.PropertyCache;
import lombok.val;
import org.apache.logging.log4j.util.Strings;

/**
 * @author: chejiangyi
 * @version: 2019-06-15 10:04
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

	public static Object getProperty(String key) {
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
				LogUtil.info(module, "设置" + key + "=" + propertyValue + " " + message);
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
