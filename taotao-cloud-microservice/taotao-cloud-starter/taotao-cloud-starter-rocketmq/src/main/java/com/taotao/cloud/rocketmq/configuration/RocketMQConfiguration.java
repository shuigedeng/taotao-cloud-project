package com.taotao.cloud.rocketmq.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * RocketMQ配置
 *
 */
@Configuration
//@PropertySource(factory = YamlPropertySourceFactory.class, value = "classpath:mate-rocketmq.yml")
@PropertySource(value = "classpath:mate-rocketmq.yml")
public class RocketMQConfiguration {

}
