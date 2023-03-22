package com.taotao.cloud.log.biz.other;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class ShortUrlServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShortUrlServerApplication.class, args);
	}
}
