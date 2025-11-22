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

import com.taotao.boot.cache.redis.repository.RedisRepository;
import com.taotao.boot.common.constant.RedisConstants;
import com.taotao.boot.common.utils.log.LogUtils;
import com.taotao.boot.security.spring.access.security.SecurityConfigAttribute;
import com.taotao.boot.security.spring.access.security.SecurityRequest;
import com.taotao.boot.security.spring.access.security.SecurityRequestMatcher;
import com.taotao.boot.security.spring.authorization.SecurityMatcherConfigurer;
import com.taotao.boot.security.spring.authorization.SecurityMetadataSourceStorage;
import com.taotao.boot.security.spring.utils.WebUtils;
import com.taotao.cloud.gateway.exception.InvalidTokenException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import lombok.*;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationResult;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 权限认证管理器 // 验证通过则返回：AuthorizationDecision(true) // 验证失败则返回：AuthorizationDecision(false)
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2020/4/29 22:10
 */
@Component
@AllArgsConstructor
public class GatewayReactiveAuthorizationManager
        implements ReactiveAuthorizationManager<AuthorizationContext> {

    private final RedisRepository redisRepository;
    private final SecurityMatcherConfigurer securityMatcherConfigurer;
    private final SecurityMetadataSourceStorage securityMetadataSourceStorage;

	@Override
	public Mono<AuthorizationResult> authorize(Mono<Authentication> authentication, AuthorizationContext authorizationContext) {
		return authentication
			.map(
				auth -> {
					final ServerWebExchange serverWebExchange =
						authorizationContext.getExchange();

					String url = serverWebExchange.getRequest().getPath().toString();
					String method = serverWebExchange.getRequest().getMethod().name();

					if (WebUtils.isPathMatch(
						securityMatcherConfigurer.getPermitAllList(), url)) {
						LogUtils.info("Is white list resource : [{}], Passed!", url);
						return new AuthorizationDecision(true);
					}

					if (WebUtils.isPathMatch(
						securityMatcherConfigurer.getHasAuthenticatedList(), url)) {
						LogUtils.info("Is has authenticated resource : [{}]", url);
						return new AuthorizationDecision(auth.isAuthenticated());
					}

					List<SecurityConfigAttribute> configAttributes =
						findConfigAttribute(url, method, serverWebExchange);
					if (CollectionUtils.isEmpty(configAttributes)) {
						LogUtils.info("NO PRIVILEGES : [{}].", url);

						if (!securityMatcherConfigurer
							.getAuthorizationProperties()
							.getStrict()) {
							if (auth
								instanceof
								AnonymousAuthenticationToken
									anonymousAuthenticationToken) {
								LogUtils.info("anonymousAuthenticationToken : {}", url);
								return new AuthorizationDecision(false);
							}

							if (auth.isAuthenticated()) {
								LogUtils.info("Request is authenticated: [{}].", url);
								return new AuthorizationDecision(true);
							}
						}

						return new AuthorizationDecision(false);
					}

					for (SecurityConfigAttribute configAttribute : configAttributes) {
						// WebExpressionAuthorizationManager
						// webExpressionAuthorizationManager =
						//	new
						// WebExpressionAuthorizationManager(configAttribute.getAttribute());
						// AuthorizationDecision decision =
						// webExpressionAuthorizationManager.check(auth,
						// authorizationContext);
						// if (decision.isGranted()) {
						//	//LogUtils.info("Request [{}] is authorized!",
						// object.getRequest().getRequestURI());
						//	return decision;
						// }
					}

					// return new AuthorizationDecision(false);

					if (auth instanceof JwtAuthenticationToken jwtAuthenticationToken) {
						Jwt jwt = jwtAuthenticationToken.getToken();
						String kid = (String) jwt.getHeaders().get("kid");

						// 判断kid是否存在 存在表示令牌不能使用 即:用户已退出
						Boolean hasKey =
							redisRepository.exists(
								RedisConstants.LOGOUT_JWT_KEY_PREFIX + kid);
						if (hasKey) {
							throw new InvalidTokenException("无效的token");
						}
					}

					ServerWebExchange exchange = authorizationContext.getExchange();
					ServerHttpRequest request = exchange.getRequest();

					// 可在此处鉴权也可在各个微服务鉴权
					// boolean isPermission = super.hasPermission(auth,
					// request.getMethodValue(),
					// request.getURI().getPath());

					return new AuthorizationDecision(true);
				})
			.defaultIfEmpty(new AuthorizationDecision(false));
	}

	/**
     * 找到配置属性
     *
     * @param url     url
     * @param method  方法
     * @param request 请求
     * @return {@link List }<{@link SecurityConfigAttribute }>
     * @since 2023-07-04 10:00:31
     */
    private List<SecurityConfigAttribute> findConfigAttribute(
            String url, String method, ServerWebExchange request) {
        LogUtils.info("Current Request is : [{}] - [{}]", url, method);

        List<SecurityConfigAttribute> configAttributes =
                this.securityMetadataSourceStorage.getConfigAttribute(url, method);
        if (CollectionUtils.isNotEmpty(configAttributes)) {
            LogUtils.info("Get configAttributes from local storage for : [{}] - [{}]", url, method);
            return configAttributes;
        } else {
            LinkedHashMap<SecurityRequest, List<SecurityConfigAttribute>> compatible =
                    this.securityMetadataSourceStorage.getCompatible();
            if (MapUtils.isNotEmpty(compatible)) {
                // 支持含有**通配符的路径搜索
                for (Map.Entry<SecurityRequest, List<SecurityConfigAttribute>> entry :
                        compatible.entrySet()) {
                    SecurityRequestMatcher requestMatcher =
                            new SecurityRequestMatcher(entry.getKey());

                    SecurityRequest securityRequest = new SecurityRequest();

                    // todo 需要修改
                    // if (requestMatcher.matches(request)) {
                    //	LogUtils.info("Request match the wildcard [{}] - [{}]", entry.getKey(),
                    // entry.getValue());
                    //	return entry.getValue();
                    // }
                }
            }
        }

        return null;
    }



}
