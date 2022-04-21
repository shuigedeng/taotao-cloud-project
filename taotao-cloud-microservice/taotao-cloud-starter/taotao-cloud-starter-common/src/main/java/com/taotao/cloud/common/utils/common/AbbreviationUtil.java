/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
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


import com.taotao.cloud.common.utils.lang.StringUtil;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 缩写工具类 1. 需要添加自定义支持 2. 或者提供对应的 filter
 */
public final class AbbreviationUtil {

	/**
	 * 缩写util
	 */
	private AbbreviationUtil() {
	}

	/**
	 * map
	 */
	private static final Map<String, String> MAP = new ConcurrentHashMap<>();

	static {
		MAP.put("impl", "implements");
		MAP.put("msg", "message");
		MAP.put("err", "error");
		MAP.put("e", "exception");
		MAP.put("ex", "exception");
		MAP.put("doc", "document");
		MAP.put("val", "value");
		MAP.put("num", "number");

		MAP.put("vo", "value object");
		MAP.put("dto", "data transfer object");

		MAP.put("gen", "generate");
		MAP.put("dir", "directory");
		MAP.put("init", "initialize");
		MAP.put("cfg", "config");
		MAP.put("arg", "argument");
		MAP.put("args", "arguments");
	}


	/**
	 * 设置
	 *
	 * @param shortName 简称
	 * @param fullName  全程
	 */
	public static void set(final String shortName, final String fullName) {
		MAP.put(shortName, fullName);
	}

	/**
	 * 获取
	 *
	 * @param shortName 简称
	 * @return 全称
	 */
	public static String get(final String shortName) {
		return MAP.get(shortName);
	}

	/**
	 * 获取并提供默认值
	 *
	 * @param shortName    简称
	 * @param defaultValue 默认值
	 * @return 对应的全称
	 */
	public static String getOrDefault(final String shortName, final String defaultValue) {
		String value = MAP.get(shortName);

		if (StringUtil.isEmpty(value)) {
			return defaultValue;
		}
		return value;
	}


}
