/*
 * Copyright 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
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
package com.taotao.cloud.common.utils.common;

import cn.hutool.core.util.ArrayUtil;
import java.util.HashMap;
import java.util.Map;

/**
 * sql过滤
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 17:50:31
 */
public final class AntiSqlFilterUtil {

	private AntiSqlFilterUtil() {
	}

	/**
	 * KEY_WORDS
	 */
	private static final String[] KEY_WORDS = {";", "\"", "'", "/*", "*/", "--", "exec",
		"select", "update", "delete", "insert", "alter", "drop", "create", "shutdown"};

	/**
	 * getSafeParameterMap
	 *
	 * @param parameterMap parameterMap
	 * @return {@link java.util.Map }
	 * @since 2021-09-02 17:50:43
	 */
	public static Map<String, String[]> getSafeParameterMap(Map<String, String[]> parameterMap) {
		Map<String, String[]> map = new HashMap<>(parameterMap.size());
		for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
			String[] oldValues = entry.getValue();
			map.put(entry.getKey(), getSafeValues(oldValues));
		}
		return map;
	}

	/**
	 * getSafeValues
	 *
	 * @param oldValues oldValues
	 * @return java.lang.String[]
	 * @since 2021-09-02 17:50:50
	 */
	public static String[] getSafeValues(String[] oldValues) {
		if (ArrayUtil.isNotEmpty(oldValues)) {
			String[] newValues = new String[oldValues.length];
			for (int i = 0; i < oldValues.length; i++) {
				newValues[i] = getSafeValue(oldValues[i]);
			}
			return newValues;
		}
		return null;
	}

	/**
	 * getSafeValue
	 *
	 * @param oldValue oldValue
	 * @return {@link java.lang.String }
	 * @since 2021-09-02 17:51:00
	 */
	public static String getSafeValue(String oldValue) {
		if (oldValue == null || "".equals(oldValue)) {
			return oldValue;
		}
		StringBuilder sb = new StringBuilder(oldValue);
		String lowerCase = oldValue.toLowerCase();
		for (String keyWord : KEY_WORDS) {
			int x;
			while ((x = lowerCase.indexOf(keyWord)) >= 0) {
				if (keyWord.length() == 1) {
					sb.replace(x, x + 1, " ");
					lowerCase = sb.toString().toLowerCase();
					continue;
				}
				sb.delete(x, x + keyWord.length());
				lowerCase = sb.toString().toLowerCase();
			}
		}
		return sb.toString();
	}

}
