package com.taotao.cloud.health.base;

import java.lang.annotation.*;

/**
 * @author: chejiangyi
 * @version: 2019-07-28 09:36
 **/
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FieldReport {
    /**
     * 唯一名称
     * @return
     */
    String name() default "";

    /**
     * 描述
     * @return
     */
    String desc() default "";
}
