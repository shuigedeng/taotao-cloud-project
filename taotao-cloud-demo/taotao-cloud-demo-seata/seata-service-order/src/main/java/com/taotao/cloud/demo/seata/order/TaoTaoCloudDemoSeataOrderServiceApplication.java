package com.taotao.cloud.demo.seata.order;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 订单
 *
 * @since 2019/9/14
 */
@EnableDiscoveryClient
@EnableFeignClients
@MapperScan({"com.taotao.cloud.demo.seata.order.mapper"})
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class TaoTaoCloudDemoSeataOrderServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(TaoTaoCloudDemoSeataOrderServiceApplication.class, args);
    }
}
