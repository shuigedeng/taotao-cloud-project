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
package com.taotao.cloud.web.util;

import com.taotao.cloud.common.utils.LogUtil;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.env.Environment;

/**
 * Spring工具类
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2021/8/24 22:52
 */
public class SpringUtil implements ApplicationContextAware, DisposableBean {

	private static ApplicationContext applicationContext = null;
	private static final List<CallBack> CALL_BACKS = new ArrayList<>();
	private static boolean addCallback = true;

	/**
	 * <p>
	 * 针对 某些初始化方法，在SpringContextHolder 未初始化时 提交回调方法。 在SpringContextHolder 初始化后，进行回调使用
	 * </p>
	 *
	 * @param callBack 回调函数
	 * @author shuigedeng
	 * @since 2021/8/24 22:53
	 */
	public synchronized static void addCallBacks(CallBack callBack) {
		if (addCallback) {
			SpringUtil.CALL_BACKS.add(callBack);
		} else {
			LogUtil.warn("CallBack：{} 已无法添加！立即执行", callBack.getCallBackName());
			callBack.executor();
		}
	}

	/**
	 * 从静态变量applicationContext中取得Bean, 自动转型为所赋值对象的类型.
	 *
	 * @param name name
	 * @return T
	 * @author shuigedeng
	 * @since 2021/8/24 22:53
	 */
	public static <T> T getBean(String name) {
		assertContextInjected();
		return (T) applicationContext.getBean(name);
	}

	/**
	 * 从静态变量applicationContext中取得Bean, 自动转型为所赋值对象的类型.
	 *
	 * @param requiredType requiredType
	 * @return T
	 * @author shuigedeng
	 * @since 2021/8/24 22:54
	 */
	public static <T> T getBean(Class<T> requiredType) {
		assertContextInjected();
		return applicationContext.getBean(requiredType);
	}

	/**
	 * 获取SpringBoot 配置信息
	 *
	 * @param property     属性key
	 * @param defaultValue 默认值
	 * @param requiredType 返回类型
	 * @return T
	 * @author shuigedeng
	 * @since 2021/8/24 22:54
	 */
	public static <T> T getProperties(String property, T defaultValue, Class<T> requiredType) {
		T result = defaultValue;
		try {
			result = getBean(Environment.class).getProperty(property, requiredType);
		} catch (Exception ignored) {
		}
		return result;
	}

	/**
	 * 获取SpringBoot 配置信息
	 *
	 * @param property 属性key
	 * @return java.lang.String
	 * @author shuigedeng
	 * @since 2021/8/24 22:54
	 */
	public static String getProperties(String property) {
		return getProperties(property, null, String.class);
	}

	/**
	 * 获取SpringBoot 配置信息
	 *
	 * @param property     属性key
	 * @param requiredType 返回类型
	 * @return T
	 * @author shuigedeng
	 * @since 2021/8/24 22:55
	 */
	public static <T> T getProperties(String property, Class<T> requiredType) {
		return getProperties(property, null, requiredType);
	}

	/**
	 * 检查ApplicationContext不为空.
	 *
	 * @author shuigedeng
	 * @since 2021/8/24 22:55
	 */
	private static void assertContextInjected() {
		if (applicationContext == null) {
			throw new IllegalStateException("applicationContext属性未注入, 请在applicationContext" +
				".xml中定义SpringContextHolder或在SpringBoot启动类中注册SpringContextHolder.");
		}
	}

	/**
	 * 清除SpringContextHolder中的ApplicationContext为Null.
	 *
	 * @author shuigedeng
	 * @since 2021/8/24 22:55
	 */
	private static void clearHolder() {
		LogUtil.debug("清除SpringContextHolder中的ApplicationContext:"
			+ applicationContext);
		applicationContext = null;
	}

	@Override
	public void destroy() {
		SpringUtil.clearHolder();
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		if (SpringUtil.applicationContext != null) {
			LogUtil.warn("SpringContextHolder中的ApplicationContext被覆盖, 原有ApplicationContext为:"
				+ SpringUtil.applicationContext);
		}
		SpringUtil.applicationContext = applicationContext;
		if (addCallback) {
			for (CallBack callBack : SpringUtil.CALL_BACKS) {
				callBack.executor();
			}
			CALL_BACKS.clear();
		}
		SpringUtil.addCallback = false;
	}

	/**
	 * 获取ApplicationContext
	 *
	 * @return org.springframework.context.ApplicationContext
	 * @author shuigedeng
	 * @since 2021/8/24 22:55
	 */
	public static ApplicationContext getApplicationContext() {
		return SpringUtil.applicationContext;
	}

	/**
	 * 回调方法 针对某些初始化方法，在SpringUtil 初始化前时，<br> 可提交一个 提交回调任务。<br> 在SpringUtil 初始化后，进行回调使用
	 *
	 * @author shuigedeng
	 * @version 1.0.0
	 * @since 2021/8/24 22:56
	 */
	public interface CallBack {

		/**
		 * 回调执行方法
		 *
		 * @author shuigedeng
		 * @since 2021/8/24 22:56
		 */
		void executor();

		/**
		 * 本回调任务名称
		 *
		 * @return java.lang.String
		 * @author shuigedeng
		 * @since 2021/8/24 22:56
		 */
		default String getCallBackName() {
			return Thread.currentThread().getId() + ":" + this.getClass().getName();
		}
	}
}
