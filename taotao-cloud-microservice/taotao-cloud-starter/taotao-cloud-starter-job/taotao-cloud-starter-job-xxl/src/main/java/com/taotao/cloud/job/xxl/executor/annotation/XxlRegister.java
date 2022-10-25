package com.taotao.cloud.job.xxl.executor.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * xxl注册
 *
 * @author shuigedeng
 * @version 2022.09
 * @since 2022-10-25 09:43:41
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface XxlRegister {

	/**
	 * cron
	 *
	 * @return {@link String }
	 * @since 2022-10-25 09:43:41
	 */
	String cron();

	/**
	 * 工作desc
	 *
	 * @return {@link String }
	 * @since 2022-10-25 09:43:41
	 */
	String jobDesc() default "default jobDesc";

	/**
	 * 作者
	 *
	 * @return {@link String }
	 * @since 2022-10-25 09:43:41
	 */
	String author() default "default Author";

	/**
	 * 遗嘱执行人路由策略
	 *
	 * @return {@link String }
	 * @since 2022-10-25 09:43:41
	 *//*
	 * 默认为 ROUND 轮询方式
	 * 可选： FIRST 第一个
	 * */
	String executorRouteStrategy() default "ROUND";

	/**
	 * 触发状态
	 *
	 * @return int
	 * @since 2022-10-25 09:43:41
	 */
	int triggerStatus() default 0;
}
