package com.taotao.cloud.web.annotation;

import java.lang.annotation.*;

/**
* 获取程序执行时间注解
*/
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface CountTime {
}
