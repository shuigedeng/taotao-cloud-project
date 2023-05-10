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

package com.taotao.cloud.auth.biz.authentication.oauth2.qq.other;

import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.HashMap;
import java.util.Map;

/**
 * OAuth2AccessTokenResponseClient的组合类，使用了Composite Pattern（组合模式）
 * 除了同时支持GOOGLE，OKTA，GITHUB，FACEBOOK之外，可能还需要同时支持QQ、微信等多种认证服务
 * 根据registrationId选择相应的OAuth2AccessTokenResponseClient
 */
public class CompositeOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
	private Map<String, OAuth2UserService<OAuth2UserRequest, OAuth2User>> userServices;

	private static final String DefaultUserServiceKey = "default_key";

	public CompositeOAuth2UserService() {
		this.userServices = new HashMap<>();
		// DefaultOAuth2UserService是默认处理OAuth2协议获取用户逻辑的OAuth2UserService实现类
		// 将其预置到组合类CompositeOAuth2UserService中，从而默认支持GOOGLE，OKTA，GITHUB，FACEBOOK
		this.userServices.put(DefaultUserServiceKey, new DefaultOAuth2UserService());
	}

	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		ClientRegistration clientRegistration = userRequest.getClientRegistration();

		OAuth2UserService<OAuth2UserRequest, OAuth2User> service = userServices.get(clientRegistration.getRegistrationId());
		if (service == null) {
			service = userServices.get(DefaultUserServiceKey);
		}

		return service.loadUser(userRequest);
	}

	public Map<String, OAuth2UserService<OAuth2UserRequest, OAuth2User>> getUserServices() {
		return userServices;
	}
}
