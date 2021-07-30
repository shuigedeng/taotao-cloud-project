package com.taotao.cloud.security.annotation;

import java.lang.annotation.*;

/**
 * 免鉴权
 *
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NotAuth {

}
