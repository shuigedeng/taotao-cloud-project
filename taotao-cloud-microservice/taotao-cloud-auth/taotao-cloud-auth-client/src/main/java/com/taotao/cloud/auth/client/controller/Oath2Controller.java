/*
 * Copyright 2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.auth.client.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zyc
 */
@RequestMapping("/api/oath2")
@RestController
public class Oath2Controller {

	/**
	 * 获取当前认证的OAuth2用户信息，默认是保存在{@link javax.servlet.http.HttpSession}中的
	 *
	 * @param user OAuth2用户信息
	 * @return OAuth2用户信息
	 */
	@GetMapping("/user")
	public OAuth2User user(@AuthenticationPrincipal OAuth2User user) {
		return user;
	}

	/**
	 * 获取当前认证的OAuth2客户端信息，默认是保存在{@link javax.servlet.http.HttpSession}中的
	 *
	 * @param oAuth2AuthorizedClient OAuth2客户端信息
	 * @return OAuth2客户端信息
	 */
	@GetMapping("/client")
	public OAuth2AuthorizedClient user(
		@RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient oAuth2AuthorizedClient) {
		return oAuth2AuthorizedClient;
	}
}
