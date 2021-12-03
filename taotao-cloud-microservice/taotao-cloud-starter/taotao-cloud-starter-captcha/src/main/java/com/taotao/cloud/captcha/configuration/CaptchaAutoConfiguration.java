/*
 * Copyright 2002-2021 the original author or authors.
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
package com.taotao.cloud.captcha.configuration;

import cn.hutool.core.util.StrUtil;
import com.taotao.cloud.captcha.model.CaptchaConst;
import com.taotao.cloud.captcha.properties.CaptchaProperties;
import com.taotao.cloud.captcha.service.CaptchaCacheService;
import com.taotao.cloud.captcha.service.CaptchaService;
import com.taotao.cloud.captcha.service.impl.CaptchaServiceFactory;
import com.taotao.cloud.captcha.util.ImageUtils;
import com.taotao.cloud.common.constant.StarterName;
import com.taotao.cloud.common.utils.LogUtil;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.util.Base64Utils;
import org.springframework.util.FileCopyUtils;

/**
 * CaptchaServiceAutoConfiguration
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-03 20:53:53
 */
@Configuration
@EnableConfigurationProperties({CaptchaProperties.class})
@ConditionalOnProperty(prefix = CaptchaProperties.PREFIX, name = "enabled", havingValue = "true")
public class CaptchaAutoConfiguration implements InitializingBean {

	@Override
	public void afterPropertiesSet() throws Exception {
		LogUtil.started(CaptchaAutoConfiguration.class, StarterName.CAPTCHA_STARTER);
	}

	@Bean
	@ConditionalOnMissingBean
	public CaptchaService captchaService(CaptchaProperties prop) {
		Properties config = new Properties();

		config.put(CaptchaConst.CAPTCHA_CACHETYPE, prop.getCacheType().name());
		config.put(CaptchaConst.CAPTCHA_WATER_MARK, prop.getWaterMark());
		config.put(CaptchaConst.CAPTCHA_FONT_TYPE, prop.getFontType());
		config.put(CaptchaConst.CAPTCHA_TYPE, prop.getType().getCodeValue());
		config.put(CaptchaConst.CAPTCHA_INTERFERENCE_OPTIONS, prop.getInterferenceOptions());
		config.put(CaptchaConst.ORIGINAL_PATH_JIGSAW, prop.getJigsaw());
		config.put(CaptchaConst.ORIGINAL_PATH_PIC_CLICK, prop.getPicClick());
		config.put(CaptchaConst.CAPTCHA_SLIP_OFFSET, prop.getSlipOffset());
		config.put(CaptchaConst.CAPTCHA_AES_STATUS, String.valueOf(prop.getAesStatus()));
		config.put(CaptchaConst.CAPTCHA_WATER_FONT, prop.getWaterFont());
		config.put(CaptchaConst.CAPTCHA_CACAHE_MAX_NUMBER, prop.getCacheNumber());
		config.put(CaptchaConst.CAPTCHA_TIMING_CLEAR_SECOND, prop.getTimingClear());

		config.put(CaptchaConst.HISTORY_DATA_CLEAR_ENABLE, prop.getHistoryDataClearEnable() ? "1" : "0");

		config.put(
			CaptchaConst.REQ_FREQUENCY_LIMIT_ENABLE, prop.getReqFrequencyLimitEnable() ? "1" : "0");
		config.put(CaptchaConst.REQ_GET_LOCK_LIMIT, prop.getReqGetLockLimit() + "");
		config.put(CaptchaConst.REQ_GET_LOCK_SECONDS, prop.getReqGetLockSeconds() + "");
		config.put(CaptchaConst.REQ_GET_MINUTE_LIMIT, prop.getReqGetMinuteLimit() + "");
		config.put(CaptchaConst.REQ_CHECK_MINUTE_LIMIT, prop.getReqCheckMinuteLimit() + "");
		config.put(CaptchaConst.REQ_VALIDATE_MINUTE_LIMIT, prop.getReqVerifyMinuteLimit() + "");

		if ((StrUtil.isNotBlank(prop.getJigsaw()) && prop.getJigsaw().startsWith("classpath:"))
			|| (StrUtil.isNotBlank(prop.getPicClick()) && prop.getPicClick()
			.startsWith("classpath:"))) {

			//自定义resources目录下初始化底图
			config.put(CaptchaConst.CAPTCHA_INIT_ORIGINAL, "true");
			initializeBaseMap(prop.getJigsaw(), prop.getPicClick());
		}
		return CaptchaServiceFactory.getInstance(config);
	}

	@Bean
	public CaptchaCacheService captchaCacheService(CaptchaProperties captchaProperties) {
		return CaptchaServiceFactory.getCache(captchaProperties.getCacheType().name());
	}

	/**
	 * initializeBaseMap
	 *
	 * @param jigsaw   jigsaw
	 * @param picClick picClick
	 * @author shuigedeng
	 * @since 2021-09-03 20:54:00
	 */
	public static void initializeBaseMap(String jigsaw, String picClick) {
		ImageUtils.cacheBootImage(
			getResourcesImagesFile(jigsaw + "/original/*.png"),
			getResourcesImagesFile(jigsaw + "/slidingBlock/*.png"),
			getResourcesImagesFile(picClick + "/*.png")
		);
	}

	/**
	 * getResourcesImagesFile
	 *
	 * @param path path
	 * @return {@link java.util.Map }
	 * @author shuigedeng
	 * @since 2021-09-03 20:54:04
	 */
	public static Map<String, String> getResourcesImagesFile(String path) {
		Map<String, String> imgMap = new HashMap<>();
		ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		try {
			Resource[] resources = resolver.getResources(path);
			for (Resource resource : resources) {
				byte[] bytes = FileCopyUtils.copyToByteArray(resource.getInputStream());
				String string = Base64Utils.encodeToString(bytes);
				String filename = resource.getFilename();
				imgMap.put(filename, string);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return imgMap;
	}
}
