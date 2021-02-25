package com.taotao.cloud.spring.source.spring_annotation.test;

import com.taotao.cloud.spring.source.spring_annotation.tx.TxConfig;
import com.taotao.cloud.spring.source.spring_annotation.tx.UserService;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class IOCTest_Tx {

    @Test
    public void test01() {
        AnnotationConfigApplicationContext applicationContext =
                new AnnotationConfigApplicationContext(TxConfig.class);

        UserService userService = applicationContext.getBean(UserService.class);

        userService.insertUser();
        applicationContext.close();
    }

}
