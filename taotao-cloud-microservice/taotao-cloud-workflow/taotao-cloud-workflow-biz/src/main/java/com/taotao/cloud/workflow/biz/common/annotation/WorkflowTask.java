package com.taotao.cloud.workflow.biz.common.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 任务调度注解
 *
 */
@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface WorkflowTask {

    /**
     * id
     */
    String id() default "";

    /**
     * 方法名（必填）
     *
     * @return
     */
    String fullName() default "";

    /**
     * 任务说明
     *
     * @return
     */
    String description() default "";

    /**
     * 表达式（必填）
     *
     * @return
     */
    String cron() default "";

    /**
     * 开始时间
     *
     * @return
     */
    String startDate() default "";

    /**
     * 结束时间
     *
     * @return
     */
    String endDate() default "";

}
