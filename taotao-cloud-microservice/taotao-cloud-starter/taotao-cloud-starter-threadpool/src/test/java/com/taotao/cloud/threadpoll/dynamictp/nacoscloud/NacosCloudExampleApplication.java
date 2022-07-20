package com.taotao.cloud.threadpoll.dynamictp.nacoscloud;

import com.dtp.core.spring.EnableDynamicTp;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 
 */
@EnableDynamicTp
@SpringBootApplication
public class NacosCloudExampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(NacosCloudExampleApplication.class, args);
    }
}
