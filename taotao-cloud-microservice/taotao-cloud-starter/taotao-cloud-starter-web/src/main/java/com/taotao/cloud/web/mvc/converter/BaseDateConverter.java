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
package com.taotao.cloud.web.mvc.converter;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;

/**
 * 解决入参为 Date类型
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2021/8/24 23:25
 */
public abstract class BaseDateConverter<T> {

	/**
	 * 值转换
	 *
	 * @param source   源数据
	 * @param function 回调
	 * @return T 转换后的数据
	 * @author shuigedeng
	 * @since 2021/8/24 23:26
	 */
	public T convert(String source, Function<String, T> function) {
		if (source == null || source.isEmpty()) {
			return null;
		}
		String sourceTrim = source.trim();
		Set<Map.Entry<String, String>> entries = getFormat().entrySet();
		for (Map.Entry<String, String> entry : entries) {
			if (sourceTrim.matches(entry.getValue())) {
				return function.apply(entry.getKey());
			}
		}
		throw new IllegalArgumentException("无效的日期参数格式:'" + sourceTrim + "'");
	}

	/**
	 * 获取子类 具体的格式化表达式
	 *
	 * @return java.util.Map<java.lang.String, java.lang.String> 格式化
	 * @author shuigedeng
	 * @since 2021/8/24 23:26
	 */
	protected abstract Map<String, String> getFormat();
}
