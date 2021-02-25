package com.taotao.cloude.demo.seata.account;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 账号
 *
 * @since 2019/9/14
 */
@EnableDiscoveryClient
@MapperScan({"com.taotao.cloude.demo.seata.account.mapper"})
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class TaoTaoCloudDemoSeataAccountServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(TaoTaoCloudDemoSeataAccountServiceApplication.class, args);
	}
}
