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

package com.taotao.cloud.auth.application.login.social.github;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class GithubOAuth2UserService extends DefaultOAuth2UserService {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
        ClientRegistration clientRegistration = oAuth2UserRequest.getClientRegistration();
        String registrationId = clientRegistration.getRegistrationId();

        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);
        Map<String, Object> attributes = oAuth2User.getAttributes();

        String userNameAttributeName =
                clientRegistration.getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        GithubOAuth2User githubOAuth2User = objectMapper.convertValue(attributes, GithubOAuth2User.class);
        githubOAuth2User.setNameAttributeKey(userNameAttributeName);
        return githubOAuth2User;
    }
}
