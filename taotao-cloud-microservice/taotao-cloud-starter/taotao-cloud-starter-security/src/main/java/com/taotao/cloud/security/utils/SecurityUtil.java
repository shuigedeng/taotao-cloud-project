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
package com.taotao.cloud.security.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

/**
 * SecurityUtil
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2021/8/25 09:54
 */
public class SecurityUtil {

	public static Integer userId() {
		return Integer.parseInt(SecurityContextHolder.getContext().getAuthentication().getName());
	}

	public static List<String> authorities() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication instanceof JwtAuthenticationToken) {
			JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) authentication;
			Jwt principal = (Jwt) jwtAuthenticationToken.getPrincipal();
			return Arrays.asList(((String) principal.getClaims().get("scp")).split(" "));
		}
		return new ArrayList<>();
	}

	public static List<String> roles() {
		return Objects.requireNonNull(SecurityUtil.authorities())
			.stream().filter(authority -> authority.startsWith("ROLE_"))
			.collect(Collectors.toList());
	}
}
