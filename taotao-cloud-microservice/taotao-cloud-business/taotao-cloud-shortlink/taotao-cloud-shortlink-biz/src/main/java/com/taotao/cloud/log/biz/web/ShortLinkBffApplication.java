package com.taotao.cloud.log.biz.web;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * This is Description
 *
 * @since 2022/05/01
 */
@SpringBootApplication
@EnableDubbo(scanBasePackages = "com.zc.shortlink")
public class ShortLinkBffApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShortLinkBffApplication.class, args);
	}
}
