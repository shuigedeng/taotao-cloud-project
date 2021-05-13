package com.taotao.cloud.uc.biz;

import com.taotao.cloud.data.jpa.annotation.EnableTaoTaoCloudJPA;
import com.taotao.cloud.openapi.annotation.EnableTaoTaoCloudOpenapi;
import com.taotao.cloud.seata.annotation.EnableTaoTaoCloudSeata;
import com.taotao.cloud.security.taox.annotation.EnableOauth2ResourceSecurity;
import com.taotao.cloud.sentinel.annotation.EnableTaoTaoCloudSentinel;
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * TaoTaoCloudUcApplication
 *
 * @author dengtao
 * @since 2020/11/30 下午3:33
 * @version 1.0.0
 */
//@EnableTaoTaoCloudP6spy
//@EnableTaoTaoCloudFeign
//@EnableTaoTaoCloudMVC
//@EnableTaoTaoCloudXxlJob	
//@EnableTaoTaoCloudRequestLog
//@EnableAutoConfiguration(excludeName = "org.springframework.cloud.netflix.ribbon.RibbonAutoConfiguration")
@EnableEncryptableProperties
@EnableTaoTaoCloudOpenapi
@EnableTaoTaoCloudSeata
@EnableTaoTaoCloudJPA
@EnableTaoTaoCloudSentinel
@SpringBootApplication
@EnableDiscoveryClient
@EnableOauth2ResourceSecurity
public class TaoTaoCloudUcApplication {

	public static void main(String[] args) {
		SpringApplication.run(TaoTaoCloudUcApplication.class, args);
	}

}
