package com.tatoao.cloud.demo.seata.storage;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 库存
 *
 * @author zlt
 * @since 2019/9/14
 */
@EnableDiscoveryClient
@EnableFeignClients
@MapperScan({"com.tatoao.cloud.demo.seata.storage.mapper"})
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class TaoTaoCloudDemoSeataStorageServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(TaoTaoCloudDemoSeataStorageServiceApplication.class, args);
	}
}
