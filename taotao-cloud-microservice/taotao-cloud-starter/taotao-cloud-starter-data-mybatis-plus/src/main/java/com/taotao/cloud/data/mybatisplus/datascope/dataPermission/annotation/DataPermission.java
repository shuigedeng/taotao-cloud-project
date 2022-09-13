package com.taotao.cloud.data.mybatisplus.datascope.dataPermission.annotation;

import com.taotao.cloud.data.mybatisplus.datascope.dataPermission.rule.DataPermissionRule;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 数据权限注解 可声明在类或者方法上，标识使用的数据权限规则
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataPermission {

	/**
	 * 是否开启数据权限 即使不添加注解 默认是开启状态 可通过设置 enable 为 false 禁用
	 */
	boolean enable() default true;

	/**
	 * 生效的数据权限规则数组，优先级高于 {@link #excludeRules()}
	 */
	Class<? extends DataPermissionRule>[] includeRules() default {};

	/**
	 * 排除的数据权限规则数组，优先级最低
	 */
	Class<? extends DataPermissionRule>[] excludeRules() default {};

}
