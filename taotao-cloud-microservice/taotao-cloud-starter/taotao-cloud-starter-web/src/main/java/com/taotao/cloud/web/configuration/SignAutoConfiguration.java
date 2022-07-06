package com.taotao.cloud.web.configuration;

import com.taotao.cloud.web.sign.HttpConverterConfig;
import com.taotao.cloud.web.sign.advice.DecryptRequestBodyAdvice;
import com.taotao.cloud.web.sign.advice.EncryptResponseBodyAdvice;
import com.taotao.cloud.web.sign.advice.SignAspect;
import com.taotao.cloud.web.sign.properties.EncryptBodyProperties;
import com.taotao.cloud.web.sign.properties.EncryptProperties;
import com.taotao.cloud.web.sign.properties.SignProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

/**
 * SignConfiguration
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2022-03-23 08:44:00
 */
@AutoConfiguration
@ConditionalOnProperty(prefix = SignProperties.PREFIX, name = "enabled", havingValue = "true")
@Import({
	HttpConverterConfig.class,
	EncryptResponseBodyAdvice.class,
	DecryptRequestBodyAdvice.class,
	SignAspect.class})
@EnableConfigurationProperties({
	EncryptBodyProperties.class,
	EncryptProperties.class,
	SignProperties.class,})
public class SignAutoConfiguration {


}
