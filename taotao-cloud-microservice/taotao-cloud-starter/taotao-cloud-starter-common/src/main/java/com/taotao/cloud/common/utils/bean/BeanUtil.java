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
package com.taotao.cloud.common.utils.bean;

import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.lang.Assert;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.BeanProperty;
import com.taotao.cloud.common.exception.CommonRuntimeException;
import com.taotao.cloud.common.utils.collection.MapUtil;
import com.taotao.cloud.common.utils.common.ArgUtil;
import com.taotao.cloud.common.utils.convert.Converter;
import com.taotao.cloud.common.exception.BaseException;
import com.taotao.cloud.common.utils.lang.ObjectUtil;
import com.taotao.cloud.common.utils.reflect.ClassUtil;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import javax.annotation.Nullable;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.boot.convert.ApplicationConversionService;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.cglib.beans.BeanGenerator;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.util.FastByteArrayOutputStream;

/**
 * BeanUtil
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 17:45:40
 */
public class BeanUtil extends org.springframework.beans.BeanUtils {

	private BeanUtil() {
	}

	/**
	 * 实例化对象
	 *
	 * @param clazz 类
	 * @param <T>   泛型标记
	 * @return 对象
	 */
	@SuppressWarnings("unchecked")
	public static <T> T newInstance(Class<?> clazz) {
		return (T)instantiateClass(clazz);
	}

	/**
	 * 实例化对象
	 *
	 * @param clazzStr 类名
	 * @param <T>      泛型标记
	 * @return 对象
	 */
	@SuppressWarnings("unchecked")
	public static <T> T newInstance(String clazzStr) {
		try {
			Class<?> clazz = ClassUtil.forName(clazzStr, null);
			return newInstance(clazz);
		} catch (ClassNotFoundException e) {
			throw new BaseException(e);
		}
	}

	/**
	 * 获取Bean的属性, 支持 propertyName 多级 ：test.user.name
	 *
	 * @param bean         bean
	 * @param propertyName 属性名
	 * @return 属性值
	 */
	@Nullable
	public static Object getProperty(@Nullable Object bean, String propertyName) {
		if (bean == null) {
			return null;
		}
		BeanWrapper beanWrapper = PropertyAccessorFactory.forBeanPropertyAccess(bean);
		return beanWrapper.getPropertyValue(propertyName);
	}

	/**
	 * 设置Bean属性, 支持 propertyName 多级 ：test.user.name
	 *
	 * @param bean         bean
	 * @param propertyName 属性名
	 * @param value        属性值
	 */
	public static void setProperty(Object bean, String propertyName, Object value) {
		BeanWrapper beanWrapper = PropertyAccessorFactory.forBeanPropertyAccess(
			Objects.requireNonNull(bean, "bean Could not null"));
		beanWrapper.setPropertyValue(propertyName, value);
	}

	/**
	 * 浅拷贝
	 *
	 * <p>
	 * 支持 map bean
	 * </p>
	 *
	 * @param source 源对象
	 * @param <T>    泛型标记
	 * @return T
	 */
	@Nullable
	@SuppressWarnings("unchecked")
	public static <T> T clone(@Nullable T source) {
		if (source == null) {
			return null;
		}
		return (T) copy(source, source.getClass());
	}

	/**
	 * 深度拷贝
	 *
	 * @param source 待拷贝的对象
	 * @return 拷贝之后的对象
	 */
	@Nullable
	@SuppressWarnings("unchecked")
	public static <T> T deepClone(@Nullable T source) {
		if (source == null) {
			return null;
		}
		FastByteArrayOutputStream fBos = new FastByteArrayOutputStream(1024);
		try (ObjectOutputStream oos = new ObjectOutputStream(fBos)) {
			oos.writeObject(source);
			oos.flush();
		} catch (IOException ex) {
			throw new IllegalArgumentException(
				"Failed to serialize object of type: " + source.getClass(), ex);
		}
		try (ObjectInputStream ois = new ObjectInputStream(fBos.getInputStream())) {
			return (T) ois.readObject();
		} catch (IOException | ClassNotFoundException ex) {
			throw new IllegalArgumentException("Failed to deserialize object", ex);
		}
	}

