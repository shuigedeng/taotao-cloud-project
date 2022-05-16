package com.taotao.cloud.core.dynamictp.zookeepercloud;

import com.dtp.core.spring.EnableDynamicTp;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 
 */
@EnableDynamicTp
@SpringBootApplication
public class CloudZookeeperExampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(CloudZookeeperExampleApplication.class, args);
    }
}
