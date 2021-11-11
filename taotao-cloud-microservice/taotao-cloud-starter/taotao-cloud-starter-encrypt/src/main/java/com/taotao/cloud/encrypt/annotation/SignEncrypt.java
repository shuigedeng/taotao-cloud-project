package com.taotao.cloud.encrypt.annotation;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * 验证签名注解
 *
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SignEncrypt {

	long timeout() default 60000L;

	TimeUnit timeUnit() default TimeUnit.MILLISECONDS;

}
