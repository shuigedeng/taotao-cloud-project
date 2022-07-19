package com.taotao.cloud.backend.graphql;

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
public class TaoTaoCloudBackendGraphqlApplication {

	public static void main(String[] args) {
		SpringApplication.run(TaoTaoCloudBackendGraphqlApplication.class, args);
	}

}
