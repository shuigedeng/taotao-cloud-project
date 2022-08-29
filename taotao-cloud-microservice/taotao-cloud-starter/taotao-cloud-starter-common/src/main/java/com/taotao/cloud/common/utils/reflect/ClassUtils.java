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
package com.taotao.cloud.common.utils.reflect;


import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.taotao.cloud.common.constant.FieldConst;
import com.taotao.cloud.common.exception.CommonRuntimeException;
import com.taotao.cloud.common.utils.collection.ArrayUtils;
import com.taotao.cloud.common.utils.collection.CollectionUtils;
import com.taotao.cloud.common.utils.collection.MapUtils;
import com.taotao.cloud.common.utils.common.ArgUtils;

import com.taotao.cloud.common.utils.lang.ObjectUtils;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.core.BridgeMethodResolver;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.MethodParameter;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.SynthesizingMethodParameter;
import org.springframework.lang.Nullable;
import org.springframework.web.method.HandlerMethod;

/**
 * Class 工具类
 */
public final class ClassUtils extends org.springframework.util.ClassUtils {

	private ClassUtils() {
	}

	/**
	 * 序列版本编号常量
	 */
	public static final String SERIAL_VERSION_UID = "serialVersionUID";

	/**
	 * 获取对应类的默认变量名： 1. 首字母小写 String=》string
	 *
	 * @param className 类名称
	 * @return 类的默认变量名
	 */
	public static String getClassVar(final String className) {
		return className.substring(0, 1).toLowerCase() + className.substring(1);
	}

	/**
	 * 获取类所有的字段信息 ps: 这个方法有个问题 如果子类和父类有相同的字段 会不会重复 1. 还会获取到 serialVersionUID 这个字段。 0.1.77 移除
	 *
	 * @param clazz 类
	 * @return 字段列表
	 */
	public static List<Field> getAllFieldList(final Class<?> clazz) {
		List<Field> allFieldList = new ArrayList<>();
		Class<?> tempClass = clazz;
		while (tempClass != null) {
			allFieldList.addAll(Lists.newArrayList(tempClass.getDeclaredFields()));
			tempClass = tempClass.getSuperclass();
		}

		List<Field> resultList = Lists.newArrayList();
		for (Field field : allFieldList) {
			String fieldName = field.getName();

			// 跳过序列化字段
			if (FieldConst.SERIAL_VERSION_UID.equals(fieldName)) {
				continue;
			}

			field.setAccessible(true);
			resultList.add(field);
		}
		return resultList;
	}

	/**
	 * 获取可变更的字段信息 （1）过滤掉 final 的字段信息
	 *
	 * @param clazz 类信息
	 * @return 0.1.35
	 */
	public static List<Field> getModifyableFieldList(final Class clazz) {
		List<Field> allFieldList = getAllFieldList(clazz);
		if (CollectionUtils.isEmpty(allFieldList)) {
			return allFieldList;
		}

		// 过滤掉 final 的字段
		return CollectionUtils.filterList(allFieldList,
			field -> Modifier.isFinal(field.getModifiers()));
	}

	/**
	 * 获取类所有的字段信息 map ps: 这个方法有个问题 如果子类和父类有相同的字段 会不会重复 1. 还会获取到 serialVersionUID 这个字段。
	 *
	 * @param clazz 类
	 * @return 字段列表 map
	 */
	public static Map<String, Field> getAllFieldMap(final Class clazz) {
		List<Field> fieldList = ClassUtils.getAllFieldList(clazz);
		return MapUtils.toMap(fieldList, Field::getName);
	}


