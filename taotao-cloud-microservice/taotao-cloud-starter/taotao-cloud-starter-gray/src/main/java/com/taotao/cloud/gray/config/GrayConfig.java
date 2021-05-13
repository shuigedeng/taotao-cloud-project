package com.taotao.cloud.gray.config;

import com.taotao.cloud.gray.fegin.GrayFeignRequestInterceptor;
import feign.RequestInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 灰度配置类
 *
 * @author madi
 * @date 2021-03-02 18:30
 */
@Configuration
@ConditionalOnProperty(value = "gray.rule.enabled", havingValue = "true")
@LoadBalancerClients(defaultConfiguration = GrayLoadbalancerConfig.class)
public class GrayConfig {

	@Bean
	public RequestInterceptor grayFeignRequestInterceptor() {
		return new GrayFeignRequestInterceptor();
	}
}
