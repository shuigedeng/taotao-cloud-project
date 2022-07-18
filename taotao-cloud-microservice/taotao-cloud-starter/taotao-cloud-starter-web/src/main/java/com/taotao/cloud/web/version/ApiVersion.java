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
public @interface ApiVersion {

	/**
	 * 创建版本号
	 */
	VersionEnum createVersion();

	/**
	 * 创建时间
	 */
	String createDate();

	/**
	 * 创建者
	 */
	String createor() default "shuigedeng";

	/**
	 * 更新信息
	 */
	UpdateInfo[] updateInfo() default {};


	@Target({ElementType.METHOD, ElementType.TYPE})
	@Retention(RetentionPolicy.RUNTIME)
	@Documented
	public @interface UpdateInfo {

		/**
		 * 更新版本号
		 */
		VersionEnum updateVersion();

		/**
		 * 更新时间
		 */
		String updateDate();

		/**
		 * 更新内容
		 */
		String updateContent();

		/**
		 * 更新者
		 */
		String updator();
	}

}
