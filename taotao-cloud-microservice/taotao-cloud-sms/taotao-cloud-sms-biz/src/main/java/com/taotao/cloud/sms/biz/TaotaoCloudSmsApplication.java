package com.taotao.cloud.sms.biz;

import com.taotao.cloud.core.annotation.EnableTaoTaoCloudMVC;
import com.taotao.cloud.data.jpa.annotation.EnableTaoTaoCloudJPA;
import com.taotao.cloud.job.xxl.annotation.EnableTaoTaoCloudJobXxl;
import com.taotao.cloud.log.annotation.EnableTaoTaoCloudRequestLog;
import com.taotao.cloud.p6spy.annotation.EnableTaoTaoCloudP6spy;
import com.taotao.cloud.redis.annotation.EnableTaoTaoCloudRedis;
import com.taotao.cloud.feign.annotation.EnableTaoTaoCloudFeign;
import com.taotao.cloud.seata.annotation.EnableTaoTaoCloudSeata;
import com.taotao.cloud.security.annotation.EnableTaoTaoCloudOauth2ResourceServer;
import com.taotao.cloud.sentinel.annotation.EnableTaoTaoCloudSentinel;
import com.taotao.cloud.openapi.annotation.EnableTaoTaoCloudOpenapi;
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTaoTaoCloudOpenapi
@EnableTaoTaoCloudOauth2ResourceServer
@EnableTaoTaoCloudJPA
@EnableTaoTaoCloudP6spy
@EnableTaoTaoCloudFeign
@EnableTaoTaoCloudMVC
@EnableTaoTaoCloudJobXxl
@EnableTaoTaoCloudRequestLog
@EnableTaoTaoCloudRedis
@EnableTaoTaoCloudSeata
@EnableTaoTaoCloudSentinel
@EnableEncryptableProperties
@EnableTransactionManagement(proxyTargetClass = true)
@EnableDiscoveryClient
@SpringBootApplication
public class TaoTaoCloudSmsApplication {

	public static void main(String[] args) {
		SpringApplication.run(TaoTaoCloudSmsApplication.class, args);
	}

}
