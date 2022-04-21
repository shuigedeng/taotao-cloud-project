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
package com.taotao.cloud.web.validation.converter;

import com.taotao.cloud.common.enums.BaseEnum;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;

/**
 * Integer枚举转化器工厂类
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 22:10:28
 */
public class IntegerToEnumConverterFactory implements ConverterFactory<Integer, BaseEnum> {

	/**
	 * CONVERTERS
	 */
	private static final Map<Class, Converter> CONVERTERS = new ConcurrentHashMap<>();

	/**
	 * 获取一个从 Integer 转化为 T 的转换器，T 是一个泛型，有多个实现
	 *
	 * @param targetType targetType  转换后的类型
	 * @return {@link org.springframework.core.convert.converter.Converter } 返回一个转化器
	 * @author shuigedeng
	 * @since 2021-09-02 22:10:37
	 */
	@Override
	@SuppressWarnings("unchecked")
	public <T extends BaseEnum> Converter<Integer, T> getConverter(Class<T> targetType) {
		Converter<Integer, T> converter = CONVERTERS.get(targetType);
		if (converter == null) {
			converter = new IntegerToEnumConverter<>(targetType);
			CONVERTERS.put(targetType, converter);
		}
		return converter;
	}
}
