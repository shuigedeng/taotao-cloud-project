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

import com.taotao.cloud.common.enums.BaseEnum;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;

/**
 * Integer枚举转化器工厂类
 *
 * @author dengtao
 * @version 1.0.0
 * @since 2020/9/29 14:08
 */
public class IntegerToEnumConverterFactory implements ConverterFactory<Integer, BaseEnum> {

	private static final Map<Class, Converter> CONVERTERS = new ConcurrentHashMap<>();

	/**
	 * 获取一个从 Integer 转化为 T 的转换器，T 是一个泛型，有多个实现
	 *
	 * @param targetType 转换后的类型
	 * @return 返回一个转化器
	 */
	@Override
	public <T extends BaseEnum> Converter<Integer, T> getConverter(Class<T> targetType) {
		Converter<Integer, T> converter = CONVERTERS.get(targetType);
		if (converter == null) {
			converter = new IntegerToEnumConverter<>(targetType);
			CONVERTERS.put(targetType, converter);
		}
		return converter;
	}
}
