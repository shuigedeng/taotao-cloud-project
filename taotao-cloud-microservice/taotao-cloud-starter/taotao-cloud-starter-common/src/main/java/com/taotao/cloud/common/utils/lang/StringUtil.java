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
package com.taotao.cloud.common.utils.lang;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.google.common.collect.Lists;
import com.taotao.cloud.common.constant.CommonConstant;
import com.taotao.cloud.common.constant.PunctuationConst;
import com.taotao.cloud.common.enums.RandomEnum;
import com.taotao.cloud.common.exception.CommonRuntimeException;
import com.taotao.cloud.common.model.CharPool;
import com.taotao.cloud.common.model.Holder;
import com.taotao.cloud.common.support.condition.ICondition;
import com.taotao.cloud.common.support.handler.IHandler;
import com.taotao.cloud.common.utils.collection.ArrayPrimitiveUtil;
import com.taotao.cloud.common.utils.collection.ArrayUtil;
import com.taotao.cloud.common.utils.collection.CollectionUtil;
import com.taotao.cloud.common.utils.collection.MapUtil;
import com.taotao.cloud.common.utils.common.ArgUtil;
import com.taotao.cloud.common.utils.date.DateUtil;

import com.taotao.cloud.common.utils.number.NumberUtil;
import com.taotao.cloud.common.utils.reflect.ClassTypeUtil;
import com.taotao.cloud.common.utils.reflect.ClassUtil;
import com.taotao.cloud.common.utils.reflect.ReflectFieldUtil;
import com.taotao.cloud.common.utils.system.SystemUtil;
import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.UserAgent;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import javax.servlet.http.HttpServletRequest;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.PatternMatchUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.util.HtmlUtils;

/**
 * 字符串工具类
 */
public final class StringUtil extends org.springframework.util.StringUtils {

	/**
	 * 判断对象为null
	 *
	 * @param object 对象
	 * @return 对象是否为空
	 */
	//public static boolean isNull(@Nullable Object object) {
	//	return Objects.isNull(object);
	//}

	/**
	 * 判断对象不为null
	 *
	 * @param object 对象
	 * @return 对象是否不为空
	 */
	//public static boolean isNotNull(@Nullable Object object) {
	//	return Objects.nonNull(object);
	//}


	/**
	 * 替换字符串-根据索引
	 * @param str 原始字符串
	 * @param replacedStr 替换字符串
	 * @param start 开始索引，包括此索引
	 * @param end 结束索引，不包括此索引（结束索引==开始索引：将在开始索引处插入替换字符串）
	 * @return 替换后的字符串
	 */
	public static String replace(String str, String replacedStr, int start, int end) {
		StringBuilder stringBuffer = new StringBuilder();
		stringBuffer.append(str);
		stringBuffer.replace(start, end, replacedStr);
		return stringBuffer.toString();
	}

	/**
	 * 判断对象为true
	 *
	 * @param object 对象
	 * @return 对象是否为true
	 */
	public static boolean isTrue(@Nullable Boolean object) {
		return Boolean.TRUE.equals(object);
	}

	/**
	 * 判断对象为false
	 *
	 * @param object 对象
	 * @return 对象是否为false
	 */
	public static boolean isFalse(@Nullable Boolean object) {
		return object == null || Boolean.FALSE.equals(object);
	}

	/**
	 * 判断数组不为空
	 *
	 * @param array 数组
	 * @return 数组是否为空
	 */
	public static boolean isNotEmpty(@Nullable Object[] array) {
		return !isEmpty(array);
	}

	/**
	 * 判断对象不为空
	 *
	 * @param obj 数组
	 * @return 数组是否为空
	 */
	//public static boolean isNotEmpty(@Nullable Object obj) {
	//	return !ObjectUtil.isEmpty(obj);
	//}

	/**
	 * 对象 eq
	 *
	 * @param o1 Object
	 * @param o2 Object
	 * @return 是否eq
	 */
	public static boolean equals(@Nullable Object o1, @Nullable Object o2) {
		return Objects.equals(o1, o2);
	}

	/**
	 * 比较两个对象是否不相等。<br>
	 *
	 * @param o1 对象1
	 * @param o2 对象2
	 * @return 是否不eq
	 */
	public static boolean isNotEqual(Object o1, Object o2) {
		return !Objects.equals(o1, o2);
	}

	/**
	 * 返回对象的 hashCode
	 *
	 * @param obj Object
	 * @return hashCode
	 */
	public static int hashCode(@Nullable Object obj) {
		return Objects.hashCode(obj);
	}

	/**
	 * 如果对象为null，返回默认值
	 *
	 * @param object       Object
	 * @param defaultValue 默认值
	 * @return Object
	 */
	public static Object defaultIfNull(@Nullable Object object, Object defaultValue) {
		return object != null ? object : defaultValue;
	}

	/**
	 * 强转string
	 *
	 * @param object Object
	 * @return String
	 */
	@Nullable
	public static String toStr(@Nullable Object object) {
		return toStr(object, null);
	}

	/**
	 * 强转string
	 *
	 * @param object       Object
	 * @param defaultValue 默认值
	 * @return String
	 */
	@Nullable
	public static String toStr(@Nullable Object object, @Nullable String defaultValue) {
		if (null == object) {
			return defaultValue;
		}
		if (object instanceof CharSequence) {
			return ((CharSequence) object).toString();
		}
		return String.valueOf(object);
	}

	/**
	 * 对象转为 int （支持 String 和 Number），默认: 0
	 *
	 * @param object Object
	 * @return int
	 */
	public static int toInt(@Nullable Object object) {
		return toInt(object, 0);
	}

	/**
	 * 对象转为 int （支持 String 和 Number）
	 *
	 * @param object       Object
	 * @param defaultValue 默认值
	 * @return int
	 */
	public static int toInt(@Nullable Object object, int defaultValue) {
		if (object instanceof Number) {
			return ((Number) object).intValue();
		}
		if (object instanceof CharSequence) {
			String value = ((CharSequence) object).toString();
			try {
				return Integer.parseInt(value);
			} catch (final NumberFormatException nfe) {
				return defaultValue;
			}
		}
		return defaultValue;
	}

	/**
	 * 对象转为 long （支持 String 和 Number），默认: 0L
	 *
	 * @param object Object
	 * @return long
	 */
	public static long toLong(@Nullable Object object) {
		return toLong(object, 0L);
	}

	/**
	 * 对象转为 long （支持 String 和 Number），默认: 0L
	 *
	 * @param object Object
	 * @return long
	 */
	public static long toLong(@Nullable Object object, long defaultValue) {
		if (object instanceof Number) {
			return ((Number) object).longValue();
		}
		if (object instanceof CharSequence) {
			String value = ((CharSequence) object).toString();
			try {
				return Long.parseLong(value);
			} catch (final NumberFormatException nfe) {
				return defaultValue;
			}
		}
		return defaultValue;
	}

	/**
	 * 对象转为 Float
	 *
	 * @param object Object
	 * @return 结果
	 */
	public static float toFloat(@Nullable Object object) {
		return toFloat(object, 0.0f);
	}

	/**
	 * 对象转为 Float
	 *
	 * @param object       Object
	 * @param defaultValue float
	 * @return 结果
	 */
	public static float toFloat(@Nullable Object object, float defaultValue) {
		if (object instanceof Number) {
			return ((Number) object).floatValue();
		}
		if (object instanceof CharSequence) {
			String value = ((CharSequence) object).toString();
			try {
				return Float.parseFloat(value);
			} catch (NumberFormatException nfe) {
				return defaultValue;
			}
		}
		return defaultValue;
	}

	/**
	 * 对象转为 Double
	 *
	 * @param object Object
	 * @return 结果
	 */
	public static double toDouble(@Nullable Object object) {
		return toDouble(object, 0.0d);
	}

	/**
	 * 对象转为 Double
	 *
	 * @param object       Object
	 * @param defaultValue double
	 * @return 结果
	 */
	public static double toDouble(@Nullable Object object, double defaultValue) {
		if (object instanceof Number) {
			return ((Number) object).doubleValue();
		}
		if (object instanceof CharSequence) {
			String value = ((CharSequence) object).toString();
			try {
				return Double.parseDouble(value);
			} catch (NumberFormatException nfe) {
				return defaultValue;
			}
		}
		return defaultValue;
	}

	/**
	 * 对象转为 Byte
	 *
	 * @param object Object
	 * @return 结果
	 */
	public static byte toByte(@Nullable Object object) {
		return toByte(object, (byte) 0);
	}

	/**
	 * 对象转为 Byte
	 *
	 * @param object       Object
	 * @param defaultValue byte
	 * @return 结果
	 */
	public static byte toByte(@Nullable Object object, byte defaultValue) {
		if (object instanceof Number) {
			return ((Number) object).byteValue();
		}
		if (object instanceof CharSequence) {
			String value = ((CharSequence) object).toString();
			try {
				return Byte.parseByte(value);
			} catch (NumberFormatException nfe) {
				return defaultValue;
			}
		}
		return defaultValue;
	}

	/**
	 * 对象转为 Short
	 *
	 * @param object Object
	 * @return 结果
	 */
	public static short toShort(@Nullable Object object) {
		return toShort(object, (short) 0);
	}

	/**
	 * 对象转为 Short
	 *
	 * @param object       Object
	 * @param defaultValue short
	 * @return 结果
	 */
	public static short toShort(@Nullable Object object, short defaultValue) {
		if (object instanceof Number) {
			return ((Number) object).byteValue();
		}
		if (object instanceof CharSequence) {
			String value = ((CharSequence) object).toString();
			try {
				return Short.parseShort(value);
			} catch (NumberFormatException nfe) {
				return defaultValue;
			}
		}
		return defaultValue;
	}

	/**
	 * 对象转为 Boolean
	 *
	 * @param object Object
	 * @return 结果
	 */
	@Nullable
	public static Boolean toBoolean(@Nullable Object object) {
		return toBoolean(object, null);
	}

	/**
	 * 对象转为 Boolean，支持 1、0，y、yes、n、no，on、off，true、false
	 *
	 * @param object       Object
	 * @param defaultValue 默认值
	 * @return 结果
	 */
	@Nullable
	public static Boolean toBoolean(@Nullable Object object, @Nullable Boolean defaultValue) {
		if (object instanceof Boolean) {
			return (Boolean) object;
		} else if (object instanceof CharSequence) {
			String value = ((CharSequence) object).toString();
			if (StringPool.TRUE.equalsIgnoreCase(value) ||
				StringPool.Y.equalsIgnoreCase(value) ||
				StringPool.YES.equalsIgnoreCase(value) ||
				StringPool.ON.equalsIgnoreCase(value) ||
				StringPool.ONE.equalsIgnoreCase(value)) {
				return true;
			} else if (StringPool.FALSE.equalsIgnoreCase(value) ||
				StringPool.N.equalsIgnoreCase(value) ||
				StringPool.NO.equalsIgnoreCase(value) ||
				StringPool.OFF.equalsIgnoreCase(value) ||
				StringPool.ZERO.equalsIgnoreCase(value)) {
				return false;
			}
		}
		return defaultValue;
	}

	/**
	 * 判断两个对象是否不相同 1.如果不是同一种类型,则返回true
	 *
	 * @param except 期望值
	 * @param real   实际值
	 * @return 两个对象是否不同
	 */
	//public static boolean isNotEquals(Object except, Object real) {
	//	return !isEquals(except, real);
	//}

	/**
	 * 判断两个对象是否为同一对象 instanceof isInstance isAssignableFrom
	 * <p>
	 * 注意：任何一个元素为 null，则认为是不同类型。
	 *
	 * @param one 第一个元素
	 * @param two 第二个元素
	 * @return 是否为同一对象
	 */
	public static boolean isSameType(Object one, Object two) {
		if (ObjectUtil.isNull(one)
			|| isNull(two)) {
			return false;
		}
		Class clazzOne = one.getClass();

		return clazzOne.isInstance(two);
	}

	/**
	 * 不是同一个类型
	 *
	 * @param one 第一个元素
	 * @param two 第二个元素
	 * @return 是否为不同对象
	 */
	public static boolean isNotSameType(Object one, Object two) {
		return !isSameType(one, two);
	}


	/**
	 * 判断当前对象是否为空 - 对象为空 - 空字符串 - 空集合/map - 空数组 - 自定义空类型
	 *
	 * @param object 对象
	 * @return 是否为空
	 */
	public static boolean isNull(Object object) {
		return null == object;
	}

	/**
	 * 判断对象是否非null
	 *
	 * @param object 元素
	 * @return {@code true} 非空
	 */
	public static boolean isNotNull(Object object) {
		return !isNull(object);
	}

	/**
	 * 判断内容是否为空 - 空字符串 - 空集合/map - 空数组 - 自定义空类型
	 *
	 * @param object 对象
	 * @return 是否为空
	 */
	public static boolean isEmpty(Object object) {
		if (isNull(object)) {
			return true;
		}

		if (object instanceof String string) {
			return isEmpty(string);
		}
		if (object instanceof Collection collection) {
			return CollectionUtil.isEmpty(collection);
		}
		if (object instanceof Map map) {
			return MapUtil.isEmpty(map);
		}
		if (object.getClass().isArray()) {
			return Array.getLength(object) == 0;
		}

		return false;
	}

