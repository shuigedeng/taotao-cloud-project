package com.taotao.cloud.tenant.biz;

import com.taotao.cloud.common.utils.common.PropertyUtils;
import com.taotao.cloud.web.annotation.TaoTaoCloudApplication;
import org.springframework.boot.SpringApplication;


@TaoTaoCloudApplication
public class TaoTaoCloudTenantApplication {

	public static void main(String[] args) {
		PropertyUtils.setDefaultProperty("taotao-cloud-tenant");

		SpringApplication.run(TaoTaoCloudTenantApplication.class, args);
	}

}
