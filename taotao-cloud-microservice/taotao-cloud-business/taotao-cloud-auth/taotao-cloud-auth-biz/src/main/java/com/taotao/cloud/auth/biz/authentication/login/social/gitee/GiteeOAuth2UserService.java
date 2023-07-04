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

package com.taotao.cloud.auth.biz.authentication.login.social.gitee;

import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.oauth2.client.http.OAuth2ErrorResponseErrorHandler;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

/**
 * 获取微信用户信息的服务接口
 */
public class GiteeOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final RestOperations restOperations;

    public GiteeOAuth2UserService() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(new OAuth2ErrorResponseErrorHandler());
        this.restOperations = restTemplate;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
        ClientRegistration clientRegistration = oAuth2UserRequest.getClientRegistration();

        String userNameAttributeName =
                clientRegistration.getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        String accessToken = oAuth2UserRequest.getAccessToken().getTokenValue();

        Map<String, String> params = new HashMap<>();
        params.put("access_token", accessToken);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(
                "User-Agent",
                "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.100 Safari/537.36");

        HttpEntity<Object> entity = new HttpEntity<>(httpHeaders);

        String url = oAuth2UserRequest
                        .getClientRegistration()
                        .getProviderDetails()
                        .getUserInfoEndpoint()
                        .getUri() + "?access_token={access_token}";

        GiteeOAuth2User giteeOAuth2User = restOperations
                .exchange(url, HttpMethod.GET, entity, GiteeOAuth2User.class, params)
                .getBody();
        giteeOAuth2User.setNameAttributeKey(userNameAttributeName);
        return giteeOAuth2User;
    }
}
