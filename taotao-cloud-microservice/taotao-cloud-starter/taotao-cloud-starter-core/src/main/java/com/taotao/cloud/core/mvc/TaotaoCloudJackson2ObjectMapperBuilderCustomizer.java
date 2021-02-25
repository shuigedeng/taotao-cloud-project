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
package com.taotao.cloud.core.mvc;

import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.taotao.cloud.common.constant.CommonConstant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;

/**
 * 自定义LocalDateTime -> jackson
 *
 * @author dengtao
 * @version 1.0.0
 * @since 2020/9/29 11:18
 */
public class TaotaoCloudJackson2ObjectMapperBuilderCustomizer {

	@Bean
	public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
		return customizer -> {
			customizer.serializerByType(LocalDateTime.class, new LocalDateTimeSerializer(
				DateTimeFormatter.ofPattern(CommonConstant.DATETIME_FORMAT)));
			customizer.deserializerByType(LocalDateTime.class, new LocalDateTimeDeserializer(
				DateTimeFormatter.ofPattern(CommonConstant.DATETIME_FORMAT)));
		};
	}
}
