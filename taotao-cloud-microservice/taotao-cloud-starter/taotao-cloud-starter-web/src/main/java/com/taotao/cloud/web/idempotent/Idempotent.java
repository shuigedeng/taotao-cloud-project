package com.taotao.cloud.web.idempotent;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author shuigedeng
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Idempotent {

	/**
	 * 关键key key是本次请求中参数的键， 重复请求的key取自header中的rid 用来标识这个请求的唯一性 拦截器中会使用key从请求参数中获取value
	 *
	 * @return String
	 */
	String key() default "";

	/**
	 * 自定义key的前缀用来区分业务
	 */
	String perFix();

	/**
	 * 禁止重复提交的模式 默认是全部使用
	 */
	IdempotentTypeEnum ideTypeEnum() default IdempotentTypeEnum.ALL;
}
