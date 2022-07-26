package com.taotao.cloud.open.doc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 开放api文档注解，注释在类、方法、方法参数以及对象属性中
 *
 * @author shuigedeng
 * @version 2022.07
 * @since 2022-07-26 10:11:10
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.TYPE, ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
public @interface OpenApiDoc {

    /**
     * 中文名,默认为空
     *
     * @return 中文名
     */
    String cnName() default "";

    /**
     * 描述,默认为空
     *
     * @return 描述
     */
    String describe() default "";

    /**
     * 是否取消生成文档信息,仅针对类、方法、对象属性（true:取消生成，false:生成，默认生成）
     *
     * @return 是否忽略
     */
    boolean ignore() default false;

    /**
     * 返回值中文名，仅用于方法上
     *
     * @return 返回值中文名
     */
    String retCnName() default "";

    /**
     * 返回值描述，仅用于方法上
     *
     * @return 返回值描述
     */
    String retDescribe() default "";
}
