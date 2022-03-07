package com.taotao.cloud.core.heaven.annotation;

import java.lang.annotation.*;

/**
 * 线程不安全安全注解
 */
@Documented
@Inherited
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface NotThreadSafe {
}
