package com.taotao.cloud.store.biz;

import com.taotao.cloud.common.utils.common.PropertyUtils;
import com.taotao.cloud.web.annotation.TaoTaoCloudApplication;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@TaoTaoCloudApplication
public class TaoTaoCloudStoreApplication {

	public static void main(String[] args) {
		PropertyUtils.setDefaultProperty("taotao-cloud-store");

		SpringApplication.run(TaoTaoCloudStoreApplication.class, args);
	}

}
