package com.taotao.cloud.open.annotation;


import com.taotao.cloud.open.common.enums.CryModeEnum;
import com.taotao.cloud.open.config.OpenApiConfig;

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
 * @since 2022-07-26 10:08:54
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.METHOD})
public @interface OpenApiMethod {

    /**
     * 开放api方法名称，可以与方法名不同
     *
     * @return 开放api方法名称
     */
    String value() default "";

    /**
     * 返回值是否需要加密(true:需要，false:不需要，默认由{@link OpenApiConfig}决定)
     *
     * @return 返回值是否需要加密
     */
    String retEncrypt() default "";

    /**
     * 加密模式(默认由{@link OpenApiConfig}决定)
     *
     * @return 加密模式
     */
    CryModeEnum cryModeEnum() default CryModeEnum.UNKNOWN;

    /**
     * HTTP传输的数据是否启用压缩(true:压缩，false:不压缩，默认由{@link OpenApiConfig}决定)
     *
     * @return 是否启用压缩
     */
    String enableCompress() default "";

}
