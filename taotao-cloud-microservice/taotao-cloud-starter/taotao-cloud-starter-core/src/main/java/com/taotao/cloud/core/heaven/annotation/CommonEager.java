package com.taotao.cloud.core.heaven.annotation;

import java.lang.annotation.*;

/**
 * 指这个类将会被迁移到共有项目模块 heaven 模块
 */
@Inherited
@Documented
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CommonEager {
}
