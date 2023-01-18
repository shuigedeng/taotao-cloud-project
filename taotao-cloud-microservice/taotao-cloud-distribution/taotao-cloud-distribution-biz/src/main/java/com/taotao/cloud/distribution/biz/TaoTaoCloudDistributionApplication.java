package com.taotao.cloud.distribution.biz;

import com.taotao.cloud.common.utils.common.PropertyUtils;
import com.taotao.cloud.web.annotation.TaoTaoCloudApplication;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@TaoTaoCloudApplication
public class TaoTaoCloudDistributionApplication {

	public static void main(String[] args) {
		PropertyUtils.setDefaultProperty("taotao-cloud-distribution");

		SpringApplication.run(TaoTaoCloudDistributionApplication.class, args);
	}

}
