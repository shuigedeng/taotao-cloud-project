package com.taotao.cloud.java.javaee.s1.c4_spring.p2.java.processor;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.lang.Nullable;

 // 构造  、 set、xxx、 init、 xxx、 destroy
// 后处理器
public class MyBeanPostProcessor implements BeanPostProcessor {



    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("后处理1");
        System.out.println("后处理1:"+bean+"  :"+beanName);
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("后处理2");
        System.out.println("后处理2:"+bean+"  :"+beanName);
        return bean;
    }
}
