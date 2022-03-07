package com.taotao.cloud.core.heaven.annotation;

import java.lang.annotation.*;

/**
 * 用来标识每个需要加锁的状态变量以及保护该变量的锁。
 * 有助于代码的维护与审查，以及一些自动化的分析工具找到潜在的安全性错误。
 *
 * @author bbhou
 * @version 0.0.7
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD})
@Inherited
public @interface GuardeBy {

    /**
     * 指出变量由哪些锁保护。
     *
     * @return 对应的锁信息
     */
    String value() default "";

}
