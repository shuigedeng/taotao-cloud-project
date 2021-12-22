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
package com.taotao.cloud.oauth2.biz.controller;

import com.taotao.cloud.common.constant.CommonConstant;
import com.taotao.cloud.logger.annotation.RequestLogger;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Oath2Controller
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2021-12-22 15:50:56
 */
@Validated
@Tag(name = "Oauth2API", description = "Oauth2API")
@RestController
@RequestMapping("/oauth2")
public class Oauth2Controller {

	/**
	 * 获取当前认证的OAuth2用户信息，默认是保存在{@link javax.servlet.http.HttpSession}中的
	 *
	 * @param user OAuth2用户信息
	 * @return OAuth2用户信息
	 */
	@Operation(summary = "获取当前认证的OAuth2用户信息", description = "获取当前认证的OAuth2用户信息", method = CommonConstant.GET)
	@RequestLogger(description = "获取当前认证的OAuth2用户信息")
	@PreAuthorize("hasAuthority('express:company:info:id')")
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
	@Operation(summary = "获取当前认证的OAuth2客户端信息", description = "v", method = CommonConstant.GET)
	@RequestLogger(description = "获取当前认证的OAuth2客户端信息")
	@PreAuthorize("hasAuthority('express:company:info:id')")
	@GetMapping("/client")
	public OAuth2AuthorizedClient user(
		@RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient oAuth2AuthorizedClient) {
		return oAuth2AuthorizedClient;
	}
}
