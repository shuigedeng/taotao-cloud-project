package com.taotao.cloud.sms.configuration;

import com.taotao.cloud.sms.core.AliSmsTemplate;
import com.taotao.cloud.sms.core.SmsTemplate;
import com.taotao.cloud.sms.props.SmsProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 阿里短信配置
 *
 */
@Configuration
@EnableConfigurationProperties(value = SmsProperties.class)
@ConditionalOnProperty(prefix = SmsProperties.PREFIX, name = "enable", havingValue = "true")
public class AliSmsConfiguration {

	@Bean
	public SmsTemplate aliSmsTemplate(SmsProperties smsProperties) {
		return new AliSmsTemplate(smsProperties);
	}
}
