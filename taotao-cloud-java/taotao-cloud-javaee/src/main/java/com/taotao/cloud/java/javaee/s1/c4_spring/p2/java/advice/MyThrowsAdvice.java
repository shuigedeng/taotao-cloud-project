package com.taotao.cloud.java.javaee.s1.c4_spring.p2.java.advice;

import org.springframework.aop.ThrowsAdvice;

// 在核心中抛异常时，执行
public class MyThrowsAdvice implements ThrowsAdvice {
    public void afterThrowing(Exception ex){
        System.out.println("my throws");
    }
}
