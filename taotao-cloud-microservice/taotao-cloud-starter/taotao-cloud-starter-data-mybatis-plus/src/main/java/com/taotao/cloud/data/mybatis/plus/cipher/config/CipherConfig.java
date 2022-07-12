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
package com.taotao.cloud.data.mybatis.plus.cipher.config;

import com.taotao.cloud.data.mybatis.plus.cipher.interceptor.FieldEncryptInterceptor;
import com.taotao.cloud.data.mybatis.plus.cipher.service.CryptService;
import com.taotao.cloud.data.mybatis.plus.cipher.service.CryptServiceImpl;
import com.taotao.cloud.data.mybatis.plus.properties.MybatisPlusProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * CipherConfig
 *
 * @author shuigedeng
 * @version 2022.07
 * @since 2022-07-12 14:04:30
 */
@Configuration
public class CipherConfig {

	@Bean
	@ConditionalOnProperty(prefix = MybatisPlusProperties.PREFIX, name = "cipherEnable", havingValue = "true")
	public FieldEncryptInterceptor paginationInterceptor() {
		return new FieldEncryptInterceptor();
	}

	@Bean
	@ConditionalOnProperty(prefix = MybatisPlusProperties.PREFIX, name = "cipherEnable", havingValue = "true")
	public CryptService cryptService() {
		return new CryptServiceImpl();
	}
}
