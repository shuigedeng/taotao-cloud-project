package com.taotao.cloud.threadpoll.dynamictp.consul;

import com.dtp.core.spring.EnableDynamicTp;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 
 */
@EnableDynamicTp
@SpringBootApplication
public class CloudConsulExampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(CloudConsulExampleApplication.class, args);
    }
}
