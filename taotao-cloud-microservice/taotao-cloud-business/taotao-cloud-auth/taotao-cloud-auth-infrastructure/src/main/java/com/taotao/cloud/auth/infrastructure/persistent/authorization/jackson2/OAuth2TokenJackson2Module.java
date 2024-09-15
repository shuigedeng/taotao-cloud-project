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

package com.taotao.cloud.auth.infrastructure.persistent.authorization.jackson2;

import com.fasterxml.jackson.databind.module.SimpleModule;
import com.taotao.boot.security.spring.utils.Jackson2Constants;
import org.springframework.security.jackson2.SecurityJackson2Modules;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;

/**
 * <p>自定义 OAutho2 Module </p>
 *
 * @since : 2022/10/24 15:51
 */
public class OAuth2TokenJackson2Module extends SimpleModule {

	public OAuth2TokenJackson2Module() {
		super(OAuth2TokenJackson2Module.class.getName(), Jackson2Constants.VERSION);
	}

	@Override
	public void setupModule(SetupContext context) {
		SecurityJackson2Modules.enableDefaultTyping(context.getOwner());
		context.setMixInAnnotations(ClientAuthenticationMethod.class,
			ClientAuthenticationMethodMixin.class);
		context.setMixInAnnotations(AuthorizationGrantType.class,
			AuthorizationGrantTypeMixin.class);
		context.setMixInAnnotations(TokenSettings.class, TokenSettingsMixin.class);
		context.setMixInAnnotations(ClientSettings.class, ClientSettingsMixin.class);
		context.setMixInAnnotations(RegisteredClient.class, RegisteredClientMixin.class);
		context.setMixInAnnotations(OAuth2ClientAuthenticationToken.class,
			OAuth2ClientAuthenticationTokenMixin.class);
	}
}
