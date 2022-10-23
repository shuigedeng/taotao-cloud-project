package com.taotao.cloud.elasticsearch.easyes.sample;

import com.xpc.easyes.autoconfig.annotation.EsMapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 启动类
 **/
@SpringBootApplication
@EsMapperScan("com.xpc.easyes.sample.mapper")
public class EasyEsApplication {
    public static void main(String[] args) {
        SpringApplication.run(EasyEsApplication.class, args);
    }
}
