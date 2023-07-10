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

package com.taotao.cloud.auth.biz.authentication.login.social;

import com.taotao.cloud.auth.biz.authentication.login.social.weibo.WeiboOAuth2AccessTokenResponseClient;
import java.util.Collections;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.web.client.RestOperations;

/**
 * 社交委托oauth2访问令牌响应客户端
 *
 * @author shuigedeng
 * @version 2023.07
 * @see OAuth2AccessTokenResponseClient
 * @since 2023-07-10 17:40:54
 */
public class SocialDelegateOAuth2AccessTokenResponseClient
        implements OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> {

	/**
	 * 委托
	 */
	private final OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> delegate;
	/**
	 * 休息操作
	 */
	private final RestOperations restOperations;

	/**
	 * 社交委托oauth2访问令牌响应客户端
	 *
	 * @param delegate       委托
	 * @param restOperations 休息操作
	 * @return
	 * @since 2023-07-10 17:40:54
	 */
	public SocialDelegateOAuth2AccessTokenResponseClient(
            OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> delegate,
            RestOperations restOperations) {
        this.delegate = delegate;

        this.restOperations = restOperations;
    }

	/**
	 * 获取令牌响应
	 *
	 * @param authorizationGrantRequest 授权授予请求
	 * @return {@link OAuth2AccessTokenResponse }
	 * @since 2023-07-10 17:40:55
	 */
	@Override
    public OAuth2AccessTokenResponse getTokenResponse(OAuth2AuthorizationCodeGrantRequest authorizationGrantRequest) {
        String registrationId =
                authorizationGrantRequest.getClientRegistration().getRegistrationId();

        if (SocialClientProviders.WORK_WECHAT_SCAN_CLIENT.registrationId().equals(registrationId)) {
            // todo 缓存获取token 如果获取不到再请求 并放入缓存  企业微信的token不允许频繁获取
            OAuth2AccessTokenResponse tokenResponse = delegate.getTokenResponse(authorizationGrantRequest);
            String code = authorizationGrantRequest
                    .getAuthorizationExchange()
                    .getAuthorizationResponse()
                    .getCode();

            return OAuth2AccessTokenResponse.withResponse(tokenResponse)
                    .additionalParameters(Collections.singletonMap(OAuth2ParameterNames.CODE, code))
                    .build();
        }

        if ("weibo".equals(registrationId)) {
            return new WeiboOAuth2AccessTokenResponseClient(restOperations).getTokenResponse(authorizationGrantRequest);
        }

        return delegate.getTokenResponse(authorizationGrantRequest);
    }
}
