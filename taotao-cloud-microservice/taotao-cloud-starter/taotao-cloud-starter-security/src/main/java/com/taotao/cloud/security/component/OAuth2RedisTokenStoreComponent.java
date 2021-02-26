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
package com.taotao.cloud.security.component;

import com.taotao.cloud.common.constant.StarterNameConstant;
import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.security.properties.SecurityProperties;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.oauth2.provider.token.TokenStore;

/**
 * 认证服务器使用Redis存取令牌
 *
 * @author dengtao
 * @version 1.0.0
 * @since 2020/4/30 09:07
 */
@AllArgsConstructor
@ConditionalOnProperty(prefix = "taotao.cloud.oauth2.token.store", name = "type", havingValue = "redis")
public class OAuth2RedisTokenStoreComponent implements InitializingBean {

	private final RedisConnectionFactory connectionFactory;
	private final SecurityProperties securityProperties;

	@Override
	public void afterPropertiesSet() throws Exception {
		LogUtil.info("[TAOTAO CLOUD][" + StarterNameConstant.TAOTAO_CLOUD_AUTH_STARTER + "]"
			+ "redis认证token已启动, Security配置已启动");
	}

	@Bean
	public TokenStore tokenStore() {
		return new RedisTokenStoreComponent(connectionFactory, securityProperties);
	}
}
