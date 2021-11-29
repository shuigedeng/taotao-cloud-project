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

import com.taotao.cloud.common.utils.JsonUtil;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import org.aspectj.lang.JoinPoint;

/**
 * 切面基础类 封装一些基础方法
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 19:41:13
 */
public class AspectUtil {

	/**
	 * 获取切面方法上包含的指定注解
	 *
	 * @param joinPoint       joinPoint
	 * @param annotationClass annotationClass
	 * @param <T>             T
	 * @return T
	 * @author shuigedeng
	 * @since 2021-09-02 19:41:20
	 */
	public static <T extends Annotation> T getAnnotation(JoinPoint joinPoint, Class<T> annotationClass) {
		String methodName = joinPoint.getSignature().getName();
		Object[] arguments = joinPoint.getArgs();
		Method[] methods = joinPoint.getSignature().getDeclaringType().getMethods();
		for (Method m : methods) {
			if (m.getName().equals(methodName)) {
				if (m.getParameterTypes().length == arguments.length) {
					return m.getAnnotation(annotationClass);
				}
			}
		}
		return null;
	}
}
