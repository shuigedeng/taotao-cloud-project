package com.taotao.cloud.oss.artislong;

import cn.hutool.extra.spring.EnableSpringUtil;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;


/**
 * oss汽车配置
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:33:58
 */
@EnableSpringUtil
@Configuration
@ComponentScan(basePackages = "com.taotao.cloud.oss.artislong")
@EnableConfigurationProperties(OssProperties.class)
public class OssAutoConfiguration {

}
