package com.taotao.cloud.file.biz;

import com.taotao.cloud.common.utils.common.PropertyUtils;
import com.taotao.cloud.web.annotation.TaoTaoCloudApplication;
import org.springframework.boot.SpringApplication;


@TaoTaoCloudApplication
public class TaoTaoCloudFileApplication {

	public static void main(String[] args) {
		PropertyUtils.setDefaultProperty("taotao-cloud-file");

		SpringApplication.run(TaoTaoCloudFileApplication.class, args);
	}

}
