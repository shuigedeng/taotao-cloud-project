package com.taotao.cloud.java.javaee.s1.c4_spring.p2.java.advice;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

// 环绕通知
public class MyMethodInterceptor implements MethodInterceptor {
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        System.out.println("tx begin~~");
        Object ret = invocation.proceed();// 触发，执行核心功能
        System.out.println("tx end!!");
        return ret;
    }
}
