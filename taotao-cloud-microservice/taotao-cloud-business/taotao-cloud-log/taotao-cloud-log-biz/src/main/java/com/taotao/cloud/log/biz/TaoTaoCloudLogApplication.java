package com.taotao.cloud.log.biz;

import com.taotao.cloud.common.utils.common.PropertyUtils;
import com.taotao.cloud.web.annotation.TaoTaoCloudApplication;
import org.springframework.boot.SpringApplication;


@TaoTaoCloudApplication
public class TaoTaoCloudLogApplication {

	public static void main(String[] args) {
		PropertyUtils.setDefaultProperty("taotao-cloud-log");

		SpringApplication.run(TaoTaoCloudLogApplication.class, args);
	}

}
