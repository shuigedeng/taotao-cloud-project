package com.taotao.cloud.health.base.dingding;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author: chejiangyi
 * @version: 2019-07-23 13:48
 **/
@Configuration
@EnableConfigurationProperties(DingdingProperties.class)
@ConditionalOnProperty(name = "bsf.message.dingding.enabled", havingValue = "true")
public class DingdingConfiguration implements InitializingBean {

	@Override
	public void afterPropertiesSet() throws Exception {

	}

	@Bean
	public DingdingProvider getDingding() {
		return new DingdingProvider();
	}
}