	/**
	 * bean 转换为 map
	 *
	 * @param bean 原始对象
	 * @return 结果
	 * @deprecated 已废弃
	 */
	@Deprecated
	public static Map<String, Object> beanToMap(Object bean) {
		try {
			Map<String, Object> map = new LinkedHashMap<>();
			List<Field> fieldList = ClassUtils.getAllFieldList(bean.getClass());

			for (Field field : fieldList) {
				final String fieldName = field.getName();
				final Object fieldValue = field.get(bean);
				map.put(fieldName, fieldValue);
			}
			return map;
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 获取对象的实例化
	 *
	 * @param clazz 类
	 * @param <T>   泛型
	 * @return 实例化对象
	 */
	public static <T> T newInstance(final Class<T> clazz) {
		try {
			return clazz.getDeclaredConstructor().newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			throw new RuntimeException(e);
		} catch (InvocationTargetException | NoSuchMethodException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取所有字段的 read 方法列表
	 *
	 * @param clazz 类信息
	 * @return 方法列表
	 * @throws IntrospectionException if any
	 */
	public static List<Method> getAllFieldsReadMethods(final Class clazz)
		throws IntrospectionException {
		List<Field> fieldList = getAllFieldList(clazz);
		if (CollectionUtils.isEmpty(fieldList)) {
			return Collections.emptyList();
		}

		List<Method> methods = new ArrayList<>();
		for (Field field : fieldList) {
			PropertyDescriptor pd = new PropertyDescriptor(field.getName(), clazz);
			//获得get方法
			Method getMethod = pd.getReadMethod();
			methods.add(getMethod);
		}
		return methods;
	}

	/**
	 * 获取当前的 class loader
	 *
	 * @return 当前的 class loader
	 */
	public static ClassLoader currentClassLoader() {
		return Thread.currentThread().getContextClassLoader();
	}

	/**
	 * 获取类信息
	 *
	 * @param className 类名称信息
	 * @return 构建后的类信息
	 */
	public static Class getClass(final String className) {
		ArgUtils.notEmpty(className, "className");

		try {
			return currentClassLoader().loadClass(className);
		} catch (ClassNotFoundException e) {
			throw new CommonRuntimeException(e);
		}
	}

	/**
	 * 获取方法信息
	 *
	 * @param clazz      类信息
	 * @param methodName 方法名称
	 * @param paramTypes 参数类型
	 * @return 方法信息
	 */
	@SuppressWarnings("unchecked")
	public static Method getMethod(final Class clazz,
		final String methodName,
		final Class... paramTypes) {
		ArgUtils.notNull(clazz, "clazz");
		ArgUtils.notEmpty(methodName, "methodName");

		try {
			return clazz.getMethod(methodName, paramTypes);
		} catch (NoSuchMethodException e) {
			throw new CommonRuntimeException(e);
		}
	}

	/**
	 * 获取方法信息
	 *
	 * @param clazz      类信息
	 * @param methodName 方法名称
	 * @return 方法信息
	 */
	@SuppressWarnings("unchecked")
	public static Method getMethod(final Class<?> clazz,
		final String methodName) {
		ArgUtils.notNull(clazz, "clazz");
		ArgUtils.notEmpty(methodName, "methodName");

		Method[] methods = clazz.getMethods();
		for (Method method : methods) {
			if (method.getName().equals(methodName)) {
				return method;
			}
		}

		throw new CommonRuntimeException("对应方法不存在!");
	}

	/**
	 * 获取构造器信息
	 *
	 * @param clazz      类
	 * @param paramTypes 参数类型数组
	 * @return 构造器
	 */
	@SuppressWarnings("unchecked")
	public static Constructor getConstructor(final Class clazz,
		final Class... paramTypes) {
		ArgUtils.notNull(clazz, "clazz");

		try {
			return clazz.getConstructor(paramTypes);
		} catch (NoSuchMethodException e) {
			throw new CommonRuntimeException(e);
		}
	}

	/**
	 * 获取方法列表 （1）当前类和超类的 public 方法
	 *
	 * @param tClass 类型
	 * @return 结果列表
	 */
	public static List<Method> getMethodList(final Class tClass) {
		ArgUtils.notNull(tClass, "tClass");

		Method[] methods = tClass.getMethods();
		return ArrayUtils.toList(methods);
	}

	/**
	 * 获取方法列表 （1）当前类的方法，包括私有。 （2）暂时不进行递归处理，后期看是否有需要。
	 *
	 * @param tClass 类型
	 * @return 结果列表
	 */
	public static List<Method> getDeclaredMethodList(final Class tClass) {
		ArgUtils.notNull(tClass, "tClass");

		Method[] methods = tClass.getDeclaredMethods();
		return ArrayUtils.toList(methods);
	}

	/**
	 * 获取所有父类信息
	 *
	 * @param clazz 类型
	 * @return 所有父类信息
	 */
	public static List<Class> getAllSuperClass(final Class clazz) {
		ArgUtils.notNull(clazz, "clazz");

		Set<Class> classSet = Sets.newHashSet();

		// 添加所有父类
		Class tempClass = clazz.getSuperclass();
		while (tempClass != null) {
			classSet.add(tempClass);
			tempClass = tempClass.getSuperclass();
		}

		return Lists.newArrayList(classSet);
	}

	/**
	 * 获取所有接口信息
	 *
	 * @param clazz 类型
	 * @return 所有父类信息
	 */
	public static List<Class> getAllInterfaces(final Class clazz) {
		ArgUtils.notNull(clazz, "clazz");

		Set<Class> classSet = Sets.newHashSet();

		// 添加所有父类
		Class[] interfaces = clazz.getInterfaces();
		if (ArrayUtils.isNotEmpty(interfaces)) {
			classSet.addAll(ArrayUtils.toList(interfaces));

			for (Class interfaceClass : interfaces) {
				List<Class> classList = getAllInterfaces(interfaceClass);
				if (CollectionUtils.isNotEmpty(classList)) {
					classSet.addAll(classList);
				}
			}
		}

		return Lists.newArrayList(classSet);
	}

	/**
	 * 获取所有接口信息和父类信息
	 *
	 * @param clazz 类型
	 * @return 接口信息和父类信息
	 */
	public static List<Class> getAllInterfacesAndSuperClass(final Class clazz) {
		ArgUtils.notNull(clazz, "clazz");

		Set<Class> classSet = Sets.newHashSet();
		classSet.addAll(getAllInterfaces(clazz));
		classSet.addAll(getAllSuperClass(clazz));

		return Lists.newArrayList(classSet);
	}

	/**
	 * 是否可以设置
	 *
	 * @param sourceType 原始类型
	 * @param targetType 目标类型
	 * @return 结果
	 */
	public static boolean isAssignable(final Class<?> sourceType, final Class<?> targetType) {
		// 如果有任何一个字段为空，直接返回
		if (ObjectUtils.isNull(sourceType)
			|| ObjectUtils.isNull(targetType)) {
			return false;
		}

		if (sourceType.isAssignableFrom(targetType)) {
			return true;
		}

		// 基础类型的判断
		Class resolvedPrimitive;
		if (sourceType.isPrimitive()) {
			resolvedPrimitive = PrimitiveUtils.getPrimitiveType(targetType);
			return sourceType == resolvedPrimitive;
		} else {
			resolvedPrimitive = PrimitiveUtils.getPrimitiveType(targetType);
			return resolvedPrimitive != null && sourceType.isAssignableFrom(resolvedPrimitive);
		}
	}

	/**
	 * 获取Annotation
	 *
	 * @param method         Method
	 * @param annotationType 注解类
	 * @return 注解类型
	 * @since 2021-09-02 17:40:12
	 */
	@Nullable
	public static <A extends Annotation> A getAnnotation(Method method, Class<A> annotationType) {
		Class<?> targetClass = method.getDeclaringClass();
		// The method may be on an interface, but we need attributes from the target class.
		// If the target class is null, the method will be unchanged.
		Method specificMethod = ClassUtils.getMostSpecificMethod(method, targetClass);
		// If we are dealing with method with generic parameters, find the original method.
		specificMethod = BridgeMethodResolver.findBridgedMethod(specificMethod);
		// 先找方法，再找方法上的类
		A annotation = AnnotatedElementUtils.findMergedAnnotation(specificMethod, annotationType);
		if (null != annotation) {
			return annotation;
		}
		// 获取类上面的Annotation，可能包含组合注解，故采用spring的工具类
		return AnnotatedElementUtils.findMergedAnnotation(specificMethod.getDeclaringClass(),
			annotationType);
	}

	/**
	 * 判断是否有注解 Annotation
	 *
	 * @param method         Method
	 * @param annotationType 注解类
	 * @return 结果
	 * @since 2021-09-02 17:40:50
	 */
	public static <A extends Annotation> boolean isAnnotated(Method method,
		Class<A> annotationType) {
		// 先找方法，再找方法上的类
		boolean isMethodAnnotated = AnnotatedElementUtils.isAnnotated(method, annotationType);
		if (isMethodAnnotated) {
			return true;
		}
		// 获取类上面的Annotation，可能包含组合注解，故采用spring的工具类
		Class<?> targetClass = method.getDeclaringClass();
		return AnnotatedElementUtils.isAnnotated(targetClass, annotationType);
	}

	private static final ParameterNameDiscoverer PARAMETER_NAME_DISCOVERER = new DefaultParameterNameDiscoverer();

	/**
	 * 获取方法参数信息
	 *
	 * @param constructor    构造器
	 * @param parameterIndex 参数序号
	 * @return 方法参数
	 */
	public static MethodParameter getMethodParameter(Constructor<?> constructor,
		int parameterIndex) {
		MethodParameter methodParameter = new SynthesizingMethodParameter(constructor,
			parameterIndex);
		methodParameter.initParameterNameDiscovery(PARAMETER_NAME_DISCOVERER);
		return methodParameter;
	}

	/**
	 * 获取方法参数信息
	 *
	 * @param method         方法
	 * @param parameterIndex 参数序号
	 * @return 方法参数
	 */
	public static MethodParameter getMethodParameter(Method method, int parameterIndex) {
		MethodParameter methodParameter = new SynthesizingMethodParameter(method, parameterIndex);
		methodParameter.initParameterNameDiscovery(PARAMETER_NAME_DISCOVERER);
		return methodParameter;
	}

	/**
	 * 获取Annotation
	 *
	 * @param handlerMethod  HandlerMethod
	 * @param annotationType 注解类
	 * @return 注解类型
	 */
	@Nullable
	public static <A extends Annotation> A getAnnotation(HandlerMethod handlerMethod, Class<A> annotationType) {
		// 先找方法，再找方法上的类
		A annotation = handlerMethod.getMethodAnnotation(annotationType);
		if (null != annotation) {
			return annotation;
		}
		// 获取类上面的Annotation，可能包含组合注解，故采用spring的工具类
		Class<?> beanType = handlerMethod.getBeanType();
		return AnnotatedElementUtils.findMergedAnnotation(beanType, annotationType);
	}

	///**
	// * 获取Annotation
	// *
	// * @param method         Method
	// * @param annotationType 注解类
	// * @param <A>            泛型标记
	// * @return {Annotation}
	// */
	//@Nullable
	//public static <A extends Annotation> A getAnnotation(Method method, Class<A> annotationType) {
	//	Class<?> targetClass = method.getDeclaringClass();
	//	// The method may be on an interface, but we need attributes from the target class.
	//	// If the target class is null, the method will be unchanged.
	//	Method specificMethod = ClassUtil.getMostSpecificMethod(method, targetClass);
	//	// If we are dealing with method with generic parameters, find the original method.
	//	specificMethod = BridgeMethodResolver.findBridgedMethod(specificMethod);
	//	// 先找方法，再找方法上的类
	//	A annotation = AnnotatedElementUtils.findMergedAnnotation(specificMethod, annotationType);
	//	if (null != annotation) {
	//		return annotation;
	//	}
	//	// 获取类上面的Annotation，可能包含组合注解，故采用spring的工具类
	//	return AnnotatedElementUtils.findMergedAnnotation(specificMethod.getDeclaringClass(),
	//		annotationType);
	//}

	///**
	// * 获取Annotation
	// *
	// * @param handlerMethod  HandlerMethod
	// * @param annotationType 注解类
	// * @param <A>            泛型标记
	// * @return {Annotation}
	// */
	//@Nullable
	//public static <A extends Annotation> A getAnnotation(HandlerMethod handlerMethod,
	//	Class<A> annotationType) {
	//	// 先找方法，再找方法上的类
	//	A annotation = handlerMethod.getMethodAnnotation(annotationType);
	//	if (null != annotation) {
	//		return annotation;
	//	}
	//	// 获取类上面的Annotation，可能包含组合注解，故采用spring的工具类
	//	Class<?> beanType = handlerMethod.getBeanType();
	//	return AnnotatedElementUtils.findMergedAnnotation(beanType, annotationType);
	//}
	//
	///**
	// * 判断是否有注解 Annotation
	// *
	// * @param method         Method
	// * @param annotationType 注解类
	// * @param <A>            泛型标记
	// * @return {boolean}
	// */
	//public static <A extends Annotation> boolean isAnnotated(Method method,
	//	Class<A> annotationType) {
	//	// 先找方法，再找方法上的类
	//	boolean isMethodAnnotated = AnnotatedElementUtils.isAnnotated(method, annotationType);
	//	if (isMethodAnnotated) {
	//		return true;
	//	}
	//	// 获取类上面的Annotation，可能包含组合注解，故采用spring的工具类
	//	Class<?> targetClass = method.getDeclaringClass();
	//	return AnnotatedElementUtils.isAnnotated(targetClass, annotationType);
	//}

}
