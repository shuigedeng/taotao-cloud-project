package com.taotao.cloud.shortlink.biz.infrastructure.dcloud;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @Description
 * @Author 刘森飚
 **/

@MapperScan("net.xdclass.mapper")
@EnableTransactionManagement
//@EnableFeignClients
@SpringBootApplication
public class LinkApplication {

    public static void main(String[] args) {
        SpringApplication.run(LinkApplication.class, args);
    }

}
