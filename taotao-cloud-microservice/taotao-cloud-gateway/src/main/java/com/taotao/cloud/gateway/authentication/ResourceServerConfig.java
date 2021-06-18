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
package com.taotao.cloud.gateway.authentication;

import com.taotao.cloud.common.enums.ResultEnum;
import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.common.utils.ResponseUtil;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

/**
 * ResourceServerConfig
 *
 * @author dengtao
 * @version 1.0.0
 * @since 2021/06/18 14:41
 */
@AllArgsConstructor
@Configuration
@EnableWebFluxSecurity
public class ResourceServerConfig {
	@Bean
	public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
		http
			.csrf().disable()
			.httpBasic().disable()
			.authorizeExchange()
			.pathMatchers("/**").permitAll()
			.anyExchange().authenticated()
			.and()
			.exceptionHandling()
			.authenticationEntryPoint((exchange, e) -> {
				LogUtil.error("认证失败", e);
				return ResponseUtil.fail(exchange, ResultEnum.UNAUTHORIZED);
			})
			.accessDeniedHandler((exchange, e) -> {
				LogUtil.error("授权失败", e);
				return ResponseUtil.fail(exchange, ResultEnum.FORBIDDEN);
			})
			.and()
			.oauth2ResourceServer()
			.jwt();
		return http.build();
	}
}
