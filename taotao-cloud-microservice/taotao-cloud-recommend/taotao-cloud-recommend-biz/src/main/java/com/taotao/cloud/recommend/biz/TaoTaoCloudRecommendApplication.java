package com.taotao.cloud.recommend.biz;

import com.taotao.cloud.common.utils.common.PropertyUtils;
import com.taotao.cloud.web.annotation.TaoTaoCloudApplication;
import org.springframework.boot.SpringApplication;


@TaoTaoCloudApplication
public class TaoTaoCloudRecommendApplication {

	public static void main(String[] args) {
		PropertyUtils.setDefaultProperty("taotao-cloud-recommend");

		SpringApplication.run(TaoTaoCloudRecommendApplication.class, args);
	}

}
