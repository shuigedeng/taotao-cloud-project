package com.taotao.cloud.uc.biz;

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
//@EnableTaoTaoCloudOpenapi
//@EnableTaoTaoCloudOauth2ResourceServer
//@EnableTaoTaoCloudJPA
//@EnableTaoTaoCloudP6spy
//@EnableTaoTaoCloudFeign
//@EnableTaoTaoCloudMVC
//@EnableTaoTaoCloudXxlJob
//@EnableTaoTaoCloudRequestLog
//@EnableTaoTaoCloudRedis
//@EnableTaoTaoCloudSeata
//@EnableTaoTaoCloudSentinel
@EnableEncryptableProperties
@EnableTransactionManagement(proxyTargetClass = true)
@EnableAutoConfiguration(excludeName = "org.springframework.cloud.netflix.ribbon.RibbonAutoConfiguration")
@SpringCloudApplication
public class TaoTaoCloudUcApplication {

	public static void main(String[] args) {
		SpringApplication.run(TaoTaoCloudUcApplication.class, args);
	}

}
