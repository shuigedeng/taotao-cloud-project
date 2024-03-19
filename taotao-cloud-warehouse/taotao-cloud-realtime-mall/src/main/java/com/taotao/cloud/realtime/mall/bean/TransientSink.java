package com.taotao.cloud.realtime.mall.bean;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 *
 * Date: 2021/2/23
 * Desc: 用该注解标记的属性，不需要插入到ClickHouse
 */
@Target(FIELD)
@Retention(RUNTIME)
public @interface TransientSink {
}
