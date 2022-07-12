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
package com.taotao.cloud.captcha.support.graphic.configuration;

import com.taotao.cloud.captcha.support.core.definition.enums.CaptchaCategory;
import com.taotao.cloud.captcha.support.core.provider.ResourceProvider;
import com.taotao.cloud.captcha.support.graphic.renderer.ArithmeticCaptchaRenderer;
import com.taotao.cloud.captcha.support.graphic.renderer.ChineseCaptchaRenderer;
import com.taotao.cloud.captcha.support.graphic.renderer.ChineseGifCaptchaRenderer;
import com.taotao.cloud.captcha.support.graphic.renderer.SpecCaptchaRenderer;
import com.taotao.cloud.captcha.support.graphic.renderer.SpecGifCaptchaRenderer;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>Description: 图形验证码配置 </p>
 *
 * @author shuigedeng
 * @version 2022.07
 * @since 2022-07-12 12:54:23
 */
@Configuration(proxyBeanMethods = false)
public class GraphicCaptchaConfiguration {

	private static final Logger log = LoggerFactory.getLogger(GraphicCaptchaConfiguration.class);

	@PostConstruct
	public void postConstruct() {
		log.debug("SDK [Engine Captcha Graphic] Auto Configure.");
	}

	@Bean(CaptchaCategory.ARITHMETIC_CAPTCHA)
	@ConditionalOnBean(ResourceProvider.class)
	public ArithmeticCaptchaRenderer arithmeticCaptchaRenderer(ResourceProvider resourceProvider) {
		ArithmeticCaptchaRenderer arithmeticCaptchaRenderer = new ArithmeticCaptchaRenderer();
		arithmeticCaptchaRenderer.setResourceProvider(resourceProvider);
		log.trace("Bean [Arithmetic Captcha Renderer] Auto Configure.");
		return arithmeticCaptchaRenderer;
	}

	@Bean(CaptchaCategory.CHINESE_CAPTCHA)
	@ConditionalOnBean(ResourceProvider.class)
	public ChineseCaptchaRenderer chineseCaptchaRenderer(ResourceProvider resourceProvider) {
		ChineseCaptchaRenderer chineseCaptchaRenderer = new ChineseCaptchaRenderer();
		chineseCaptchaRenderer.setResourceProvider(resourceProvider);
		log.trace("Bean [Chinese Captcha Renderer] Auto Configure.");
		return chineseCaptchaRenderer;
	}

	@Bean(CaptchaCategory.CHINESE_GIF_CAPTCHA)
	@ConditionalOnBean(ResourceProvider.class)
	public ChineseGifCaptchaRenderer chineseGifCaptchaRenderer(ResourceProvider resourceProvider) {
		ChineseGifCaptchaRenderer chineseGifCaptchaRenderer = new ChineseGifCaptchaRenderer();
		chineseGifCaptchaRenderer.setResourceProvider(resourceProvider);
		log.trace(" Bean [Chinese Gif Captcha Renderer] Auto Configure.");
		return chineseGifCaptchaRenderer;
	}

	@Bean(CaptchaCategory.SPEC_GIF_CAPTCHA)
	@ConditionalOnBean(ResourceProvider.class)
	public SpecGifCaptchaRenderer specGifCaptchaRenderer(ResourceProvider resourceProvider) {
		SpecGifCaptchaRenderer specGifCaptchaRenderer = new SpecGifCaptchaRenderer();
		specGifCaptchaRenderer.setResourceProvider(resourceProvider);
		log.trace(" Bean [Spec Gif Captcha Renderer] Auto Configure.");
		return specGifCaptchaRenderer;
	}

	@Bean(CaptchaCategory.SPEC_CAPTCHA)
	@ConditionalOnBean(ResourceProvider.class)
	public SpecCaptchaRenderer specCaptchaRenderer(ResourceProvider resourceProvider) {
		SpecCaptchaRenderer specCaptchaRenderer = new SpecCaptchaRenderer();
		specCaptchaRenderer.setResourceProvider(resourceProvider);
		log.trace("Bean [Spec Captcha Renderer] Auto Configure.");
		return specCaptchaRenderer;
	}
}
