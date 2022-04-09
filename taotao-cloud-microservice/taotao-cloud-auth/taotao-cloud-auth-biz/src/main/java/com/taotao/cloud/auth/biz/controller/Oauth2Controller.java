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
package com.taotao.cloud.auth.biz.controller;

import com.taotao.cloud.common.constant.CommonConstant;
import com.taotao.cloud.common.constant.RedisConstant;
import com.taotao.cloud.common.exception.BaseException;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.common.utils.common.SecurityUtil;
import com.taotao.cloud.logger.annotation.RequestLogger;
import com.taotao.cloud.redis.repository.RedisRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
@RequestMapping("/auth/oauth2")
public class Oauth2Controller {

	@Autowired
	private RedisRepository redisRepository;

	/**
	 * 获取当前认证的OAuth2用户信息，默认是保存在{@link javax.servlet.http.HttpSession}中的
	 *
	 * @param user OAuth2用户信息
	 * @return OAuth2用户信息
	 */
	@Operation(summary = "获取当前认证的OAuth2用户信息", description = "获取当前认证的OAuth2用户信息", method = CommonConstant.GET)
	@RequestLogger("获取当前认证的OAuth2用户信息")
	@PreAuthorize("hasAuthority('express:company:info:id')")
	@GetMapping("/user")
	public Result<OAuth2User> user(@AuthenticationPrincipal OAuth2User user) {
		return Result.success(user);
	}

	/**
	 * 获取当前认证的OAuth2客户端信息，默认是保存在{@link javax.servlet.http.HttpSession}中的
	 *
	 * @param oAuth2AuthorizedClient OAuth2客户端信息
	 * @return OAuth2客户端信息
	 */
	@Operation(summary = "获取当前认证的OAuth2客户端信息", description = "v", method = CommonConstant.GET)
	@RequestLogger("获取当前认证的OAuth2客户端信息")
	@PreAuthorize("hasAuthority('express:company:info:id')")
	@GetMapping("/client")
	public Result<OAuth2AuthorizedClient> user(
		@RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient oAuth2AuthorizedClient) {
		return Result.success(oAuth2AuthorizedClient);
	}

	@Operation(summary = "退出系统", description = "退出系统", method = CommonConstant.POST)
	@RequestLogger("退出系统")
	@PostMapping("/logout")
	public Result<Boolean> logout() {
		Authentication authentication = SecurityUtil.getAuthentication();
		if (authentication instanceof JwtAuthenticationToken jwtAuthenticationToken) {
			Jwt jwt = jwtAuthenticationToken.getToken();
			String kid = (String) jwt.getHeaders().get("kid");
			try {
				long epochSecond = jwt.getExpiresAt().getEpochSecond();
				long nowTime = LocalDateTime.now().toInstant(ZoneOffset.of("+8")).getEpochSecond();

				// 标识jwt令牌失效
				redisRepository.setEx(RedisConstant.LOGOUT_JWT_KEY_PREFIX + kid, "",
					epochSecond - nowTime);

				// 添加用户退出日志

				// 删除用户在线信息

				return Result.success(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		throw new BaseException("退出失败");
	}


}
