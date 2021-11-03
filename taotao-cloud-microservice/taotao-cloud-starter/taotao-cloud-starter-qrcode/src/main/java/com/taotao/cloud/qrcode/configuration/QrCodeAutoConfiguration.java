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
package com.taotao.cloud.qrcode.configuration;

import com.taotao.cloud.common.constant.StarterNameConstant;
import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.qrcode.properties.QrCodeProperties;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

/**
 * P6spyAutoConfiguration
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2020/10/14 09:18
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(prefix = QrCodeProperties.PREFIX, name = "enabled", havingValue = "true")
public class QrCodeAutoConfiguration implements InitializingBean {

	@Override
	public void afterPropertiesSet() throws Exception {
		LogUtil.started(QrCodeAutoConfiguration.class, StarterNameConstant.P6SPY_STARTER);
	}

}
