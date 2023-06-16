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

package com.taotao.cloud.auth.biz.authentication.authentication.oauth2;

import com.alibaba.fastjson2.JSONObject;
import com.taotao.cloud.auth.biz.authentication.authentication.oauth2.weibo.WeiboOAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationExchange;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.web.client.RestOperations;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class DelegateOAuth2AccessTokenResponseClient
        implements OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> {

    private final OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> delegate;
    private final RestOperations restOperations;

    public DelegateOAuth2AccessTokenResponseClient(
            OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> delegate,
            RestOperations restOperations) {
        this.delegate = delegate;

        this.restOperations = restOperations;
    }

    @Override
    public OAuth2AccessTokenResponse getTokenResponse(OAuth2AuthorizationCodeGrantRequest authorizationGrantRequest) {
        String registrationId =
                authorizationGrantRequest.getClientRegistration().getRegistrationId();

        if (ClientProviders.WORK_WECHAT_SCAN_CLIENT.registrationId().equals(registrationId)) {
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
          return new WeiboOAuth2AccessTokenResponseClient(restOperations)
			  .getTokenResponse(authorizationGrantRequest);
        }

        return delegate.getTokenResponse(authorizationGrantRequest);
    }
}
