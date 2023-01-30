package com.taotao.cloud.flowable.biz;

import com.taotao.cloud.common.utils.common.PropertyUtils;
import com.taotao.cloud.web.annotation.TaoTaoCloudApplication;
import org.springframework.boot.SpringApplication;

/**
 * @author shuigedeng
 * @version 2022.03
 * @since 2020/11/20 上午10:43
 */
@TaoTaoCloudApplication
public class TaoTaoCloudFlowableApplication {

	public static void main(String[] args) {
		PropertyUtils.setDefaultProperty("taotao-cloud-flowable");

		SpringApplication.run(TaoTaoCloudFlowableApplication.class, args);
	}

}
