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

package com.taotao.cloud.auth.biz.uaa.configuration;

import com.taotao.boot.security.spring.authentication.login.social.oauth2client.SocialDelegateMapOAuth2AccessTokenResponseConverter;
import com.taotao.boot.security.spring.authentication.login.social.oauth2client.SocialDelegateOAuth2RefreshTokenRequestEntityConverter;
import java.util.Arrays;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProviderBuilder;
import org.springframework.security.oauth2.client.endpoint.DefaultRefreshTokenTokenResponseClient;
import org.springframework.security.oauth2.client.http.OAuth2ErrorResponseErrorHandler;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.core.http.converter.OAuth2AccessTokenResponseHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

/**
 * 兼容微信刷新token
 *
 * @author shuigedeng
 * @version 2023.07
 * @since 2023-07-10 11:43:29
 */
@Configuration(proxyBeanMethods = false)
public class OAuth2ClientManagerConfiguration {

    /**
     * O auth 2 authorized client manager o auth 2 authorized client manager.
     *
     * @param clientRegistrationRepository the client registration repository
     * @param authorizedClientRepository   the authorized client repository
     * @return the o auth 2 authorized client manager
     */
    @Bean
    OAuth2AuthorizedClientManager oAuth2AuthorizedClientManager(
            ClientRegistrationRepository clientRegistrationRepository,
            OAuth2AuthorizedClientRepository authorizedClientRepository) {
        DefaultOAuth2AuthorizedClientManager authorizedClientManager =
                new DefaultOAuth2AuthorizedClientManager(
                        clientRegistrationRepository, authorizedClientRepository);
        DefaultRefreshTokenTokenResponseClient defaultRefreshTokenTokenResponseClient =
                new DefaultRefreshTokenTokenResponseClient();

        defaultRefreshTokenTokenResponseClient.setRequestEntityConverter(
                new SocialDelegateOAuth2RefreshTokenRequestEntityConverter());
        OAuth2AccessTokenResponseHttpMessageConverter messageConverter =
                new OAuth2AccessTokenResponseHttpMessageConverter();
        // 微信返回的content-type 是 text-plain
        messageConverter.setSupportedMediaTypes(
                Arrays.asList(
                        MediaType.APPLICATION_JSON,
                        MediaType.TEXT_PLAIN,
                        new MediaType("application", "*+json")));

        // 兼容微信解析
        messageConverter.setAccessTokenResponseConverter(
                new SocialDelegateMapOAuth2AccessTokenResponseConverter());

        RestTemplate restTemplate =
                new RestTemplate(Arrays.asList(new FormHttpMessageConverter(), messageConverter));

        restTemplate.setErrorHandler(new OAuth2ErrorResponseErrorHandler());
        defaultRefreshTokenTokenResponseClient.setRestOperations(restTemplate);

        authorizedClientManager.setAuthorizedClientProvider(
                OAuth2AuthorizedClientProviderBuilder.builder()
                        .authorizationCode()
                        .refreshToken(
                                (refreshTokenGrantBuilder) -> {
                                    refreshTokenGrantBuilder.accessTokenResponseClient(
                                            defaultRefreshTokenTokenResponseClient);
                                })
                        .clientCredentials()
                        .build());
        return authorizedClientManager;
    }
}
