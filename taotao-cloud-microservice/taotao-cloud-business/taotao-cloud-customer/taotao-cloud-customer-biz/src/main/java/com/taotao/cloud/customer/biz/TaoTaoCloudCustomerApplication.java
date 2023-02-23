package com.taotao.cloud.customer.biz;

import com.taotao.cloud.common.utils.common.PropertyUtils;
import com.taotao.cloud.web.annotation.TaoTaoCloudApplication;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * @author shuigedeng
 * @version 2022.03
 * @since 2020/11/20 上午10:43
 */
@TaoTaoCloudApplication
public class TaoTaoCloudCustomerApplication {

	public static void main(String[] args) {
		PropertyUtils.setDefaultProperty("taotao-cloud-customer");

		SpringApplication.run(TaoTaoCloudCustomerApplication.class, args);
	}

}
