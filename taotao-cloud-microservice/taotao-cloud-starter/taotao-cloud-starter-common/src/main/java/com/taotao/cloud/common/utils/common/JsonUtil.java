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
package com.taotao.cloud.common.utils.common;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.type.CollectionLikeType;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.taotao.cloud.common.constant.CommonConstant;
import com.taotao.cloud.common.constant.PunctuationConst;
import com.taotao.cloud.common.exception.BaseException;
import com.taotao.cloud.common.support.function.CheckedConsumer;
import com.taotao.cloud.common.support.json.JacksonModule;
import com.taotao.cloud.common.utils.collection.CollectionUtil;
import com.taotao.cloud.common.utils.date.DateUtil;
import com.taotao.cloud.common.utils.exception.ExceptionUtil;
import com.taotao.cloud.common.utils.guava.Guavas;
import com.taotao.cloud.common.utils.io.FileUtil;
import com.taotao.cloud.common.utils.lang.ObjectUtil;
import com.taotao.cloud.common.utils.lang.StringUtil;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Stack;
import java.util.TimeZone;
import javax.annotation.Nullable;

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
		MAPPER.setPropertyNamingStrategy(PropertyNamingStrategies.LOWER_CAMEL_CASE);
		// 使用bean名称
		//MAPPER.enable(MapperFeature.USE_STD_BEAN_NAMING);
		// 所有日期格式都统一为固定格式
		MAPPER.setDateFormat(new SimpleDateFormat(CommonConstant.DATETIME_FORMAT, Locale.CHINA));
		MAPPER.registerModule(new Jdk8Module());

		// 注册自定义模块
		MAPPER.registerModule(new JacksonModule());
	}

	/**
	 * 对象转换为json字符串
	 *
	 * @param object 要转换的对象
	 * @return {@link java.lang.String }
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
	 * @since 2021-09-02 16:38:13
	 */
	@SuppressWarnings("unchecked")
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
	 * @since 2021-09-02 16:39:09
	 */
	@SuppressWarnings("unchecked")
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
	 * @since 2021-09-02 16:39:18
	 */
	@SuppressWarnings("unchecked")
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
	/**
	 * 将对象序列化成json字符串
	 *
	 * @param object javaBean
	 * @return jsonString json字符串
	 */
	@Nullable
	public static String toJson(@Nullable Object object) {
		if (object == null) {
			return null;
		}
		try {
			return getInstance().writeValueAsString(object);
		} catch (JsonProcessingException e) {
			throw ExceptionUtil.unchecked(e);
		}
	}

	/**
	 * 将对象序列化成 json byte 数组
	 *
	 * @param object javaBean
	 * @return jsonString json字符串
	 */
	public static byte[] toJsonAsBytes(@Nullable Object object) {
		if (object == null) {
			return new byte[0];
		}
		try {
			return getInstance().writeValueAsBytes(object);
		} catch (JsonProcessingException e) {
			throw ExceptionUtil.unchecked(e);
		}
	}

	/**
	 * 将json字符串转成 JsonNode
	 *
	 * @param jsonString jsonString
	 * @return jsonString json字符串
	 */
	public static JsonNode readTree(String jsonString) {
		Objects.requireNonNull(jsonString, "jsonString is null");
		try {
			return getInstance().readTree(jsonString);
		} catch (IOException e) {
			throw ExceptionUtil.unchecked(e);
		}
	}

	/**
	 * 将json字符串转成 JsonNode
	 *
	 * @param in InputStream
	 * @return jsonString json字符串
	 */
	public static JsonNode readTree(InputStream in) {
		Objects.requireNonNull(in, "InputStream in is null");
		try {
			return getInstance().readTree(in);
		} catch (IOException e) {
			throw ExceptionUtil.unchecked(e);
		}
	}

	/**
	 * 将json字符串转成 JsonNode
	 *
	 * @param content content
	 * @return jsonString json字符串
	 */
	public static JsonNode readTree(byte[] content) {
		Objects.requireNonNull(content, "byte[] content is null");
		try {
			return getInstance().readTree(content);
		} catch (IOException e) {
			throw ExceptionUtil.unchecked(e);
		}
	}

	/**
	 * 将json字符串转成 JsonNode
	 *
	 * @param jsonParser JsonParser
	 * @return jsonString json字符串
	 */
	public static JsonNode readTree(JsonParser jsonParser) {
		Objects.requireNonNull(jsonParser, "jsonParser is null");
		try {
			return getInstance().readTree(jsonParser);
		} catch (IOException e) {
			throw ExceptionUtil.unchecked(e);
		}
	}

	/**
	 * 将json byte 数组反序列化成对象
	 *
	 * @param content   json bytes
	 * @param valueType class
	 * @param <T>       T 泛型标记
	 * @return Bean
	 */
	@Nullable
	public static <T> T readValue(@Nullable byte[] content, Class<T> valueType) {
		if (ObjectUtil.isEmpty(content)) {
			return null;
		}
		try {
			return getInstance().readValue(content, valueType);
		} catch (IOException e) {
			throw ExceptionUtil.unchecked(e);
		}
	}

	/**
	 * 将json反序列化成对象
	 *
	 * @param jsonString jsonString
	 * @param valueType  class
	 * @param <T>        T 泛型标记
	 * @return Bean
	 */
	@Nullable
	public static <T> T readValue(@Nullable String jsonString, Class<T> valueType) {
		if (StringUtil.isBlank(jsonString)) {
			return null;
		}
		try {
			return getInstance().readValue(jsonString, valueType);
		} catch (IOException e) {
			throw ExceptionUtil.unchecked(e);
		}
	}

	/**
	 * 将json反序列化成对象
	 *
	 * @param in        InputStream
	 * @param valueType class
	 * @param <T>       T 泛型标记
	 * @return Bean
	 */
	@Nullable
	public static <T> T readValue(@Nullable InputStream in, Class<T> valueType) {
		if (in == null) {
			return null;
		}
		try {
			return getInstance().readValue(in, valueType);
		} catch (IOException e) {
			throw ExceptionUtil.unchecked(e);
		}
	}

	/**
	 * 将json反序列化成对象
	 *
	 * @param content       bytes
	 * @param typeReference 泛型类型
	 * @param <T>           T 泛型标记
	 * @return Bean
	 */
	@Nullable
	public static <T> T readValue(@Nullable byte[] content, TypeReference<T> typeReference) {
		if (ObjectUtil.isEmpty(content)) {
			return null;
		}
		try {
			return getInstance().readValue(content, typeReference);
		} catch (IOException e) {
			throw ExceptionUtil.unchecked(e);
		}
	}

	/**
	 * 将json反序列化成对象
	 *
	 * @param jsonString    jsonString
	 * @param typeReference 泛型类型
	 * @param <T>           T 泛型标记
	 * @return Bean
	 */
	@Nullable
	public static <T> T readValue(@Nullable String jsonString, TypeReference<T> typeReference) {
		if (StringUtil.isBlank(jsonString)) {
			return null;
		}
		try {
			return getInstance().readValue(jsonString, typeReference);
		} catch (IOException e) {
			throw ExceptionUtil.unchecked(e);
		}
	}

	/**
	 * 将json反序列化成对象
	 *
	 * @param in            InputStream
	 * @param typeReference 泛型类型
	 * @param <T>           T 泛型标记
	 * @return Bean
	 */
	@Nullable
	public static <T> T readValue(@Nullable InputStream in, TypeReference<T> typeReference) {
		if (in == null) {
			return null;
		}
		try {
			return getInstance().readValue(in, typeReference);
		} catch (IOException e) {
			throw ExceptionUtil.unchecked(e);
		}
	}

	/**
	 * 将json反序列化成对象
	 *
	 * @param content  bytes
	 * @param javaType JavaType
	 * @param <T>      T 泛型标记
	 * @return Bean
	 */
	@Nullable
	public static <T> T readValue(@Nullable byte[] content, JavaType javaType) {
		if (ObjectUtil.isEmpty(content)) {
			return null;
		}
		try {
			return getInstance().readValue(content, javaType);
		} catch (IOException e) {
			throw ExceptionUtil.unchecked(e);
		}
	}

	/**
	 * 将json反序列化成对象
	 *
	 * @param jsonString jsonString
	 * @param javaType   JavaType
	 * @param <T>        T 泛型标记
	 * @return Bean
	 */
	@Nullable
	public static <T> T readValue(@Nullable String jsonString, JavaType javaType) {
		if (StringUtil.isBlank(jsonString)) {
			return null;
		}
		try {
			return getInstance().readValue(jsonString, javaType);
		} catch (IOException e) {
			throw ExceptionUtil.unchecked(e);
		}
	}

	/**
	 * 将json反序列化成对象
	 *
	 * @param in       InputStream
	 * @param javaType JavaType
	 * @param <T>      T 泛型标记
	 * @return Bean
	 */
	@Nullable
	public static <T> T readValue(@Nullable InputStream in, JavaType javaType) {
		if (in == null) {
			return null;
		}
		try {
			return getInstance().readValue(in, javaType);
		} catch (IOException e) {
			throw ExceptionUtil.unchecked(e);
		}
	}

	/**
	 * 封装 map type，keyClass String
	 *
	 * @param valueClass value 类型
	 * @return MapType
	 */
	public static MapType getMapType(Class<?> valueClass) {
		return getMapType(String.class, valueClass);
	}

	/**
	 * 封装 map type
	 *
	 * @param keyClass   key 类型
	 * @param valueClass value 类型
	 * @return MapType
	 */
	public static MapType getMapType(Class<?> keyClass, Class<?> valueClass) {
		return getInstance().getTypeFactory().constructMapType(Map.class, keyClass, valueClass);
	}

	/**
	 * 封装 map type
	 *
	 * @param elementClass 集合值类型
	 * @return CollectionLikeType
	 */
	public static CollectionLikeType getListType(Class<?> elementClass) {
		return getInstance().getTypeFactory().constructCollectionLikeType(List.class, elementClass);
	}

	/**
	 * 封装参数化类型
	 *
	 * <p>
	 * 例如： Map.class, String.class, String.class 对应 Map[String, String]
	 * </p>
	 *
	 * @param parametrized     泛型参数化
	 * @param parameterClasses 泛型参数类型
	 * @return JavaType
	 */
	public static JavaType getParametricType(Class<?> parametrized, Class<?>... parameterClasses) {
		return getInstance().getTypeFactory().constructParametricType(parametrized, parameterClasses);
	}

	/**
	 * 读取集合
	 *
	 * @param content      bytes
	 * @param elementClass elementClass
	 * @param <T>          泛型
	 * @return 集合
	 */
	public static <T> List<T> readList(@Nullable byte[] content, Class<T> elementClass) {
		if (ObjectUtil.isEmpty(content)) {
			return Collections.emptyList();
		}
		try {
			return getInstance().readValue(content, getListType(elementClass));
		} catch (IOException e) {
			throw ExceptionUtil.unchecked(e);
		}
	}

	/**
	 * 读取集合
	 *
	 * @param content      InputStream
	 * @param elementClass elementClass
	 * @param <T>          泛型
	 * @return 集合
	 */
	public static <T> List<T> readList(@Nullable InputStream content, Class<T> elementClass) {
		if (content == null) {
			return Collections.emptyList();
		}
		try {
			return getInstance().readValue(content, getListType(elementClass));
		} catch (IOException e) {
			throw ExceptionUtil.unchecked(e);
		}
	}

	/**
	 * 读取集合
	 *
	 * @param content      bytes
	 * @param elementClass elementClass
	 * @param <T>          泛型
	 * @return 集合
	 */
	public static <T> List<T> readList(@Nullable String content, Class<T> elementClass) {
		if (ObjectUtil.isEmpty(content)) {
			return Collections.emptyList();
		}
		try {
			return getInstance().readValue(content, getListType(elementClass));
		} catch (IOException e) {
			throw ExceptionUtil.unchecked(e);
		}
	}

	/**
	 * 读取集合
	 *
	 * @param content bytes
	 * @return 集合
	 */
	public static Map<String, Object> readMap(@Nullable byte[] content) {
		return readMap(content, Object.class);
	}

	/**
	 * 读取集合
	 *
	 * @param content InputStream
	 * @return 集合
	 */
	public static Map<String, Object> readMap(@Nullable InputStream content) {
		return readMap(content, Object.class);
	}

	/**
	 * 读取集合
	 *
	 * @param content bytes
	 * @return 集合
	 */
	public static Map<String, Object> readMap(@Nullable String content) {
		return readMap(content, Object.class);
	}

	/**
	 * 读取集合
	 *
	 * @param content    bytes
	 * @param valueClass 值类型
	 * @param <V>        泛型
	 * @return 集合
	 */
	public static <V> Map<String, V> readMap(@Nullable byte[] content, Class<?> valueClass) {
		return readMap(content, String.class, valueClass);
	}

	/**
	 * 读取集合
	 *
	 * @param content    InputStream
	 * @param valueClass 值类型
	 * @param <V>        泛型
	 * @return 集合
	 */
	public static <V> Map<String, V> readMap(@Nullable InputStream content, Class<?> valueClass) {
		return readMap(content, String.class, valueClass);
	}

	/**
	 * 读取集合
	 *
	 * @param content    bytes
	 * @param valueClass 值类型
	 * @param <V>        泛型
	 * @return 集合
	 */
	public static <V> Map<String, V> readMap(@Nullable String content, Class<?> valueClass) {
		return readMap(content, String.class, valueClass);
	}

	/**
	 * 读取集合
	 *
	 * @param content    bytes
	 * @param keyClass   key类型
	 * @param valueClass 值类型
	 * @param <K>        泛型
	 * @param <V>        泛型
	 * @return 集合
	 */
	public static <K, V> Map<K, V> readMap(@Nullable byte[] content, Class<?> keyClass, Class<?> valueClass) {
		if (ObjectUtil.isEmpty(content)) {
			return Collections.emptyMap();
		}
		try {
			return getInstance().readValue(content, getMapType(keyClass, valueClass));
		} catch (IOException e) {
			throw ExceptionUtil.unchecked(e);
		}
	}

	/**
	 * 读取集合
	 *
	 * @param content    InputStream
	 * @param keyClass   key类型
	 * @param valueClass 值类型
	 * @param <K>        泛型
	 * @param <V>        泛型
	 * @return 集合
	 */
	public static <K, V> Map<K, V> readMap(@Nullable InputStream content, Class<?> keyClass, Class<?> valueClass) {
		if (ObjectUtil.isEmpty(content)) {
			return Collections.emptyMap();
		}
		try {
			return getInstance().readValue(content, getMapType(keyClass, valueClass));
		} catch (IOException e) {
			throw ExceptionUtil.unchecked(e);
		}
	}

	/**
	 * 读取集合
	 *
	 * @param content    bytes
	 * @param keyClass   key类型
	 * @param valueClass 值类型
	 * @param <K>        泛型
	 * @param <V>        泛型
	 * @return 集合
	 */
	public static <K, V> Map<K, V> readMap(@Nullable String content, Class<?> keyClass, Class<?> valueClass) {
		if (ObjectUtil.isEmpty(content)) {
			return Collections.emptyMap();
		}
		try {
			return getInstance().readValue(content, getMapType(keyClass, valueClass));
		} catch (IOException e) {
			throw ExceptionUtil.unchecked(e);
		}
	}

	/**
	 * jackson 的类型转换
	 *
	 * @param fromValue   来源对象
	 * @param toValueType 转换的类型
	 * @param <T>         泛型标记
	 * @return 转换结果
	 */
	public static <T> T convertValue(Object fromValue, Class<T> toValueType) {
		return getInstance().convertValue(fromValue, toValueType);
	}

	/**
	 * jackson 的类型转换
	 *
	 * @param fromValue   来源对象
	 * @param toValueType 转换的类型
	 * @param <T>         泛型标记
	 * @return 转换结果
	 */
	public static <T> T convertValue(Object fromValue, JavaType toValueType) {
		return getInstance().convertValue(fromValue, toValueType);
	}

	/**
	 * jackson 的类型转换
	 *
	 * @param fromValue      来源对象
	 * @param toValueTypeRef 泛型类型
	 * @param <T>            泛型标记
	 * @return 转换结果
	 */
	public static <T> T convertValue(Object fromValue, TypeReference<T> toValueTypeRef) {
		return getInstance().convertValue(fromValue, toValueTypeRef);
	}

	/**
	 * tree 转对象
	 *
	 * @param treeNode  TreeNode
	 * @param valueType valueType
	 * @param <T>       泛型标记
	 * @return 转换结果
	 */
	public static <T> T treeToValue(TreeNode treeNode, Class<T> valueType) {
		try {
			return getInstance().treeToValue(treeNode, valueType);
		} catch (JsonProcessingException e) {
			throw ExceptionUtil.unchecked(e);
		}
	}

	/**
	 * 对象转 tree
	 *
	 * @param fromValue fromValue
	 * @param <T>       泛型标记
	 * @return 转换结果
	 */
	public static <T extends JsonNode> T valueToTree(@Nullable Object fromValue) {
		return getInstance().valueToTree(fromValue);
	}

	/**
	 * 判断是否可以序列化
	 *
	 * @param value 对象
	 * @return 是否可以序列化
	 */
	public static boolean canSerialize(@Nullable Object value) {
		if (value == null) {
			return true;
		}
		return getInstance().canSerialize(value.getClass());
	}

	/**
	 * 判断是否可以反序列化
	 *
	 * @param type JavaType
	 * @return 是否可以反序列化
	 */
	public static boolean canDeserialize(JavaType type) {
		return getInstance().canDeserialize(type);
	}

	/**
	 * 检验 json 格式
	 *
	 * @param jsonString json 字符串
	 * @return 是否成功
	 */
	public static boolean isValidJson(String jsonString) {
		return isValidJson(mapper -> mapper.readTree(jsonString));
	}

	/**
	 * 检验 json 格式
	 *
	 * @param content json byte array
	 * @return 是否成功
	 */
	public static boolean isValidJson(byte[] content) {
		return isValidJson(mapper -> mapper.readTree(content));
	}

	/**
	 * 检验 json 格式
	 *
	 * @param input json input stream
	 * @return 是否成功
	 */
	public static boolean isValidJson(InputStream input) {
		return isValidJson(mapper -> mapper.readTree(input));
	}

	/**
	 * 检验 json 格式
	 *
	 * @param jsonParser json parser
	 * @return 是否成功
	 */
	public static boolean isValidJson(JsonParser jsonParser) {
		return isValidJson(mapper -> mapper.readTree(jsonParser));
	}

	/**
	 * 检验 json 格式
	 *
	 * @param consumer ObjectMapper consumer
	 * @return 是否成功
	 */
	public static boolean isValidJson(CheckedConsumer<ObjectMapper> consumer) {
		ObjectMapper mapper = getInstance().copy();
		mapper.enable(DeserializationFeature.FAIL_ON_TRAILING_TOKENS);
		mapper.enable(DeserializationFeature.FAIL_ON_READING_DUP_TREE_KEY);
		try {
			consumer.accept(mapper);
			return true;
		} catch (Throwable e) {
			return false;
		}
	}

	/**
	 * 创建 ObjectNode
	 *
	 * @return ObjectNode
	 */
	public static ObjectNode createObjectNode() {
		return getInstance().createObjectNode();
	}

	/**
	 * 创建 ArrayNode
	 *
	 * @return ArrayNode
	 */
	public static ArrayNode createArrayNode() {
		return getInstance().createArrayNode();
	}

	/**
	 * 获取 ObjectMapper 实例
	 *
	 * @return ObjectMapper
	 */
	public static ObjectMapper getInstance() {
		return JacksonHolder.INSTANCE;
	}

	private static class JacksonHolder {
		private static final ObjectMapper INSTANCE = new JacksonObjectMapper();
	}

	private static class JacksonObjectMapper extends ObjectMapper {
		private static final long serialVersionUID = 4288193147502386170L;

		private static final Locale CHINA = Locale.CHINA;

		JacksonObjectMapper() {
			super(jsonFactory());
			super.setLocale(CHINA);
			super.setDateFormat(new SimpleDateFormat(DateUtil.PATTERN_DATETIME, CHINA));
			// 单引号
			super.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
			// 忽略json字符串中不识别的属性
			super.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			// 忽略无法转换的对象
			super.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
			super.setTimeZone(TimeZone.getTimeZone(ZoneId.systemDefault()));
			super.findAndRegisterModules();
		}

		JacksonObjectMapper(ObjectMapper src) {
			super(src);
		}

		private static JsonFactory jsonFactory() {
			return JsonFactory.builder()
				// 可解析反斜杠引用的所有字符
				.configure(JsonReadFeature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER, true)
				// 允许JSON字符串包含非引号控制字符（值小于32的ASCII字符，包含制表符和换行符）
				.configure(JsonReadFeature.ALLOW_UNESCAPED_CONTROL_CHARS, true)
				.build();
		}

		@Override
		public ObjectMapper copy() {
			return new JacksonObjectMapper(this);
		}
	}

	//public static void main(String[] args) throws IOException {
	//	String message = "{\"payload\":{\"cid\":\"1\",\"businessType\":\"0\",\"city\":\"北京市\",\"cityId\":\"265\",\"name\":\"fsfds\",\"carNum\":\"156156\"},\"headers\":{\"delivery_queue_name\":\"riven\",\"message_id\":\"de6b5e53-0442-40ba-b66f-ae0c13c09801\",\"expected_delay_millis\":8000,\"send_timestamp\":1645168609854}}";
	//	JsonNode jsonNode = JsonUtil.parse(message);
	//	String payload = jsonNode.get("payload").toString();
	//
	//	Map<String, Object> headers = JsonUtil.readMap(jsonNode.get("headers").toString());
	//	System.out.println("sdfasdf");
	//
	//}

	/**
	 * 获取索引列表
	 *
	 * @param compressJsonPath 压缩的 json 路径
	 * @param size 大下
	 * @return 结果列表
	 */
	public static List<String> getIndexList(final String compressJsonPath, final int size) {
		final String json = com.taotao.cloud.common.utils.io.FileUtil.getFileContent(compressJsonPath);
		if (com.taotao.cloud.common.utils.lang.StringUtil.isEmptyTrim(json) || size <= 0) {
			return Collections.emptyList();
		}

		List<Integer> prefixList = CollectionUtil.fill(size);
		return getIndexList(compressJsonPath, prefixList);
	}

	/**
	 * 获取索引列表
	 *
	 * @param compressJsonPath 压缩的 json 路径
	 * @param indexPrefixList  索引前缀列表
	 * @return 结果列表
	 */
	public static List<String> getIndexList(final String compressJsonPath, final List<?> indexPrefixList) {
		final String json = FileUtil.getFileContent(compressJsonPath);
		if (com.taotao.cloud.common.utils.lang.StringUtil.isEmptyTrim(json) || CollectionUtil.isEmpty(indexPrefixList)) {
			return Collections.emptyList();
		}

		List<String> results = Guavas.newArrayList(indexPrefixList.size());
		Stack<Integer> stack = new Stack<>();
		List<String> indexList = Guavas.newArrayList(indexPrefixList.size());

		for (int i = 0; i < json.length(); i++) {
			final char ch = json.charAt(i);

			if ('{' == ch) {
				stack.push(i);
			}
			if ('}' == ch) {
				Integer startIndex = stack.pop();
				int endIndex = i + 1;

				final int byteStartIndex = json.substring(0, startIndex).getBytes().length;
				final int byteEndIndex = byteStartIndex + json.substring(startIndex, endIndex)
					.getBytes().length;

				String result = byteStartIndex + PunctuationConst.COMMA + byteEndIndex;
				indexList.add(result);
			}
		}

		for (int i = 0; i < indexPrefixList.size(); i++) {
			final String prefix = getPrefix(indexPrefixList.get(i));
			String result = prefix + indexList.get(i);
			results.add(result);
		}

		return results;
	}

	/**
	 * 获取前缀
	 * @param object 对象
	 * @return 结果
	 */
	private static String getPrefix(Object object) {
		if(com.taotao.cloud.common.utils.lang.ObjectUtil.isNull(object)) {
			return com.taotao.cloud.common.utils.lang.StringUtil.EMPTY;
		}
		String string = object.toString();
		if(com.taotao.cloud.common.utils.lang.StringUtil.isEmptyTrim(string)) {
			return com.taotao.cloud.common.utils.lang.StringUtil.EMPTY;
		}

		return string+ com.taotao.cloud.common.utils.lang.StringUtil.BLANK;
	}
}
