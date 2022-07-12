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
package com.taotao.cloud.captcha.support.hutool.configuration;

import com.taotao.cloud.captcha.support.core.definition.enums.CaptchaCategory;
import com.taotao.cloud.captcha.support.core.provider.ResourceProvider;
import com.taotao.cloud.captcha.support.hutool.renderer.CircleCaptchaRenderer;
import com.taotao.cloud.captcha.support.hutool.renderer.GifCaptchaRenderer;
import com.taotao.cloud.captcha.support.hutool.renderer.LineCaptchaRenderer;
import com.taotao.cloud.captcha.support.hutool.renderer.ShearCaptchaRenderer;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>Description: Hutool 验证码配置 </p>
 *
 * @author shuigedeng
 * @version 2022.07
 * @since 2022-07-12 12:57:00
 */
@Configuration(proxyBeanMethods = false)
public class HutoolCaptchaConfiguration {

	private static final Logger log = LoggerFactory.getLogger(HutoolCaptchaConfiguration.class);

	@PostConstruct
	public void postConstruct() {
		log.debug("SDK [Engine Captcha Hutool] Auto Configure.");
	}

	@Bean(CaptchaCategory.HUTOOL_LINE_CAPTCHA)
	@ConditionalOnBean(ResourceProvider.class)
	public LineCaptchaRenderer lineCaptchaRenderer(ResourceProvider resourceProvider) {
		LineCaptchaRenderer lineCaptchaRenderer = new LineCaptchaRenderer();
		lineCaptchaRenderer.setResourceProvider(resourceProvider);
		log.trace("Bean [Hutool Line Captcha Renderer] Auto Configure.");
		return lineCaptchaRenderer;
	}

	@Bean(CaptchaCategory.HUTOOL_CIRCLE_CAPTCHA)
	@ConditionalOnBean(ResourceProvider.class)
	public CircleCaptchaRenderer circleCaptchaRenderer(ResourceProvider resourceProvider) {
		CircleCaptchaRenderer circleCaptchaRenderer = new CircleCaptchaRenderer();
		circleCaptchaRenderer.setResourceProvider(resourceProvider);
		log.trace(" Bean [Hutool Circle Captcha Renderer] Auto Configure.");
		return circleCaptchaRenderer;
	}

	@Bean(CaptchaCategory.HUTOOL_SHEAR_CAPTCHA)
	@ConditionalOnBean(ResourceProvider.class)
	public ShearCaptchaRenderer shearCaptchaRenderer(ResourceProvider resourceProvider) {
		ShearCaptchaRenderer shearCaptchaRenderer = new ShearCaptchaRenderer();
		shearCaptchaRenderer.setResourceProvider(resourceProvider);
		log.trace(" Bean [Hutool Shear Captcha Renderer] Auto Configure.");
		return shearCaptchaRenderer;
	}

	@Bean(CaptchaCategory.HUTOOL_GIF_CAPTCHA)
	@ConditionalOnBean(ResourceProvider.class)
	public GifCaptchaRenderer gifCaptchaRenderer(ResourceProvider resourceProvider) {
		GifCaptchaRenderer gifCaptchaRenderer = new GifCaptchaRenderer();
		gifCaptchaRenderer.setResourceProvider(resourceProvider);
		log.trace("Bean [Hutool Gif Captcha Renderer] Auto Configure.");
		return gifCaptchaRenderer;
	}
}
