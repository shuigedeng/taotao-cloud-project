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
package com.taotao.cloud.gateway.authentication;

import com.taotao.cloud.common.constant.RedisConstant;
import com.taotao.cloud.gateway.exception.InvalidTokenException;
import com.taotao.cloud.redis.repository.RedisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 权限认证管理器
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2020/4/29 22:10
 */
@Component
public class CustomReactiveAuthorizationManager implements ReactiveAuthorizationManager<AuthorizationContext> {

	@Autowired
	private RedisRepository redisRepository;

	@Override
	public Mono<AuthorizationDecision> check(Mono<Authentication> authentication,
		AuthorizationContext authorizationContext) {
		return authentication.map(auth -> {

			if (auth instanceof JwtAuthenticationToken jwtAuthenticationToken) {
				Jwt jwt = jwtAuthenticationToken.getToken();
				String kid = (String) jwt.getHeaders().get("kid");

				// 判断kid是否存在 存在表示令牌不能使用 即:用户已退出
				Boolean hasKey = redisRepository.exists(RedisConstant.LOGOUT_JWT_KEY_PREFIX + kid);
				if(hasKey){
					throw new InvalidTokenException("无效的token");
				}
			}

			ServerWebExchange exchange = authorizationContext.getExchange();
			ServerHttpRequest request = exchange.getRequest();

			//可在此处鉴权也可在各个微服务鉴权
            //boolean isPermission = super.hasPermission(auth, request.getMethodValue(), request.getURI().getPath());

			return new AuthorizationDecision(true);
		}).defaultIfEmpty(new AuthorizationDecision(false));
	}

}
