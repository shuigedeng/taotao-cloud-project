package com.taotao.cloud.sms.configuration;

import com.taotao.cloud.sms.core.AliSmsTemplate;
import com.taotao.cloud.sms.core.SmsTemplate;
import com.taotao.cloud.sms.props.AliSmsProperties;
import com.taotao.cloud.sms.props.SmsProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

/**
 * 短信配置
 */
@ConditionalOnProperty(prefix = SmsProperties.PREFIX, name = "enabled", havingValue = "true")
public class SmsConfiguration {

	@Bean(name = "aliSmsTemplate")
	@ConditionalOnProperty(prefix = SmsProperties.PREFIX, name = "type", havingValue = "ali")
	public SmsTemplate aliSmsTemplate(AliSmsProperties smsProperties) {
		return new AliSmsTemplate(smsProperties);
	}
}
