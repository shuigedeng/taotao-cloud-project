package com.taotao.cloud.stock.biz;

import com.taotao.cloud.common.utils.common.PropertyUtils;
import com.taotao.cloud.web.annotation.TaoTaoCloudApplication;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@MapperScan("com.user.demo.service.infrastructure.*.mapper")
@EnableJpaRepositories(basePackages = "com.taotao.cloud.stock.biz.repository.inf")
@TaoTaoCloudApplication
public class TaoTaoCloudStockApplication {

	public static void main(String[] args) {
		PropertyUtils.setDefaultProperty("taotao-cloud-stock");

		SpringApplication.run(TaoTaoCloudStockApplication.class, args);
	}

}
