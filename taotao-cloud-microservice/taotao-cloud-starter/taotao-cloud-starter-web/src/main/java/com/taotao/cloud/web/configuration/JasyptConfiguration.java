package com.taotao.cloud.web.configuration;

import com.taotao.cloud.web.jasypt.EncryptAspect;
import com.taotao.cloud.web.properties.EncryptProperties;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

/**
 * 加解密自动注入配置
 */
public class JasyptConfiguration {

	@Bean
	@ConditionalOnProperty(prefix = EncryptProperties.PREFIX, name = "enabled", havingValue = "true")
	public EncryptAspect encryptAspect(StringEncryptor stringEncryptor) {
		return new EncryptAspect(stringEncryptor);
	}
}