	/**
	 * copy 对象属性，默认不使用Convert
	 *
	 * <p>
	 * 支持 map bean copy
	 * </p>
	 *
	 * @param source 源对象
	 * @param clazz  类名
	 * @param <T>    泛型标记
	 * @return T
	 */
	@Nullable
	public static <T> T copy(@Nullable Object source, Class<T> clazz) {
		if (source == null) {
			return null;
		}
		return copy(source, source.getClass(), clazz);
	}

	/**
	 * copy 对象属性，默认不使用Convert
	 *
	 * <p>
	 * 支持 map bean copy
	 * </p>
	 *
	 * @param source      源对象
	 * @param sourceClazz 源类型
	 * @param targetClazz 转换成的类型
	 * @return T
	 */
	@Nullable
	public static <T> T copy(@Nullable Object source, Class sourceClazz, Class<T> targetClazz) {
		if (source == null) {
			return null;
		}
		BeanCopier copier = BeanCopier.create(sourceClazz, targetClazz, false);
		T to = newInstance(targetClazz);
		copier.copy(source, to, null);
		return to;
	}

	/**
	 * copy 列表对象，默认不使用Convert
	 *
	 * <p>
	 * 支持 map bean copy
	 * </p>
	 *
	 * @param sourceList  源列表
	 * @param targetClazz 转换成的类型
	 * @return T
	 */
	public static <T> List<T> copy(@Nullable Collection<?> sourceList, Class<T> targetClazz) {
		return copy(sourceList, (List<T>) null, targetClazz);
	}

	/**
	 * copy 列表对象，默认不使用Convert
	 *
	 * <p>
	 * 支持 map bean copy
	 * </p>
	 *
	 * @param sourceList  源列表
	 * @param targetList  目标列表
	 * @param targetClazz 转换成的类型
	 * @return T
	 */
	public static <T> List<T> copy(@Nullable Collection<?> sourceList, @Nullable List<T> targetList,
		Class<T> targetClazz) {
		if (sourceList == null || sourceList.isEmpty()) {
			return Collections.emptyList();
		}
		if (targetList == null) {
			targetList = new ArrayList<>(sourceList.size());
		}
		Class<?> sourceClazz = null;
		for (Object source : sourceList) {
			if (source == null) {
				continue;
			}
			if (sourceClazz == null) {
				sourceClazz = source.getClass();
			}
			T bean = copy(source, sourceClazz, targetClazz);
			targetList.add(bean);
		}
		return targetList;
	}

	/**
	 * 拷贝对象
	 *
	 * <p>
	 * 支持 map bean copy
	 * </p>
	 *
	 * @param source     源对象
	 * @param targetBean 需要赋值的对象
	 */
	public static void copy(@Nullable Object source, @Nullable Object targetBean) {
		if (source == null || targetBean == null) {
			return;
		}
		BeanCopier copier = BeanCopier
			.create(source.getClass(), targetBean.getClass(), false);

		copier.copy(source, targetBean, null);
	}

	/**
	 * 拷贝对象，source 属性做 null 判断，Map 不支持，map 会做 instanceof 判断，不会
	 *
	 * <p>
	 * 支持 bean copy
	 * </p>
	 *
	 * @param source     源对象
	 * @param targetBean 需要赋值的对象
	 */
	public static void copyNonNull(@Nullable Object source, @Nullable Object targetBean) {
		if (source == null || targetBean == null) {
			return;
		}
		BeanCopier copier = BeanCopier
			.create(source.getClass(), targetBean.getClass(), false);

		copier.copy(source, targetBean, null);
	}

