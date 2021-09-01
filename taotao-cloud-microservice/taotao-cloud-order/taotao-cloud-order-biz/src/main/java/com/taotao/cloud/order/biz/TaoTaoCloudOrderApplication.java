package com.taotao.cloud.order.biz;

import com.taotao.cloud.web.annotation.TaoTaoCloudApplication;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;

@TaoTaoCloudApplication
public class TaoTaoCloudOrderApplication {

	public static void main(String[] args) {
		SpringApplication.run(TaoTaoCloudOrderApplication.class, args);
	}

}
