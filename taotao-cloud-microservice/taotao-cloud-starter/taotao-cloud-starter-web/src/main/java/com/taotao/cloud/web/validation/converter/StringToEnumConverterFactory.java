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
package com.taotao.cloud.web.validation.converter;

import cn.hutool.core.map.MapUtil;
import com.taotao.cloud.common.enums.BaseEnum;
import java.util.Map;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;

/**
 * string枚举转化器工厂类
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 22:12:49
 */
public class StringToEnumConverterFactory implements ConverterFactory<String, BaseEnum> {

	/**
	 * CONVERTERS
	 */
	private static final Map<Class, Converter> CONVERTERS = MapUtil.newHashMap();

	@Override
	@SuppressWarnings("unchecked")
	public <T extends BaseEnum> Converter<String, T> getConverter(Class<T> targetType) {
		Converter<String, T> converter = CONVERTERS.get(targetType);
		if (converter == null) {
			converter = new StringToEnumConverter<>(targetType);
			CONVERTERS.put(targetType, converter);
		}
		return converter;
	}
}
