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

package com.taotao.cloud.common.convert;

import com.fasterxml.jackson.annotation.JsonValue;
import com.taotao.cloud.common.utils.CollectionUtil;
import com.taotao.cloud.common.utils.ConvertUtil;
import com.taotao.cloud.common.utils.LogUtil;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.ConditionalGenericConverter;
import org.springframework.lang.Nullable;

/**
 * 接收参数 同 jackson Enum -》 String 转换
 *
  * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 19:41:13
 */
public class EnumToStringConverter implements ConditionalGenericConverter {

	/**
	 * 缓存 Enum 类信息，提供性能
	 */
	private static final ConcurrentMap<Class<?>, AccessibleObject> ENUM_CACHE_MAP = new ConcurrentHashMap<>(
		8);

	@Nullable
	private static AccessibleObject getAnnotation(Class<?> clazz) {
		Set<AccessibleObject> accessibleObjects = new HashSet<>();
		// JsonValue METHOD, FIELD
		Field[] fields = clazz.getDeclaredFields();
		Collections.addAll(accessibleObjects, fields);
		// methods
		Method[] methods = clazz.getDeclaredMethods();
		Collections.addAll(accessibleObjects, methods);
		for (AccessibleObject accessibleObject : accessibleObjects) {
			// 复用 jackson 的 JsonValue 注解
			JsonValue jsonValue = accessibleObject.getAnnotation(JsonValue.class);
			if (jsonValue != null && jsonValue.value()) {
				accessibleObject.setAccessible(true);
				return accessibleObject;
			}
		}
		return null;
	}

	@Override
	public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
		return true;
	}

	@Override
	public Set<ConvertiblePair> getConvertibleTypes() {
		Set<ConvertiblePair> pairSet = new HashSet<>(3);
		pairSet.add(new ConvertiblePair(Enum.class, String.class));
		pairSet.add(new ConvertiblePair(Enum.class, Integer.class));
		pairSet.add(new ConvertiblePair(Enum.class, Long.class));
		return Collections.unmodifiableSet(pairSet);
	}

	@Nullable
	@Override
	public Object convert(@Nullable Object source, TypeDescriptor sourceType,
		TypeDescriptor targetType) {
		if (source == null) {
			return null;
		}
		Class<?> sourceClazz = sourceType.getType();
		AccessibleObject accessibleObject = CollectionUtil.computeIfAbsent(ENUM_CACHE_MAP,
			sourceClazz, EnumToStringConverter::getAnnotation);
		Class<?> targetClazz = targetType.getType();
		// 如果为null，走默认的转换
		if (accessibleObject == null) {
			if (String.class == targetClazz) {
				return ((Enum) source).name();
			}
			int ordinal = ((Enum) source).ordinal();
			return ConvertUtil.convert(ordinal, targetClazz);
		}
		try {
			return EnumToStringConverter.invoke(sourceClazz, accessibleObject, source, targetClazz);
		} catch (Exception e) {
			LogUtil.error(e.getMessage(), e);
		}
		return null;
	}

	@Nullable
	private static Object invoke(Class<?> clazz, AccessibleObject accessibleObject, Object source,
		Class<?> targetClazz)
		throws IllegalAccessException, InvocationTargetException {
		Object value = null;
		if (accessibleObject instanceof Field) {
			Field field = (Field) accessibleObject;
			value = field.get(source);
		} else if (accessibleObject instanceof Method) {
			Method method = (Method) accessibleObject;
			Class<?> paramType = method.getParameterTypes()[0];
			// 类型转换
			Object object = ConvertUtil.convert(source, paramType);
			value = method.invoke(clazz, object);
		}
		if (value == null) {
			return null;
		}
		return ConvertUtil.convert(value, targetClazz);
	}
}
