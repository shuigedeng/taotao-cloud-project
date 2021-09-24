package com.taotao.cloud.backend.graphql;

import com.taotao.cloud.data.jpa.annotation.EnableTaoTaoCloudJPA;
import com.taotao.cloud.feign.annotation.EnableTaoTaoCloudFeign;
import com.taotao.cloud.log.annotation.EnableTaoTaoCloudRequestLog;
import com.taotao.cloud.p6spy.annotation.EnableTaoTaoCloudP6spy;
import com.taotao.cloud.seata.annotation.EnableTaoTaoCloudSeata;
import com.taotao.cloud.sentinel.annotation.EnableTaoTaoCloudSentinel;
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTaoTaoCloudJPA
@EnableTaoTaoCloudP6spy
@EnableTaoTaoCloudFeign
@EnableTaoTaoCloudRequestLog
@EnableTaoTaoCloudSeata
@EnableTaoTaoCloudSentinel
@EnableEncryptableProperties
@EnableTransactionManagement(proxyTargetClass = true)
@EnableDiscoveryClient
@SpringBootApplication
public class TaoTaoCloudBackendGraphqlApplication {

	public static void main(String[] args) {
		SpringApplication.run(TaoTaoCloudBackendGraphqlApplication.class, args);
	}

}
