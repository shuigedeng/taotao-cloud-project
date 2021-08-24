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
package com.taotao.cloud.common.json;

import static com.taotao.cloud.common.utils.DateUtils.DEFAULT_DATE_FORMAT;
import static com.taotao.cloud.common.utils.DateUtils.DEFAULT_DATE_TIME_FORMAT;
import static com.taotao.cloud.common.utils.DateUtils.DEFAULT_TIME_FORMAT;

import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.PackageVersion;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import com.taotao.cloud.common.enums.BaseEnum;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * jackson 自定义序列化  反序列化 规则
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2020/5/2 16:49
 */
public class JacksonModule extends SimpleModule {

	public JacksonModule() {
		super(PackageVersion.VERSION);
		this.addDeserializer(LocalDateTime.class, LocalDateTimeDeserializer.INSTANCE);
		this.addDeserializer(LocalDate.class,
			new LocalDateDeserializer(DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT)));
		this.addDeserializer(LocalTime.class,
			new LocalTimeDeserializer(DateTimeFormatter.ofPattern(DEFAULT_TIME_FORMAT)));
		this.addSerializer(LocalDateTime.class,
			new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT)));
		this.addSerializer(LocalDate.class,
			new LocalDateSerializer(DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT)));
		this.addSerializer(LocalTime.class,
			new LocalTimeSerializer(DateTimeFormatter.ofPattern(DEFAULT_TIME_FORMAT)));
		this.addSerializer(Long.class, ToStringSerializer.instance);
		this.addSerializer(Long.TYPE, ToStringSerializer.instance);
		this.addSerializer(BigInteger.class, ToStringSerializer.instance);
		this.addSerializer(BigDecimal.class, ToStringSerializer.instance);
		this.addSerializer(BaseEnum.class, EnumSerializer.INSTANCE);
	}

}
