package com.taotao.cloud.log.biz.shortlink;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * This is Description
 *
 * @since 2022/05/01
 */
@SpringBootApplication
@EnableDubbo(scanBasePackages = "com.zc.shortlink.rpc")
public class ShortLinkServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShortLinkServerApplication.class, args);
	}
}
