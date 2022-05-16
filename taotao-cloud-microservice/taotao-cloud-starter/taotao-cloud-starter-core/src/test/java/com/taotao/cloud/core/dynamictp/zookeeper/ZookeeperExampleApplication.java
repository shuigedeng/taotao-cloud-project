package com.taotao.cloud.core.dynamictp.zookeeper;

import com.dtp.core.spring.EnableDynamicTp;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 
 */
@EnableDynamicTp
@SpringBootApplication
public class ZookeeperExampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZookeeperExampleApplication.class, args);
    }
}
