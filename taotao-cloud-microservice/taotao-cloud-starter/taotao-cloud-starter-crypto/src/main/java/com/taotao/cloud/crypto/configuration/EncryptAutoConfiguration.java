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
package com.taotao.cloud.crypto.configuration;

import com.taotao.cloud.crypto.properties.EncryptProperties;
import com.taotao.cloud.common.constant.StarterName;
import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.crypto.encrypt.EncryptAspect;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * EncryptConfiguration
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 22:13:23
 */
@AutoConfiguration
@EnableConfigurationProperties({EncryptProperties.class})
@ConditionalOnProperty(prefix = EncryptProperties.PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
public class EncryptAutoConfiguration implements InitializingBean {

	@Override
	public void afterPropertiesSet() throws Exception {
		LogUtils.started(EncryptAutoConfiguration.class, StarterName.WEB_STARTER);
	}

	@Bean
	@ConditionalOnBean({StringEncryptor.class})
	public EncryptAspect encryptAspect(StringEncryptor stringEncryptor) {
		LogUtils.started(EncryptAspect.class, StarterName.WEB_STARTER);

		return new EncryptAspect(stringEncryptor);
	}
}
