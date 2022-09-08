package com.taotao.cloud.data.mybatisplus.datascope.perm;

import java.lang.annotation.*;

/**
 * 数据权限控制注解
 * @see NestedPermission
 * @author xxm
 * @date 2021/12/22
 */
@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Permission {

    /**
     * 数据范围权限
     */
    boolean dataScope() default true;

    /**
     * 查询字段权限
     */
    boolean selectField() default true;

}
