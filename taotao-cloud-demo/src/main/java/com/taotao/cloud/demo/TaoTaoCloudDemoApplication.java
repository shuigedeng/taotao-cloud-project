package com.taotao.cloud.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//@EnableMultiDinger
//@DingerScan(basePackages = "com.taotao.cloud.sys.biz.dingtalk")
@SpringBootApplication
public class TaoTaoCloudDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(TaoTaoCloudDemoApplication.class, args);
	}

}
