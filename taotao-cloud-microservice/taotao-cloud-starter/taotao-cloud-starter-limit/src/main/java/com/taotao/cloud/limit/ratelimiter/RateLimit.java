package com.taotao.cloud.limit.ratelimiter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 速率限制
 *
 * @author shuigedeng
 * @version 2022.09
 * @since 2022-10-26 08:51:31
 */
@Target(value = {ElementType.METHOD})
@Retention(value = RetentionPolicy.RUNTIME)
public @interface RateLimit {

	/**
	 * 时间窗口流量数量
	 *
	 * @return long
	 * @since 2022-10-26 08:51:31
	 */
	long rate();

	/**
	 * 时间窗口流量数量表达式
	 *
	 * @return {@link String }
	 * @since 2022-10-26 08:51:31
	 */
	String rateExpression() default "";

	/**
	 * 时间窗口，最小单位秒，如 2s，2h , 2d
	 *
	 * @return {@link String }
	 * @since 2022-10-26 08:51:31
	 */
	String rateInterval();

	/**
	 * 获取key
	 *
	 * @return {@link String[] }
	 * @since 2022-10-26 08:51:31
	 */
	String[] keys() default {};

	/**
	 * 自定义业务 key 的 Function
	 *
	 * @return {@link String }
	 * @since 2022-10-26 08:51:31
	 */
	String keyFunction() default "";

	/**
	 * 限流后的自定义回退后的拒绝逻辑
	 *
	 * @return {@link String }
	 * @since 2022-10-26 08:51:31
	 */
	String fallbackFunction() default "";


}
