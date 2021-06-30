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

import com.taotao.cloud.common.utils.LogUtil;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import reactor.core.publisher.Mono;

/**
 * 自定义认证管理器
 *
 * @author dengtao
 * @version 1.0.0
 * @since 2020/4/29 22:09
 */
public class AuthenticationManagerComponent implements ReactiveAuthenticationManager {

	private static final String FAILURE = "token已失效";
	private static final String EXPIRED = "token已过期";
	private static final String FAILED = "用户认证失败";


	@Override
	public Mono<Authentication> authenticate(Authentication authentication) {
		LogUtil.info("taotao cloud user authentication info : {0}", authentication.toString());

		return Mono.justOrEmpty(authentication);

		//从Redis中获取当前路径可访问角色列表
//	    URI uri = authorizationContext.getExchange().getRequest().getURI();
//	    Object obj = redisTemplate.opsForHash().get(RedisConstant.RESOURCE_ROLES_MAP, uri.getPath());
//	    List<String> authorities = Convert.toList(String.class,obj);
//	    authorities = authorities.stream().map(i -> i = AuthConstant.AUTHORITY_PREFIX + i).collect(
//		    Collectors.toList());
//	    //认证通过且角色匹配的用户可访问当前路径
//	    return mono
//		    .filter(Authentication::isAuthenticated)
//		    .flatMapIterable(Authentication::getAuthorities)
//		    .map(GrantedAuthority::getAuthority)
//		    .any(authorities::contains)
//		    .map(AuthorizationDecision::new)
//		    .defaultIfEmpty(new AuthorizationDecision(false));

//        return Mono.justOrEmpty(authentication)
//                .filter(a -> a instanceof BearerTokenAuthenticationToken)
//                .cast(BearerTokenAuthenticationToken.class)
//                .map(BearerTokenAuthenticationToken::getToken)
//                .flatMap((accessTokenValue -> {
//	                LogUtil.info("token: "+ accessTokenValue);
////                    OAuth2AccessToken accessToken = tokenStore.readAccessToken(accessTokenValue);
////                    if (accessToken == null) {
////                        return Mono.error(new InvalidTokenException(FAILURE));
////                    } else if (accessToken.isExpired()) {
////                        tokenStore.removeAccessToken(accessToken);
////                        return Mono.error(new InvalidTokenException(EXPIRED));
////                    }
////
////                    OAuth2Authentication result = tokenStore.readAuthentication(accessToken);
////                    if (result == null) {
////                        return Mono.error(new InvalidTokenException(FAILED));
////                    }
//                    return Mono.just(accessTokenValue);
//                }))
//                .cast(Authentication.class);
	}
}
