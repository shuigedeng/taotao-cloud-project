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
package com.taotao.cloud.common.utils.reflect;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.exceptions.UtilException;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ReflectUtil;
import com.taotao.cloud.common.exception.BaseException;
import com.taotao.cloud.common.exception.BusinessException;
import com.taotao.cloud.common.utils.lang.StringUtil;
import com.taotao.cloud.common.utils.log.LogUtil;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.RecordComponent;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.annotation.Nullable;
import org.springframework.beans.BeansException;
import org.springframework.cglib.core.CodeGenerationException;
import org.springframework.core.convert.Property;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.util.ReflectionUtils;

/**
 * ReflectionUtil
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 15:02:23
 */
public class ReflectionUtil extends ReflectionUtils {

	/**
	 * 获取 Bean 的所有 get方法
	 *
	 * @param type 类
	 * @return PropertyDescriptor数组
	 */
	public static PropertyDescriptor[] getBeanGetters(Class type) {
		return getPropertyDescriptors(type, true, false);
	}

	/**
	 * 获取 Bean 的所有 set方法
	 *
	 * @param type 类
	 * @return PropertyDescriptor数组
	 */
	public static PropertyDescriptor[] getBeanSetters(Class type) {
		return getPropertyDescriptors(type, false, true);
	}

	/**
	 * 获取 Bean 的所有 PropertyDescriptor
	 *
	 * @param type  类
	 * @param read  读取方法
	 * @param write 写方法
	 * @return PropertyDescriptor数组
	 */
	public static PropertyDescriptor[] getPropertyDescriptors(Class type, boolean read,
		boolean write) {
		try {
			PropertyDescriptor[] all = BeanUtil.getPropertyDescriptors(type);
			if (read && write) {
				return all;
			} else {
				List<PropertyDescriptor> properties = new ArrayList<>(all.length);
				for (PropertyDescriptor pd : all) {
					if (read && pd.getReadMethod() != null) {
						properties.add(pd);
					} else if (write && pd.getWriteMethod() != null) {
						properties.add(pd);
					}
				}
				return properties.toArray(new PropertyDescriptor[0]);
			}
		} catch (BeansException ex) {
			throw new CodeGenerationException(ex);
		}
	}

	/**
	 * 获取 bean 的属性信息
	 *
	 * @param propertyType 类型
	 * @param propertyName 属性名
	 * @return {Property}
	 */
	@Nullable
	public static Property getProperty(Class<?> propertyType, String propertyName) {
		PropertyDescriptor propertyDescriptor = BeanUtil.getPropertyDescriptor(propertyType,
			propertyName);
		if (propertyDescriptor == null) {
			return null;
		}
		return getProperty(propertyType, propertyDescriptor, propertyName);
	}

	/**
	 * 获取 bean 的属性信息
	 *
	 * @param propertyType       类型
	 * @param propertyDescriptor PropertyDescriptor
	 * @param propertyName       属性名
	 * @return {Property}
	 */
	public static Property getProperty(Class<?> propertyType, PropertyDescriptor propertyDescriptor,
		String propertyName) {
		Method readMethod = propertyDescriptor.getReadMethod();
		Method writeMethod = propertyDescriptor.getWriteMethod();
		return new Property(propertyType, readMethod, writeMethod, propertyName);
	}

	/**
	 * 获取 bean 的属性信息
	 *
	 * @param propertyType 类型
	 * @param propertyName 属性名
	 * @return {Property}
	 */
	@Nullable
	public static TypeDescriptor getTypeDescriptor(Class<?> propertyType, String propertyName) {
		Property property = getProperty(propertyType, propertyName);
		if (property == null) {
			return null;
		}
		return new TypeDescriptor(property);
	}

