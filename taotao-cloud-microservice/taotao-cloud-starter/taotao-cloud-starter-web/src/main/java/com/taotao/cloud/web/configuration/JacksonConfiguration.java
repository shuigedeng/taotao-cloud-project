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
package com.taotao.cloud.web.configuration;

import static com.taotao.cloud.common.utils.date.DateUtil.DEFAULT_DATE_FORMAT;
import static com.taotao.cloud.common.utils.date.DateUtil.DEFAULT_DATE_TIME_FORMAT;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import com.taotao.cloud.common.constant.CommonConstant;
import com.taotao.cloud.common.constant.StarterName;
import com.taotao.cloud.common.support.json.JacksonModule;
import com.taotao.cloud.common.support.json.LocalDateTimeDeserializer;
import com.taotao.cloud.common.utils.date.DateUtil;
import com.taotao.cloud.common.utils.log.LogUtil;
import com.taotao.cloud.web.properties.XssProperties;
import com.taotao.cloud.web.xss.XssStringJsonDeserializer;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.TimeZone;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * LimitConfiguration
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 21:28:08
 */
@Configuration
@EnableConfigurationProperties({XssProperties.class})
public class JacksonConfiguration implements InitializingBean {

	@Override
	public void afterPropertiesSet() throws Exception {
		LogUtil.started(JacksonConfiguration.class, StarterName.WEB_STARTER);
	}

	@Bean
	public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer(
		XssProperties xssProperties) {

		return customizer -> {
			ObjectMapper mapper = customizer.createXmlMapper(true).build();

			mapper.findAndRegisterModules();
			mapper.setLocale(Locale.CHINA);
			// 时区
			mapper.setTimeZone(TimeZone.getTimeZone(ZoneId.systemDefault()));
			//去掉默认的时间戳格式
			mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
			// 忽略在json字符串中存在，但是在java对象中不存在对应属性的情况
			//忽略未知字段
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			// 忽略空Bean转json的错误
			//在使用spring boot + jpa/hibernate，如果实体字段上加有FetchType.LAZY，并使用jackson序列化为json串时，会遇到SerializationFeature.FAIL_ON_EMPTY_BEANS异常
			mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
			// 允许不带引号的字段名称
			mapper.configure(JsonReadFeature.ALLOW_UNQUOTED_FIELD_NAMES.mappedFeature(), true);
			// 允许单引号
			mapper.configure(JsonReadFeature.ALLOW_SINGLE_QUOTES.mappedFeature(), true);
			// allow int startWith 0
			mapper.configure(JsonReadFeature.ALLOW_LEADING_ZEROS_FOR_NUMBERS.mappedFeature(), true);
			// 允许字符串存在转义字符：\r \n \t
			//该特性决定parser是否允许JSON字符串包含非引号控制字符（值小于32的ASCII字符，包含制表符和换行符）。 如果该属性关闭，则如果遇到这些字符，则会抛出异常。JSON标准说明书要求所有控制符必须使用引号，因此这是一个非标准的特性
			mapper.configure(JsonReadFeature.ALLOW_UNESCAPED_CONTROL_CHARS.mappedFeature(), true);
			// 忽略不能转义的字符
			mapper.configure(JsonReadFeature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER.mappedFeature(),
				true);
			// 包含null
			mapper.setSerializationInclusion(Include.ALWAYS);
			// 使用驼峰式
			mapper.setPropertyNamingStrategy(PropertyNamingStrategies.LOWER_CAMEL_CASE);
			// 使用bean名称
			//mapper.enable(MapperFeature.USE_STD_BEAN_NAMING);
			// 所有日期格式都统一为固定格式
			mapper.setDateFormat(
				new SimpleDateFormat(CommonConstant.DATETIME_FORMAT, Locale.CHINA));
			mapper.registerModule(new Jdk8Module());

			// 注册自定义模块
			mapper.registerModule(new JacksonModule());

			customizer.configure(mapper);

			customizer.serializerByType(LocalDateTime.class,
				new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT)));
			customizer.deserializerByType(LocalDateTime.class, new LocalDateTimeDeserializer(
				DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT)));

			customizer.deserializerByType(LocalDate.class,
				new LocalDateDeserializer(DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT)));
			customizer.serializerByType(LocalDate.class,
				new LocalDateSerializer(DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT)));

			customizer.deserializerByType(LocalTime.class, new LocalTimeDeserializer(
				DateTimeFormatter.ofPattern(DateUtil.DEFAULT_TIME_FORMAT)));
			customizer.serializerByType(LocalTime.class,
				new LocalTimeSerializer(DateTimeFormatter.ofPattern(DateUtil.DEFAULT_TIME_FORMAT)));

			customizer.failOnEmptyBeans(false);
			customizer.failOnUnknownProperties(false);

			// 配置跨站攻击 反序列化处理器
			if (xssProperties.getRequestBodyEnabled()) {
				customizer.deserializerByType(String.class, new XssStringJsonDeserializer());
			}
		};
	}
}
