package com.taotao.cloud.wechat.biz;

import com.taotao.cloud.common.utils.common.PropertyUtils;
import com.taotao.cloud.web.annotation.TaoTaoCloudApplication;
import org.springframework.boot.SpringApplication;


@TaoTaoCloudApplication
public class TaoTaoCloudWechatApplication {

	public static void main(String[] args) {
		PropertyUtils.setDefaultProperty("taotao-cloud-wechat");

		SpringApplication.run(TaoTaoCloudWechatApplication.class, args);
	}

}
