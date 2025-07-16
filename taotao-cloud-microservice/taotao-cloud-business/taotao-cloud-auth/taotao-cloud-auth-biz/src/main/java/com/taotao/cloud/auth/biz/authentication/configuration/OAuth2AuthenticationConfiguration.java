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

import com.taotao.boot.cache.redis.repository.RedisRepository;
import com.taotao.boot.security.spring.authentication.login.social.oauth2client.SocialDelegateClientRegistrationRepository;
import com.taotao.cloud.auth.biz.authentication.processor.AESCryptoProcessor;
import com.taotao.cloud.auth.biz.authentication.processor.HttpCryptoProcessor;
import com.taotao.cloud.auth.biz.authentication.processor.RSACryptoProcessor;
import com.taotao.cloud.auth.biz.authentication.properties.OAuth2AuthenticationProperties;
import com.taotao.cloud.auth.biz.authentication.stamp.LockedUserDetailsStampManager;
import com.taotao.cloud.auth.biz.authentication.stamp.SignInFailureLimitedStampManager;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientPropertiesMapper;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.registration.ClientRegistration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>OAuth2 认证基础模块配置 </p>
 *
 * @author shuigedeng
 * @version 2023.07
 * @since 2023-07-10 17:14:18
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties({OAuth2AuthenticationProperties.class})
public class OAuth2AuthenticationConfiguration {

    /**
     * 日志
     */
    private static final Logger log = LoggerFactory.getLogger(
        OAuth2AuthenticationConfiguration.class);

    /**
     * 后期构建
     *
     * @since 2023-07-10 17:14:18
     */
    @PostConstruct
    public void postConstruct() {
        log.info("SDK [OAuth2 Authentication] Auto Configure.");
    }

    /**
     * redis存储库
     */
    @Autowired
    private RedisRepository redisRepository;

    /**
     * 委托客户注册存储库
     *
     * @param properties 属性
     * @return {@link SocialDelegateClientRegistrationRepository }
     * @since 2023-07-10 17:14:19
     */
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

    /**
     * http加密处理器
     *
     * @return {@link HttpCryptoProcessor }
     * @since 2023-07-10 17:14:19
     */
    @Bean
    public HttpCryptoProcessor httpCryptoProcessor() {
        return new HttpCryptoProcessor(redisRepository, new RSACryptoProcessor(),
            new AESCryptoProcessor());
    }

    /**
     * 锁定用户详细信息邮票管理器
     *
     * @param authenticationProperties 身份验证属性
     * @return {@link LockedUserDetailsStampManager }
     * @since 2023-07-10 17:14:20
     */
    @Bean
    public LockedUserDetailsStampManager lockedUserDetailsStampManager(
        OAuth2AuthenticationProperties authenticationProperties) {
        LockedUserDetailsStampManager manager =
            new LockedUserDetailsStampManager(redisRepository, authenticationProperties);
        log.info("Bean [Locked UserDetails Stamp Manager] Auto Configure.");
        return manager;
    }

    /**
     * 登录失败有限邮票经理
     *
     * @param authenticationProperties 身份验证属性
     * @return {@link SignInFailureLimitedStampManager }
     * @since 2023-07-10 17:14:20
     */
    @Bean
    public SignInFailureLimitedStampManager signInFailureLimitedStampManager(
        OAuth2AuthenticationProperties authenticationProperties) {
        SignInFailureLimitedStampManager manager =
            new SignInFailureLimitedStampManager(redisRepository, authenticationProperties);
        log.info("Bean [SignIn Failure Limited Stamp Manager] Auto Configure.");
        return manager;
    }

    /**
     * auth2表单登录参数配置器
     *
     * @param authenticationProperties 身份验证属性
     * @return {@link OAuth2FormLoginUrlConfigurer }
     * @since 2023-07-10 17:14:21
     */
    @Bean
    public OAuth2FormLoginUrlConfigurer auth2FormLoginParameterConfigurer(
        OAuth2AuthenticationProperties authenticationProperties) {z
        OAuth2FormLoginUrlConfigurer configurer = new OAuth2FormLoginUrlConfigurer(
            authenticationProperties);
        log.info("Bean [OAuth2 FormLogin Parameter Configurer] Auto Configure.");
        return configurer;
    }
}
