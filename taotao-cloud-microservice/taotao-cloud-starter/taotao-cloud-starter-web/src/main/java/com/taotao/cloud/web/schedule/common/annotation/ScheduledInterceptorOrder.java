package com.taotao.cloud.web.schedule.common.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ScheduledInterceptorOrder
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-02-09 16:02:48
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD})
@Documented
public @interface ScheduledInterceptorOrder {
    int value() default 0;
}
