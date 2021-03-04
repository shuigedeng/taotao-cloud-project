package com.taotao.cloud.uc.biz;

import com.taotao.cloud.core.annotation.EnableTaoTaoCloudMVC;
import com.taotao.cloud.data.jpa.annotation.EnableTaoTaoCloudJPA;
import com.taotao.cloud.job.annotation.EnableTaoTaoCloudXxlJob;
import com.taotao.cloud.log.annotation.EnableTaoTaoCloudRequestLog;
import com.taotao.cloud.p6spy.annotation.EnableTaoTaoCloudP6spy;
import com.taotao.cloud.redis.annotation.EnableTaoTaoCloudRedis;
import com.taotao.cloud.ribbon.annotation.EnableTaoTaoCloudFeign;
import com.taotao.cloud.seata.annotation.EnableTaoTaoCloudSeata;
import com.taotao.cloud.security.annotation.EnableTaoTaoCloudOauth2ResourceServer;
import com.taotao.cloud.sentinel.annotation.EnableTaoTaoCloudSentinel;
import com.taotao.cloud.openapi.annotation.EnableTaoTaoCloudOpenapi;
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * TaoTaoCloudUcApplication
 *
 * @author dengtao
 * @since 2020/11/30 下午3:33
 * @version 1.0.0
 */
@EnableTaoTaoCloudOpenapi
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
@EnableEncryptableProperties
@EnableTransactionManagement(proxyTargetClass = true)
@EnableAutoConfiguration(excludeName = "org.springframework.cloud.netflix.ribbon.RibbonAutoConfiguration")
@SpringCloudApplication
public class TaoTaoCloudUcApplication {

	public static void main(String[] args) {
		SpringApplication.run(TaoTaoCloudUcApplication.class, args);
	}

}
