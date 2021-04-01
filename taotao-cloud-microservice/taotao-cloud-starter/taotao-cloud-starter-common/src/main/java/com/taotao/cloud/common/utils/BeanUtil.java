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

import cn.hutool.core.bean.copier.CopyOptions;
import com.taotao.cloud.common.exception.BaseException;
import java.util.Arrays;
import lombok.experimental.UtilityClass;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * BeanUtil
 *
 * @author dengtao
 * @version 1.0.0
 * @since 2019/9/8
 */
@UtilityClass
public class BeanUtil {

	/**
	 * 获取bean
	 *
	 * @param type     类型
	 * @param required 是否必须
	 * @return T
	 * @author dengtao
	 * @since 2020/10/15 14:54
	 */
	public <T> T getBean(Class<T> type, boolean required) {
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

	/**
	 * 获取bean
	 *
	 * @param type     类型
	 * @param required 是否必须
	 * @return java.lang.Object
	 * @author dengtao
	 * @since 2020/10/15 14:55
	 */
	public Object getBean(String type, boolean required) {
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
	 * @return java.lang.String
	 * @author dengtao
	 * @since 2020/10/15 14:55
	 */
	public String getBeanDefinitionText() {
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
	 * 注册bean
	 *
	 * @param name  name
	 * @param clazz clazz
	 * @param args  args
	 * @author dengtao
	 * @since 2020/10/15 14:43
	 */
	public void registerBean(String name,
		Class clazz,
		Object... args) {
		ConfigurableApplicationContext applicationContext = ContextUtil.getApplicationContext();
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
	 * @author dengtao
	 * @since 2020/10/15 14:44
	 */
	public void registerBean(String name,
		Class clazz,
		BeanDefinitionBuilder beanDefinitionBuilder) {
		ConfigurableApplicationContext applicationContext = ContextUtil.getApplicationContext();
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
	 * @author dengtao
	 * @since 2020/10/15 14:44
	 */
	public void unRegisterBean(String name) {
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
	 * @author dengtao
	 * @since 2020/10/15 14:45
	 */
	public void checkRegisterBean(ApplicationContext applicationContext, String name, Class clazz) {
		if (applicationContext.containsBean(name)) {
			Object bean = applicationContext.getBean(name);
			if (!bean.getClass().isAssignableFrom(clazz)) {
				throw new BaseException("BeanName 重复注册" + name);
			}
		}
	}

	/**
	 * 复制Bean对象属性<br>
	 *
	 * @param source 源Bean对象
	 * @param target 目标Bean对象
	 */
	public void copyIgnoredNull(Object source, Object target) {
		cn.hutool.core.bean.BeanUtil
			.copyProperties(source, target, CopyOptions.create().ignoreNullValue().ignoreError());
	}

	/**
	 * 复制Bean对象属性<br>
	 *
	 * @param source 源Bean对象
	 * @param target 目标Bean对象
	 */
	public void copyIncludeNull(Object source, Object target) {
		cn.hutool.core.bean.BeanUtil
			.copyProperties(source, target, CopyOptions.create().ignoreError());
	}
}
