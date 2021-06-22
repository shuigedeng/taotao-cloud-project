package com.taotao.cloud.common.annotation.log;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 操作日志注解
 *
 * @author zuihou
 * @date 2019/2/1
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SysLog {
    /**
     * 是否启用 操作日志
     *
     * @return 是否启用
     */
    boolean enabled() default true;

    /**
     * 描述
     *
     * @return {String}
     */
    String value() default "";

    /**
     * 是否拼接Controller类上的描述值
     *
     * @return 是否拼接Controller类上的描述值
     */
    boolean controllerApiValue() default true;

    /**
     * 记录执行参数
     *
     * @return 是否记录执行参数
     */
    boolean request() default true;

    /**
     * 当 request = false时， 方法报错记录请求参数
     *
     * @return 当 request = false时， 方法报错记录请求参数
     */
    boolean requestByError() default true;

    /**
     * 记录返回参数
     *
     * @return 是否记录返回参数
     */
    boolean response() default true;
}
