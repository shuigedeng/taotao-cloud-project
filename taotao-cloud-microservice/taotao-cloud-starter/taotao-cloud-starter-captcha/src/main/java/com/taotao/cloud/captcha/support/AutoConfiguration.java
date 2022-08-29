/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.captcha.support;

import com.taotao.cloud.captcha.support.core.processor.CaptchaRendererFactory;
import com.taotao.cloud.captcha.support.core.properties.CaptchaProperties;
import com.taotao.cloud.captcha.support.core.provider.ResourceProvider;
import com.taotao.cloud.common.utils.log.LogUtils;
import javax.annotation.PostConstruct;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>Description: 验证码自动注入 </p>
 *
 * @author shuigedeng
 * @version 2022.07
 * @since 2022-07-12 12:53:13
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(CaptchaProperties.class)
public class AutoConfiguration {

	@PostConstruct
	public void postConstruct() {
		LogUtils.info("Starter [Engine Captcha Starter] Auto Configure.");
	}

	@Bean
	@ConditionalOnMissingBean
	public ResourceProvider resourceProvider(CaptchaProperties captchaProperties) {
		ResourceProvider resourceProvider = new ResourceProvider(captchaProperties);
		LogUtils.info("[Resource Provider] Auto Configure.");
		return resourceProvider;
	}

	@Bean
	@ConditionalOnMissingBean
	public CaptchaRendererFactory captchaRendererFactory() {
		CaptchaRendererFactory captchaRendererFactory = new CaptchaRendererFactory();
		LogUtils.info("Bean [Captcha Renderer Factory] Auto Configure.");
		return captchaRendererFactory;
	}
}
