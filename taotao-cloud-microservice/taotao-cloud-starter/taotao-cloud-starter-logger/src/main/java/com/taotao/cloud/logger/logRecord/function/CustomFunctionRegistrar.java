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
package com.taotao.cloud.logger.logRecord.function;

import com.taotao.cloud.common.utils.log.LogUtil;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

/**
 * 自定义函数注册
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-26 14:43:17
 */
public class CustomFunctionRegistrar implements ApplicationContextAware {

	/**
	 * 应用程序上下文
	 */
	private ApplicationContext applicationContext;

	/**
	 * 函数图
	 */
	private static Map<String, Method> functionMap = new HashMap<>();

	@Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
        Map<String, Object> beanWithAnnotation = applicationContext.getBeansWithAnnotation(LogRecordFunc.class);
        beanWithAnnotation.values()
            .forEach(component -> {
                    Method[] methods = component.getClass().getMethods();
                    LogRecordFunc classLogRecordFunc = component.getClass().getAnnotation(LogRecordFunc.class);
                    String prefixName = classLogRecordFunc.value();
                    if (StringUtils.hasText(prefixName)) {
                        prefixName += "_";
                    }

                    if (methods.length > 0) {
                        for (Method method : methods) {
                            if (method.isAnnotationPresent(LogRecordFunc.class) && isStaticMethod(method)) {
                                LogRecordFunc logRecordFunc = method.getAnnotation(LogRecordFunc.class);
                                String registerName = StringUtils.hasText(logRecordFunc.value()) ? logRecordFunc.value() : method.getName();
                                functionMap.put(prefixName + registerName, method);
                                LogUtil.info("LogRecord register custom function [{}] as name [{}]", method, prefixName + registerName);
                            }
                        }
                    }
                }
            );
    }

	/**
	 * 注册
	 *
	 * @param context 上下文
	 * @since 2022-04-26 14:43:18
	 */
	public static void register(StandardEvaluationContext context) {
        functionMap.forEach(context::registerFunction);
    }

	/**
	 * 判断是否为静态方法
	 *
	 * @param method 待判断的方法
	 * @return boolean
	 * @since 2022-04-26 14:43:18
	 */
	private static boolean isStaticMethod(Method method) {
        if (method == null) {
            return false;
        }
        int modifiers = method.getModifiers();
        return Modifier.isStatic(modifiers);
    }

	/**
	 * 让应用程序上下文
	 *
	 * @return {@link ApplicationContext }
	 * @since 2022-04-26 14:43:18
	 */
	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	/**
	 * 得到函数图
	 *
	 * @return {@link Map }<{@link String }, {@link Method }>
	 * @since 2022-04-26 14:43:18
	 */
	public static Map<String, Method> getFunctionMap() {
		return functionMap;
	}

	/**
	 * 集函数图
	 *
	 * @param functionMap 函数图
	 * @since 2022-04-26 14:43:18
	 */
	public static void setFunctionMap(Map<String, Method> functionMap) {
		CustomFunctionRegistrar.functionMap = functionMap;
	}
}
