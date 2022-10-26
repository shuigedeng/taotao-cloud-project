package com.taotao.cloud.limit.ratelimiter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 速率限制关键
 *
 * @author shuigedeng
 * @version 2022.09
 * @since 2022-10-26 08:56:31
 */
@Target(value = {ElementType.PARAMETER, ElementType.TYPE})
@Retention(value = RetentionPolicy.RUNTIME)
public @interface RateLimitKey {
	/**
	 * 价值
	 *
	 * @return {@link String }
	 * @since 2022-10-26 08:56:32
	 */
	String value() default "";
}
