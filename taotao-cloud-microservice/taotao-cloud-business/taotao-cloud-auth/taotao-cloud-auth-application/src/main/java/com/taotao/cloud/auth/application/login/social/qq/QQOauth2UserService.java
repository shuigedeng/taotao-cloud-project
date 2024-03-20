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

package com.taotao.cloud.auth.application.login.social.qq;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.cloud.common.utils.log.LogUtils;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.http.OAuth2ErrorResponseErrorHandler;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

public class QQOauth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private static final String QQ_OPEN_ID_URL = "https://graph.qq.com/oauth2.0/me";

    private final RestOperations restOperations;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public QQOauth2UserService() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(new OAuth2ErrorResponseErrorHandler());
        this.restOperations = restTemplate;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        ClientRegistration clientRegistration = userRequest.getClientRegistration();
        String registrationId = clientRegistration.getRegistrationId();

        String tokenValue = userRequest.getAccessToken().getTokenValue();
        // openId请求
        RequestEntity<?> openIdRequest = RequestEntity.get(UriComponentsBuilder.fromUriString(QQ_OPEN_ID_URL)
                        .queryParam("access_token", tokenValue)
                        .build()
                        .toUri())
                .build();

        // openId响应
        ResponseEntity<String> openIdResponse =
                restOperations.exchange(openIdRequest, new ParameterizedTypeReference<String>() {});

        LogUtils.info("qq的openId响应信息：{}", openIdResponse);

        // openId响应是类似callback( {"client_id":"YOUR_APPID","openid":"YOUR_OPENID"} );这样的字符串
        String openId = null;
        try {
            openId = extractQqOpenId(Objects.requireNonNull(openIdResponse.getBody()));
        } catch (JsonProcessingException e) {
            LogUtils.error(e);
        }

        // userInfo请求
        RequestEntity<?> userInfoRequest = RequestEntity.get(UriComponentsBuilder.fromUriString(clientRegistration
                                .getProviderDetails()
                                .getUserInfoEndpoint()
                                .getUri())
                        .queryParam("access_token", tokenValue)
                        .queryParam("openid", openId)
                        .queryParam("oauth_consumer_key", clientRegistration.getClientId())
                        .build()
                        .toUri())
                .build();

        // userInfo响应
        ResponseEntity<String> userInfoResponse =
                restOperations.exchange(userInfoRequest, new ParameterizedTypeReference<String>() {});

        LogUtils.info("qq的userInfo响应信息：{}", userInfoResponse);

        String userNameAttributeName =
                clientRegistration.getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();
        Map<String, Object> userAttributes = null;
        try {
            userAttributes = extractQqUserInfo(Objects.requireNonNull(userInfoResponse.getBody()));
        } catch (JsonProcessingException e) {
            LogUtils.error(e);
        }
        Set<GrantedAuthority> authorities = new LinkedHashSet<>();
        authorities.add(new OAuth2UserAuthority(userAttributes));
        OAuth2AccessToken token = userRequest.getAccessToken();
        for (String authority : token.getScopes()) {
            authorities.add(new SimpleGrantedAuthority("SCOPE_" + authority));
        }

        QQOAuth2User qqoAuth2User = objectMapper.convertValue(userAttributes, QQOAuth2User.class);
        qqoAuth2User.setAttributes(userAttributes);
        qqoAuth2User.setAuthorities(authorities);
        qqoAuth2User.setNameAttributeKey(userNameAttributeName);

        //		return new DefaultOAuth2User(authorities, userAttributes, userNameAttributeName);
        return qqoAuth2User;
    }

    /**
     * 提取qq的openId
     *
     * @param openIdResponse qq的openId响应字符串
     * @return qq的openId
     */
    private String extractQqOpenId(String openIdResponse) throws JsonProcessingException {
        String openId = openIdResponse.substring(openIdResponse.indexOf('(') + 1, openIdResponse.indexOf(')'));
        Map<String, String> map = objectMapper.readValue(openId, new TypeReference<>() {});
        return map.get("openid");
    }

    /**
     * 提取qq的用户信息
     *
     * @param userInfoResponse qq的用户信息响应字符串
     * @return qq的用户信息
     */
    private Map<String, Object> extractQqUserInfo(String userInfoResponse) throws JsonProcessingException {
        return objectMapper.readValue(userInfoResponse, new TypeReference<>() {});
    }
}
