package com.taotao.cloud.common.utils;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
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
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.taotao.cloud.common.constant.CommonConstant;
import com.taotao.cloud.common.exception.BaseException;
import com.taotao.cloud.common.json.LampJacksonModule;
import com.taotao.cloud.common.json.RemoteDataDeserializer;
import com.taotao.cloud.common.model.RemoteData;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import lombok.experimental.UtilityClass;

/**
 * 基于 Jackson 的 json 工具类
 *
 * @author dengtao
 * @version 1.0.0
 * @since 2019/3/5
 */
@UtilityClass
public class JsonUtil {

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
		// 排除空值字段
		MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		// 使用驼峰式
		MAPPER.setPropertyNamingStrategy(PropertyNamingStrategy.LOWER_CAMEL_CASE);
		// 使用bean名称
		MAPPER.enable(MapperFeature.USE_STD_BEAN_NAMING);
		// 所有日期格式都统一为固定格式
		MAPPER.setDateFormat(new SimpleDateFormat(CommonConstant.DATETIME_FORMAT, Locale.CHINA));
		MAPPER.registerModule(new Jdk8Module());
		MAPPER.registerModule(new JavaTimeModule());

		// 注册自定义模块
		MAPPER.registerModule(new LampJacksonModule());
		MAPPER.registerModule(
			new SimpleModule().addDeserializer(RemoteData.class, RemoteDataDeserializer.INSTANCE));
	}

	/**
	 * 对象转换为json字符串
	 *
	 * @param o 要转换的对象
	 * @return java.lang.String
	 * @author dengtao
	 * @since 2021/2/25 16:21
	 */
	public static String toJSONString(Object o) {
		return toJSONString(o, false);
	}

	/**
	 * 对象转换为json字符串
	 *
	 * @param o      要转换的对象
	 * @param format 是否格式化json
	 * @return java.lang.String
	 * @author dengtao
	 * @since 2021/2/25 16:21
	 */
	public static String toJSONString(Object o, boolean format) {
		try {
			if (o == null) {
				return "";
			}
			if (o instanceof Number) {
				return o.toString();
			}
			if (o instanceof String) {
				return (String) o;
			}
			if (format) {
				return MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(o);
			}
			return MAPPER.writeValueAsString(o);
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
	 * @author dengtao
	 * @since 2021/2/25 16:22
	 */
	public static <T> T toObject(String json, Class<T> cls) {
		if (StrUtil.isBlank(json) || cls == null) {
			return null;
		}
		try {
			return MAPPER.readValue(json, cls);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 字符串转换为指定对象，并增加泛型转义 例如：List<Integer> test = toObject(jsonStr, List.class, Integer.class);
	 *
	 * @param json             json字符串
	 * @param parametrized     目标对象
	 * @param parameterClasses 泛型对象
	 * @return T
	 * @author dengtao
	 * @since 2021/2/25 16:22
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
			throw new RuntimeException(e);
		}
	}

	/**
	 * 字符串转换为指定对象
	 *
	 * @param json          json字符串
	 * @param typeReference 目标对象类型
	 * @return T
	 * @author dengtao
	 * @since 2021/2/25 16:22
	 */
	public static <T> T toObject(String json, TypeReference<T> typeReference) {
		if (StrUtil.isBlank(json) || typeReference == null) {
			return null;
		}
		try {
			return MAPPER.readValue(json, typeReference);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 字符串转换为JsonNode对象
	 *
	 * @param json json字符串
	 * @return T
	 * @author dengtao
	 * @since 2021/2/25 16:22
	 */
	public static JsonNode parse(String json) {
		if (StrUtil.isBlank(json)) {
			return null;
		}
		try {
			return MAPPER.readTree(json);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 对象转换为map对象
	 *
	 * @param o 要转换的对象
	 * @return T
	 * @author dengtao
	 * @since 2021/2/25 16:22
	 */
	public static <K, V> Map<K, V> toMap(Object o) {
		if (o == null) {
			return null;
		}
		if (o instanceof String) {
			return toObject((String) o, Map.class);
		}
		return MAPPER.convertValue(o, Map.class);
	}

	/**
	 * json字符串转换为list对象
	 *
	 * @param json json字符串
	 * @return T
	 * @author dengtao
	 * @since 2021/2/25 16:22
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
	 * @return T
	 * @author dengtao
	 * @since 2021/2/25 16:22
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
