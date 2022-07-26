package com.taotao.cloud.open.client.annotation;


import com.taotao.cloud.open.client.config.OpenApiClientConfig;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 开放api服务引用注解，用于引用远程openapi服务，需将此注解标识在接口上
 *
 * <p>
 * 注：服务引用若要生效，需要进行配置，请参考{@link OpenApiClientConfig}进行配置
 * </p>
 *
 * @author wanghuidong
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.TYPE})
public @interface OpenApiRef {

    /**
     * 开放api名称
     *
     * @return 开放api名称
     */
    String value() default "";
}
