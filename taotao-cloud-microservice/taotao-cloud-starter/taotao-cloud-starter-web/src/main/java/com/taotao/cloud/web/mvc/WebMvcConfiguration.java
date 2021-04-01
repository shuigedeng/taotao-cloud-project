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
package com.taotao.cloud.web.mvc;

import com.taotao.cloud.core.mvc.converter.IntegerToEnumConverterFactory;
import com.taotao.cloud.core.mvc.converter.StringToEnumConverterFactory;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 自定义mvc配置
 *
 * @author dengtao
 * @version 1.0.0
 * @since 2020/9/29 14:30
 */
public class WebMvcConfiguration implements WebMvcConfigurer {

	/**
	 * 枚举类的转换器工厂 addConverterFactory
	 */
	@Override
	public void addFormatters(FormatterRegistry registry) {
		registry.addConverterFactory(new IntegerToEnumConverterFactory());
		registry.addConverterFactory(new StringToEnumConverterFactory());
	}

//	@Override
//	public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
//		Oauth2HttpMessageConverter oauth2HttpMessageConverter = new Oauth2HttpMessageConverter();
//		converters.add(0, oauth2HttpMessageConverter);
//	}
}
