package com.taotao.cloud.encrypt.annotation;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * 验证签名注解
 *
 * @author shuigedeng
 * @version 2022.07
 * @since 2022-07-06 15:08:45
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SignEncrypt {

	/**
	 * 超时
	 *
	 * @return long
	 * @since 2022-07-06 15:08:45
	 */
	long timeout() default 60000L;

	/**
	 * 时间单位
	 *
	 * @return {@link TimeUnit }
	 * @since 2022-07-06 15:08:46
	 */
	TimeUnit timeUnit() default TimeUnit.MILLISECONDS;

}
