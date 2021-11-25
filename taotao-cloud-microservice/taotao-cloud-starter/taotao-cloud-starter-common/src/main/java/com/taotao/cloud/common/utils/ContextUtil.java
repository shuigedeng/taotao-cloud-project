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

import com.taotao.cloud.common.exception.BaseException;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.web.context.ConfigurableWebServerApplicationContext;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * 上下文工具类
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 17:37:14
 */
public class ContextUtil {

	private ContextUtil() {
	}

	/**
	 * mainClass
	 */
	public static Class<?> mainClass;
	/**
	 * applicationContext
	 */
	public static ConfigurableApplicationContext applicationContext;

	/**
	 * setApplicationContext
	 *
	 * @param applicationContext applicationContext
	 * @author shuigedeng
	 * @since 2021-09-02 17:37:28
	 */
	public static void setApplicationContext(ConfigurableApplicationContext applicationContext) {
		ContextUtil.applicationContext = applicationContext;
	}

	/**
	 * getApplicationContext
	 *
	 * @return {@link ConfigurableApplicationContext }
	 * @author shuigedeng
	 * @since 2021-09-02 17:37:32
	 */
	public static ConfigurableApplicationContext getApplicationContext() {
		return applicationContext;
	}

	/**
	 * isWeb
	 *
	 * @return boolean
	 * @author shuigedeng
	 * @since 2021-09-02 17:37:36
	 */
	public static boolean isWeb() {
		return getConfigurableWebServerApplicationContext() != null;
	}

	/**
	 * getConfigurableWebServerApplicationContext
	 *
	 * @return {@link ConfigurableWebServerApplicationContext }
	 * @author shuigedeng
	 * @since 2021-09-02 17:37:38
	 */
	public static ConfigurableWebServerApplicationContext getConfigurableWebServerApplicationContext() {
		ApplicationContext context = getApplicationContext();
		if (context instanceof ConfigurableWebServerApplicationContext) {
			return (ConfigurableWebServerApplicationContext) context;
		}
		return null;
	}

	/**
	 * 获取bean
	 *
	 * @param type     类型
	 * @param required 是否必须
	 * @return T
	 * @author shuigedeng
	 * @since 2021-09-02 17:37:46
	 */
	public static <T> T getBean(Class<T> type, boolean required) {
		ConfigurableApplicationContext applicationContext = ContextUtil.getApplicationContext();
		if (type != null && applicationContext != null) {
			try {
				if (required) {
					return applicationContext.getBean(type);
				} else {
					if (applicationContext.getBeansOfType(type).size() > 0) {
						return applicationContext.getBean(type);
					}
				}
			} catch (NoSuchBeanDefinitionException e) {
				return null;
			}
		}
		return null;
	}

	public static <T> T getBean(Class<T> type, String name, boolean required) {
		if (type != null && applicationContext != null) {
			if (required) {
				return applicationContext.getBean(name, type);
			} else {
				if (applicationContext.getBeansOfType(type).size() > 0) {
					return applicationContext.getBean(name, type);
				}

			}
		}
		return null;
	}

	/**
	 * 获取bean
	 *
	 * @param type     类型
	 * @param required 是否必须
	 * @return {@link Object }
	 * @author shuigedeng
	 * @since 2021-09-02 17:37:57
	 */
	public static Object getBean(String type, boolean required) {
		ConfigurableApplicationContext applicationContext = ContextUtil.getApplicationContext();
		if (type != null && applicationContext != null) {
			if (required) {
				return applicationContext.getBean(type);
			} else {
				if (applicationContext.containsBean(type)) {
					return applicationContext.getBean(type);
				}
			}
		}
		return null;
	}

	/**
	 * 获取bean定义信息
	 *
	 * @return {@link java.lang.String }
	 * @author shuigedeng
	 * @since 2021-09-02 17:38:08
	 */
	public static String getBeanDefinitionText() {
		ConfigurableApplicationContext applicationContext = ContextUtil.getApplicationContext();
		String[] beans = applicationContext.getBeanDefinitionNames();
		Arrays.sort(beans);
		StringBuilder sb = new StringBuilder();
		for (String bean : beans) {
			sb.append(bean).append(" -> ")
				.append(ContextUtil.getApplicationContext().getBean(bean).getClass());
		}
		return sb.toString();
	}

