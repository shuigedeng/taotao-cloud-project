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

import cn.hutool.core.util.ArrayUtil;
import java.util.HashMap;
import java.util.Map;

/**
 * sql过滤
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2020/4/30 10:29
 */
public final class AntiSqlFilterUtils {

	private AntiSqlFilterUtils() {
	}

	private static final String[] KEY_WORDS = {";", "\"", "'", "/*", "*/", "--", "exec",
		"select", "update", "delete", "insert", "alter", "drop", "create", "shutdown"};

	public static Map<String, String[]> getSafeParameterMap(Map<String, String[]> parameterMap) {
		Map<String, String[]> map = new HashMap<>(parameterMap.size());
		for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
			String[] oldValues = entry.getValue();
			map.put(entry.getKey(), getSafeValues(oldValues));
		}
		return map;
	}

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
