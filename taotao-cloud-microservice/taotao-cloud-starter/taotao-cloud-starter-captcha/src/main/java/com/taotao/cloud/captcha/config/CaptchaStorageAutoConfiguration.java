package com.taotao.cloud.captcha.config;

import com.taotao.cloud.captcha.properties.CaptchaProperties;
import com.taotao.cloud.captcha.service.CaptchaCacheService;
import com.taotao.cloud.captcha.service.impl.CaptchaServiceFactory;
import org.springframework.context.annotation.Bean;

/**
 * 存储策略自动配置.
 */
public class CaptchaStorageAutoConfiguration {

	@Bean(name = "CaptchaCacheService")
	public CaptchaCacheService captchaCacheService(CaptchaProperties captchaProperties) {
		return CaptchaServiceFactory.getCache(captchaProperties.getCacheType().name());
	}
}