	/**
	 * 获取所有被注解的
	 *
	 * @param anno anno
	 * @return {@link java.util.Map }
	 * @author shuigedeng
	 * @since 2021-09-02 17:38:14
	 */
	public static Map<String, Object> getBeansWithAnnotation(Class<? extends Annotation> anno) {
		Map<String, Object> map;
		try {
			//获取注解的 bean
			map = applicationContext.getBeansWithAnnotation(anno);
		} catch (Exception e) {
			map = null;
		}
		return map;
	}

	/**
	 * 获取 bean 的类型
	 *
	 * @param clazz clazz
	 * @return {@link java.util.List }
	 * @author shuigedeng
	 * @since 2021-09-02 17:38:22
	 */
	public static <T> List<T> getBeansOfType(Class<T> clazz) {
		//声明一个结果
		Map<String, T> map;
		try {
			//获取类型
			map = applicationContext.getBeansOfType(clazz);
		} catch (Exception e) {
			map = null;
		}
		//返回 bean 的类型
		return map == null ? null : new ArrayList<>(map.values());
	}

	/**
	 * 注册bean
	 *
	 * @param name  name
	 * @param clazz clazz
	 * @param args  args
	 * @author shuigedeng
	 * @since 2021-09-02 17:38:27
	 */
	public static void registerBean(String name, Class clazz, Object... args) {
		ConfigurableApplicationContext applicationContext = getApplicationContext();
		checkRegisterBean(applicationContext, name, clazz);
		BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder
			.genericBeanDefinition(clazz);
		for (Object arg : args) {
			beanDefinitionBuilder.addConstructorArgValue(arg);
		}
		BeanDefinition beanDefinition = beanDefinitionBuilder.getRawBeanDefinition();
		BeanDefinitionRegistry beanFactory = (BeanDefinitionRegistry) applicationContext
			.getBeanFactory();
		beanFactory.registerBeanDefinition(name, beanDefinition);
	}

	/**
	 * 注册bean
	 *
	 * @param name                  name
	 * @param clazz                 clazz
	 * @param beanDefinitionBuilder beanDefinitionBuilder
	 * @author shuigedeng
	 * @since 2021-09-02 17:38:33
	 */
	public static void registerBean(String name, Class clazz,
		BeanDefinitionBuilder beanDefinitionBuilder) {
		ConfigurableApplicationContext applicationContext = getApplicationContext();
		checkRegisterBean(applicationContext, name, clazz);
		BeanDefinition beanDefinition = beanDefinitionBuilder.getRawBeanDefinition();
		BeanDefinitionRegistry beanFactory = (BeanDefinitionRegistry) applicationContext
			.getBeanFactory();
		beanFactory.registerBeanDefinition(name, beanDefinition);
	}

	/**
	 * 取消注册bean
	 *
	 * @param name name
	 * @author shuigedeng
	 * @since 2021-09-02 17:38:37
	 */
	public static void unRegisterBean(String name) {
		ConfigurableApplicationContext applicationContext = ContextUtil.getApplicationContext();
		BeanDefinitionRegistry beanFactory = (BeanDefinitionRegistry) applicationContext
			.getBeanFactory();
		beanFactory.removeBeanDefinition(name);

	}

	/**
	 * 检查已注册的bean
	 *
	 * @param applicationContext applicationContext
	 * @param name               name
	 * @param clazz              clazz
	 * @author shuigedeng
	 * @since 2021-09-02 17:38:43
	 */
	public static void checkRegisterBean(ApplicationContext applicationContext, String name,
		Class clazz) {
		if (applicationContext.containsBean(name)) {
			Object bean = applicationContext.getBean(name);
			if (!bean.getClass().isAssignableFrom(clazz)) {
				throw new BaseException("BeanName 重复注册" + name);
			}
		}
	}

}
