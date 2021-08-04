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
 */
public class SpringUtil implements ApplicationContextAware, DisposableBean {

	private static ApplicationContext applicationContext = null;
	private static final List<CallBack> CALL_BACKS = new ArrayList<>();
	private static boolean addCallback = true;

	/**
	 * 针对 某些初始化方法，在SpringContextHolder 未初始化时 提交回调方法。 在SpringContextHolder 初始化后，进行回调使用
	 *
	 * @param callBack 回调函数
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
	 */
	public static <T> T getBean(String name) {
		assertContextInjected();
		return (T) applicationContext.getBean(name);
	}

	/**
	 * 从静态变量applicationContext中取得Bean, 自动转型为所赋值对象的类型.
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
	 * @return /
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
	 * @return /
	 */
	public static String getProperties(String property) {
		return getProperties(property, null, String.class);
	}

	/**
	 * 获取SpringBoot 配置信息
	 *
	 * @param property     属性key
	 * @param requiredType 返回类型
	 * @return /
	 */
	public static <T> T getProperties(String property, Class<T> requiredType) {
		return getProperties(property, null, requiredType);
	}

	/**
	 * 检查ApplicationContext不为空.
	 */
	private static void assertContextInjected() {
		if (applicationContext == null) {
			throw new IllegalStateException("applicationContext属性未注入, 请在applicationContext" +
				".xml中定义SpringContextHolder或在SpringBoot启动类中注册SpringContextHolder.");
		}
	}

	/**
	 * 清除SpringContextHolder中的ApplicationContext为Null.
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
	 */
	public static ApplicationContext getApplicationContext() {
		return SpringUtil.applicationContext;
	}

	/**
	 * 回调方法
	 *
	 * @author shuigedeng 针对某些初始化方法，在SpringUtil 初始化前时，<br> 可提交一个 提交回调任务。<br> 在SpringUtil 初始化后，进行回调使用
	 */
	public static interface CallBack {

		/**
		 * 回调执行方法
		 */
		void executor();

		/**
		 * 本回调任务名称
		 *
		 * @return /
		 */
		default String getCallBackName() {
			return Thread.currentThread().getId() + ":" + this.getClass().getName();
		}
	}
}
