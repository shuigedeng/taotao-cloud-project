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

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.taotao.cloud.common.constant.CommonConstant;
import com.taotao.cloud.common.exception.BaseException;
import com.taotao.cloud.common.json.JacksonModule;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

/**
 * 基于 Jackson 的 json 工具类
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 16:37:15
 */
public class JsonUtil {

	private JsonUtil() {
	}

	/**
	 * MAPPER
	 */
	public static final ObjectMapper MAPPER = new ObjectMapper();

	static {
		MAPPER.findAndRegisterModules();
		MAPPER.setLocale(Locale.CHINA);
		// 时区
		MAPPER.setTimeZone(TimeZone.getTimeZone(ZoneId.systemDefault()));
		//去掉默认的时间戳格式
		MAPPER.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		// 忽略在json字符串中存在，但是在java对象中不存在对应属性的情况
		//忽略未知字段
		MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		// 忽略空Bean转json的错误
		//在使用spring boot + jpa/hibernate，如果实体字段上加有FetchType.LAZY，并使用jackson序列化为json串时，会遇到SerializationFeature.FAIL_ON_EMPTY_BEANS异常
		MAPPER.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		// 允许不带引号的字段名称
		MAPPER.configure(JsonReadFeature.ALLOW_UNQUOTED_FIELD_NAMES.mappedFeature(), true);
		// 允许单引号
		MAPPER.configure(JsonReadFeature.ALLOW_SINGLE_QUOTES.mappedFeature(), true);
		// allow int startWith 0
		MAPPER.configure(JsonReadFeature.ALLOW_LEADING_ZEROS_FOR_NUMBERS.mappedFeature(), true);
		// 允许字符串存在转义字符：\r \n \t
		//该特性决定parser是否允许JSON字符串包含非引号控制字符（值小于32的ASCII字符，包含制表符和换行符）。 如果该属性关闭，则如果遇到这些字符，则会抛出异常。JSON标准说明书要求所有控制符必须使用引号，因此这是一个非标准的特性
		MAPPER.configure(JsonReadFeature.ALLOW_UNESCAPED_CONTROL_CHARS.mappedFeature(), true);
		// 忽略不能转义的字符
		MAPPER.configure(JsonReadFeature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER.mappedFeature(),
			true);
		// 包含null
		MAPPER.setSerializationInclusion(Include.ALWAYS);
		// 使用驼峰式
		MAPPER.setPropertyNamingStrategy(PropertyNamingStrategy.LOWER_CAMEL_CASE);
		// 使用bean名称
		MAPPER.enable(MapperFeature.USE_STD_BEAN_NAMING);
		// 所有日期格式都统一为固定格式
		MAPPER.setDateFormat(new SimpleDateFormat(CommonConstant.DATETIME_FORMAT, Locale.CHINA));
		MAPPER.registerModule(new Jdk8Module());
		MAPPER.registerModule(new JavaTimeModule());

		// 注册自定义模块
		MAPPER.registerModule(new JacksonModule());
	}

	/**
	 * 对象转换为json字符串
	 *
	 * @param object 要转换的对象
	 * @return {@link java.lang.String }
	 * @author shuigedeng
	 * @since 2021-09-02 16:37:25
	 */
	public static String toJSONString(Object object) {
		return toJSONString(object, false);
	}

	/**
	 * 对象转换为json字符串
	 *
	 * @param object 要转换的对象
	 * @param format 是否格式化json
	 * @return {@link java.lang.String }
	 * @author shuigedeng
	 * @since 2021-09-02 16:37:57
	 */
	public static String toJSONString(Object object, boolean format) {
		try {
			if (object == null) {
				return "";
			}
			if (object instanceof Number) {
				return object.toString();
			}
			if (object instanceof String) {
				return (String) object;
			}
			if (format) {
				return MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(object);
			}
			return MAPPER.writeValueAsString(object);
		} catch (JsonProcessingException e) {
			throw new BaseException(e.getMessage());
		}
	}

