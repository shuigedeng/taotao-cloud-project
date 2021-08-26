package com.taotao.cloud.opencv.biz;


@EnableTaoTaoCloudUploadFile
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
@EnableTransactionManagement(proxyTargetClass = true)
@EnableDiscoveryClient
@SpringBootApplication
public class TaoTaoCloudDfsApplication {

	public static void main(String[] args) {
		SpringApplication.run(TaoTaoCloudDfsApplication.class, args);
	}

}
