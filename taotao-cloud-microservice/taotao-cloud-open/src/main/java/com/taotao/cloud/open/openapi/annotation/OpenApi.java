package com.taotao.cloud.open.openapi.annotation;

import com.taotao.cloud.open.openapi.config.OpenApiConfig;
import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 开放api注解，标识一个开放api
 * <p>
 * 注：
 * 1.若要OpenApi生效，必须先配置一些参数，请写一个类实现{@link OpenApiConfig}然后标注@Component确保被注入spring容器中
 * 2.该注解已经集成@Component注解，直接将此注解标识在一个bean类上，然后确保项目能够扫描到这些bean的包即可
 * </p>
 *
 * @author wanghuidong
 */
@Component
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.TYPE})
public @interface OpenApi {

    /**
     * 开放api名称，也是注入spring容器中的bean的名称，注意不要和已有的bean名称冲突
     *
     * @return 开放api名称
     */
    String value() default "";
}
