package com.taotao.cloud.threadpoll.dynamictp.apollo;

import com.dtp.core.spring.EnableDynamicTp;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 
 */
@EnableDynamicTp
@SpringBootApplication
public class ApolloExampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApolloExampleApplication.class, args);
    }
}
