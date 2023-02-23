package com.taotao.cloud.workflow.biz;

import com.taotao.cloud.common.utils.common.PropertyUtils;
import com.taotao.cloud.web.annotation.TaoTaoCloudApplication;
import org.springframework.boot.SpringApplication;


@TaoTaoCloudApplication
public class TaoTaoCloudWorkflowApplication {

	public static void main(String[] args) {
		PropertyUtils.setDefaultProperty("taotao-cloud-workflow");

		SpringApplication.run(TaoTaoCloudWorkflowApplication.class, args);
	}

}