	/**
	 * 拷贝对象并对不同类型属性进行转换
	 *
	 * <p>
	 * 支持 map bean copy
	 * </p>
	 *
	 * @param source      源对象
	 * @param targetClazz 转换成的类
	 * @return T
	 */
	@Nullable
	public static <T> T copyWithConvert(@Nullable Object source, Class<T> targetClazz) {
		if (source == null) {
			return null;
		}
		return copyWithConvert(source, source.getClass(), targetClazz);
	}

	/**
	 * 拷贝对象并对不同类型属性进行转换
	 *
	 * <p>
	 * 支持 map bean copy
	 * </p>
	 *
	 * @param source      源对象
	 * @param sourceClazz 源类
	 * @param targetClazz 转换成的类
	 * @return T
	 */
	@Nullable
	public static <T> T copyWithConvert(@Nullable Object source, Class<?> sourceClazz,
		Class<T> targetClazz) {
		if (source == null) {
			return null;
		}
		BeanCopier copier = BeanCopier.create(sourceClazz, targetClazz, true);
		T to = newInstance(targetClazz);
		copier.copy(source, to, new Converter(sourceClazz, targetClazz));
		return to;
	}

	/**
	 * 拷贝列表并对不同类型属性进行转换
	 *
	 * <p>
	 * 支持 map bean copy
	 * </p>
	 *
	 * @param sourceList  源对象列表
	 * @param targetClazz 转换成的类
	 * @return List
	 */
	public static <T> List<T> copyWithConvert(@Nullable Collection<?> sourceList,
		Class<T> targetClazz) {
		if (sourceList == null || sourceList.isEmpty()) {
			return Collections.emptyList();
		}
		List<T> outList = new ArrayList<>(sourceList.size());
		Class<?> sourceClazz = null;
		for (Object source : sourceList) {
			if (source == null) {
				continue;
			}
			if (sourceClazz == null) {
				sourceClazz = source.getClass();
			}
			T bean = copyWithConvert(source, sourceClazz, targetClazz);
			outList.add(bean);
		}
		return outList;
	}

	/**
	 * Copy the property values of the given source bean into the target class.
	 * <p>Note: The source and target classes do not have to match or even be derived
	 * from each other, as long as the properties match. Any bean properties that the source bean
	 * exposes but the target bean does not will silently be ignored.
	 * <p>This is just a convenience method. For more complex transfer needs,
	 *
	 * @param source      the source bean
	 * @param targetClazz the target bean class
	 * @return T
	 * @throws BeansException if the copying failed
	 */
	@Nullable
	public static <T> T copyProperties(@Nullable Object source, Class<T> targetClazz)
		throws BeansException {
		if (source == null) {
			return null;
		}
		T to = newInstance(targetClazz);
		copyProperties(source, to);
		return to;
	}

	/**
	 * Copy the property values of the given source bean into the target class.
	 * <p>Note: The source and target classes do not have to match or even be derived
	 * from each other, as long as the properties match. Any bean properties that the source bean
	 * exposes but the target bean does not will silently be ignored.
	 * <p>This is just a convenience method. For more complex transfer needs,
	 *
	 * @param sourceList  the source list bean
	 * @param targetClazz the target bean class
	 * @return List
	 * @throws BeansException if the copying failed
	 */
	public static <T> List<T> copyProperties(@Nullable Collection<?> sourceList,
		Class<T> targetClazz) throws BeansException {
		if (sourceList == null || sourceList.isEmpty()) {
			return Collections.emptyList();
		}
		List<T> outList = new ArrayList<>(sourceList.size());
		for (Object source : sourceList) {
			if (source == null) {
				continue;
			}
			T bean = copyProperties(source, targetClazz);
			outList.add(bean);
		}
		return outList;
	}

	/**
	 * 将对象装成map形式，map 不可写
	 *
	 * @param bean 源对象
	 * @return {Map}
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> toMap(@Nullable Object bean) {
		if (bean == null) {
			return new HashMap<>(0);
		}
		return BeanMap.create(bean);
	}

	/**
	 * 将对象装成map形式，map 可写
	 *
	 * @param bean 源对象
	 * @return {Map}
	 */
	public static Map<String, Object> toNewMap(@Nullable Object bean) {
		return new HashMap<>(toMap(bean));
	}

