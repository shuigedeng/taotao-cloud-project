package com.taotao.cloud.encrypt.annotation;

import java.lang.annotation.*;

/**
 * 独立注解
 * 用于部分类和方法加密使用
 *
 * @author shuigedeng
 * @version 2022.07
 * @since 2022-07-06 15:08:57
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SeparateEncrypt {
}
