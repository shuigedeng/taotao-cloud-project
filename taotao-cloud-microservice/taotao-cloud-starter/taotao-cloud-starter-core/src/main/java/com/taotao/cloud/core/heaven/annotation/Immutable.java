package com.taotao.cloud.core.heaven.annotation;

import java.lang.annotation.*;

/**
 * 不可变类
 *
 * @author bbhou
 * @version 0.0.7
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Inherited
public @interface Immutable {
}