	/**
	 * 判断对象是否非空
	 *
	 * @param object 对象
	 * @return 是否非空
	 */
	public static boolean isNotEmpty(Object object) {
		return !isEmpty(object);
	}

	/**
	 * 判断两个对象是否相同 1.如果不是同一种类型,则直接返回false
	 *
	 * @param except 期望值
	 * @param real   实际值
	 * @return 两个对象是否相同
	 */
	public static boolean isEquals(Object except, Object real) {
		//1. 不是同一种类型
		if (isNotSameType(except, real)) {
			return false;
		}

		final Class<?> exceptClass = except.getClass();
		final Class<?> realClass = except.getClass();

		//2. 基本类型
		if (exceptClass.isPrimitive() && realClass.isPrimitive() && except != real) {
			return false;
		}

		//3. 数组
		if (ClassTypeUtil.isArray(exceptClass) && ClassTypeUtil.isArray(realClass)) {
			Object[] exceptArray = (Object[]) except;
			Object[] realArray = (Object[]) real;
			return Arrays.equals(exceptArray, realArray);
		}

		//3. Collection

		//4. map
		if (ClassTypeUtil.isMap(exceptClass) && ClassTypeUtil.isMap(realClass)) {
			Map exceptMap = (Map) except;
			Map realMap = (Map) real;
			return exceptMap.equals(realMap);
		}

		return except.equals(real);
	}

	/**
	 * 判断两个对象是否不相同 1.如果不是同一种类型,则返回true
	 *
	 * @param except 期望值
	 * @param real   实际值
	 * @return 两个对象是否不同
	 */
	public static boolean isNotEquals(Object except, Object real) {
		return !isEquals(except, real);
	}

	/**
	 * 对象转字符串
	 *
	 * @param object 对象
	 * @return 结果
	 */
	//public static String objectToString(final Object object) {
	//	return objectToString(object, null);
	//}

	/**
	 * 对象转字符串
	 *
	 * @param object       对象
	 * @param defaultValue 默认值，原始对象为 null 时返回。
	 * @return 结果
	 */
	//public static String objectToString(final Object object,
	//	final String defaultValue) {
	//	if (isNull(object)) {
	//		return defaultValue;
	//	}
	//	return object.toString();
	//}

	/**
	 * 判断所有参数皆为null
	 *
	 * @param object 对象
	 * @param others 其他参数
	 * @return 是否都为空
	 * @see #isNull(Object) 增强版本
	 */
	public static boolean isNull(final Object object, final Object... others) {
		if (isNull(object)) {
			// 其他列表不为空，则遍历
			if (ArrayUtil.isNotEmpty(others)) {
				for (Object other : others) {
					if (isNotNull(other)) {
						return false;
					}
				}
				return true;
			}
			return true;
		}
		return false;
	}

	/**
	 * 判断两个元素是否相等或者都为 Null
	 *
	 * @param left  元素1
	 * @param right 元素2
	 * @return 是否相等或者都为 Null
	 */
	public static boolean isEqualsOrNull(final Object left, final Object right) {
		if (isNull(left, right)) {
			return true;
		}
		if (isNull(left) || isNull(right)) {
			return false;
		}
		return isEquals(left, right);
	}

	/**
	 * 可遍历的元素对象的某个元素，转换为列表
	 *
	 * @param object  可遍历对象
	 * @param handler 转换方式
	 * @param <R>     R 泛型
	 * @return 结果列表
	 */
	@SuppressWarnings("unchecked")
	public static <R> List<R> toList(final Object object, IHandler<Object, R> handler) {
		if (isNull(object)) {
			return Collections.emptyList();
		}

		final Class clazz = object.getClass();

		// 集合
		if (ClassTypeUtil.isCollection(clazz)) {
			Collection collection = (Collection) object;
			return CollectionUtil.toList(collection, handler);
		}

		// 数组
		if (clazz.isArray()) {
			return ArrayUtil.toList(object, handler);
		}

		throw new UnsupportedOperationException(
			"Not support foreach() for class: " + clazz.getName());
	}

	/**
	 * 获取实体对象对应的 class 信息
	 *
	 * @param object 实例对象
	 * @return 对象 class 信息
	 */
	public static Class<?> getClass(final Object object) {
		if (isNull(object)) {
			return null;
		}
		return object.getClass();
	}

	/**
	 * empty 转换为 null
	 *
	 * @param object 对象
	 */
	public static void emptyToNull(Object object) {
		if (null == object) {
			return;
		}

		List<Field> fieldList = ClassUtil.getAllFieldList(object.getClass());
		for (Field field : fieldList) {
			Object value = ReflectFieldUtil.getValue(field, object);
			if (isEmpty(value)) {
				ReflectFieldUtil.setValue(field, object, null);
			}
		}
	}

	/**
	 * 基于反射的属性拷贝
	 *
	 * @param source 源头
	 * @param target 目标
	 */
	public static void copyProperties(Object source, Object target) {
		if (source == null || target == null) {
			return;
		}

		Map<String, Field> sourceFieldMap = ClassUtil.getAllFieldMap(source.getClass());
		Map<String, Field> targetFieldMap = ClassUtil.getAllFieldMap(target.getClass());

		// 遍历
		for (Map.Entry<String, Field> entry : sourceFieldMap.entrySet()) {
			String sourceFieldName = entry.getKey();
			Field sourceField = entry.getValue();
			Field targetField = targetFieldMap.get(sourceFieldName);

			if (targetField == null) {
				continue;
			}

			if (ClassUtil.isAssignable(sourceField.getType(), targetField.getType())) {
				Object sourceVal = ReflectFieldUtil.getValue(sourceField, source);
				ReflectFieldUtil.setValue(targetField, target, sourceVal);
			}
		}
	}

	/**
	 * 是否为相同的值 null null 被认为相同
	 *
	 * @param valueOne 第一个
	 * @param valueTwo 第二个
	 * @return 是否
	 */
	public static boolean isSameValue(Object valueOne, Object valueTwo) {
		if (valueOne == null && valueTwo == null) {
			return true;
		}

		if (valueOne == null || valueTwo == null) {
			return false;
		}

		return valueOne.equals(valueTwo);
	}

	/**
	 * 大写的字母
	 */
	public static final String LETTERS_UPPER = "ABCDEFGHIJKLMNOPQRSTUVWSXYZ";

	/**
	 * 小写的字母
	 */
	public static final String LETTERS_LOWER = "abcdefghijklmnopqrstuvwsxyz";

	/**
	 * 空白信息的表达式
	 */
	private static final Pattern BLANK_PATTERN = Pattern.compile("\\s*|\t|\r|\n");

	private StringUtil() {
	}

	/**
	 * 空字符串
	 */
	public static final String EMPTY = "";

	/**
	 * 空 json
	 */
	public static final String EMPTY_JSON = "{}";

	/**
	 * 空格
	 */
	public static final String BLANK = " ";

	/**
	 * 新行
	 * <p>
	 * System.lineSeparator() 实际的文本效果是2行
	 */
	public static final String NEW_LINE = "";

	/**
	 * 是否不为换行符
	 *
	 * @param line 内容
	 * @return 是否
	 */
	public static boolean isNotReturnLine(String line) {
		return !isReturnLine(line);
	}

	/**
	 * 是否为换行符
	 *
	 * @param line 内容
	 * @return 是否
	 */
	public static boolean isReturnLine(String line) {
		if (StringUtil.isEmpty(line)) {
			return true;
		}

		String trim = line.trim();
		if (StringUtil.isEmpty(trim)) {
			return true;
		}

		if (NEW_LINE.equals(line)) {
			return true;
		}

		return false;
	}

