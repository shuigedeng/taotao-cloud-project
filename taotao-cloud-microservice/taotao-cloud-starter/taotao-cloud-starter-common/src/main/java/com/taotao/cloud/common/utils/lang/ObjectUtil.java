/*
 * Copyright (c)  2019. houbinbin Inc.
 * heaven All rights reserved.
 */

package com.taotao.cloud.common.utils.lang;


import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.taotao.cloud.common.support.handler.IHandler;
import com.taotao.cloud.common.utils.collection.ArrayUtil;
import com.taotao.cloud.common.utils.collection.CollectionUtil;
import com.taotao.cloud.common.utils.collection.MapUtil;
import com.taotao.cloud.common.utils.reflect.ClassTypeUtil;
import com.taotao.cloud.common.utils.reflect.ClassUtil;
import com.taotao.cloud.common.utils.reflect.ReflectFieldUtil;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.springframework.lang.Nullable;

/**
 * Object 工具类
 */
public final class ObjectUtil extends org.springframework.util.ObjectUtils {

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
	//public static boolean isSameType(Object one, Object two) {
	//	if (com.taotao.cloud.common.utils.common.ObjectUtil.isNull(one)
	//		|| com.taotao.cloud.common.utils.common.ObjectUtil.isNull(two)) {
	//		return false;
	//	}
	//	Class clazzOne = one.getClass();
	//
	//	return clazzOne.isInstance(two);
	//}

	/**
	 * 不是同一个类型
	 *
	 * @param one 第一个元素
	 * @param two 第二个元素
	 * @return 是否为不同对象
	 */
	//public static boolean isNotSameType(Object one, Object two) {
	//	return !isSameType(one, two);
	//}

	/**
	 * 判断当前对象是否为空 - 对象为空 - 空字符串 - 空集合/map - 空数组 - 自定义空类型
	 *
	 * @param object 对象
	 * @return 是否为空
	 */
	//public static boolean isNull(Object object) {
	//	return null == object;
	//}

	/**
	 * 判断对象是否非null
	 *
	 * @param object 元素
	 * @return {@code true} 非空
	 */
	//public static boolean isNotNull(Object object) {
	//	return !isNull(object);
	//}

	/**
	 * 判断内容是否为空 - 空字符串 - 空集合/map - 空数组 - 自定义空类型
	 *
	 * @param object 对象
	 * @return 是否为空
	 */
	//public static boolean isEmpty(Object object) {
	//	if (isNull(object)) {
	//		return true;
	//	}
	//
	//	if (object instanceof String) {
	//		String string = (String) object;
	//		return StringUtil.isEmpty(string);
	//	}
	//	if (object instanceof Collection) {
	//		Collection collection = (Collection) object;
	//		return CollectionUtil.isEmpty(collection);
	//	}
	//	if (object instanceof Map) {
	//		Map map = (Map) object;
	//		return MapUtil.isEmpty(map);
	//	}
	//	if (object.getClass().isArray()) {
	//		return Array.getLength(object) == 0;
	//	}
	//
	//	return false;
	//}

	/**
	 * 判断对象是否非空
	 *
	 * @param object 对象
	 * @return 是否非空
	 */
	//public static boolean isNotEmpty(Object object) {
	//	return !isEmpty(object);
	//}

	/**
	 * 判断两个对象是否相同 1.如果不是同一种类型,则直接返回false
	 *
	 * @param except 期望值
	 * @param real   实际值
	 * @return 两个对象是否相同
	 */
	//public static boolean isEquals(Object except, Object real) {
	//	//1. 不是同一种类型
	//	if (isNotSameType(except, real)) {
	//		return false;
	//	}
	//
	//	final Class exceptClass = except.getClass();
	//	final Class realClass = except.getClass();
	//
	//	//2. 基本类型
	//	if (exceptClass.isPrimitive()
	//		&& realClass.isPrimitive()
	//		&& except != real) {
	//		return false;
	//	}
	//
	//	//3. 数组
	//	if (ClassTypeUtil.isArray(exceptClass)
	//		&& ClassTypeUtil.isArray(realClass)) {
	//		Object[] exceptArray = (Object[]) except;
	//		Object[] realArray = (Object[]) real;
	//		return Arrays.equals(exceptArray, realArray);
	//	}
	//
	//	//3. Collection
	//
	//	//4. map
	//	if (ClassTypeUtil.isMap(exceptClass) && ClassTypeUtil.isMap(realClass)) {
	//		Map exceptMap = (Map) except;
	//		Map realMap = (Map) real;
	//		return exceptMap.equals(realMap);
	//	}
	//
	//	return except.equals(real);
	//}

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
	 */
	//public static boolean isNull(final Object object, final Object... others) {
	//	if (ObjectUtil.isNull(object)) {
	//		// 其他列表不为空，则遍历
	//		if (isNotEmpty(others)) {
	//			for (Object other : others) {
	//				if (isNotNull(other)) {
	//					return false;
	//				}
	//			}
	//			return true;
	//		}
	//		return true;
	//	}
	//	return false;
	//}

