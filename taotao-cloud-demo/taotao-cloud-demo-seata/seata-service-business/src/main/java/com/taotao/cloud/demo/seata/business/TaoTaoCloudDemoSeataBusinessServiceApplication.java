package com.taotao.cloud.demo.seata.business;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 业务
 *
 * @since 2019/9/14
 */
@EnableDiscoveryClient
@EnableFeignClients
@SpringBootApplication
public class TaoTaoCloudDemoSeataBusinessServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(TaoTaoCloudDemoSeataBusinessServiceApplication.class, args);
	}
}