	/**
	 * 获取 类属性信息
	 *
	 * @param propertyType       类型
	 * @param propertyDescriptor PropertyDescriptor
	 * @param propertyName       属性名
	 * @return {Property}
	 */
	public static TypeDescriptor getTypeDescriptor(Class<?> propertyType,
		PropertyDescriptor propertyDescriptor, String propertyName) {
		Method readMethod = propertyDescriptor.getReadMethod();
		Method writeMethod = propertyDescriptor.getWriteMethod();
		Property property = new Property(propertyType, readMethod, writeMethod, propertyName);
		return new TypeDescriptor(property);
	}

	/**
	 * 获取 类属性
	 *
	 * @param clazz     类信息
	 * @param fieldName 属性名
	 * @return Field
	 */
	@Nullable
	public static Field getField(Class<?> clazz, String fieldName) {
		while (clazz != Object.class) {
			try {
				return clazz.getDeclaredField(fieldName);
			} catch (NoSuchFieldException e) {
				clazz = clazz.getSuperclass();
			}
		}
		return null;
	}

	/**
	 * 获取 所有 field 属性上的注解
	 *
	 * @param clazz           类
	 * @param fieldName       属性名
	 * @param annotationClass 注解
	 * @param <T>             注解泛型
	 * @return 注解
	 */
	@Nullable
	public static <T extends Annotation> T getAnnotation(Class<?> clazz, String fieldName,
		Class<T> annotationClass) {
		Field field = ReflectUtil.getField(clazz, fieldName);
		if (field == null) {
			return null;
		}
		return field.getAnnotation(annotationClass);
	}


	/**
	 * 重写 setField 的方法，用于处理 setAccessible 的问题
	 *
	 * @param field  Field
	 * @param target Object
	 * @param value  value
	 */
	public static void setField(Field field, @Nullable Object target, @Nullable Object value) {
		makeAccessible(field);
		ReflectionUtils.setField(field, target, value);
	}

	/**
	 * 重写 setField 的方法，用于处理 setAccessible 的问题
	 *
	 * @param field  Field
	 * @param target Object
	 * @return value
	 */
	@Nullable
	public static Object getField(Field field, @Nullable Object target) {
		makeAccessible(field);
		return ReflectionUtils.getField(field, target);
	}

	/**
	 * 重写 setField 的方法，用于处理 setAccessible 的问题
	 *
	 * @param fieldName Field name
	 * @param target    Object
	 * @return value
	 */
	@Nullable
	public static Object getField(String fieldName, @Nullable Object target) {
		if (target == null) {
			return null;
		}
		Class<?> targetClass = target.getClass();
		Field field = getField(targetClass, fieldName);
		if (field == null) {
			throw new IllegalArgumentException(fieldName + " not in" + targetClass);
		}
		return getField(field, target);
	}

	/**
	 * 重写 invokeMethod 的方法，用于处理 setAccessible 的问题
	 *
	 * @param method Method
	 * @param target Object
	 * @return value
	 */
	@Nullable
	public static Object invokeMethod(Method method, @Nullable Object target) {
		return invokeMethod(method, target, new Object[0]);
	}

	/**
	 * 重写 invokeMethod 的方法，用于处理 setAccessible 的问题
	 *
	 * @param method Method
	 * @param target Object
	 * @param args   args
	 * @return value
	 */
	@Nullable
	public static Object invokeMethod(Method method, @Nullable Object target,
		@Nullable Object... args) {
		makeAccessible(method);
		return ReflectionUtils.invokeMethod(method, target, args);
	}

	private ReflectionUtil() {
	}

	/**
	 * classForName
	 *
	 * @param type 类型
	 * @return 类型
	 * @since 2021-09-02 15:02:32
	 */
	public static Class<?> classForName(String type) {
		try {
			return Class.forName(type);
		} catch (Exception exp) {
			throw new BaseException(exp.getMessage());
		}
	}

	/**
	 * 类型
	 *
	 * @param type 类型
	 * @return 类型
	 * @since 2021-09-02 15:02:39
	 */
	public static Class<?> tryClassForName(String type) {
		try {
			return Class.forName(type);
		} catch (Exception exp) {
			return null;
		}
	}

