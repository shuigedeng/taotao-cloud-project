package com.taotao.cloud.web.version;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * api版本
 *
 * @author shuigedeng
 * @version 2022.07
 * @since 2022-07-18 10:21:19
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ApiInfo {

	/**
	 * 创建版本号
	 */
	Create create();

	/**
	 * 更新信息
	 */
	Update[] update() default {};

	@Target({ElementType.METHOD, ElementType.TYPE})
	@Retention(RetentionPolicy.RUNTIME)
	@Documented
	public @interface Create {

		/**
		 * 创建版本号
		 */
		VersionEnum version();

		/**
		 * 创建时间
		 */
		String date();

		/**
		 * 创建内容
		 */
		String content() default "";

		/**
		 * 创建者
		 */
		String createor() default "shuigedeng";
	}

	@Target({ElementType.METHOD, ElementType.TYPE})
	@Retention(RetentionPolicy.RUNTIME)
	@Documented
	public @interface Update {

		/**
		 * 更新版本号
		 */
		VersionEnum version();

		/**
		 * 更新时间
		 */
		String date();

		/**
		 * 更新内容
		 */
		String content();

		/**
		 * 更新者
		 */
		String updator() default "shuigedeng";
	}

}
