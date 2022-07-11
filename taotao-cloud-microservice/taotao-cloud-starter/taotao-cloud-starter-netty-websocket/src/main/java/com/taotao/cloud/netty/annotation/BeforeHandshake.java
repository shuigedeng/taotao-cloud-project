package com.taotao.cloud.netty.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 当有新的连接进入时，对该方法进行回调 注入参数的类型:Session、HttpHeaders... 
 *
 * @author shuigedeng
 * @version 2022.07
 * @since 2022-07-11 10:12:41
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface BeforeHandshake {

}
