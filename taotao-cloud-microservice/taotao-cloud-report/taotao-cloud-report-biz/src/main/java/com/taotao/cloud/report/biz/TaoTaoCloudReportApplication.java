package com.taotao.cloud.report.biz;

import com.taotao.cloud.seata.annotation.EnableTaoTaoCloudSeata;
import com.taotao.cloud.sentinel.annotation.EnableTaoTaoCloudSentinel;
import com.taotao.cloud.web.annotation.TaoTaoCloudApplication;
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@MapperScan(basePackages = "com.taotao.cloud.store.biz.mapper")
@EnableJpaRepositories(basePackages = "com.taotao.cloud.report.biz.repository.inf")
@TaoTaoCloudApplication
public class TaoTaoCloudReportApplication {

	public static void main(String[] args) {
		SpringApplication.run(TaoTaoCloudReportApplication.class, args);
	}

}
