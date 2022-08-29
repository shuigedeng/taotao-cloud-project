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
package com.taotao.cloud.common.utils.aop;

import com.taotao.cloud.common.utils.log.LogUtils;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import org.aspectj.lang.JoinPoint;
import org.springframework.aop.framework.AdvisedSupport;
import org.springframework.aop.framework.AopProxy;

import java.lang.reflect.Field;

/**
 * 获取代理原始对象的工具
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:05:43
 */
public class AopUtils extends org.springframework.aop.support.AopUtils {

	/**
	 * 获取代理对象的原始对象
	 *
	 * @param proxy 代理
	 * @return {@link Object }
	 * @since 2022-04-27 17:05:43
	 */
	public static Object getTarget(Object proxy) {
        // 不是代理对象，直接返回参数对象
        if (!isAopProxy(proxy)) {
            return proxy;
        }
        // 判断是否是jdk还是cglib代理的对象
        try {
            if (isJdkDynamicProxy(proxy)) {
                return getJdkDynamicProxyTargetObject(proxy);
            } else {
                return getCglibProxyTargetObject(proxy);
            }
        } catch (Exception e) {
            LogUtils.error("获取代理对象异常", e);
            return null;
        }
    }

	/**
	 * 获取cglib代理的对象
	 *
	 * @param proxy 代理
	 * @return {@link Object }
	 * @since 2022-04-27 17:05:44
	 */
	private static Object getCglibProxyTargetObject(Object proxy) throws Exception {
        Field h = proxy.getClass().getDeclaredField("CGLIB$CALLBACK_0");
        h.setAccessible(true);
        Object dynamicAdvisedInterceptor = h.get(proxy);
        Field advised = dynamicAdvisedInterceptor.getClass().getDeclaredField("advised");
        advised.setAccessible(true);
        return ((AdvisedSupport) advised.get(dynamicAdvisedInterceptor)).getTargetSource().getTarget();
    }

	/**
	 * 获取jdk代理的对象
	 *
	 * @param proxy 代理
	 * @return {@link Object }
	 * @since 2022-04-27 17:05:44
	 */
	private static Object getJdkDynamicProxyTargetObject(Object proxy) throws Exception {
        Field h = proxy.getClass().getSuperclass().getDeclaredField("h");
        h.setAccessible(true);
        AopProxy aopProxy = (AopProxy) h.get(proxy);
        Field advised = aopProxy.getClass().getDeclaredField("advised");
        advised.setAccessible(true);
        return ((AdvisedSupport) advised.get(aopProxy)).getTargetSource().getTarget();
    }

	/**
	 * 获取切面方法上包含的指定注解
	 *
	 * @param joinPoint       joinPoint
	 * @param annotationClass 注解类
	 * @return 注解类型
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
