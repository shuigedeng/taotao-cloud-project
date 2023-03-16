package com.taotao.cloud.job.biz;

import com.taotao.cloud.common.utils.common.PropertyUtils;
import com.taotao.cloud.web.annotation.TaoTaoCloudApplication;
import org.springframework.boot.SpringApplication;


@TaoTaoCloudApplication
public class TaoTaoCloudJobApplication {

	public static void main(String[] args) {
		PropertyUtils.setDefaultProperty("taotao-cloud-job");

		SpringApplication.run(TaoTaoCloudJobApplication.class, args);
	}

}
