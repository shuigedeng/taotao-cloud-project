package com.taotao.cloud.rpc.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 客户端服务配置 用于服务代理的注解，只能对成员变量使用
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Reference {

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

	/**
	 * 重试次数，服务端未能在超时时间内 响应，允许触发超时的次数
	 *
	 * @return
	 */
	public int retries() default 2;

	/**
	 * 超时时间，即 客户端最长允许等待 服务端时长，超时即触发重试机制
	 *
	 * @return
	 */
	public long timeout() default 3000;

	/**
	 * 异步时间，即等待服务端异步响应的时间 只在超时重试机制使用，非超时重试情况下默认使用 阻塞等待方式（asyncTime 字段 缺省 或者 <= 0 将启用） 使用前须知：
	 * ${asyncTime} > ${timeout}
	 *
	 * @return
	 */
	public long asyncTime() default 0;

}
