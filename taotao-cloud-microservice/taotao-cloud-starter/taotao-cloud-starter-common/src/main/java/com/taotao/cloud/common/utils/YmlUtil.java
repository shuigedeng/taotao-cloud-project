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

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.yaml.snakeyaml.Yaml;

/**
 * YmlUtil
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2021/8/25 20:24
 */
public class YmlUtil {

	private YmlUtil() {
	}

	private static final String BOOTSTRAP_YML = "bootstrap.yml";
	private static final String APPLICATION_YML = "application.yml";

	/**
	 * 获取bootstrap.yml配置内容
	 *
	 * @param key 键
	 * @return 值
	 */
	public static Object getBootstrapValue(String key) {
		return getValueByYml(BOOTSTRAP_YML, key);
	}

	/**
	 * 获取application.yml配置内容
	 *
	 * @param key 键
	 * @return 值
	 */
	public static Object getApplicationValue(String key) {
		return getValueByYml(APPLICATION_YML, key);
	}

	/**
	 * 获取yml配置内容
	 *
	 * @param fileName yml文件名
	 * @param key      键
	 * @return 值
	 */
	public static Object getValueByYml(String fileName, String key) {
		Map<String, Object> map = getYml(fileName);
		if (MapUtils.isEmpty(map)) {
			return null;
		}

		Object result = null;
		String[] keys = key.split("\\.");
		for (String k : keys) {
			Object o = map.get(k);
			if (ObjectUtils.isNotEmpty(o)) {
				if (o instanceof Map && !k.equals(key) && !key.endsWith("." + k)) {
					map = (Map<String, Object>) o;
				} else {
					result = o;
				}
			} else {
				result = null;
			}
		}

		return result;
	}

	/**
	 * 获取 yml 文件内容
	 *
	 * @param fileName yml文件名
	 * @return
	 */
	public static Map<String, Object> getYml(String fileName) {
		if (StringUtils.isBlank(fileName)) {
			return null;
		}
		try (InputStream inputStream = YmlUtil.class.getClassLoader()
			.getResourceAsStream(fileName)) {
			if (ObjectUtils.isEmpty(inputStream)) {
				return null;
			}
			Yaml yaml = new Yaml();
			return yaml.loadAs(inputStream, Map.class);
		} catch (IOException e) {
			LogUtil.error("IO流处理失败", e);
		}
		return null;
	}

}
