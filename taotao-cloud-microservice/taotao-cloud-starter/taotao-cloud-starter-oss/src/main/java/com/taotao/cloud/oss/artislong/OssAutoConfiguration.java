package com.taotao.cloud.oss.artislong;

import cn.hutool.extra.spring.EnableSpringUtil;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;


@EnableSpringUtil
@Configuration
@ComponentScan(basePackages = "com.taotao.cloud.oss.artislong")
@EnableConfigurationProperties(OssProperties.class)
public class OssAutoConfiguration {

}
