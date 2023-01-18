package com.taotao.cloud.im.biz;

import com.taotao.cloud.common.utils.common.PropertyUtils;
import com.taotao.cloud.web.annotation.TaoTaoCloudApplication;
import org.springframework.boot.SpringApplication;

/**
 * https://gitee.com/lakaola/im-platform
 */
@TaoTaoCloudApplication
public class TaoTaoCloudImApplication {

	public static void main(String[] args) {
		PropertyUtils.setDefaultProperty("taotao-cloud-im");

		SpringApplication.run(TaoTaoCloudImApplication.class, args);
	}

}