	/**
	 * 将map 转为 bean
	 *
	 * @param beanMap   map
	 * @param valueType 对象类型
	 * @return {T}
	 */
	public static <T> T toBean(Map<String, Object> beanMap, Class<T> valueType) {
		Objects.requireNonNull(beanMap, "beanMap Could not null");
		T to = newInstance(valueType);
		if (beanMap.isEmpty()) {
			return to;
		}
		copy(beanMap, to);
		return to;
	}

	/**
	 * 给一个Bean添加字段
	 *
	 * @param superBean 父级Bean
	 * @param props     新增属性
	 * @return 对象
	 */
	@Nullable
	public static Object generator(@Nullable Object superBean, BeanProperty... props) {
		if (superBean == null) {
			return null;
		}
		Class<?> superclass = superBean.getClass();
		Object genBean = generator(superclass, props);
		copy(superBean, genBean);
		return genBean;
	}

	/**
	 * 给一个class添加字段
	 *
	 * @param superclass 父级
	 * @param props      新增属性
	 * @return 对象
	 */
	public static Object generator(Class<?> superclass, BeanProperty... props) {
		BeanGenerator generator = new BeanGenerator();
		generator.setSuperclass(superclass);
		generator.setUseCache(true);
		generator.setContextClass(superclass);
		for (BeanProperty prop : props) {
			generator.addProperty(prop.getName(), prop.getClass());
		}
		return generator.create();
	}

	/**
	 * 比较对象
	 *
	 * @param src  源对象
	 * @param dist 新对象
	 * @return {BeanDiff}
	 */
	public static BeanDiff diff(final Object src, final Object dist) {
		Assert.notNull(src, "diff Object src is null.");
		Assert.notNull(src, "diff Object dist is null.");
		return diff(toMap(src), toMap(dist));
	}

	/**
	 * 比较Map
	 *
	 * @param src  源Map
	 * @param dist 新Map
	 * @return {BeanDiff}
	 */
	public static BeanDiff diff(final Map<String, Object> src, final Map<String, Object> dist) {
		Assert.notNull(src, "diff Map src is null.");
		Assert.notNull(src, "diff Map dist is null.");
		// 改变
		Map<String, Object> difference = new HashMap<>(8);
		difference.putAll(src);
		difference.putAll(dist);
		difference.entrySet().removeAll(src.entrySet());
		// 老值
		Map<String, Object> oldValues = new HashMap<>(8);
		difference.keySet().forEach((k) -> oldValues.put(k, src.get(k)));
		BeanDiff diff = new BeanDiff();
		diff.getFields().addAll(difference.keySet());
		diff.getOldValues().putAll(oldValues);
		diff.getNewValues().putAll(difference);
		return diff;
	}


	/**
	 * 复制Bean对象属性<br>
	 *
	 * @param source 源Bean对象
	 * @param target 目标Bean对象
	 * @since 2021-09-02 17:45:45
	 */
	public static void copyIgnoredNull(Object source, Object target) {
		cn.hutool.core.bean.BeanUtil
			.copyProperties(source, target, CopyOptions.create().ignoreNullValue().ignoreError());
	}

	/**
	 * 复制Bean对象属性<br>
	 *
	 * @param source 源Bean对象
	 * @param target 目标Bean对象
	 * @since 2021-09-02 17:45:45
	 */
	public static void copyIncludeNull(Object source, Object target) {
		cn.hutool.core.bean.BeanUtil
			.copyProperties(source, target, CopyOptions.create().ignoreError());
	}

	/**
	 * convert 类型转换
	 *
	 * @param value 值
	 * @param type  类型
	 * @return T
	 * @since 2021-09-02 17:46:05
	 */
	public static <T> T convert(Object value, Class<T> type) {
		if (value == null) {
			return null;
		}
		return ApplicationConversionService.getSharedInstance().convert(value, type);
	}