	/**
	 * 字符串转换为指定对象
	 *
	 * @param json json字符串
	 * @param cls  目标对象
	 * @return T
	 * @author shuigedeng
	 * @since 2021-09-02 16:38:13
	 */
	public static <T> T toObject(String json, Class<T> cls) {
		if (StrUtil.isBlank(json) || cls == null) {
			return null;
		}
		try {
			return MAPPER.readValue(json, cls);
		} catch (IOException e) {
			throw new BaseException(e);
		}
	}

	/**
	 * 字符串转换为指定对象，并增加泛型转义 例如：List<Integer> test = toObject(jsonStr, List.class, Integer.class);
	 *
	 * @param json             json字符串
	 * @param parametrized     目标对象
	 * @param parameterClasses 泛型对象
	 * @return T
	 * @author shuigedeng
	 * @since 2021-09-02 16:38:36
	 */
	public static <T> T toObject(String json, Class<?> parametrized, Class<?>... parameterClasses) {
		if (StrUtil.isBlank(json) || parametrized == null) {
			return null;
		}
		try {
			JavaType javaType = MAPPER.getTypeFactory()
				.constructParametricType(parametrized, parameterClasses);
			return MAPPER.readValue(json, javaType);
		} catch (IOException e) {
			throw new BaseException(e);
		}
	}

	/**
	 * 字符串转换为指定对象
	 *
	 * @param json          json字符串
	 * @param typeReference 目标对象类型
	 * @return T
	 * @author shuigedeng
	 * @since 2021-09-02 16:38:49
	 */
	public static <T> T toObject(String json, TypeReference<T> typeReference) {
		if (StrUtil.isBlank(json) || typeReference == null) {
			return null;
		}
		try {
			return MAPPER.readValue(json, typeReference);
		} catch (IOException e) {
			throw new BaseException(e);
		}
	}

	/**
	 * 字符串转换为JsonNode对象
	 *
	 * @param json json字符串
	 * @return {@link com.fasterxml.jackson.databind.JsonNode }
	 * @author shuigedeng
	 * @since 2021-09-02 16:39:01
	 */
	public static JsonNode parse(String json) {
		if (StrUtil.isBlank(json)) {
			return null;
		}
		try {
			return MAPPER.readTree(json);
		} catch (IOException e) {
			throw new BaseException(e);
		}
	}

	/**
	 * 对象转换为map对象
	 *
	 * @param object 要转换的对象
	 * @return {@link java.util.Map }
	 * @author shuigedeng
	 * @since 2021-09-02 16:39:09
	 */
	public static <K, V> Map<K, V> toMap(Object object) {
		if (object == null) {
			return null;
		}
		if (object instanceof String) {
			return toObject((String) object, Map.class);
		}
		return MAPPER.convertValue(object, Map.class);
	}

	/**
	 * json字符串转换为list对象
	 *
	 * @param json json字符串
	 * @return {@link java.util.List }
	 * @author shuigedeng
	 * @since 2021-09-02 16:39:18
	 */
	public static <T> List<T> toList(String json) {
		if (StrUtil.isNotBlank(json)) {
			try {
				return MAPPER.readValue(json, List.class);
			} catch (JsonProcessingException e) {
				throw new BaseException(e.getMessage());
			}
		}
		return new ArrayList<>();
	}

	/**
	 * json字符串转换为list对象，并指定元素类型
	 *
	 * @param json json字符串
	 * @param cls  list的元素类型
	 * @return {@link java.util.List }
	 * @author shuigedeng
	 * @since 2021-09-02 16:39:29
	 */
	public static <T> List<T> toList(String json, Class<T> cls) {
		if (StrUtil.isBlank(json)) {
			return new ArrayList<>();
		}
		try {
			JavaType javaType = MAPPER.getTypeFactory().constructParametricType(List.class, cls);
			return MAPPER.readValue(json, javaType);
		} catch (JsonProcessingException e) {
			throw new BaseException(e.getMessage());
		}
	}
}
