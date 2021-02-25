package com.taotao.cloud.dfs.biz;

import com.taotao.cloud.core.annotation.EnableTaoTaoCloudMVC;
import com.taotao.cloud.data.jpa.annotation.EnableTaoTaoCloudJPA;
import com.taotao.cloud.file.annotation.EnableTaoTaoCloudFile;
import com.taotao.cloud.job.annotation.EnableTaoTaoCloudXxlJob;
import com.taotao.cloud.log.annotation.EnableTaoTaoCloudRequestLog;
import com.taotao.cloud.p6spy.annotation.EnableTaoTaoCloudP6spy;
import com.taotao.cloud.redis.annotation.EnableTaoTaoCloudRedis;
import com.taotao.cloud.ribbon.annotation.EnableTaoTaoCloudFeign;
import com.taotao.cloud.seata.annotation.EnableTaoTaoCloudSeata;
import com.taotao.cloud.security.annotation.EnableTaoTaoCloudOauth2ResourceServer;
import com.taotao.cloud.sentinel.annotation.EnableTaoTaoCloudSentinel;
import com.taotao.cloud.swagger.annotation.EnableTaoTaoCloudSwagger2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTaoTaoCloudFile
@EnableTaoTaoCloudSwagger2
@EnableTaoTaoCloudOauth2ResourceServer
@EnableTaoTaoCloudJPA
@EnableTaoTaoCloudP6spy
@EnableTaoTaoCloudFeign
@EnableTaoTaoCloudMVC
@EnableTaoTaoCloudXxlJob
@EnableTaoTaoCloudRequestLog
@EnableTaoTaoCloudRedis
@EnableTaoTaoCloudSeata
@EnableTaoTaoCloudSentinel
@EnableTransactionManagement(proxyTargetClass = true)
@EnableDiscoveryClient
@SpringBootApplication
public class TaotaoCloudDfsApplication {

	public static void main(String[] args) {
		SpringApplication.run(TaotaoCloudDfsApplication.class, args);
	}

}