	/**
	 * 类型转换
	 *
	 * @param value 值
	 * @param type  类型
	 * @return T
	 * @since 2021-09-02 17:46:40
	 */
	public static <T> T tryConvert(Object value, Class<T> type) {
		try {
			return convert(value, type);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * bean 转换为 map
	 *
	 * @param bean 原始对象
	 * @return 结果
	 */
	public static Map<String, Object> beanToMap(Object bean) {
		ArgUtil.notNull(bean, "bean");

		try {
			Map<String, Object> map = new LinkedHashMap<>();
			Field[] fieldList = bean.getClass().getFields();

			for (Field field : fieldList) {
				final String fieldName = field.getName();
				final Object fieldValue = field.get(bean);
				map.put(fieldName, fieldValue);
			}
			return map;
		} catch (IllegalAccessException e) {
			throw new CommonRuntimeException(e);
		}
	}

	/**
	 * map 中的值设置到 bean 中 （1）map 为空，则直接返回 （2）map 中有对应的 key 且 value 不为空，则进行值的设置。
	 *
	 * @param map  map 信息
	 * @param bean 原始对象
	 */
	public static void mapToBean(final Map<String, Object> map, final Object bean) {
		ArgUtil.notNull(bean, "bean");
		if (MapUtil.isEmpty(map)) {
			return;
		}

		try {
			Field[] fieldList = bean.getClass().getFields();

			for (Field field : fieldList) {
				final String fieldName = field.getName();
				final Object fieldValue = map.get(fieldName);

				if (ObjectUtil.isNotNull(fieldValue)) {
					field.set(bean, fieldValue);
				}
			}
		} catch (IllegalAccessException e) {
			throw new CommonRuntimeException(e);
		}
	}

	/**
	 * 属性拷贝
	 *
	 * @param source 源头
	 * @param target 目标
	 */
	public static void copyProperties(final Object source, final Object target) {
		ObjectUtil.copyProperties(source, target);
	}

	///**
	// * 深度克隆
	// *
	// * @param obj obj
	// * @param <T> T
	// * @return T
	// * @since 2021-09-02 17:47:00
	// */
	//public static <T> T deepClone(T obj) {
	//	try {
	//		try (ByteArrayOutputStream byteOut = new ByteArrayOutputStream()) {
	//			try (ObjectOutputStream out = new ObjectOutputStream(byteOut)) {
	//				out.writeObject(obj);
	//				try (ByteArrayInputStream byteIn = new ByteArrayInputStream(
	//					byteOut.toByteArray())) {
	//					ObjectInputStream in = new ObjectInputStream(byteIn);
	//					return (T) in.readObject();
	//				}
	//			}
	//		}
	//	} catch (Exception e) {
	//		throw new BaseException(e.getMessage());
	//	}
	//}

	/**
	 * 跟踪类变动比较
	 *
	 * @author shuigedeng
	 * @version 2021.9
	 * @since 2021-09-02 19:41:13
	 */
	public static class BeanDiff {

		/**
		 * 变更字段
		 */
		@JsonIgnore
		private transient Set<String> fields = new HashSet<>();
		/**
		 * 旧值
		 */
		@JsonIgnore
		private transient Map<String, Object> oldValues = new HashMap<>();
		/**
		 * 新值
		 */
		@JsonIgnore
		private transient Map<String, Object> newValues = new HashMap<>();

		public Set<String> getFields() {
			return fields;
		}

		public Map<String, Object> getOldValues() {
			return oldValues;
		}

		public Map<String, Object> getNewValues() {
			return newValues;
		}

		public void setFields(Set<String> fields) {
			this.fields = fields;
		}

		public void setOldValues(Map<String, Object> oldValues) {
			this.oldValues = oldValues;
		}

		public void setNewValues(Map<String, Object> newValues) {
			this.newValues = newValues;
		}
	}
}
