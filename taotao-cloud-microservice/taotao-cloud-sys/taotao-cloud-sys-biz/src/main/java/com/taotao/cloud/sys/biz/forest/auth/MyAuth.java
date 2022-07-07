package com.taotao.cloud.sys.biz.forest.auth;

import com.dtflys.forest.annotation.MethodLifeCycle;
import com.dtflys.forest.annotation.RequestAttributes;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用Forest自定义注解实现一个自定义的签名加密注解
 * 凡用此接口修饰的方法或接口，其对应的所有请求都会执行自定义的签名加密过程
 * 而自定义的签名加密过程，由这里的@MethodLifeCycle注解指定的生命周期类进行处理
 * 可以将此注解用在接口类和方法上
 */
@Documented
/** 重点： @MethodLifeCycle注解指定该注解的生命周期类*/
@MethodLifeCycle(MyAuthLifeCycle.class)
@RequestAttributes
@Retention(RetentionPolicy.RUNTIME)
/** 指定该注解可用于类上或方法上 */
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface MyAuth {

    /**
     * 自定义注解的属性：用户名
     * 所有自定注解的属性可以在生命周期类中被获取到
     */
    String username();

    /**
     * 自定义注解的属性：密码
     * 所有自定注解的属性可以在生命周期类中被获取到
     */
    String password();
}
