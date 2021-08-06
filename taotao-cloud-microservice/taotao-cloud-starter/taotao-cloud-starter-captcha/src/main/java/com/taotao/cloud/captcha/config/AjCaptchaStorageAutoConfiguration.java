package com.taotao.cloud.captcha.config;

import com.taotao.cloud.captcha.properties.AjCaptchaProperties;
import com.taotao.cloud.captcha.service.CaptchaCacheService;
import com.taotao.cloud.captcha.service.impl.CaptchaServiceFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 存储策略自动配置.
 */
@Configuration
public class AjCaptchaStorageAutoConfiguration {

	@Bean(name = "AjCaptchaCacheService")
	public CaptchaCacheService captchaCacheService(AjCaptchaProperties ajCaptchaProperties) {
		//缓存类型redis/local/....
		return CaptchaServiceFactory.getCache(ajCaptchaProperties.getCacheType().name());
	}
}
