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

package com.taotao.cloud.auth.biz.authentication.configuration;

import com.taotao.cloud.auth.biz.authentication.login.form.OAuth2FormLoginUrlConfigurer;
import com.taotao.cloud.auth.biz.authentication.login.social.SocialDelegateClientRegistrationRepository;
import com.taotao.cloud.auth.biz.authentication.processor.AESCryptoProcessor;
import com.taotao.cloud.auth.biz.authentication.processor.HttpCryptoProcessor;
import com.taotao.cloud.auth.biz.authentication.processor.RSACryptoProcessor;
import com.taotao.cloud.auth.biz.authentication.properties.OAuth2AuthenticationProperties;
import com.taotao.cloud.auth.biz.authentication.stamp.LockedUserDetailsStampManager;
import com.taotao.cloud.auth.biz.authentication.stamp.SignInFailureLimitedStampManager;
import com.taotao.cloud.cache.redis.repository.RedisRepository;
import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientPropertiesMapper;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.registration.ClientRegistration;

/**
 * <p>Description: OAuth2 认证基础模块配置 </p>
 *
 * @author shuigedeng
 * @version 2023.07
 * @since 2023-07-04 10:31:46
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties({OAuth2AuthenticationProperties.class})
public class OAuth2AuthenticationConfiguration {

    private static final Logger log = LoggerFactory.getLogger(OAuth2AuthenticationConfiguration.class);

    @PostConstruct
    public void postConstruct() {
        log.info("SDK [OAuth2 Authentication] Auto Configure.");
    }

    @Autowired
    private RedisRepository redisRepository;

    @Bean
    public SocialDelegateClientRegistrationRepository delegateClientRegistrationRepository(
            OAuth2ClientProperties properties) {
        SocialDelegateClientRegistrationRepository clientRegistrationRepository =
                new SocialDelegateClientRegistrationRepository();
        if (properties != null) {
            Map<String, ClientRegistration> clientRegistrations =
                    new OAuth2ClientPropertiesMapper(properties).asClientRegistrations();
            List<ClientRegistration> registrations = new ArrayList<>(clientRegistrations.values());
            registrations.forEach(clientRegistrationRepository::addClientRegistration);
        }
        return clientRegistrationRepository;
    }

    @Bean
    public HttpCryptoProcessor httpCryptoProcessor() {
        return new HttpCryptoProcessor(redisRepository, new RSACryptoProcessor(), new AESCryptoProcessor());
    }

    @Bean
    public LockedUserDetailsStampManager lockedUserDetailsStampManager(
            OAuth2AuthenticationProperties authenticationProperties) {
        LockedUserDetailsStampManager manager =
                new LockedUserDetailsStampManager(redisRepository, authenticationProperties);
        log.info("Bean [Locked UserDetails Stamp Manager] Auto Configure.");
        return manager;
    }

    @Bean
    public SignInFailureLimitedStampManager signInFailureLimitedStampManager(
            OAuth2AuthenticationProperties authenticationProperties) {
        SignInFailureLimitedStampManager manager =
                new SignInFailureLimitedStampManager(redisRepository, authenticationProperties);
        log.info("Bean [SignIn Failure Limited Stamp Manager] Auto Configure.");
        return manager;
    }

    @Bean
    public OAuth2FormLoginUrlConfigurer auth2FormLoginParameterConfigurer(
            OAuth2AuthenticationProperties authenticationProperties) {
        OAuth2FormLoginUrlConfigurer configurer = new OAuth2FormLoginUrlConfigurer(authenticationProperties);
        log.info("Bean [OAuth2 FormLogin Parameter Configurer] Auto Configure.");
        return configurer;
    }
}
