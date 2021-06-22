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
package com.taotao.cloud.common.aspect;

import com.taotao.cloud.common.utils.JsonUtil;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import org.aspectj.lang.JoinPoint;


/**
 * 切面基础类
 * <p>
 * 封装一些基础方法
 *
 * @author dengtao
 * @version 1.0.0
 * @since 2021/6/22 17:06
 */
public abstract class BaseAspect {

	/**
	 * 获取切面方法上包含的指定注解
	 *
	 * @param joinPoint
	 * @param annotationClass
	 * @param <T>
	 */
	public <T extends Annotation> T getAnnotation(JoinPoint joinPoint, Class<T> annotationClass) {
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

	/**
	 * 默认key策略
	 *
	 * @param targetName
	 * @param methodName
	 * @param arguments
	 * @return
	 */
	public String getCacheKey(String key, String targetName, String methodName,
		Object[] arguments) {
		StringBuilder sb = new StringBuilder();
		if (key != null && key.length() > 0) {
			sb.append(key);
		} else {
			sb.append(targetName).append(".").append(methodName);
		}
		if (arguments != null && (arguments.length != 0)) {
			sb.append("#").append(JsonUtil.toJSONString(arguments));
		}
		return sb.toString().replace("[", "").replace("\"", "").replace("]", "")
			.replace("com.gofun.", "");
	}

	/**
	 * 获取key 根据condition
	 *
	 * @param key
	 * @param condition
	 * @param arguments
	 * @return
	 */
	public String getCacheKey(String key, String condition, Object[] arguments) {
		StringBuilder sb = new StringBuilder();
		sb.append(key);
		String argJson = JsonUtil.toJSONString(arguments);
		String[] params = null;
		if (condition != null && condition.trim().startsWith("#")) {
			condition = condition.trim();
			params = condition.split(",");
			for (String param : params) {
				param = param.replace("#", "");
//				JSONObject val = (JSONObject) JSONPath.read(condition, param);
			}
		}
		return sb.toString();
	}
}
