package com.taotao.cloud.generator.maku.common.annotation;

import org.springframework.web.bind.annotation.Mapping;

import java.lang.annotation.*;

/**
 * 参数加解密注解
 *
 * @author 李淼 Milo 505754686@qq.com
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Mapping
@Documented
public @interface EncryptParameter {
}
