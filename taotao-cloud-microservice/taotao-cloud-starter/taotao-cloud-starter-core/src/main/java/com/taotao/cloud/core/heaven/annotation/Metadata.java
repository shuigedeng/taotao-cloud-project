package com.taotao.cloud.core.heaven.annotation;

import java.lang.annotation.*;

/**
 * 元注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@Target(ElementType.ANNOTATION_TYPE)
public @interface Metadata {
}
