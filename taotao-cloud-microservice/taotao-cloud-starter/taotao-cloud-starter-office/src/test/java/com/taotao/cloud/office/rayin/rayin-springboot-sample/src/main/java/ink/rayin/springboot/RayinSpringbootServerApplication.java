package com.taotao.cloud.office.rayin.rayin;

import ink.rayin.springboot.EnableRayinPdfAdpter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;


@SpringBootApplication
@EnableRayinPdfAdpter
public class RayinSpringbootServerApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(RayinSpringbootServerApplication.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(RayinSpringbootServerApplication.class);
	}
}
