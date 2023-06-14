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

package com.taotao.cloud.auth.biz.authentication.authentication.oauth2.qq.other;

import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.web.client.RestTemplate;

public class QQOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, QQUserInfo> {
    // 获取用户信息的API
    private static final String QQ_URL_GET_USER_INFO =
            "https://graph.qq.com/user/get_user_info?oauth_consumer_key={appId}&openid={openId}&access_token={access_token}";

    private RestTemplate restTemplate;

    private RestTemplate getRestTemplate() {
        if (restTemplate == null) {
            restTemplate = new RestTemplate();
            // 通过Jackson JSON processing library直接将返回值绑定到对象
            restTemplate.getMessageConverters().add(new JacksonFromTextHtmlHttpMessageConverter());
        }

        return restTemplate;
    }

    @Override
    public QQUserInfo loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // 第一步：获取openId接口响应
        String accessToken = userRequest.getAccessToken().getTokenValue();
        String openIdUrl = userRequest
                        .getClientRegistration()
                        .getProviderDetails()
                        .getUserInfoEndpoint()
                        .getUri()
                + "?access_token={accessToken}";
        String result = getRestTemplate().getForObject(openIdUrl, String.class, accessToken);
        // 提取openId
        String openId = result.substring(result.lastIndexOf(":\"") + 2, result.indexOf("\"}"));

        // 第二步：获取用户信息
        String appId = userRequest.getClientRegistration().getClientId();
        QQUserInfo qqUserInfo =
                getRestTemplate().getForObject(QQ_URL_GET_USER_INFO, QQUserInfo.class, appId, openId, accessToken);
        // 为用户信息类补充openId
        if (qqUserInfo != null) {
            qqUserInfo.setOpenId(openId);
        }
        return qqUserInfo;
    }

    // @EnableWebSecurity(debug = true)
    // public class SecurityConfig extends WebSecurityConfigurerAdapter {
    //
    // 	public static final String QQRegistrationId = "qq";
    // 	public static final String WeChatRegistrationId = "wechat";
    //
    // 	public static final String LoginPagePath = "/login/oauth2";
    //
    // 	@Override
    // 	public void configure(HttpSecurity http) throws Exception {
    // 		http.authorizeRequests()
    // 			.antMatchers(LoginPagePath).permitAll()
    // 			.anyRequest()
    // 			.authenticated();
    // 		http.oauth2Login()
    // 			// 使用CompositeOAuth2AccessTokenResponseClient
    // 			.tokenEndpoint().accessTokenResponseClient(this.accessTokenResponseClient())
    // 			.and()
    // 			.userInfoEndpoint()
    // 			.customUserType(QQUserInfo.class, QQRegistrationId)
    // 			// 使用CompositeOAuth2UserService
    // 			.userService(oauth2UserService())
    // 			// 可选，要保证与redirect-uri-template匹配
    // 			.and()
    // 			.redirectionEndpoint().baseUri("/register/social/*");
    //
    // 		//自定义登录页
    // 		http.oauth2Login().loginPage(LoginPagePath);
    // 	}
    //
    // 	private OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest>
    // accessTokenResponseClient() {
    // 		CompositeOAuth2AccessTokenResponseClient client = new
    // CompositeOAuth2AccessTokenResponseClient();
    // 		// 加入QQ自定义QQOAuth2AccessTokenResponseClient
    // 		client.getOAuth2AccessTokenResponseClients().put(QQRegistrationId, new
    // QQOAuth2AccessTokenResponseClient());
    // 		return client;
    // 	}
    //
    // 	private OAuth2UserService<OAuth2UserRequest, OAuth2User> oauth2UserService() {
    // 		CompositeOAuth2UserService service = new CompositeOAuth2UserService();
    // 		// 加入QQ自定义QQOAuth2UserService
    // 		service.getUserServices().put(QQRegistrationId, new QQOAuth2UserService());
    // 		return service;
    // 	}
    // }

    // spring:
    //   security:
    //     oauth2:
    //       client:
    //         registration:
    //           github:
    //             client-id: {custom}
    //             client-secret: {custom}
    //             redirect-uri-template: "{baseUrl}/register/social/{registrationId}"
    //           qq:
    //             client-id: {custom appId}
    //             client-secret: {custom appKey}
    //             provider: qq
    //             client-name: QQ登录
    //             authorization-grant-type: authorization_code
    //             client-authentication-method: post
    //             scope: get_user_info,list_album,upload_pic,do_like
    //             redirect-uri-template: "{baseUrl}/register/social/{registrationId}"
    //         provider:
    //           qq:
    //             authorization-uri: https://graph.qq.com/oauth2.0/authorize
    //             token-uri: https://graph.qq.com/oauth2.0/token
    //             # 配置为QQ获取OpenId的Url
    //             user-info-uri: https://graph.qq.com/oauth2.0/me
    //             user-name-attribute: "nickname"
}