	/**
	 * 获取类中方法
	 *
	 * @param cls        类
	 * @param methodName 方法名
	 * @return 对象方法
	 * @since 2021-09-02 15:02:47
	 */
	public static Method findMethod(Class<?> cls, String methodName) {
		Method find = null;
		while (cls != null) {
			for (Method[] methods : new Method[][]{cls.getMethods(), cls.getDeclaredMethods()}) {
				for (Method method : methods) {
					if (method.getName().equalsIgnoreCase(methodName)) {
						find = method;
						break;
					}
				}
			}
			cls = cls.getSuperclass();
		}
		return find;
	}

	/**
	 * 通过枚举名称获取枚举对象
	 *
	 * @param cls        枚举类
	 * @param methodName 方法名称
	 * @param name       name  枚举名称 区分大小写 必须完全一样
	 * @return 对象方法
	 * @since 2021-09-15 15:56:01
	 */
	public static Object findEnumObjByName(Class<?> cls, String methodName, String name)
		throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
		Object[] objects = cls.getEnumConstants();
		Method method = cls.getMethod(methodName);
		for (Object object : objects) {
			Object invoke = method.invoke(object);
			if (invoke.equals(name)) {
				return object;
			}
		}
		return null;
	}


	/**
	 * 获取类中方法
	 *
	 * @param cls        类型
	 * @param methodName 方法名
	 * @param argsTypes  参数类型
	 * @return 对象方法
	 * @since 2021-09-02 15:03:01
	 */
	public static Method findMethod0(Class<?> cls, String methodName, Class<?>... argsTypes)
		throws NoSuchMethodException, SecurityException {
		Method find = null;
		if (cls != null) {
			find = cls.getMethod(methodName, argsTypes);
		}
		return find;
	}

	/**
	 * 调用对象方法
	 *
	 * @param obj          对象
	 * @param methodName   方法名
	 * @param param        参数
	 * @param defaultValue 默认值
	 * @return 对象数据
	 * @since 2021-09-02 15:03:09
	 */
	public static <T> T tryCallMethod(Object obj, String methodName, Object[] param,
		T defaultValue) {
		try {
			if (obj != null) {
				Method find = findMethod(obj.getClass(), methodName);
				if (find != null) {
					if (!find.isAccessible()) {
						find.setAccessible(true);
					}
					return (T) find.invoke(obj, param);
				}
			}
			return defaultValue;
		} catch (Exception exp) {
			LogUtil.error(exp);
			return defaultValue;
		}
	}

	/**
	 * 调用对象方法
	 *
	 * @param obj        对象
	 * @param methodName 方法名
	 * @param param      参数
	 * @return 对象数据
	 * @since 2021-09-02 15:03:17
	 */
	public static Object callMethod(Object obj, String methodName, Object[] param) {
		try {
			Method find = findMethod(obj.getClass(), methodName);
			if (find != null) {
				return find.invoke(obj, param);
			}
			throw new Exception("未找到方法" + StringUtil.nullToEmpty(methodName));
		} catch (Exception exp) {
			LogUtil.error(exp);
			throw new BaseException(exp.getMessage());
		}
	}

	/**
	 * 调用对象方法
	 *
	 * @param clazz      类型
	 * @param methodName 方法名
	 * @param params     参数
	 * @return 对象数据
	 * @since 2021-09-02 15:03:25
	 */
	public static Object callMethod(Class<?> clazz, String methodName, Object[] params) {
		try {
			Method find = findMethod(clazz, methodName);
			if (find != null) {
				return find.invoke(null, params);
			}
			throw new Exception("未找到方法" + StringUtil.nullToEmpty(methodName));
		} catch (Exception exp) {
			LogUtil.error(exp);
			throw new BaseException(exp.getMessage());
		}
	}

	/**
	 * 调用对象方法
	 *
	 * @param clazz      类型
	 * @param methodName 方法名
	 * @param params     参数
	 * @param paramTypes 参数类型
	 * @return 对象数据
	 * @since 2021-09-02 15:03:38
	 */
	public static Object callMethodWithParams(Class<?> clazz, String methodName, Object[] params,
		Class<?>... paramTypes) {
		try {
			Method find = findMethod0(clazz, methodName, paramTypes);
			if (find != null) {
				return find.invoke(null, params);
			}
			throw new Exception("未找到方法" + StringUtil.nullToEmpty(methodName));
		} catch (Exception exp) {
			LogUtil.error(exp);
			throw new BaseException(exp.getMessage());
		}
	}

	/**
	 * 调用对象方法
	 *
	 * @param object     对象
	 * @param methodName 方法名
	 * @param params     参数
	 * @param paramTypes 参数类型
	 * @return 对象数据
	 * @since 2021-09-02 15:03:48
	 */
	public static Object callMethodWithParams(Object object, String methodName, Object[] params,
		Class<?>... paramTypes) {
		try {
			Method find = findMethod0(object.getClass(), methodName, paramTypes);
			if (find != null) {
				return find.invoke(object, params);
			}
			throw new Exception("未找到方法" + StringUtil.nullToEmpty(methodName));
		} catch (Exception exp) {
			LogUtil.error(exp);
			throw new BaseException(exp.getMessage());
		}
	}

	/**
	 * 获取字段
	 *
	 * @param cls  类型
	 * @param name 字段名称
	 * @return 字段
	 * @since 2021-09-02 15:03:57
	 */
	public static Field findField(Class<?> cls, String name) {
		Field find = null;
		while (cls != null) {
			for (Field[] fields : new Field[][]{cls.getFields(), cls.getDeclaredFields()}) {
				for (Field field : fields) {
					if (field.getName().equalsIgnoreCase(name)) {
						find = field;
						return find;
					}
				}
			}
			cls = cls.getSuperclass();
		}
		return find;
	}

	/**
	 * 查询record
	 *
	 * @param cls  类型
	 * @param name 名称
	 * @return record类型
	 * @since 2022-03-28 11:23:44
	 */
	public static RecordComponent findRecord(Class<?> cls, String name) {
		RecordComponent find = null;
		while (cls != null) {
			RecordComponent[] recordComponents = cls.getRecordComponents();
			if (recordComponents.length != 0) {
				for (RecordComponent recordComponent : recordComponents) {
					if (recordComponent.getName().equalsIgnoreCase(name)) {
						find = recordComponent;
						return find;
					}
				}
			}
			cls = cls.getSuperclass();
		}
		return find;
	}

	/**
	 * 获取字段值
	 *
	 * @param obj  对象
	 * @param name 字段名称
	 * @return 字段值
	 * @since 2021-09-02 15:04:04
	 */
	public static <T> T getFieldValue(Object obj, String name) {
		try {
			Field find = findField(obj.getClass(), name);
			if (find != null) {
				if (!find.isAccessible()) {
					find.setAccessible(true);
				}
				return (T) find.get(obj);
			}
			throw new Exception("未找到字段" + StringUtil.nullToEmpty(name));
		} catch (Exception e) {
			LogUtil.error(e);
			throw new BaseException(e.getMessage());
		}
	}

	/**
	 * 获取字段值
	 *
	 * @param obj          对象
	 * @param name         字段名称
	 * @param defaultValue 默认值
	 * @return 字段值
	 * @since 2021-09-02 15:04:13
	 */
	public static <T> T tryGetFieldValue(Object obj, String name, T defaultValue) {
		try {
			if (obj != null) {
				Field find = findField(obj.getClass(), name);
				if (find != null) {
					if (!find.isAccessible()) {
						find.setAccessible(true);
					}
					return (T) find.get(obj);
				}
			}
			return defaultValue;
		} catch (Exception exp) {
			LogUtil.error(exp);
			return defaultValue;
		}
	}

	/**
	 * 获取静态字段
	 *
	 * @param cls          对象全路径
	 * @param name         字段名称
	 * @param defaultValue 默认值
	 * @return 静态字段值
	 * @since 2021-09-02 15:04:21
	 */
	public static <T> T tryGetStaticFieldValue(String cls, String name, T defaultValue) {
		try {
			return tryGetStaticFieldValue(Class.forName(cls), name, defaultValue);
		} catch (Exception exp) {
			LogUtil.error(exp);
			return defaultValue;
		}
	}

	/**
	 * 获取静态字段
	 *
	 * @param cls          类型
	 * @param name         字段名称
	 * @param defaultValue 默认值
	 * @return 静态字段值
	 * @since 2021-09-02 15:04:28
	 */
	public static <T> T tryGetStaticFieldValue(Class<?> cls, String name, T defaultValue) {
		try {
			if (cls != null) {
				Field find = findField(cls, name);
				if (find != null) {
					if (!find.isAccessible()) {
						find.setAccessible(true);
					}
					return (T) find.get(cls);
				}
			}
			return defaultValue;
		} catch (Exception exp) {
			LogUtil.error(exp);
			return defaultValue;
		}
	}

	/**
	 * 设置字段
	 *
	 * @param field 字段
	 * @param obj   对象
	 * @param value 值
	 * @since 2021-09-02 15:04:36
	 */
	public static void setFieldValue(Field field, Object obj, Object value) {
		try {
			if (!field.isAccessible()) {
				field.setAccessible(true);
			}
			field.set(obj, value);
		} catch (Exception exp) {
			LogUtil.error(exp);
			throw new BaseException(exp.getMessage());
		}
	}

	/**
	 * copyPropertiesIfRecord 主要用于 复制dto对象到t对象中
	 *
	 * @param t   实体
	 * @param dto dto
	 * @return 实体对象
	 * @since 2021-10-20 09:19:45
	 */
	public static <T, DTO> T copyPropertiesIfRecord(T t, DTO dto) {
		if (dto.getClass().isRecord()) {
			Field[] fields = dto.getClass().getDeclaredFields();
			for (Field field : fields) {
				if (!field.getName().equals("serialVersionUID")) {
					Object value = ReflectionUtil.tryGetValue(dto, field.getName());
					Field field1 = ReflectionUtil.findField(t.getClass(), field.getName());
					if (Objects.nonNull(field1) && Objects.nonNull(value)) {
						ReflectionUtil.setFieldValue(field1, t, value);
					}
				}
			}
		} else {
			BeanUtil.copyProperties(dto, t, CopyOptions.create().ignoreNullValue());
		}
		return t;
	}

	/**
	 * copyDataIfRecord 主要用于复制t对象到vo对象中
	 *
	 * @param clazz vo对象class
	 * @param t     t
	 * @return vo对象
	 * @since 2021-10-20 10:42:23
	 */
	public static <T, VO> VO copyPropertiesIfRecord(Class<VO> clazz, T t) {
		VO vo;
		if (clazz.isRecord()) {
			Field[] fields = clazz.getDeclaredFields();
			List<Object> params = new ArrayList<>();
			List<Field> fieldList = new ArrayList<>();

			for (Field field : fields) {
				if (!field.getName().equals("serialVersionUID")) {
					Object value = ReflectionUtil.tryGetValue(t, field.getName());
					params.add(value);
					fieldList.add(field);
				}
			}

			vo = newInstance(fieldList, clazz, params.toArray());
		} else {
			vo = ReflectUtil.newInstanceIfPossible(clazz);
			BeanUtil.copyProperties(t, vo, CopyOptions.create().ignoreNullValue());
		}
		return vo;
	}

	/**
	 * 构造实例
	 *
	 * @param fields 字段
	 * @param clazz  类型
	 * @param params 参数
	 * @return 对象
	 * @since 2022-03-28 11:19:22
	 */
	public static <T> T newInstance(List<Field> fields, Class<T> clazz, Object... params)
		throws UtilException {
		if (ArrayUtil.isEmpty(params)) {
			final Constructor<T> constructor = ReflectUtil.getConstructor(clazz);
			try {
				return constructor.newInstance();
			} catch (Exception e) {
				throw new UtilException(e, "Instance class [{}] error!", clazz);
			}
		}

		final Class<?>[] paramTypes = getClasses(fields);
		final Constructor<T> constructor = ReflectUtil.getConstructor(clazz, paramTypes);
		if (null == constructor) {
			throw new UtilException("No Constructor matched for parameter types: [{}]",
				new Object[]{paramTypes});
		}
		try {
			return constructor.newInstance(params);
		} catch (Exception e) {
			throw new UtilException(e, "Instance class [{}] error!", clazz);
		}
	}

	/**
	 * 获取字段类型列表
	 *
	 * @param fields 字段列表
	 * @return 字段类型列表
	 * @since 2022-03-28 11:18:46
	 */
	public static Class<?>[] getClasses(List<Field> fields) {
		Class<?>[] classes = new Class<?>[fields.size()];
		for (int i = 0; i < fields.size(); i++) {
			Field field = fields.get(i);
			classes[i] = field.getType();
		}
		return classes;
	}

	/**
	 * 校验字段是否存在
	 *
	 * @param dtoClass    dto类型
	 * @param entityClass 实体类型
	 * @return 结果
	 * @since 2021-10-13 17:36:08
	 */
	public static Boolean checkField(Class<?> dtoClass, Class<?> entityClass) {
		Field[] declaredFields = dtoClass.getDeclaredFields();
		RecordComponent[] recordComponents = dtoClass.getRecordComponents();

		if (declaredFields.length == 0) {
			if (Objects.isNull(recordComponents) || recordComponents.length == 0) {
				throw new BusinessException("字段参数不存在");
			}
		}

		if (declaredFields.length != 0) {
			for (Field declaredField : declaredFields) {
				String filedName = declaredField.getName();

				if (!filedName.equals("serialVersionUID")) {
					Field field = ReflectionUtil.findField(entityClass, filedName);
					if (Objects.isNull(field)) {
						throw new BusinessException(filedName + "字段值错误");
					}
				}
			}
		}

		if (Objects.nonNull(recordComponents) && recordComponents.length != 0) {
			for (RecordComponent recordComponent : recordComponents) {
				String filedName = recordComponent.getName();
				if (!filedName.equals("serialVersionUID")) {
					Field field = ReflectionUtil.findField(entityClass, filedName);
					if (Objects.isNull(field)) {
						throw new BusinessException(filedName + "字段值错误");
					}
				}
			}
		}

		return true;
	}

	/**
	 * 校验字段
	 *
	 * @param filedName   字段名称
	 * @param entityClass 实体类
	 * @return {@link Boolean }
	 * @author 结果
	 * @since 2021-10-13 17:36:08
	 */
	public static Boolean checkField(String filedName, Class<?> entityClass) {
		if (!filedName.equals("serialVersionUID")) {
			Field field = ReflectionUtil.findField(entityClass, filedName);
			if (Objects.isNull(field)) {
				throw new BusinessException(filedName + "字段值错误");
			}
		}

		return true;
	}

	/**
	 * 获取值
	 *
	 * @param obj  对象
	 * @param path 路径
	 * @param deft 默认值
	 * @return 字段值
	 * @since 2021-09-02 15:04:43
	 */
	public static <T> T tryGetValue(Object obj, String path, T deft) {
		if (obj == null || path == null || path.length() == 0) {
			return deft;
		}
		Object object = obj;
		for (String name : path.split("\\.")) {
			if (object == null) {
				break;
			}
			Object value = tryGetFieldValue(object, name, null);
			if (value == null) {
				object = tryCallMethod(object, name, null, null);
			} else {
				object = value;
			}
		}
		return object == null ? deft : (T) object;
	}

	/**
	 * 获取字段值
	 *
	 * @param obj  对象
	 * @param path 路径
	 * @return 字段值
	 * @since 2021-09-02 15:04:53
	 */
	public static <T> T tryGetValue(Object obj, String path) {
		return tryGetValue(obj, path, null);
	}
}
