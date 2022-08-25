package com.taotao.cloud.openapi.client.annotation;


import com.taotao.cloud.openapi.client.config.OpenApiClientConfig;
import com.taotao.cloud.openapi.common.enums.CryModeEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 开放api方法注解，标识一个开放api的方法
 * <p>
 * 注：目前支持的参数类型有基本类型、字符串、数组、普通javabean、List等
 * </p>
 *
 * @author shuigedeng
 * @version 2022.07
 * @since 2022-07-26 10:02:27
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.METHOD})
public @interface OpenApiMethod {


	/**
	 * 开放api方法名称，可以与方法名不同
	 *
	 * @return {@link String }
	 * @since 2022-07-26 10:02:28
	 */
	String value() default "";

	/**
	 * 返回值是否需要解密(true:需要，false:不需要，默认由{@link OpenApiClientConfig}决定)
	 *
	 * @return {@link String }
	 * @since 2022-07-26 10:02:28
	 */
	String retDecrypt() default "";

	/**
	 * 加密模式(默认由{@link OpenApiClientConfig}决定)
	 *
	 * @return {@link CryModeEnum }
	 * @since 2022-07-26 10:02:28
	 */
	CryModeEnum cryModeEnum() default CryModeEnum.UNKNOWN;

	/**
	 * 设置HTTP建立连接超时时间（单位秒），默认由{@link OpenApiClientConfig}决定
	 *
	 * @return int
	 * @since 2022-07-26 10:02:28
	 */
	int httpConnectionTimeout() default -1;

	/**
	 * 设置HTTP数据传输超时时间（单位秒），默认由{@link OpenApiClientConfig}决定
	 *
	 * @return int
	 * @since 2022-07-26 10:02:28
	 */
	int httpReadTimeout() default -1;

	/**
	 * 数据是否启用压缩(true:压缩，false:不压缩，默认由{@link OpenApiClientConfig}决定)
	 *
	 * @return {@link String }
	 * @since 2022-07-26 10:02:28
	 */
	String enableCompress() default "";
}
