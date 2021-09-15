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
import com.taotao.cloud.captcha.controller.CaptchaController;
import com.taotao.cloud.captcha.model.Const;
import com.taotao.cloud.captcha.properties.CaptchaProperties;
import com.taotao.cloud.captcha.service.CaptchaService;
import com.taotao.cloud.captcha.service.impl.CaptchaServiceFactory;
import com.taotao.cloud.captcha.util.ImageUtils;
import com.taotao.cloud.common.constant.StarterNameConstant;
import com.taotao.cloud.common.utils.LogUtil;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
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
@Import(CaptchaController.class)
@ConditionalOnWebApplication
public class CaptchaControllerAutoConfiguration implements InitializingBean {

	@Override
	public void afterPropertiesSet() throws Exception {
		LogUtil.started(CaptchaControllerAutoConfiguration.class, StarterNameConstant.CAPTCHA_STARTER);
	}


}
