package com.taotao.cloud.rpc.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 服务端服务配置
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Service {

	/**
	 * 服务名
	 *
	 * @return
	 */
	public String name() default "";

	/**
	 * 服务分组
	 *
	 * @return
	 */
	public String group() default "";
}
