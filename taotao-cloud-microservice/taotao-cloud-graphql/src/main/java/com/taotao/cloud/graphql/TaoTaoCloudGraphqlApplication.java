package com.taotao.cloud.graphql;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// @EnableTaoTaoCloudJpa
// @EnableTaoTaoCloudP6spy
// @EnableTaoTaoCloudFeign
// @EnableTaoTaoCloudLogger
// @EnableTaoTaoCloudSeata
// @EnableTaoTaoCloudSentinel
// @EnableEncryptableProperties
// @EnableTransactionManagement(proxyTargetClass = true)
// @EnableDiscoveryClient
@SpringBootApplication
public class TaoTaoCloudGraphqlApplication {

	public static void main(String[] args) {
		SpringApplication.run(TaoTaoCloudGraphqlApplication.class, args);
	}

}
