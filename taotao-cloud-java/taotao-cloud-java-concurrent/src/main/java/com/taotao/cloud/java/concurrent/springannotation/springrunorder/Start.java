package com.taotao.cloud.java.springannotation.springrunorder;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Start {
    public static void main(String[] args) {
        new ClassPathXmlApplicationContext("spring.xml");
    }
}
