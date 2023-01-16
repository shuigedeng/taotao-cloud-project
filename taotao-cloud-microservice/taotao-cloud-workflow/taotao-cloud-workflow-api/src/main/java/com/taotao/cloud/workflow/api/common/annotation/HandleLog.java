package com.taotao.cloud.workflow.api.common.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 请求日志注解
 *
 */
@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface HandleLog {

    /**
     * 操作模块
     *
     * @return
     */
    String moduleName() default "";

    /**
     * 操作方式
     *
     * @return
     */
    String requestMethod() default "";

}
