package com.taotao.cloud.uc.biz;

import com.taotao.cloud.data.jpa.annotation.EnableTaoTaoCloudJPA;
import com.taotao.cloud.job.annotation.EnableTaoTaoCloudXxlJob;
import com.taotao.cloud.loadbalancer.annotation.EnableTaoTaoCloudFeign;
import com.taotao.cloud.loadbalancer.annotation.EnableTaoTaoCloudHttpClient;
import com.taotao.cloud.loadbalancer.annotation.EnableTaoTaoCloudLoadbalancer;
import com.taotao.cloud.log.annotation.EnableTaoTaoCloudRequestLog;
import com.taotao.cloud.openapi.annotation.EnableTaoTaoCloudOpenapi;
import com.taotao.cloud.p6spy.annotation.EnableTaoTaoCloudP6spy;
import com.taotao.cloud.seata.annotation.EnableTaoTaoCloudSeata;
import com.taotao.cloud.security.taox.annotation.EnableTaoTaoCloudOauth2ResourceSecurity;
import com.taotao.cloud.sentinel.annotation.EnableTaoTaoCloudSentinel;
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * TaoTaoCloudUcApplication
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2020/11/30 下午3:33
 */
@EnableTaoTaoCloudRequestLog
@EnableTaoTaoCloudXxlJob
@EnableTaoTaoCloudP6spy
@EnableTaoTaoCloudFeign
@EnableTaoTaoCloudOpenapi
@EnableTaoTaoCloudSeata
@EnableTaoTaoCloudJPA
@EnableTaoTaoCloudLoadbalancer
@EnableTaoTaoCloudHttpClient
@EnableTaoTaoCloudSentinel
@EnableTaoTaoCloudOauth2ResourceSecurity
@EnableEncryptableProperties
@EnableDiscoveryClient
@SpringBootApplication
public class TaoTaoCloudUcApplication {

	public static void main(String[] args) {
		SpringApplication.run(TaoTaoCloudUcApplication.class, args);
	}

}
