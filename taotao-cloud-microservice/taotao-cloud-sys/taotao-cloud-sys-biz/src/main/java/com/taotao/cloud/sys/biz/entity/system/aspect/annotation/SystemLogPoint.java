package com.taotao.cloud.sys.biz.entity.system.aspect.annotation;

import java.lang.annotation.*;

/**
 * 系统日志AOP注解
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface SystemLogPoint {

    /**
     * 日志名称
     *
     * @return
     */
    String description() default "";

    /**
     * 自定义日志内容
     *
     * @return
     */
    String customerLog() default "";
}
