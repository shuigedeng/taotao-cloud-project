package com.taotao.cloud.schedule.common.utils.proxy;

import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;

/**
 * ProxyUtils
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2022-03-25 15:10:42
 */
public class ProxyUtils {

	/**
	 * getInstance
	 *
	 * @param clazz       clazz
	 * @param interceptor interceptor
	 * @return 对象
	 * @since 2022-03-25 15:10:39
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getInstance(Class<T> clazz, MethodInterceptor interceptor) {
		//System.setProperty(DebuggingClassWriter.DEBUG_LOCATION_PROPERTY, "C:\\tmp\\file");
		//字节码加强器：用来创建动态代理类
		Enhancer enhancer = new Enhancer();
		//代理的目标对象
		enhancer.setSuperclass(clazz);
		//回调类，在代理类方法调用时会回调Callback类的intercept方法
		enhancer.setCallback(interceptor);
		//创建代理类
		Object result = enhancer.create();
		return (T) result;
	}
}
