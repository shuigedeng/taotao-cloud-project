package com.taotao.cloud.log.biz.shortlink.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value = ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestNotRepeat {

    /**
     * 时间间隔：毫秒 - 默认1000ms内的请求
     */
    String timeInterval() default "1000";

    /**
     * 属性，表示将获取哪个属性的值
     */
    String[] excludeField() default "";

}
