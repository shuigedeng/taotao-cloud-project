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

package com.taotao.cloud.auth.biz.authentication.oauth2.weibo;

import jakarta.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.client.RestTemplate;

public class WeiboOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    @Resource private RestTemplate restTemplate;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest)
            throws OAuth2AuthenticationException {
        Map<String, Object> additionalParameters = oAuth2UserRequest.getAdditionalParameters();
        String uid = additionalParameters.get("uid").toString();

        String access_token = oAuth2UserRequest.getAccessToken().getTokenValue();

        Map<String, String> params = new HashMap<>();
        params.put("uid", uid);
        params.put("access_token", access_token);

        String baseUri =
                oAuth2UserRequest
                        .getClientRegistration()
                        .getProviderDetails()
                        .getUserInfoEndpoint()
                        .getUri();
        String userInfoUri = baseUri + "?uid={uid}" + "&access_token={access_token}";
        System.out.println(userInfoUri);

        return restTemplate.getForObject(userInfoUri, WeiboOAuth2User.class, params);
    }
}
