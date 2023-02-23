package com.taotao.cloud.generator.biz;

import com.taotao.cloud.common.utils.common.PropertyUtils;
import com.taotao.cloud.web.annotation.TaoTaoCloudApplication;
import org.springframework.boot.SpringApplication;


@TaoTaoCloudApplication
public class TaoTaoCloudGeneratorApplication {

	public static void main(String[] args) {
		PropertyUtils.setDefaultProperty("taotao-cloud-report");

		SpringApplication.run(TaoTaoCloudGeneratorApplication.class, args);
	}

}
