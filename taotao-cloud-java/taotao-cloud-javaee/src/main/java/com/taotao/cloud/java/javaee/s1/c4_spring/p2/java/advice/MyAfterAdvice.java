package com.taotao.cloud.java.javaee.s1.c4_spring.p2.java.advice;

import org.springframework.aop.AfterReturningAdvice;

import java.lang.reflect.Method;

// 后置通知，在核心之后执行，如果核心有异常，则不执行
public class MyAfterAdvice implements AfterReturningAdvice {
    @Override
    public void afterReturning(Object returnValue, Method method, Object[] args, Object target) throws Throwable {
        System.out.println("after!!");
    }
}
