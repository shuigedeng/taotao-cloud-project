package com.taotao.cloud.java.javaee.s1.c4_spring.p2.test;
import org.junit.Test;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class ProcessorTest {

    @Test
    public void testProcessor(){
        ApplicationContext context  = new ClassPathXmlApplicationContext("/spring-context.xml");
    }
}