	/**
	 * 判断两个元素是否相等或者都为 Null
	 *
	 * @param left  元素1
	 * @param right 元素2
	 * @return 是否相等或者都为 Null
	 */
	//public static boolean isEqualsOrNull(final Object left, final Object right) {
	//	if (isNull(left, right)) {
	//		return true;
	//	}
	//	if (isNull(left) || isNull(right)) {
	//		return false;
	//	}
	//	return isEquals(left, right);
	//}

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
	//public static Class getClass(final Object object) {
	//	if (com.taotao.cloud.common.utils.common.ObjectUtil.isNull(object)) {
	//		return null;
	//	}
	//	return object.getClass();
	//}

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
	//public static boolean isSameValue(Object valueOne, Object valueTwo) {
	//	if (valueOne == null && valueTwo == null) {
	//		return true;
	//	}
	//
	//	if (valueOne == null || valueTwo == null) {
	//		return false;
	//	}
	//
	//	return valueOne.equals(valueTwo);
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
			|| ObjectUtil.isNull(two)) {
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

		if (object instanceof String) {
			String string = (String) object;
			return StringUtil.isEmpty(string);
		}
		if (object instanceof Collection) {
			Collection collection = (Collection) object;
			return CollectionUtil.isEmpty(collection);
		}
		if (object instanceof Map) {
			Map map = (Map) object;
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

		final Class exceptClass = except.getClass();
		final Class realClass = except.getClass();

		//2. 基本类型
		if (exceptClass.isPrimitive()
			&& realClass.isPrimitive()
			&& except != real) {
			return false;
		}

		//3. 数组
		if (ClassTypeUtil.isArray(exceptClass)
			&& ClassTypeUtil.isArray(realClass)) {
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
	public static String objectToString(final Object object) {
		return objectToString(object, null);
	}

	/**
	 * 对象转字符串
	 *
	 * @param object       对象
	 * @param defaultValue 默认值，原始对象为 null 时返回。
	 * @return 结果
	 */
	public static String objectToString(final Object object,
		final String defaultValue) {
		if (ObjectUtil.isNull(object)) {
			return defaultValue;
		}
		return object.toString();
	}

	/**
	 * 判断所有参数皆为null
	 *
	 * @param object 对象
	 * @param others 其他参数
	 * @return 是否都为空
	 * @see #isNull(Object) 增强版本
	 */
	public static boolean isNull(final Object object, final Object... others) {
		if (ObjectUtil.isNull(object)) {
			// 其他列表不为空，则遍历
			if (ArrayUtil.isNotEmpty(others)) {
				for (Object other : others) {
					if (ObjectUtil.isNotNull(other)) {
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
	//public static <R> List<R> toList(final Object object, IHandler<Object, R> handler) {
	//	if (ObjectUtil.isNull(object)) {
	//		return Collections.emptyList();
	//	}
	//
	//	final Class clazz = object.getClass();
	//
	//	// 集合
	//	if (ClassTypeUtil.isCollection(clazz)) {
	//		Collection collection = (Collection) object;
	//		return CollectionUtil.toList(collection, handler);
	//	}
	//
	//	// 数组
	//	if (clazz.isArray()) {
	//		return ArrayUtil.toList(object, handler);
	//	}
	//
	//	throw new UnsupportedOperationException(
	//		"Not support foreach() for class: " + clazz.getName());
	//}

	/**
	 * 获取实体对象对应的 class 信息
	 *
	 * @param object 实例对象
	 * @return 对象 class 信息
	 */
	//public static Class getClass(final Object object) {
	//	if (ObjectUtil.isNull(object)) {
	//		return null;
	//	}
	//	return object.getClass();
	//}

	/**
	 * empty 转换为 null
	 *
	 * @param object 对象
	 */
	//public static void emptyToNull(Object object) {
	//	if (null == object) {
	//		return;
	//	}
	//
	//	List<Field> fieldList = ClassUtil.getAllFieldList(object.getClass());
	//	for (Field field : fieldList) {
	//		Object value = ReflectFieldUtil.getValue(field, object);
	//		if (ObjectUtil.isEmpty(value)) {
	//			ReflectFieldUtil.setValue(field, object, null);
	//		}
	//	}
	//}

	/**
	 * 基于反射的属性拷贝
	 *
	 * @param source 源头
	 * @param target 目标
	 */
	//public static void copyProperties(Object source, Object target) {
	//	if (source == null || target == null) {
	//		return;
	//	}
	//
	//	Map<String, Field> sourceFieldMap = ClassUtil.getAllFieldMap(source.getClass());
	//	Map<String, Field> targetFieldMap = ClassUtil.getAllFieldMap(target.getClass());
	//
	//	// 遍历
	//	for (Map.Entry<String, Field> entry : sourceFieldMap.entrySet()) {
	//		String sourceFieldName = entry.getKey();
	//		Field sourceField = entry.getValue();
	//		Field targetField = targetFieldMap.get(sourceFieldName);
	//
	//		if (targetField == null) {
	//			continue;
	//		}
	//
	//		if (ClassUtil.isAssignable(sourceField.getType(), targetField.getType())) {
	//			Object sourceVal = ReflectFieldUtil.getValue(sourceField, source);
	//			ReflectFieldUtil.setValue(targetField, target, sourceVal);
	//		}
	//	}
	//}

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

}
