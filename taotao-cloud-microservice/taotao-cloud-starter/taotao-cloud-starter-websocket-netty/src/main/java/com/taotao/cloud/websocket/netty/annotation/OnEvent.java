package com.taotao.cloud.websocket.netty.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 当接收到Netty的事件时，对该方法进行回调 注入参数的类型:Session、Object
 *
 * @author shuigedeng
 * @version 2022.07
 * @since 2022-07-11 09:59:25
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface OnEvent {

}
