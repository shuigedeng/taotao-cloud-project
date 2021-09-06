package com.taotao.cloud.uc.biz;

import com.alibaba.druid.pool.DruidDataSource;
import com.taotao.cloud.dingtalk.annatations.DingerScan;
import com.taotao.cloud.dingtalk.annatations.EnableMultiDinger;
import com.taotao.cloud.web.annotation.TaoTaoCloudApplication;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.SpringApplication;

/**
 * TaoTaoCloudUcApplication
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2020/11/30 下午3:33
 */
@DingerScan(basePackages = "com.taotao.cloud.uc.biz.dingtalk")
@EnableMultiDinger
@TaoTaoCloudApplication
public class TaoTaoCloudUcApplication {

	public static void main(String[] args) {
		SpringApplication.run(TaoTaoCloudUcApplication.class, args);
	}

}
