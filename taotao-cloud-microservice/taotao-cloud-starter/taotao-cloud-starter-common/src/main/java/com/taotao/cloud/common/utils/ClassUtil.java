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

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import org.springframework.core.BridgeMethodResolver;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.lang.Nullable;
import org.springframework.util.ClassUtils;

/**
 * 类操作工具类
 *
 * @author shuigedeng
 */
public class ClassUtil extends ClassUtils {

	/**
	 * 获取Annotation
	 *
	 * @param method         Method
	 * @param annotationType 注解类
	 * @param <A>            泛型标记
	 * @return {Annotation}
	 */
	@Nullable
	public static <A extends Annotation> A getAnnotation(Method method, Class<A> annotationType) {
		Class<?> targetClass = method.getDeclaringClass();
		// The method may be on an interface, but we need attributes from the target class.
		// If the target class is null, the method will be unchanged.
		Method specificMethod = ClassUtil.getMostSpecificMethod(method, targetClass);
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
	 * @param <A>            泛型标记
	 * @return {boolean}
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

}
