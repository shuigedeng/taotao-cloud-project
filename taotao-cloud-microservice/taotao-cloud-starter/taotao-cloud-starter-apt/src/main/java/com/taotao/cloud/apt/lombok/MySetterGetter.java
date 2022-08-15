package com.taotao.cloud.apt.lombok;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE) // 注解只在源码中保留@Target(ElementType.TYPE) // 用于修饰类
public @interface MySetterGetter {
}


