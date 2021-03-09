package com.taotao.cloud.java.javaee.s1.c4_spring.p2.java.advice;

import org.springframework.aop.MethodBeforeAdvice;

import java.lang.reflect.Method;

// 前置通知类
public class MyBeforeAdvice implements MethodBeforeAdvice {
    @Override
    public void before(Method method, Object[] objects, Object o) throws Throwable {
        // 额外功能
        System.out.println("事务控制2");
        System.out.println("日志打印2");
    }
}