	/**
	 * 是否全部为大写
	 *
	 * @param string 待检验字符
	 * @return 是否为大写
	 */
	public static boolean isUpperCase(final String string) {
		if (StringUtil.isEmpty(string)) {
			return false;
		}

		char[] characters = string.toCharArray();
		for (char c : characters) {
			if (!Character.isUpperCase(c)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 是否全部为小写
	 *
	 * @param string 待检验字符
	 * @return 是否为大写
	 */
	public static boolean isLowerCase(final String string) {
		if (StringUtil.isEmpty(string)) {
			return false;
		}

		char[] characters = string.toCharArray();
		for (char c : characters) {
			if (!Character.isLowerCase(c)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 是否包含大写字母
	 *
	 * @param string 待检验字符
	 * @return 是否为大写
	 */
	public static boolean containsUppercase(final String string) {
		if (StringUtil.isEmpty(string)) {
			return false;
		}

		char[] characters = string.toCharArray();
		for (char c : characters) {
			if (Character.isUpperCase(c)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 是否包含小写字母
	 *
	 * @param string 待检验字符
	 * @return 是否为大写
	 */
	public static boolean containsLowercase(final String string) {
		if (StringUtil.isEmpty(string)) {
			return false;
		}

		char[] characters = string.toCharArray();
		for (char c : characters) {
			if (Character.isLowerCase(c)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 是否全部由字母组成 1. 大写字母 2. 小写字母
	 *
	 * @param string 字符串
	 * @return 结果
	 */
	public static boolean isLetter(final String string) {
		return isCharsCondition(string, new ICondition<Character>() {
			@Override
			public boolean condition(Character character) {
				return Character.isLowerCase(character)
					|| Character.isUpperCase(character);
			}
		});
	}

	/**
	 * 是否全部为数字
	 *
	 * @param string 字符串
	 * @return 是否为数字
	 */
	public static boolean isDigit(final String string) {
		return isCharsCondition(string, new ICondition<Character>() {
			@Override
			public boolean condition(Character character) {
				return Character.isDigit(character);
			}
		});
	}

	/**
	 * 是否全部为数字或者字母
	 *
	 * @param string 字符串
	 * @return 是否数字或者字母
	 */
	public static boolean isDigitOrLetter(final String string) {
		return isCharsCondition(string, new ICondition<Character>() {
			@Override
			public boolean condition(Character character) {
				return CharUtil.isDigitOrLetter(character);
			}
		});
	}

	/**
	 * 字符串是否全部满足某一个条件
	 *
	 * @param string    原始字符串
	 * @param condition 条件
	 * @return 是否满足
	 */
	private static boolean isCharsCondition(final String string,
		final ICondition<Character> condition) {
		if (StringUtil.isEmpty(string)) {
			return false;
		}

		char[] chars = string.toCharArray();
		for (char c : chars) {
			if (!condition.condition(c)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 是否为空
	 *
	 * @param string 字符串
	 * @return {@code true} 为空
	 */
	//public static boolean isEmpty(final String string) {
	//	return null == string || EMPTY.equals(string);
	//}

	/**
	 * 是否为空-进行 trim 之后
	 *
	 * @param string 原始字符串
	 * @return 是否
	 */
	public static boolean isEmptyTrim(final String string) {
		if (isEmpty(string)) {
			return true;
		}

		String trim = trim(string);
		return isEmpty(trim);
	}

	/**
	 * 是否不为空-进行 trim 之后
	 *
	 * @param string 原始字符串
	 * @return 是否
	 */
	public static boolean isNotEmptyTrim(final String string) {
		return !isEmptyTrim(string);
	}

	/**
	 * 是否为空的 json
	 *
	 * @param json json 信息
	 * @return 是否
	 */
	public static boolean isEmptyJson(final String json) {
		if (isEmptyTrim(json)) {
			return true;
		}

		String trim = json.trim();
		return EMPTY_JSON.equals(trim);
	}

	/**
	 * 是否为非空
	 *
	 * @param string 字符串
	 * @return {@code true} 为非空
	 */
	//public static boolean isNotEmpty(final String string) {
	//	return !isEmpty(string);
	//}

	/**
	 * 是否为空
	 *
	 * @param str 字符串
	 * @return 是否为空
	 */
	public static boolean isBlank(String str) {
		int strLen;
		if (str != null && (strLen = str.length()) != 0) {
			for (int i = 0; i < strLen; ++i) {
				if (!Character.isWhitespace(str.charAt(i))) {
					return false;
				}
			}

			return true;
		} else {
			return true;
		}
	}

	/**
	 * 是否不为空
	 *
	 * @param str 字符串
	 * @return 是否不为空
	 */
	public static boolean isNotBlank(String str) {
		return !isBlank(str);
	}


	/**
	 * 根据任意多的空格进行分割字符串。 1. 入参为空,则返回空字符串数组
	 *
	 * @param string 字符串
	 * @return 割字符串数组
	 */
	public static String[] splitByAnyBlank(final String string) {
		if (StringUtil.isEmpty(string)) {
			return new String[0];
		}

		final String pattern = "\\s+|\u0013";
		return string.split(pattern);
	}

	/**
	 * 过滤掉所有的空格 （1）trim （2）移除所有的空格
	 *
	 * @param string 原始字符串
	 * @return 过滤后的内容
	 */
	public static String trimAnyBlank(final String string) {
		if (StringUtil.isEmpty(string)) {
			return string;
		}

		String trim = string.trim();
		return trim.replaceAll("\\s+|\u0013", "");
	}

	/**
	 * 替换掉任意空格
	 *
	 * @param string      原始字符串
	 * @param replacement 待替换的文本
	 * @return 结果
	 */
	public static String replaceAnyBlank(final String string,
		final String replacement) {
		if (StringUtil.isEmpty(string)) {
			return string;
		}

		Matcher m = BLANK_PATTERN.matcher(string);
		String result = m.replaceAll(replacement);
		//160 &nbsp;
		result = result.replaceAll("\\u00A0", replacement);
		return result;
	}

	/**
	 * 替换掉任意空格为空
	 *
	 * @param string 原始字符串
	 * @return 结果
	 */
	public static String replaceAnyBlank(final String string) {
		return replaceAnyBlank(string, StringUtil.EMPTY);
	}

	/**
	 * 过滤掉所有的标点符号 （1）trim （2）移除标点符号 （3）移除 symbol
	 *
	 * @param string 原始字符串
	 * @return 过滤后的内容
	 */
	public static String trimAnyPunctionAndSymbol(final String string) {
		if (StringUtil.isEmpty(string)) {
			return string;
		}

		String trim = string.trim();
		return trim.replaceAll("\\p{P}|\\p{S}", "");
	}

	/**
	 * 获取的驼峰写法。 1.这是 mybatis-gen 源码
	 *
	 * @param inputString             输入字符串
	 * @param firstCharacterUppercase 首字母是否大写。
	 * @return 驼峰写法
	 */
	public static String getCamelCaseString(String inputString, boolean firstCharacterUppercase) {
		StringBuilder sb = new StringBuilder();

		boolean nextUpperCase = false;
		for (int i = 0; i < inputString.length(); i++) {
			char c = inputString.charAt(i);

			switch (c) {
				case '_':
				case '-':
				case '@':
				case '$':
				case '#':
				case ' ':
				case '/':
				case '&':
					if (sb.length() > 0) {
						nextUpperCase = true;
					}
					break;

				default:
					if (nextUpperCase) {
						sb.append(Character.toUpperCase(c));
						nextUpperCase = false;
					} else {
						sb.append(Character.toLowerCase(c));
					}
					break;
			}
		}

		if (firstCharacterUppercase) {
			sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
		}

		return sb.toString();
	}

	/**
	 * 首字母小写
	 *
	 * @param str 字符串
	 * @return 首字母小写字符串
	 */
	public static String firstToLowerCase(String str) {
		if (str == null || str.trim().length() == 0) {
			return str;
		}
		if (str.length() == 1) {
			return str.toLowerCase();
		}
		return str.substring(0, 1).toLowerCase() + str.substring(1);
	}

	/**
	 * 首字母大写
	 *
	 * @param str 字符串
	 * @return 首字母大写结果
	 */
	public static String firstToUpperCase(String str) {
		if (str == null || str.trim().length() == 0) {
			return str;
		}
		if (str.length() == 1) {
			return str.toUpperCase();
		}
		return str.substring(0, 1).toUpperCase() + str.substring(1);
	}

	/**
	 * 默认为 “” 1. 如果为 null TO "" 2. 返回本身
	 *
	 * @param string 字符串
	 * @return 非 null 的字符串
	 */
	public static String defaultEmpty(final String string) {
		if (isEmpty(string)) {
			return EMPTY;
		}
		return string;
	}

	/**
	 * 将数组进行逗号连接
	 *
	 * @param array object array
	 * @return join string
	 */
	public static String join(Object... array) {
		return join(array, PunctuationConst.COMMA);
	}

	/**
	 * 将数组进行连接
	 *
	 * @param array     object array
	 * @param separator 分隔符
	 * @return join string
	 * @see #join(Object[], String, int, int) 核心实现
	 */
	//public static String join(Object[] array, String separator) {
	//	final int endIndex = ArrayUtil.getEndIndex(-1, array);
	//	return join(array, separator, 0, endIndex);
	//}

	/**
	 * 拼接
	 *
	 * @param splitter 拼接符
	 * @param objects  结果
	 * @return 结果
	 */
	public static String join(String splitter, Object... objects) {
		return join(objects, splitter);
	}

	/**
	 * 将数组进行连接 from:    apache lang3
	 *
	 * @param array      object array
	 * @param separator  分隔符
	 * @param startIndex 开始下标
	 * @param endIndex   结束下标
	 * @return join string
	 */
	public static String join(Object[] array, String separator, int startIndex, int endIndex) {
		if (array == null) {
			return null;
		}

		if (separator == null) {
			separator = "";
		}

		int noOfItems = endIndex - startIndex;
		if (noOfItems < 0) {
			return "";
		} else {
			StringBuilder buf = new StringBuilder(noOfItems * 16);

			for (int i = startIndex; i <= endIndex; ++i) {
				if (i > startIndex) {
					buf.append(separator);
				}

				if (array[i] != null) {
					buf.append(array[i]);
				}
			}

			return buf.toString();
		}
	}

	/**
	 * 字符串拼接 将其范围扩展到对象列表 注意：如果有 null 属性，会导致直接报错。此处不再处理。
	 *
	 * @param collection 集合列表
	 * @param splitter   分隔符
	 * @param startIndex 开始下标
	 * @param endIndex   结束下标
	 * @param <E>        泛型
	 * @return 结果
	 */
	public static <E> String join(final Collection<E> collection, final String splitter,
		final int startIndex, final int endIndex) {
		if (CollectionUtil.isEmpty(collection)) {
			return StringUtil.EMPTY;
		}

		final String actualSplitter = StringUtil.nullToDefault(splitter, StringUtil.EMPTY);
		StringBuilder stringBuilder = new StringBuilder();

		Iterator<E> iterator = collection.iterator();
		// 循环直到 startIndex
		for (int i = 0; i < startIndex; i++) {
			iterator.next();
		}
		stringBuilder.append(iterator.next().toString());
		for (int i = startIndex; i < endIndex; i++) {
			stringBuilder.append(actualSplitter).append(iterator.next().toString());
		}
		return stringBuilder.toString();
	}

	/**
	 * 字符串拼接 将其范围扩展到对象列表 注意：如果有 null 属性，会导致直接报错。此处不再处理。
	 *
	 * @param collection 集合信息
	 * @param splitter   分隔符
	 * @param <E>        泛型
	 * @return 结果
	 */
	//public static <E> String join(final Collection<E> collection, final String splitter) {
	//	final int endIndex = CollectionUtil.getEndIndex(-1, collection);
	//	return join(collection, splitter, 0, endIndex);
	//}

	/**
	 * 字符串按逗号拼接拼接
	 *
	 * @param collection 集合信息
	 * @param <E>        泛型
	 * @return 结果
	 */
	//public static <E> String join(final Collection<E> collection) {
	//	return join(collection, PunctuationConst.COMMA);
	//}

	/**
	 * 驼峰命名转下划线
	 *
	 * @param camelStr 驼峰字符串
	 * @return 下划线字符串
	 */
	public static String camelToUnderline(String camelStr) {
		if (StringUtil.isEmpty(camelStr)) {
			return StringUtil.EMPTY;
		}

		StringBuilder sb = new StringBuilder();
		char[] chars = camelStr.toCharArray();
		for (char c : chars) {
			if (Character.isUpperCase(c)) {
				sb.append('_');
				sb.append(Character.toLowerCase(c));
			} else {
				sb.append(c);
			}
		}

		return sb.toString();
	}

	/**
	 * 下划线转驼峰命名
	 *
	 * @param underlineStr 下划线字符串
	 * @return 驼峰字符串
	 */
	public static String underlineToCamel(String underlineStr) {
		if (StringUtil.isEmpty(underlineStr)) {
			return StringUtil.EMPTY;
		}

		int len = underlineStr.length();
		StringBuilder sb = new StringBuilder(len);
		for (int i = 0; i < len; i++) {
			char c = underlineStr.charAt(i);
			if (c == '_') {
				if (++i < len) {
					sb.append(Character.toUpperCase(underlineStr.charAt(i)));
				}
			} else {
				sb.append(c);
			}
		}

		return sb.toString();
	}

	/**
	 * 重复多少次
	 *
	 * @param component 组成信息
	 * @param times     重复次数
	 * @return 重复多次的字符串结果
	 */
	public static String repeat(final String component, final int times) {
		if (StringUtil.isEmpty(component)
			|| times <= 0) {
			return StringUtil.EMPTY;
		}

		StringBuilder stringBuffer = new StringBuilder();
		for (int i = 0; i < times; i++) {
			stringBuffer.append(component);
		}

		return stringBuffer.toString();
	}

	/**
	 * 构建新的字符串
	 *
	 * @param original     原始对象
	 * @param middle       中间隐藏信息
	 * @param prefixLength 前边信息长度
	 * @return 构建后的新字符串
	 */
	public static String buildString(final Object original,
		final String middle,
		final int prefixLength) {
		if (ObjectUtil.isNull(original)) {
			return null;
		}

		final String string = original.toString();
		final int stringLength = string.length();

		String prefix = "";
		String suffix = "";

		if (stringLength >= prefixLength) {
			prefix = string.substring(0, prefixLength);
		} else {
			prefix = string.substring(0, stringLength);
		}

		int suffixLength = stringLength - prefix.length() - middle.length();
		if (suffixLength > 0) {
			suffix = string.substring(stringLength - suffixLength);
		}

		return prefix + middle + suffix;
	}

	/**
	 * 过滤掉空格
	 *
	 * @param original 原始字符串
	 * @return 过滤后的字符串
	 */
	public static String trim(final String original) {
		if (StringUtil.isEmpty(original)) {
			return original;
		}
		return original.trim();
	}

	/**
	 * 如果字符串是<code>null</code>，则返回指定默认字符串，否则返回字符串本身。
	 *
	 * <pre>
	 * nullToDefault(null, &quot;default&quot;)  = &quot;default&quot;
	 * nullToDefault(&quot;&quot;, &quot;default&quot;)    = &quot;&quot;
	 * nullToDefault(&quot;  &quot;, &quot;default&quot;)  = &quot;  &quot;
	 * nullToDefault(&quot;bat&quot;, &quot;default&quot;) = &quot;bat&quot;
	 * </pre>
	 *
	 * @param str        要转换的字符串
	 * @param defaultStr 默认字符串
	 * @return 字符串本身或指定的默认字符串
	 */
	public static String nullToDefault(CharSequence str, String defaultStr) {
		return (str == null) ? defaultStr : str.toString();
	}

	/**
	 * 将已有字符串填充为规定长度，如果已有字符串超过这个长度则返回这个字符串
	 *
	 * @param str        被填充的字符串
	 * @param filledChar 填充的字符
	 * @param len        填充长度
	 * @param isPre      是否填充在前
	 * @return 填充后的字符串
	 */
	public static String fill(String str, char filledChar, int len, boolean isPre) {
		final int strLen = str.length();
		if (strLen > len) {
			return str;
		}

		String filledStr = StringUtil.repeat(String.valueOf(filledChar), len - strLen);
		return isPre ? filledStr.concat(str) : str.concat(filledStr);
	}

	/**
	 * 对象转换为字符串 1. 对数组特殊处理 {@link Arrays#toString(Object[])} 避免打印无意义的信息（v0.1.14）
	 *
	 * @param object          对象
	 * @param defaultWhenNull 对象为空时的默认值
	 * @return 结果
	 */
	public static String objectToString(final Object object,
		final String defaultWhenNull) {
		if (ObjectUtil.isNull(object)) {
			return defaultWhenNull;
		}
		Class type = object.getClass();
		if (ClassTypeUtil.isArray(type)) {
			Object[] arrays = (Object[]) object;
			return Arrays.toString(arrays);
		}
		return object.toString();
	}

	/**
	 * 对象转换为字符串 1. 默认为空时返回 null
	 *
	 * @param object 对象
	 * @return 结果
	 */
	public static String objectToString(final Object object) {
		return objectToString(object, null);
	}

	/**
	 * 对 single 的信息重复多次
	 *
	 * @param single 单个字符
	 * @param times  重复次数
	 * @return 结果
	 * @see #repeat(String, int) 重复
	 */
	@Deprecated
	public static String times(final String single,
		final int times) {
		if (StringUtil.isEmpty(single)) {
			return single;
		}
		if (times <= 0) {
			return single;
		}

		StringBuilder stringBuilder = new StringBuilder();
		for (int i = 0; i < times; i++) {
			stringBuilder.append(single);
		}
		return stringBuilder.toString();
	}

	/**
	 * 首字母大写
	 *
	 * @param string 字符串
	 * @return 大写的结果
	 */
	public static String capitalFirst(final String string) {
		if (StringUtil.isEmpty(string)) {
			return string;
		}

		if (string.length() <= 1) {
			return string.toUpperCase();
		}

		char capitalChar = Character.toUpperCase(string.charAt(0));
		return capitalChar + string.substring(1);
	}

	/**
	 * 严格拆分 【传统拆分】 1:2:3:31::32:4 结果是：[1, 2, 3, 31, , 32, 4]
	 * <p>
	 * 【严格拆分】 严格匹配 : 拆分符，如果有多个，则不进行拆分。 结果：[1, 2, 3, 31::32, 4]
	 * <p>
	 * 实现逻辑： （1）根据 index 获取所有的下标。+length（当前步长） （2）获取当前的所有拆分下标，获取 times+1 的拆分下标 （3）从当前下标中移除 times+1
	 * 的下标。并且移除连续的信息。 连续：times+1 的下标，后续的 times 步长。如果不连续，则中断。 （4）根据过滤后的列表生成最后的结果。
	 *
	 * @param string    原始字符串
	 * @param splitUnit 分隔单元
	 * @param times     次数
	 * @return 结果
	 */
	public static List<String> splitStrictly(final String string,
		final char splitUnit,
		final int times) {
		if (StringUtil.isEmpty(string)) {
			return Collections.emptyList();
		}
		if (times <= 0) {
			return Collections.singletonList(string);
		}

		// 分别获取索引列表
		final String split = CharUtil.repeat(splitUnit, times);
		final String moreSplit = CharUtil.repeat(splitUnit, times + 1);
		final List<Integer> splitIndexList = getIndexList(string, split);
		final List<Integer> moreSplitIndexList = getIndexList(string, moreSplit);

		// 移除重复下标
		final List<Integer> removeIndexList = getSerialFilterList(splitIndexList,
			moreSplitIndexList, times);

		// 构建结果列表
		Collection<Integer> trimIndexList = CollectionUtil.difference(splitIndexList,
			removeIndexList);
		return subStringList(string, trimIndexList, times);
	}

	/**
	 * 获取满足条件连续的列表 （1）当前信息 （2）连续的索引信息
	 *
	 * @param allList    所有的整数
	 * @param filterList 待排除的整数
	 * @param step       步长
	 * @return 结果列表
	 */
	private static List<Integer> getSerialFilterList(final List<Integer> allList,
		final List<Integer> filterList, final int step) {
		List<Integer> resultList = Lists.newArrayList();

		resultList.addAll(filterList);
		// 根据 index+times 为步长进行连续判断。不存在则跳过
		for (Integer filter : filterList) {
			// 从匹配的下一个元素开始
			final int startIndex = allList.indexOf(filter) + 1;
			int stepTimes = 1;
			for (int i = startIndex; i < allList.size(); i++) {
				final Integer indexVal = allList.get(i);
				final int nextVal = step * stepTimes + filter;
				if (indexVal.equals(nextVal)) {
					resultList.add(nextVal);
				} else {
					// 跳出当前循环
					break;
				}
				stepTimes++;
			}
		}

		return resultList;
	}

	/**
	 * 根据下标截取列表
	 * <p>
	 * 【最后的截取问题】 最后构建的结果： string=1::2::3:31:::32::4: index=[1,4,15] ignore=2
	 * <p>
	 * 每次截取： [0,1) [1+2,4) [15+2,]
	 *
	 * @param string          原始字符串
	 * @param indexCollection 下标列表
	 * @param ignoreLength    每次忽略跳过的长度。用于跳过 split 字符。
	 * @return 结果列表
	 */
	public static List<String> subStringList(final String string,
		final Collection<Integer> indexCollection,
		final int ignoreLength) {
		if (StringUtil.isEmpty(string)) {
			return Collections.emptyList();
		}
		if (CollectionUtil.isEmpty(indexCollection)) {
			return Collections.singletonList(string);
		}

		List<String> resultList = Lists.newArrayList();
		int startIndex = 0;
		for (Integer index : indexCollection) {
			// 最后的位置添加空字符串
			if (startIndex > string.length() - 1) {
				resultList.add(StringUtil.EMPTY);
				break;
			}
			String subString = string.substring(startIndex, index);
			resultList.add(subString);
			startIndex = index + ignoreLength;
		}
		// 最后的结果信息
		if (startIndex < string.length()) {
			String subString = string.substring(startIndex);
			resultList.add(subString);
		}

		return resultList;
	}

	/**
	 * 获取所有符合条件的下标类表 【下标】 1:2:3:31::32:4:
	 * <p>
	 * [1, 3, 5, 8, 9, 12, 14]
	 * <p>
	 * 问题：这个下标没有过滤 split。 如果想过滤分隔符，应该如下： (0,1) (1+split.length, 3) ... 1,2,
	 *
	 * @param string 原始字符串
	 * @param split  分隔字符串
	 * @return 下标列表
	 */
	public static List<Integer> getIndexList(final String string,
		final String split) {
		if (StringUtil.isEmpty(string)
			|| StringUtil.isEmpty(split)) {
			return Collections.emptyList();
		}

		List<Integer> indexList = Lists.newArrayList();
		int startIndex = 0;
		while (startIndex < string.length()) {
			startIndex = string.indexOf(split, startIndex);
			if (startIndex < 0) {
				break;
			}
			indexList.add(startIndex);
			startIndex += split.length();
		}
		return indexList;
	}


	/**
	 * 获取字符串对应的下标信息
	 *
	 * @param string             字符串
	 * @param symbol             分隔符
	 * @param ignoreDoubleQuotes 是否忽略双引号中的信息
	 * @return 结果列表
	 */
	public static List<Integer> getIndexList(final String string,
		final char symbol,
		final boolean ignoreDoubleQuotes) {
		if (StringUtil.isEmpty(string)) {
			return Collections.emptyList();
		}

		List<Integer> resultList = Lists.newArrayList();
		char[] chars = string.toCharArray();

		boolean doubleQuotesStart = false;
		char preChar = CommonConstant.BLANK;
		for (int i = 0; i < chars.length; i++) {
			char currentChar = chars[i];

			preChar = getPreChar(preChar, currentChar);
			// 上一个字符不是转义，且当前为 "。则进行状态的切换
			if (CommonConstant.BACK_SLASH != preChar
				&& CommonConstant.DOUBLE_QUOTES == currentChar) {
				doubleQuotesStart = !doubleQuotesStart;
			}

			// 等于且不在双引号中。
			if (currentChar == symbol) {
				// 忽略双引号中的信息 && 不在双引号中。
				if (ignoreDoubleQuotes) {
					if (!doubleQuotesStart) {
						resultList.add(i);
					}
				} else {
					resultList.add(i);
				}
			}
		}
		return resultList;
	}

	/**
	 * 获取上一个字符
	 * <p>
	 * 保证转义字符的两次抵消。
	 *
	 * @param preChar     上一个字符
	 * @param currentChar 当前字符
	 * @return 结果
	 */
	@Deprecated
	private static char getPreChar(final char preChar, final char currentChar) {
		// 判断前一个字符是什么
		if (CommonConstant.BACK_SLASH == preChar
			&& CommonConstant.BACK_SLASH == currentChar) {
			return CommonConstant.BLANK;
		}
		return currentChar;
	}

	/**
	 * 根据索引下标直接拆分
	 *
	 * @param string    原始字符串
	 * @param indexList 结果列表
	 * @return 结果
	 */
	public static List<String> splitByIndexes(final String string, final List<Integer> indexList) {
		if (StringUtil.isEmpty(string)) {
			return Collections.emptyList();
		}
		if (CollectionUtil.isEmpty(indexList)) {
			return Collections.singletonList(string);
		}

		List<String> resultList = Lists.newArrayList();

		int preIndex = 0;
		for (Integer anIndexList : indexList) {
			int currentIndex = anIndexList;
			if (currentIndex > preIndex) {
				resultList.add(string.substring(preIndex, currentIndex));
			}
			preIndex = currentIndex + 1;
		}
		// 判断最后一个下标
		final int lastIndex = indexList.get(indexList.size() - 1);
		if (lastIndex + 1 < string.length()) {
			resultList.add(string.substring(lastIndex + 1));
		}
		return resultList;
	}

	/**
	 * 字符串转字节数组
	 *
	 * @param string 字符串
	 * @return 字节数组
	 */
	public static byte[] stringToBytes(final String string) {
		if (ObjectUtil.isNull(string)) {
			return null;
		}

		return string.getBytes();
	}

	/**
	 * 字节数组转字符串
	 *
	 * @param bytes 字节数组
	 * @return 字符串
	 */
	public static String bytesToString(final byte[] bytes) {
		if (ArrayPrimitiveUtil.isEmpty(bytes)) {
			return null;
		}

		return new String(bytes);
	}

	/**
	 * 拆分为字符串数组
	 *
	 * @param string   字符串
	 * @param splitter 拆分符号
	 * @return 字符串数组
	 */
	public static String[] splitToStringArray(final String string, final String splitter) {
		if (StringUtil.isEmpty(string)) {
			return null;
		}

		return string.split(splitter);
	}

	/**
	 * 拆分为字符串数组
	 *
	 * @param string 字符串
	 * @return 字符串数组
	 */
	public static String[] splitToStringArray(final String string) {
		return splitToStringArray(string, PunctuationConst.COMMA);
	}

	/**
	 * 数组拼接为字符串
	 *
	 * @param array     数组
	 * @param splitters 分隔符
	 * @return 拼接结果
	 */
	public static String join(final byte[] array, final String... splitters) {
		if (ArrayPrimitiveUtil.isEmpty(array)) {
			return StringUtil.EMPTY;
		}

		String splitter = getSplitter(splitters);
		List<Byte> lists = ArrayPrimitiveUtil.toList(array, new IHandler<Byte, Byte>() {
			@Override
			public Byte handle(Byte value) {
				return value;
			}
		});
		return join(lists, splitter);
	}

	/**
	 * 数组拼接为字符串
	 *
	 * @param array     数组
	 * @param splitters 分隔符
	 * @return 拼接结果
	 */
	public static String join(final char[] array, final String... splitters) {
		if (ArrayPrimitiveUtil.isEmpty(array)) {
			return StringUtil.EMPTY;
		}

		String splitter = getSplitter(splitters);
		List<Character> lists = ArrayPrimitiveUtil.toList(array,
			new IHandler<Character, Character>() {
				@Override
				public Character handle(Character value) {
					return value;
				}
			});
		return join(lists, splitter);
	}

	/**
	 * 数组拼接为字符串
	 *
	 * @param array     数组
	 * @param splitters 分隔符
	 * @return 拼接结果
	 */
	public static String join(final short[] array, final String... splitters) {
		if (ArrayPrimitiveUtil.isEmpty(array)) {
			return StringUtil.EMPTY;
		}

		String splitter = getSplitter(splitters);
		List<Short> lists = ArrayPrimitiveUtil.toList(array, new IHandler<Short, Short>() {
			@Override
			public Short handle(Short value) {
				return value;
			}
		});
		return join(lists, splitter);
	}

	/**
	 * 数组拼接为字符串
	 *
	 * @param array     数组
	 * @param splitters 分隔符
	 * @return 拼接结果
	 */
	public static String join(final long[] array, final String... splitters) {
		if (ArrayPrimitiveUtil.isEmpty(array)) {
			return StringUtil.EMPTY;
		}

		String splitter = getSplitter(splitters);
		List<Long> lists = ArrayPrimitiveUtil.toList(array, new IHandler<Long, Long>() {
			@Override
			public Long handle(Long value) {
				return value;
			}
		});
		return join(lists, splitter);
	}

	/**
	 * 数组拼接为字符串
	 *
	 * @param array     数组
	 * @param splitters 分隔符
	 * @return 拼接结果
	 */
	public static String join(final float[] array, final String... splitters) {
		if (ArrayPrimitiveUtil.isEmpty(array)) {
			return StringUtil.EMPTY;
		}

		String splitter = getSplitter(splitters);
		List<Float> lists = ArrayPrimitiveUtil.toList(array, new IHandler<Float, Float>() {
			@Override
			public Float handle(Float value) {
				return value;
			}
		});
		return join(lists, splitter);
	}

	/**
	 * 数组拼接为字符串
	 *
	 * @param array     数组
	 * @param splitters 分隔符
	 * @return 拼接结果
	 */
	public static String join(final double[] array, final String... splitters) {
		if (ArrayPrimitiveUtil.isEmpty(array)) {
			return StringUtil.EMPTY;
		}

		String splitter = getSplitter(splitters);
		List<Double> lists = ArrayPrimitiveUtil.toList(array, new IHandler<Double, Double>() {
			@Override
			public Double handle(Double value) {
				return value;
			}
		});
		return join(lists, splitter);
	}

	/**
	 * 数组拼接为字符串
	 *
	 * @param array     数组
	 * @param splitters 分隔符
	 * @return 拼接结果
	 */
	public static String join(final boolean[] array, final String... splitters) {
		if (ArrayPrimitiveUtil.isEmpty(array)) {
			return StringUtil.EMPTY;
		}

		String splitter = getSplitter(splitters);
		List<Boolean> lists = ArrayPrimitiveUtil.toList(array, new IHandler<Boolean, Boolean>() {
			@Override
			public Boolean handle(Boolean value) {
				return value;
			}
		});
		return join(lists, splitter);
	}

	/**
	 * 数组拼接为字符串
	 *
	 * @param array     数组
	 * @param splitters 分隔符
	 * @return 拼接结果
	 */
	public static String join(final int[] array, final String... splitters) {
		if (ArrayPrimitiveUtil.isEmpty(array)) {
			return StringUtil.EMPTY;
		}

		String splitter = getSplitter(splitters);
		List<Integer> integers = ArrayPrimitiveUtil.toList(array, new IHandler<Integer, Integer>() {
			@Override
			public Integer handle(Integer integer) {
				return integer;
			}
		});
		return join(integers, splitter);
	}

	/**
	 * 获取指定的分隔符
	 *
	 * @param splitters 分隔符
	 * @return 字符串
	 */
	private static String getSplitter(final String... splitters) {
		if (ArrayUtil.isEmpty(splitters)) {
			return PunctuationConst.COMMA;
		}

		return splitters[0];
	}

	/**
	 * 拆分为列表
	 *
	 * @param string   字符串
	 * @param splitter 分隔符号
	 * @return 字符串列表
	 */
	public static List<String> splitToList(final String string,
		final String splitter) {
		ArgUtil.notEmpty(splitter, "splitter");

		if (StringUtil.isEmpty(string)) {
			return Lists.newArrayList();
		}

		String[] strings = string.split(splitter);
		return Lists.newArrayList(strings);
	}

	/**
	 * 拆分为列表
	 *
	 * @param string 字符串
	 * @return 字符串列表
	 */
	public static List<String> splitToList(final String string) {
		return splitToList(string, PunctuationConst.COMMA);
	}


	/**
	 * 转换为数组字符
	 *
	 * @param string 字符串
	 * @return 结果
	 */
	public static Character[] toCharacterArray(final String string) {
		final char[] chars = string.toCharArray();
		Character[] newArray = new Character[chars.length];

		for (int i = 0; i < chars.length; i++) {
			newArray[i] = chars[i];
		}

		return newArray;
	}

	/**
	 * 转换为列表字符
	 *
	 * @param string 字符串
	 * @return 结果
	 */
	public static List<Character> toCharacterList(final String string) {
		final char[] chars = string.toCharArray();
		List<Character> newList = new ArrayList<>(chars.length);

		for (char aChar : chars) {
			newList.add(aChar);
		}

		return newList;
	}

	/**
	 * 转换为 char 字符串列表
	 *
	 * @param string 字符串
	 * @return 字符串列表
	 */
	public static List<String> toCharStringList(final String string) {
		if (StringUtil.isEmpty(string)) {
			return Lists.newArrayList();
		}

		char[] chars = string.toCharArray();
		return ArrayPrimitiveUtil.toList(chars, new IHandler<Character, String>() {
			@Override
			public String handle(Character character) {
				return String.valueOf(character);
			}
		});
	}

	/**
	 * 将字符串中的全角字符转为半角
	 *
	 * @param string 字符串
	 * @return 转换之后的字符串
	 */
	public static String toHalfWidth(final String string) {
		return characterHandler(string, new IHandler<Character, Character>() {
			@Override
			public Character handle(Character character) {
				return CharUtil.toHalfWidth(character);
			}
		});
	}

	/**
	 * 将字符串中的半角字符转为全角
	 *
	 * @param string 字符串
	 * @return 转换之后的字符串
	 */
	public static String toFullWidth(final String string) {
		return characterHandler(string, new IHandler<Character, Character>() {
			@Override
			public Character handle(Character character) {
				return CharUtil.toFullWidth(character);
			}
		});
	}

	/**
	 * 字符的处理
	 *
	 * @param string  字符串
	 * @param handler 处理类
	 * @return 结果
	 */
	private static String characterHandler(final String string,
		final IHandler<Character, Character> handler) {
		if (StringUtil.isEmpty(string)) {
			return string;
		}

		//1. 转换为列表
		char[] chars = string.toCharArray();
		char[] resultChars = new char[chars.length];
		for (int i = 0; i < chars.length; i++) {
			resultChars[i] = handler.handle(chars[i]);
		}

		//2. 构建结果
		return new String(resultChars);
	}

	/**
	 * 过滤掉非中文字符
	 *
	 * @param string 字符串
	 * @return 结果
	 */
	public static String trimNotChinese(final String string) {
		if (StringUtil.isEmptyTrim(string)) {
			return StringUtil.EMPTY;
		}

		char[] chars = string.toCharArray();
		StringBuilder stringBuilder = new StringBuilder();
		for (Character character : chars) {
			if (CharUtil.isChinese(character)) {
				stringBuilder.append(character);
			}
		}

		return stringBuilder.toString();
	}

	/**
	 * 避免默认实现的问题
	 *
	 * @param object 对象
	 * @return 结果
	 * @see String#valueOf(Object) 默认实现会把 null 转换为 "null"
	 */
	public static String valueOf(final Object object) {
		if (ObjectUtil.isNull(object)) {
			return null;
		}

		return String.valueOf(object);
	}

	/**
	 * 左补信息
	 *
	 * @param original     原始字符串
	 * @param targetLength 目标长度
	 * @param unit         补的元素
	 * @return 结果
	 */
	public static String leftPadding(final String original,
		final int targetLength,
		final char unit) {
		ArgUtil.notNull(original, "original");

		//1. fast-return
		final int originalLength = original.length();
		if (originalLength >= targetLength) {
			return original;
		}

		//2. 循环补零
		StringBuilder stringBuilder = new StringBuilder(targetLength);
		for (int i = originalLength; i < targetLength; i++) {
			stringBuilder.append(unit);
		}
		stringBuilder.append(original);

		return stringBuilder.toString();
	}

	/**
	 * 左补信息 默认左补零 0
	 *
	 * @param original     原始字符串
	 * @param targetLength 目标长度
	 * @return 结果
	 */
	public static String leftPadding(final String original,
		final int targetLength) {
		return leftPadding(original, targetLength, '0');
	}

	/**
	 * 获取第一个字符
	 *
	 * @param text 文本
	 * @return 结果
	 */
	public static Character getFirstChar(final String text) {
		if (StringUtil.isEmpty(text)) {
			return null;
		}

		return text.charAt(0);
	}

	/**
	 * 空转换为 null
	 *
	 * @param text 文本
	 * @return 结果
	 */
	public static String emptyToNull(String text) {
		if (StringUtil.isEmpty(text)) {
			return null;
		}
		return text;
	}

	/**
	 * 转换为 boolean 类型
	 *
	 * @param text 文本
	 * @return 结果
	 */
	public static Boolean toBool(final String text) {
		return "YES".equalsIgnoreCase(text)
			|| "TRUE".equalsIgnoreCase(text)
			|| "1".equalsIgnoreCase(text);
	}

	/**
	 * 转换为 boolean 类型
	 *
	 * @param text 文本
	 * @return 结果
	 */
	public static Character toChar(final String text) {
		if (isEmpty(text)) {
			return null;
		}

		return text.charAt(0);
	}

	/**
	 * 转换为 Byte 类型
	 *
	 * @param text 文本
	 * @return 结果
	 */
	public static Byte toByte(final String text) {
		if (isEmpty(text)) {
			return null;
		}

		return Byte.valueOf(text);
	}

	/**
	 * 转换为 Short 类型
	 *
	 * @param text 文本
	 * @return 结果
	 */
	public static Short toShort(final String text) {
		if (isEmpty(text)) {
			return null;
		}

		return Short.valueOf(text);
	}

	/**
	 * 转换为 Integer 类型
	 *
	 * @param text 文本
	 * @return 结果
	 */
	public static Integer toInt(final String text) {
		if (isEmpty(text)) {
			return null;
		}

		return Integer.valueOf(text);
	}

	/**
	 * 转换为 Long 类型
	 *
	 * @param text 文本
	 * @return 结果
	 */
	public static Long toLong(final String text) {
		if (isEmpty(text)) {
			return null;
		}

		return Long.valueOf(text);
	}

	/**
	 * 转换为 Float 类型
	 *
	 * @param text 文本
	 * @return 结果
	 */
	public static Float toFloat(final String text) {
		if (isEmpty(text)) {
			return null;
		}

		return Float.valueOf(text);
	}

	/**
	 * 转换为 Float 类型
	 *
	 * @param text 文本
	 * @return 结果
	 */
	public static Double toDouble(final String text) {
		if (isEmpty(text)) {
			return null;
		}

		return Double.valueOf(text);
	}

	/**
	 * 转换为 BigInteger 类型
	 *
	 * @param text 文本
	 * @return 结果
	 */
	public static BigInteger toBigInteger(final String text) {
		if (isEmpty(text)) {
			return null;
		}

		return new BigInteger(text);
	}

	/**
	 * 转换为 BigDecimal 类型
	 *
	 * @param text 文本
	 * @return 结果
	 */
	public static BigDecimal toBigDecimal(final String text) {
		if (isEmpty(text)) {
			return null;
		}

		return new BigDecimal(text);
	}

	/**
	 * 转换为 Date 类型
	 *
	 * @param text       文本
	 * @param dateFormat 格式化
	 * @return 结果
	 */
	public static Date toDate(final String text, final String dateFormat) {
		if (isEmpty(text)) {
			return null;
		}

		return DateUtil.getFormatDate(text, dateFormat);
	}

	/**
	 * 转换为 BigDecimal 类型
	 *
	 * @param text 文本
	 * @return 结果
	 */
	public static Date toDate(final String text) {
		return toDate(text, DateUtil.PURE_DATE_FORMAT);
	}

	/**
	 * 转换为字符串
	 *
	 * @param date   日期
	 * @param format 格式化
	 * @return 结果
	 */
	public static String toString(Date date, String format) {
		return DateUtil.getDateFormat(date, format);
	}

	/**
	 * 转换为字符串
	 *
	 * @param date 日期
	 * @return 结果
	 */
	public static String toString(Date date) {
		return toString(date, DateUtil.PURE_DATE_FORMAT);
	}

	/**
	 * 转换为字符串
	 *
	 * @param object 对象
	 * @return 结果
	 */
	public static String toString(Object object) {
		if (null == object) {
			return null;
		}

		return object.toString();
	}

	/**
	 * 转换为字符串
	 *
	 * @param bytes   字节
	 * @param charset 编码
	 * @return 结果
	 */
	public static String toString(byte[] bytes, String charset) {
		try {
			return new String(bytes, charset);
		} catch (UnsupportedEncodingException e) {
			throw new CommonRuntimeException(e);
		}
	}

	/**
	 * 转换为字符串
	 *
	 * @param bytes 字节
	 * @return 结果
	 */
	public static String toString(byte[] bytes) {
		return toString(bytes, CommonConstant.UTF8);
	}

	/**
	 * 转换为 bytes
	 *
	 * @param text    文本
	 * @param charset 编码
	 * @return 结果
	 */
	public static byte[] getBytes(String text, String charset) {
		try {
			return text.getBytes(charset);
		} catch (UnsupportedEncodingException e) {
			throw new CommonRuntimeException(e);
		}
	}

	/**
	 * 转换为 bytes
	 *
	 * @param text 文本
	 * @return 结果
	 */
	public static byte[] getBytes(String text) {
		return getBytes(text, CommonConstant.UTF8);
	}

	/**
	 * 是否全部是英文
	 *
	 * @param text 文本
	 * @return 结果
	 */
	public static boolean isEnglish(String text) {
		if (StringUtil.isEmpty(text)) {
			return false;
		}

		char[] chars = text.toCharArray();
		for (char c : chars) {
			if (!CharUtil.isEnglish(c)) {
				return false;
			}
		}

		return true;
	}

	/**
	 * 是否全部是中文
	 *
	 * @param text 文本
	 * @return 结果
	 */
	public static boolean isChinese(String text) {
		if (StringUtil.isEmpty(text)) {
			return false;
		}

		char[] chars = text.toCharArray();
		for (char c : chars) {
			if (!CharUtil.isChinese(c)) {
				return false;
			}
		}

		return true;
	}

	/**
	 * 包信息调整为路径信息
	 *
	 * @param packageName 包信息
	 * @return 結果
	 */
	public static String packageToPath(String packageName) {
		if (StringUtil.isEmpty(packageName)) {
			return packageName;
		}

		return packageName.replaceAll("\\.", "/");
	}

	/**
	 * 字符串截取
	 *
	 * @param text       文本
	 * @param startIndex 开始位置
	 * @param length     长度
	 * @return 结果
	 */
	public static String subString(String text, int startIndex, int length) {
		if (StringUtil.isEmpty(text)) {
			return text;
		}

		// 长度
		if (length <= 0) {
			return null;
		}

		//避免越界
		int endIndex = startIndex + length;
		if (endIndex > text.length()) {
			endIndex = text.length();
		}

		return text.substring(startIndex, endIndex);
	}

	/**
	 * 在不同的操作系统中，对换号符的定义是不同的，比如：
	 * <p>
	 * 1. \n unix,linux系统，好像新的mac也是这样的。
	 * <p>
	 * 2. \r 有的mac系统
	 * <p>
	 * 3. \r\n window系统。
	 * <p>
	 * 自己观察，你会发现规律，其实用一个正则表达式就可以满足： \r?\n
	 *
	 * @param content 内容
	 * @return 结果
	 */
	public static List<String> contentToLines(String content) {
		if (content == null) {
			return null;
		}

		// 根据换行符分割
		String[] strings = content.split("\\r?\\n");
		return ArrayUtil.toList(strings);
	}

	/**
	 * 字符串按照换行符拼接为新的内容
	 *
	 * @param lines 行
	 * @return 结果
	 */
	public static String linesToContent(List<String> lines) {
		if (CollectionUtil.isEmpty(lines)) {
			return null;
		}

		StringBuilder stringBuilder = new StringBuilder();

		for (int i = 0; i < lines.size() - 1; i++) {
			stringBuilder.append(lines.get(i))
				.append(SystemUtil.getLineSeparator());
		}

		stringBuilder.append(lines.get(lines.size() - 1));

		return stringBuilder.toString();
	}

	/**
	 * 根据长度进行文本截断
	 *
	 * @param text      文本
	 * @param limitSize 限制长度
	 * @return 结果列表
	 */
	public static List<String> splitByLength(String text, int limitSize) {
		if (StringUtil.isEmpty(text)) {
			return Collections.emptyList();
		}

		final int totalLength = text.length();

		int times = totalLength / limitSize;
		if (totalLength % limitSize != 0) {
			times++;
		}

		List<String> resultList = new ArrayList<>(times);

		for (int i = 0; i < times; i++) {
			int startIndex = i * limitSize;
			int endIndex = (i + 1) * limitSize;

			// 越界处理
			if (endIndex > totalLength) {
				endIndex = totalLength;
			}
			String subText = text.substring(startIndex, endIndex);
			resultList.add(subText);
		}

		return resultList;
	}


	private static final char SEPARATOR = '_';

	private static final String UNKNOWN = "unknown";


	/**
	 * 特殊字符正则，sql特殊字符和空白符
	 */
	private final static Pattern SPECIAL_CHARS_REGEX = Pattern.compile("[`'\"|/,;()-+*%#·•�　\\s]");
	/**
	 * <p>The maximum size to which the padding constant(s) can expand.</p>
	 */
	private static final int PAD_LIMIT = 8192;

	/**
	 * 驼峰命名法工具
	 *
	 * @return toCamelCase(" hello_world ") == "helloWorld" toCapitalizeCamelCase("hello_world") ==
	 * "HelloWorld" toUnderScoreCase("helloWorld") = "hello_world"
	 */
	public static String toCamelCase(String s) {
		if (s == null) {
			return null;
		}

		s = s.toLowerCase();

		StringBuilder sb = new StringBuilder(s.length());
		boolean upperCase = false;
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);

			if (c == SEPARATOR) {
				upperCase = true;
			} else if (upperCase) {
				sb.append(Character.toUpperCase(c));
				upperCase = false;
			} else {
				sb.append(c);
			}
		}

		return sb.toString();
	}

	/**
	 * 驼峰命名法工具
	 *
	 * @return toCamelCase(" hello_world ") == "helloWorld" toCapitalizeCamelCase("hello_world") ==
	 * "HelloWorld" toUnderScoreCase("helloWorld") = "hello_world"
	 */
	public static String toCapitalizeCamelCase(String s) {
		if (s == null) {
			return null;
		}
		s = toCamelCase(s);
		return s.substring(0, 1).toUpperCase() + s.substring(1);
	}

	/**
	 * 驼峰命名法工具
	 *
	 * @return toCamelCase(" hello_world ") == "helloWorld" toCapitalizeCamelCase("hello_world") ==
	 * "HelloWorld" toUnderScoreCase("helloWorld") = "hello_world"
	 */
	static String toUnderScoreCase(String s) {
		if (s == null) {
			return null;
		}

		StringBuilder sb = new StringBuilder();
		boolean upperCase = false;
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);

			boolean nextUpperCase = true;

			if (i < (s.length() - 1)) {
				nextUpperCase = Character.isUpperCase(s.charAt(i + 1));
			}

			if ((i > 0) && Character.isUpperCase(c)) {
				if (!upperCase || !nextUpperCase) {
					sb.append(SEPARATOR);
				}
				upperCase = true;
			} else {
				upperCase = false;
			}

			sb.append(Character.toLowerCase(c));
		}

		return sb.toString();
	}

	/**
	 * 根据ip获取详细地址
	 */
	public static String getCityInfo(String ip) {
		String api = String.format(CommonConstant.IP_URL, ip);
		JSONObject object = JSONUtil.parseObj(HttpUtil.get(api));
		return object.get("addr", String.class);
	}

	public static String getBrowser(HttpServletRequest request) {
		UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
		Browser browser = userAgent.getBrowser();
		return browser.getName();
	}

	/**
	 * 获得当天是周几
	 */
	public static String getWeekDay() {
		String[] weekDays = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());

		int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
		if (w < 0) {
			w = 0;
		}
		return weekDays[w];
	}

	/**
	 * 首字母变小写
	 *
	 * @param str 字符串
	 * @return {String}
	 */
	public static String firstCharToLower(String str) {
		char firstChar = str.charAt(0);
		if (firstChar >= CharPool.UPPER_A && firstChar <= CharPool.UPPER_Z) {
			char[] arr = str.toCharArray();
			arr[0] += (CharPool.LOWER_A - CharPool.UPPER_A);
			return new String(arr);
		}
		return str;
	}

	/**
	 * 首字母变大写
	 *
	 * @param str 字符串
	 * @return {String}
	 */
	public static String firstCharToUpper(String str) {
		char firstChar = str.charAt(0);
		if (firstChar >= CharPool.LOWER_A && firstChar <= CharPool.LOWER_Z) {
			char[] arr = str.toCharArray();
			arr[0] -= (CharPool.LOWER_A - CharPool.UPPER_A);
			return new String(arr);
		}
		return str;
	}

	/**
	 * Check whether the given {@code CharSequence} contains actual <em>text</em>.
	 * <p>More specifically, this method returns {@code true} if the
	 * {@code CharSequence} is not {@code null}, its length is greater than 0, and it contains at
	 * least one non-whitespace character.
	 * <pre class="code">
	 * StringUtil.isBlank(null) = true
	 * StringUtil.isBlank("") = true
	 * StringUtil.isBlank(" ") = true
	 * StringUtil.isBlank("12345") = false
	 * StringUtil.isBlank(" 12345 ") = false
	 * </pre>
	 *
	 * @param cs the {@code CharSequence} to check (may be {@code null})
	 * @return {@code true} if the {@code CharSequence} is not {@code null}, its length is greater
	 * than 0, and it does not contain whitespace only
	 * @see Character#isWhitespace
	 */
	public static boolean isBlank(@Nullable final CharSequence cs) {
		return !StringUtil.hasText(cs);
	}

	/**
	 * <p>Checks if a CharSequence is not empty (""), not null and not whitespace only.</p>
	 * <pre>
	 * StringUtil.isNotBlank(null)	  = false
	 * StringUtil.isNotBlank("")		= false
	 * StringUtil.isNotBlank(" ")	   = false
	 * StringUtil.isNotBlank("bob")	 = true
	 * StringUtil.isNotBlank("  bob  ") = true
	 * </pre>
	 *
	 * @param cs the CharSequence to check, may be null
	 * @return {@code true} if the CharSequence is not empty and not null and not whitespace
	 * @see Character#isWhitespace
	 */
	public static boolean isNotBlank(@Nullable final CharSequence cs) {
		return StringUtil.hasText(cs);
	}

	/**
	 * 有 任意 一个 Blank
	 *
	 * @param css CharSequence
	 * @return boolean
	 */
	public static boolean isAnyBlank(final CharSequence... css) {
		if (ObjectUtil.isEmpty(css)) {
			return true;
		}
		return Stream.of(css).anyMatch(StringUtil::isBlank);
	}

	/**
	 * 有 任意 一个 Blank
	 *
	 * @param css CharSequence
	 * @return boolean
	 */
	public static boolean isAnyBlank(Collection<CharSequence> css) {
		if (CollectionUtil.isEmpty(css)) {
			return true;
		}
		return css.stream().anyMatch(StringUtil::isBlank);
	}

	/**
	 * 是否全非 Blank
	 *
	 * @param css CharSequence
	 * @return boolean
	 */
	public static boolean isNoneBlank(final CharSequence... css) {
		if (ObjectUtil.isEmpty(css)) {
			return false;
		}
		return Stream.of(css).allMatch(StringUtil::isNotBlank);
	}

	/**
	 * 是否全非 Blank
	 *
	 * @param css CharSequence
	 * @return boolean
	 */
	public static boolean isNoneBlank(Collection<CharSequence> css) {
		if (CollectionUtil.isEmpty(css)) {
			return false;
		}
		return css.stream().allMatch(StringUtil::isNotBlank);
	}

	/**
	 * 有 任意 一个 Blank
	 *
	 * @param css CharSequence
	 * @return boolean
	 */
	public static boolean isAnyNotBlank(CharSequence... css) {
		if (ObjectUtil.isEmpty(css)) {
			return false;
		}
		return Stream.of(css).anyMatch(StringUtil::isNoneBlank);
	}

	/**
	 * 有 任意 一个 Blank
	 *
	 * @param css CharSequence
	 * @return boolean
	 */
	public static boolean isAnyNotBlank(Collection<CharSequence> css) {
		if (CollectionUtil.isEmpty(css)) {
			return false;
		}
		return css.stream().anyMatch(StringUtil::isNoneBlank);
	}

	/**
	 * 判断一个字符串是否是数字
	 *
	 * @param cs the CharSequence to check, may be null
	 * @return {boolean}
	 */
	public static boolean isNumeric(final CharSequence cs) {
		if (StringUtil.isBlank(cs)) {
			return false;
		}
		for (int i = cs.length(); --i >= 0; ) {
			int chr = cs.charAt(i);
			if (chr < 48 || chr > 57) {
				return false;
			}
		}
		return true;
	}

	/**
	 * startWith char
	 *
	 * @param cs CharSequence
	 * @param c  char
	 * @return {boolean}
	 */
	public static boolean startWith(CharSequence cs, char c) {
		return cs.charAt(0) == c;
	}

	/**
	 * endWith char
	 *
	 * @param cs CharSequence
	 * @param c  char
	 * @return {boolean}
	 */
	public static boolean endWith(CharSequence cs, char c) {
		return cs.charAt(cs.length() - 1) == c;
	}

	/**
	 * 将字符串中特定模式的字符转换成map中对应的值
	 * <p>
	 * use: format("my name is ${name}, and i like ${like}!", {"name":"xxx", "like": "Java"})
	 *
	 * @param message 需要转换的字符串
	 * @param params  转换所需的键值对集合
	 * @return 转换后的字符串
	 */
	public static String format(@Nullable String message, @Nullable Map<String, ?> params) {
		// message 为 null 返回空字符串
		if (message == null) {
			return StringPool.EMPTY;
		}
		// 参数为 null 或者为空
		if (params == null || params.isEmpty()) {
			return message;
		}
		// 替换变量
		StringBuilder sb = new StringBuilder((int) (message.length() * 1.5));
		int cursor = 0;
		for (int start, end; (start = message.indexOf(StringPool.DOLLAR_LEFT_BRACE, cursor)) != -1
			&& (end = message.indexOf(CharPool.RIGHT_BRACE, start)) != -1; ) {
			sb.append(message, cursor, start);
			String key = message.substring(start + 2, end);
			Object value = params.get(StringUtil.trimWhitespace(key));
			sb.append(value == null ? StringPool.EMPTY : value);
			cursor = end + 1;
		}
		sb.append(message.substring(cursor));
		return sb.toString();
	}

	/**
	 * 同 log 格式的 format 规则
	 * <p>
	 * use: format("my name is {}, and i like {}!", "xxx", "Java")
	 *
	 * @param message   需要转换的字符串
	 * @param arguments 需要替换的变量
	 * @return 转换后的字符串
	 */
	public static String format(@Nullable String message, @Nullable Object... arguments) {
		// message 为 null 返回空字符串
		if (message == null) {
			return StringPool.EMPTY;
		}
		// 参数为 null 或者为空
		if (arguments == null || arguments.length == 0) {
			return message;
		}
		StringBuilder sb = new StringBuilder((int) (message.length() * 1.5));
		int cursor = 0;
		int index = 0;
		int argsLength = arguments.length;
		for (int start, end; (start = message.indexOf(CharPool.LEFT_BRACE, cursor)) != -1
			&& (end = message.indexOf(CharPool.RIGHT_BRACE, start)) != -1 && index < argsLength; ) {
			sb.append(message, cursor, start);
			sb.append(arguments[index]);
			cursor = end + 1;
			index++;
		}
		sb.append(message.substring(cursor));
		return sb.toString();
	}

	/**
	 * 格式化执行时间，单位为 ms 和 s，保留三位小数
	 *
	 * @param nanos 纳秒
	 * @return 格式化后的时间
	 */
	public static String format(long nanos) {
		if (nanos < 1) {
			return "0ms";
		}
		double millis = (double) nanos / (1000 * 1000);
		// 不够 1 ms，最小单位为 ms
		if (millis > 1000) {
			return String.format("%.3fs", millis / 1000);
		} else {
			return String.format("%.3fms", millis);
		}
	}

	/**
	 * Convert a {@code Collection} into a delimited {@code String} (e.g., CSV).
	 * <p>Useful for {@code toString()} implementations.
	 *
	 * @param coll the {@code Collection} to convert
	 * @return the delimited {@code String}
	 */
	public static String join(Collection<?> coll) {
		return StringUtil.collectionToCommaDelimitedString(coll);
	}

	/**
	 * Convert a {@code Collection} into a delimited {@code String} (e.g. CSV).
	 * <p>Useful for {@code toString()} implementations.
	 *
	 * @param coll  the {@code Collection} to convert
	 * @param delim the delimiter to use (typically a ",")
	 * @return the delimited {@code String}
	 */
	public static String join(Collection<?> coll, String delim) {
		return StringUtil.collectionToDelimitedString(coll, delim);
	}

	/**
	 * Convert a {@code String} array into a comma delimited {@code String} (i.e., CSV).
	 * <p>Useful for {@code toString()} implementations.
	 *
	 * @param arr the array to display
	 * @return the delimited {@code String}
	 */
	//public static String join(Object[] arr) {
	//	return StringUtil.arrayToCommaDelimitedString(arr);
	//}

	/**
	 * Convert a {@code String} array into a delimited {@code String} (e.g. CSV).
	 * <p>Useful for {@code toString()} implementations.
	 *
	 * @param arr   the array to display
	 * @param delim the delimiter to use (typically a ",")
	 * @return the delimited {@code String}
	 */
	public static String join(Object[] arr, String delim) {
		return StringUtil.arrayToDelimitedString(arr, delim);
	}

	/**
	 * 分割 字符串
	 *
	 * @param str       字符串
	 * @param delimiter 分割符
	 * @return 字符串数组
	 */
	public static String[] split(@Nullable String str, @Nullable String delimiter) {
		return StringUtil.delimitedListToStringArray(str, delimiter);
	}

	/**
	 * 分割 字符串 删除常见 空白符
	 *
	 * @param str       字符串
	 * @param delimiter 分割符
	 * @return 字符串数组
	 */
	public static String[] splitTrim(@Nullable String str, @Nullable String delimiter) {
		return StringUtil.delimitedListToStringArray(str, delimiter, " \t\n\n\f");
	}

	/**
	 * 字符串是否符合指定的 表达式
	 *
	 * <p>
	 * pattern styles: "xxx*", "*xxx", "*xxx*" and "xxx*yyy"
	 * </p>
	 *
	 * @param pattern 表达式
	 * @param str     字符串
	 * @return 是否匹配
	 */
	public static boolean simpleMatch(@Nullable String pattern, @Nullable String str) {
		return PatternMatchUtils.simpleMatch(pattern, str);
	}

	/**
	 * 字符串是否符合指定的 表达式
	 *
	 * <p>
	 * pattern styles: "xxx*", "*xxx", "*xxx*" and "xxx*yyy"
	 * </p>
	 *
	 * @param patterns 表达式 数组
	 * @param str      字符串
	 * @return 是否匹配
	 */
	public static boolean simpleMatch(@Nullable String[] patterns, String str) {
		return PatternMatchUtils.simpleMatch(patterns, str);
	}

	/**
	 * 生成uuid，采用 jdk 9 的形式，优化性能
	 *
	 * @return UUID
	 */
	public static String getUUID() throws UnsupportedEncodingException {
		ThreadLocalRandom random = ThreadLocalRandom.current();
		long lsb = random.nextLong();
		long msb = random.nextLong();
		byte[] buf = new byte[32];
		formatUnsignedLong(lsb, buf, 20, 12);
		formatUnsignedLong(lsb >>> 48, buf, 16, 4);
		formatUnsignedLong(msb, buf, 12, 4);
		formatUnsignedLong(msb >>> 16, buf, 8, 4);
		formatUnsignedLong(msb >>> 32, buf, 0, 8);
		return new String(buf, StandardCharsets.UTF_8);
	}

	private static void formatUnsignedLong(long val, byte[] buf, int offset, int len) {
		int charPos = offset + len;
		int radix = 1 << 4;
		int mask = radix - 1;
		do {
			buf[--charPos] = NumberUtil.DIGITS[((int) val) & mask];
			val >>>= 4;
		} while (charPos > offset);
	}

	/**
	 * 转义HTML用于安全过滤
	 *
	 * @param html html
	 * @return {String}
	 */
	public static String escapeHtml(String html) {
		return HtmlUtils.htmlEscape(html);
	}

	/**
	 * 清理字符串，清理出某些不可见字符和一些sql特殊字符
	 *
	 * @param txt 文本
	 * @return {String}
	 */
	@Nullable
	public static String cleanText(@Nullable String txt) {
		if (txt == null) {
			return null;
		}
		return SPECIAL_CHARS_REGEX.matcher(txt).replaceAll(StringPool.EMPTY);
	}

	/**
	 * 获取标识符，用于参数清理
	 *
	 * @param param 参数
	 * @return 清理后的标识符
	 */
	@Nullable
	public static String cleanIdentifier(@Nullable String param) {
		if (param == null) {
			return null;
		}
		StringBuilder paramBuilder = new StringBuilder();
		for (int i = 0; i < param.length(); i++) {
			char c = param.charAt(i);
			if (Character.isJavaIdentifierPart(c)) {
				paramBuilder.append(c);
			}
		}
		return paramBuilder.toString();
	}

	/**
	 * 随机数生成
	 *
	 * @param count 字符长度
	 * @return 随机数
	 */
	public static String random(int count) {
		return StringUtil.random(count, RandomEnum.ALL);
	}

	/**
	 * 随机数生成
	 *
	 * @param count      字符长度
	 * @param randomEnum 随机数类别
	 * @return 随机数
	 */
	public static String random(int count, RandomEnum randomEnum) {
		if (count == 0) {
			return StringPool.EMPTY;
		}
		Assert.isTrue(count > 0, "Requested random string length " + count + " is less than 0.");
		final Random random = Holder.SECURE_RANDOM;
		char[] buffer = new char[count];
		for (int i = 0; i < count; i++) {
			String factor = randomEnum.getFactor();
			buffer[i] = factor.charAt(random.nextInt(factor.length()));
		}
		return new String(buffer);
	}

	/**
	 * 参考自 commons lang 微调
	 *
	 * <p>Returns padding using the specified delimiter repeated
	 * to a given length.</p>
	 *
	 * <pre>
	 * StringUtils.repeat('e', 0)  = ""
	 * StringUtils.repeat('e', 3)  = "eee"
	 * StringUtils.repeat('e', -2) = ""
	 * </pre>
	 *
	 * <p>Note: this method does not support padding with
	 * <a href="http://www.unicode.org/glossary/#supplementary_character">Unicode Supplementary
	 * Characters</a> as they require a pair of {@code char}s to be represented.
	 * </p>
	 *
	 * @param ch     character to repeat
	 * @param repeat number of times to repeat char, negative treated as zero
	 * @return String with repeated character
	 */
	public static String repeat(final char ch, final int repeat) {
		if (repeat <= 0) {
			return StringPool.EMPTY;
		}
		final char[] buf = new char[repeat];
		Arrays.fill(buf, ch);
		return new String(buf);
	}

	/**
	 * 参考自 commons lang 微调
	 *
	 * <p>Gets the leftmost {@code len} characters of a String.</p>
	 *
	 * <p>If {@code len} characters are not available, or the
	 * String is {@code null}, the String will be returned without an exception. An empty String is
	 * returned if len is negative.</p>
	 *
	 * <pre>
	 * StringUtils.left(null, *)    = null
	 * StringUtils.left(*, -ve)     = ""
	 * StringUtils.left("", *)      = ""
	 * StringUtils.left("abc", 0)   = ""
	 * StringUtils.left("abc", 2)   = "ab"
	 * StringUtils.left("abc", 4)   = "abc"
	 * </pre>
	 *
	 * @param str the CharSequence to get the leftmost characters from, may be null
	 * @param len the length of the required String
	 * @return the leftmost characters, {@code null} if null String input
	 */
	@Nullable
	public static String left(@Nullable final String str, final int len) {
		if (str == null) {
			return null;
		}
		if (len < 0) {
			return StringPool.EMPTY;
		}
		if (str.length() <= len) {
			return str;
		}
		return str.substring(0, len);
	}

	/**
	 * 参考自 commons lang 微调
	 *
	 * <p>Gets the rightmost {@code len} characters of a String.</p>
	 *
	 * <p>If {@code len} characters are not available, or the String
	 * is {@code null}, the String will be returned without an an exception. An empty String is
	 * returned if len is negative.</p>
	 *
	 * <pre>
	 * StringUtils.right(null, *)    = null
	 * StringUtils.right(*, -ve)     = ""
	 * StringUtils.right("", *)      = ""
	 * StringUtils.right("abc", 0)   = ""
	 * StringUtils.right("abc", 2)   = "bc"
	 * StringUtils.right("abc", 4)   = "abc"
	 * </pre>
	 *
	 * @param str the String to get the rightmost characters from, may be null
	 * @param len the length of the required String
	 * @return the rightmost characters, {@code null} if null String input
	 */
	@Nullable
	public static String right(@Nullable final String str, final int len) {
		if (str == null) {
			return null;
		}
		if (len < 0) {
			return StringPool.EMPTY;
		}
		int length = str.length();
		if (length <= len) {
			return str;
		}
		return str.substring(length - len);
	}

	/**
	 * 参考自 commons lang 微调
	 *
	 * <p>Right pad a String with spaces (' ').</p>
	 *
	 * <p>The String is padded to the size of {@code size}.</p>
	 *
	 * <pre>
	 * StringUtils.rightPad(null, *)   = null
	 * StringUtils.rightPad("", 3)     = "   "
	 * StringUtils.rightPad("bat", 3)  = "bat"
	 * StringUtils.rightPad("bat", 5)  = "bat  "
	 * StringUtils.rightPad("bat", 1)  = "bat"
	 * StringUtils.rightPad("bat", -1) = "bat"
	 * </pre>
	 *
	 * @param str  the String to pad out, may be null
	 * @param size the size to pad to
	 * @return right padded String or original String if no padding is necessary, {@code null} if
	 * null String input
	 */
	@Nullable
	public static String rightPad(@Nullable final String str, final int size) {
		return rightPad(str, size, CharPool.SPACE);
	}

	/**
	 * 参考自 commons lang 微调
	 *
	 * <p>Right pad a String with a specified character.</p>
	 *
	 * <p>The String is padded to the size of {@code size}.</p>
	 *
	 * <pre>
	 * StringUtils.rightPad(null, *, *)     = null
	 * StringUtils.rightPad("", 3, 'z')     = "zzz"
	 * StringUtils.rightPad("bat", 3, 'z')  = "bat"
	 * StringUtils.rightPad("bat", 5, 'z')  = "batzz"
	 * StringUtils.rightPad("bat", 1, 'z')  = "bat"
	 * StringUtils.rightPad("bat", -1, 'z') = "bat"
	 * </pre>
	 *
	 * @param str     the String to pad out, may be null
	 * @param size    the size to pad to
	 * @param padChar the character to pad with
	 * @return right padded String or original String if no padding is necessary, {@code null} if
	 * null String input
	 */
	@Nullable
	public static String rightPad(@Nullable final String str, final int size, final char padChar) {
		if (str == null) {
			return null;
		}
		final int pads = size - str.length();
		if (pads <= 0) {
			// returns original String when possible
			return str;
		}
		if (pads > PAD_LIMIT) {
			return rightPad(str, size, String.valueOf(padChar));
		}
		return str.concat(repeat(padChar, pads));
	}

	/**
	 * 参考自 commons lang 微调
	 *
	 * <p>Right pad a String with a specified String.</p>
	 *
	 * <p>The String is padded to the size of {@code size}.</p>
	 *
	 * <pre>
	 * StringUtils.rightPad(null, *, *)      = null
	 * StringUtils.rightPad("", 3, "z")      = "zzz"
	 * StringUtils.rightPad("bat", 3, "yz")  = "bat"
	 * StringUtils.rightPad("bat", 5, "yz")  = "batyz"
	 * StringUtils.rightPad("bat", 8, "yz")  = "batyzyzy"
	 * StringUtils.rightPad("bat", 1, "yz")  = "bat"
	 * StringUtils.rightPad("bat", -1, "yz") = "bat"
	 * StringUtils.rightPad("bat", 5, null)  = "bat  "
	 * StringUtils.rightPad("bat", 5, "")    = "bat  "
	 * </pre>
	 *
	 * @param str    the String to pad out, may be null
	 * @param size   the size to pad to
	 * @param padStr the String to pad with, null or empty treated as single space
	 * @return right padded String or original String if no padding is necessary, {@code null} if
	 * null String input
	 */
	@Nullable
	public static String rightPad(@Nullable final String str, final int size, String padStr) {
		if (str == null) {
			return null;
		}
		if (!StringUtil.hasLength(padStr)) {
			padStr = StringPool.SPACE;
		}
		final int padLen = padStr.length();
		final int strLen = str.length();
		final int pads = size - strLen;
		if (pads <= 0) {
			// returns original String when possible
			return str;
		}
		if (padLen == 1 && pads <= PAD_LIMIT) {
			return rightPad(str, size, padStr.charAt(0));
		}
		if (pads == padLen) {
			return str.concat(padStr);
		} else if (pads < padLen) {
			return str.concat(padStr.substring(0, pads));
		} else {
			final char[] padding = new char[pads];
			final char[] padChars = padStr.toCharArray();
			for (int i = 0; i < pads; i++) {
				padding[i] = padChars[i % padLen];
			}
			return str.concat(new String(padding));
		}
	}

	/**
	 * 参考自 commons lang 微调
	 *
	 * <p>Left pad a String with spaces (' ').</p>
	 *
	 * <p>The String is padded to the size of {@code size}.</p>
	 *
	 * <pre>
	 * StringUtils.leftPad(null, *)   = null
	 * StringUtils.leftPad("", 3)     = "   "
	 * StringUtils.leftPad("bat", 3)  = "bat"
	 * StringUtils.leftPad("bat", 5)  = "  bat"
	 * StringUtils.leftPad("bat", 1)  = "bat"
	 * StringUtils.leftPad("bat", -1) = "bat"
	 * </pre>
	 *
	 * @param str  the String to pad out, may be null
	 * @param size the size to pad to
	 * @return left padded String or original String if no padding is necessary, {@code null} if
	 * null String input
	 */
	@Nullable
	public static String leftPad(@Nullable final String str, final int size) {
		return leftPad(str, size, CharPool.SPACE);
	}

	/**
	 * 参考自 commons lang 微调
	 *
	 * <p>Left pad a String with a specified character.</p>
	 *
	 * <p>Pad to a size of {@code size}.</p>
	 *
	 * <pre>
	 * StringUtils.leftPad(null, *, *)     = null
	 * StringUtils.leftPad("", 3, 'z')     = "zzz"
	 * StringUtils.leftPad("bat", 3, 'z')  = "bat"
	 * StringUtils.leftPad("bat", 5, 'z')  = "zzbat"
	 * StringUtils.leftPad("bat", 1, 'z')  = "bat"
	 * StringUtils.leftPad("bat", -1, 'z') = "bat"
	 * </pre>
	 *
	 * @param str     the String to pad out, may be null
	 * @param size    the size to pad to
	 * @param padChar the character to pad with
	 * @return left padded String or original String if no padding is necessary, {@code null} if
	 * null String input
	 */
	@Nullable
	public static String leftPad(@Nullable final String str, final int size, final char padChar) {
		if (str == null) {
			return null;
		}
		final int pads = size - str.length();
		if (pads <= 0) {
			// returns original String when possible
			return str;
		}
		if (pads > PAD_LIMIT) {
			return leftPad(str, size, String.valueOf(padChar));
		}
		return repeat(padChar, pads).concat(str);
	}

	/**
	 * 参考自 commons lang 微调
	 *
	 * <p>Left pad a String with a specified String.</p>
	 *
	 * <p>Pad to a size of {@code size}.</p>
	 *
	 * <pre>
	 * StringUtils.leftPad(null, *, *)      = null
	 * StringUtils.leftPad("", 3, "z")      = "zzz"
	 * StringUtils.leftPad("bat", 3, "yz")  = "bat"
	 * StringUtils.leftPad("bat", 5, "yz")  = "yzbat"
	 * StringUtils.leftPad("bat", 8, "yz")  = "yzyzybat"
	 * StringUtils.leftPad("bat", 1, "yz")  = "bat"
	 * StringUtils.leftPad("bat", -1, "yz") = "bat"
	 * StringUtils.leftPad("bat", 5, null)  = "  bat"
	 * StringUtils.leftPad("bat", 5, "")    = "  bat"
	 * </pre>
	 *
	 * @param str    the String to pad out, may be null
	 * @param size   the size to pad to
	 * @param padStr the String to pad with, null or empty treated as single space
	 * @return left padded String or original String if no padding is necessary, {@code null} if
	 * null String input
	 */
	@Nullable
	public static String leftPad(@Nullable final String str, final int size, String padStr) {
		if (str == null) {
			return null;
		}
		if (!StringUtils.hasLength(padStr)) {
			padStr = StringPool.SPACE;
		}
		final int padLen = padStr.length();
		final int strLen = str.length();
		final int pads = size - strLen;
		if (pads <= 0) {
			// returns original String when possible
			return str;
		}
		if (padLen == 1 && pads <= PAD_LIMIT) {
			return leftPad(str, size, padStr.charAt(0));
		}
		if (pads == padLen) {
			return padStr.concat(str);
		} else if (pads < padLen) {
			return padStr.substring(0, pads).concat(str);
		} else {
			final char[] padding = new char[pads];
			final char[] padChars = padStr.toCharArray();
			for (int i = 0; i < pads; i++) {
				padding[i] = padChars[i % padLen];
			}
			return new String(padding).concat(str);
		}
	}

	/**
	 * 参考自 commons lang 微调
	 *
	 * <p>Gets {@code len} characters from the middle of a String.</p>
	 *
	 * <p>If {@code len} characters are not available, the remainder
	 * of the String will be returned without an exception. If the String is {@code null}, {@code
	 * null} will be returned. An empty String is returned if len is negative or exceeds the length
	 * of {@code str}.</p>
	 *
	 * <pre>
	 * StringUtils.mid(null, *, *)    = null
	 * StringUtils.mid(*, *, -ve)     = ""
	 * StringUtils.mid("", 0, *)      = ""
	 * StringUtils.mid("abc", 0, 2)   = "ab"
	 * StringUtils.mid("abc", 0, 4)   = "abc"
	 * StringUtils.mid("abc", 2, 4)   = "c"
	 * StringUtils.mid("abc", 4, 2)   = ""
	 * StringUtils.mid("abc", -2, 2)  = "ab"
	 * </pre>
	 *
	 * @param str the String to get the characters from, may be null
	 * @param pos the position to start from, negative treated as zero
	 * @param len the length of the required String
	 * @return the middle characters, {@code null} if null String input
	 */
	@Nullable
	public static String mid(@Nullable final String str, int pos, final int len) {
		if (str == null) {
			return null;
		}
		int length = str.length();
		if (len < 0 || pos > length) {
			return StringPool.EMPTY;
		}
		if (pos < 0) {
			pos = 0;
		}
		if (length <= pos + len) {
			return str.substring(pos);
		}
		return str.substring(pos, pos + len);
	}

	/**
	 * 判断是否 http 地址
	 *
	 * @param text 文本
	 * @return 是否 http 地址
	 */
	public static boolean isHttpUrl(String text) {
		return text.startsWith("http://") || text.startsWith("https://");
	}


	/**
	 * nullToEmpty
	 *
	 * @param str str
	 * @return {@link String }
	 * @author shuigedeng
	 * @since 2021-09-02 14:51:15
	 */
	public static String nullToEmpty(Object str) {
		return str != null ? str.toString() : "";
	}

	/**
	 * isEmpty
	 *
	 * @param str str
	 * @return boolean
	 * @author shuigedeng
	 * @since 2021-09-02 14:51:23
	 */
	public static boolean isEmpty(String str) {
		return str == null || str.isEmpty();
	}

	/**
	 * 部分字符串获取
	 *
	 * @param str    字符串
	 * @param maxlen 最大长度
	 * @return {@link String } 字符串
	 * @author shuigedeng
	 * @since 2021-09-02 14:51:32
	 */
	public static String subString2(String str, int maxlen) {
		if (StringUtil.isEmpty(str)) {
			return str;
		}
		if (str.length() <= maxlen) {
			return str;
		}
		return str.substring(0, maxlen);
	}

	/**
	 * 部分字符串获取 超出部分末尾...
	 *
	 * @param str    字符串
	 * @param maxlen 最大长度
	 * @return {@link String } 字符串
	 * @author shuigedeng
	 * @since 2021-09-02 14:51:46
	 */
	public static String subString3(String str, int maxlen) {
		if (StringUtil.isEmpty(str)) {
			return str;
		}
		if (str.length() <= maxlen) {
			return str;
		}
		return str.substring(0, maxlen) + "...";
	}

	/**
	 * Check that the given {@code String} is neither {@code null} nor of length 0.
	 * <p>Note: this method returns {@code true} for a {@code String} that
	 * purely consists of whitespace.
	 *
	 * @param str the {@code String} to check (may be {@code null})
	 * @return {@code true} if the {@code String} is not {@code null} and has length
	 * @see #hasLength(CharSequence)
	 * @see #hasText(String)
	 */
	public static boolean hasLength(String str) {
		return (str != null && !str.isEmpty());
	}

	/**
	 * Check that the given {@code CharSequence} is neither {@code null} nor of length 0.
	 * <p>Note: this method returns {@code true} for a {@code CharSequence}
	 * that purely consists of whitespace.
	 * <p><pre class="code">
	 * StringUtils.hasLength(null) = false
	 * StringUtils.hasLength("") = false
	 * StringUtils.hasLength(" ") = true
	 * StringUtils.hasLength("Hello") = true
	 * </pre>
	 *
	 * @param str the {@code CharSequence} to check (may be {@code null})
	 * @return {@code true} if the {@code CharSequence} is not {@code null} and has length
	 * @see #hasLength(String)
	 * @see #hasText(CharSequence)
	 */
	public static boolean hasLength(CharSequence str) {
		return (str != null && str.length() > 0);
	}

	/**
	 * Check whether the given {@code CharSequence} contains actual <em>text</em>.
	 * <p>More specifically, this method returns {@code true} if the
	 * {@code CharSequence} is not {@code null}, its length is greater than 0, and it contains at
	 * least one non-whitespace character.
	 * <p><pre class="code">
	 * StringUtils.hasText(null) = false
	 * StringUtils.hasText("") = false
	 * StringUtils.hasText(" ") = false
	 * StringUtils.hasText("12345") = true
	 * StringUtils.hasText(" 12345 ") = true
	 * </pre>
	 *
	 * @param str the {@code CharSequence} to check (may be {@code null})
	 * @return {@code true} if the {@code CharSequence} is not {@code null}, its length is greater
	 * than 0, and it does not contain whitespace only
	 * @see Character#isWhitespace
	 */
	public static boolean hasText(CharSequence str) {
		if (!hasLength(str)) {
			return false;
		}
		int strLen = str.length();
		for (int i = 0; i < strLen; i++) {
			if (!Character.isWhitespace(str.charAt(i))) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Check whether the given {@code String} contains actual <em>text</em>.
	 * <p>More specifically, this method returns {@code true} if the
	 * {@code String} is not {@code null}, its length is greater than 0, and it contains at least
	 * one non-whitespace character.
	 *
	 * @param str the {@code String} to check (may be {@code null})
	 * @return {@code true} if the {@code String} is not {@code null}, its length is greater than 0,
	 * and it does not contain whitespace only
	 * @see #hasText(CharSequence)
	 */
	public static boolean hasText(String str) {
		return hasText((CharSequence) str);
	}

	/**
	 * trimToNull
	 *
	 * @param nextLine nextLine
	 * @return {@link String }
	 * @author shuigedeng
	 * @since 2021-09-02 14:51:58
	 */
	public static String trimToNull(String nextLine) {
		return nextLine.trim();
	}


	/**
	 * 如果给定字符串{@code str}中不包含{@code appendStr}，则在{@code str}后追加{@code appendStr}；
	 * 如果已包含{@code appendStr}，则在{@code str}后追加{@code otherwise}
	 *
	 * @param str       给定的字符串
	 * @param appendStr 需要追加的内容
	 * @param otherwise 当{@code appendStr}不满足时追加到{@code str}后的内容
	 * @return 追加后的字符串
	 */
	public static String appendIfNotContain(String str, String appendStr, String otherwise) {
		if (isEmpty(str) || isEmpty(appendStr)) {
			return str;
		}
		if (str.contains(appendStr)) {
			return str.concat(otherwise);
		}
		return str.concat(appendStr);
	}

	/**
	 * 过滤特殊字符串
	 *
	 * @param str 字符串
	 * @return {@link String }
	 * @since 2022-05-30 16:40:31
	 */
	public static String filterSpecialChart(String str) {
		String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		return m.replaceAll("").trim();
	}
}

