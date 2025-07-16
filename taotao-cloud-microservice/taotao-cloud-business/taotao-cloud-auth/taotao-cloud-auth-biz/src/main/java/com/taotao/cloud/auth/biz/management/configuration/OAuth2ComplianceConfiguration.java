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

package com.taotao.cloud.auth.biz.management.configuration;

import com.taotao.cloud.auth.biz.authentication.stamp.LockedUserDetailsStampManager;
import com.taotao.cloud.auth.biz.authentication.stamp.SignInFailureLimitedStampManager;
import com.taotao.cloud.auth.biz.management.compliance.OAuth2AccountStatusManager;
import com.taotao.cloud.auth.biz.management.compliance.listener.AccountAutoEnableListener;
import com.taotao.cloud.auth.biz.management.compliance.listener.AuthenticationFailureListener;
import com.taotao.cloud.auth.biz.management.compliance.processor.changer.AccountStatusChanger;
import com.taotao.cloud.auth.biz.management.compliance.processor.changer.TtcAccountStatusChanger;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * <p>OAuth2 应用安全合规配置 </p>
 *
 * @author shuigedeng
 * @version 2023.07
 * @since 2023-07-04 11:41:27
 */
@Configuration(proxyBeanMethods = false)
public class OAuth2ComplianceConfiguration {

    private static final Logger log = LoggerFactory.getLogger(OAuth2ComplianceConfiguration.class);

    @PostConstruct
    public void postConstruct() {
        log.info("SDK [OAuth2 Compliance] Auto Configure.");
    }

    @Bean
    public AccountStatusChanger accountStatusChanger() {
        TtcAccountStatusChanger ttcAccountStatusChanger = new TtcAccountStatusChanger();
        log.info("Bean [Account Status Changer] Auto Configure.");
        return ttcAccountStatusChanger;
    }

    @Bean
    public OAuth2AccountStatusManager accountStatusManager(
            UserDetailsService userDetailsService,
            AccountStatusChanger accountStatusChanger,
            LockedUserDetailsStampManager lockedUserDetailsStampManager) {
        OAuth2AccountStatusManager manager =
                new OAuth2AccountStatusManager(
                        userDetailsService, accountStatusChanger, lockedUserDetailsStampManager);
        log.info("Bean [OAuth2 Account Status Manager] Auto Configure.");
        return manager;
    }

    @Bean
    public AccountAutoEnableListener accountLockStatusListener(
            RedisMessageListenerContainer redisMessageListenerContainer,
            OAuth2AccountStatusManager accountStatusManager) {
        AccountAutoEnableListener listener =
                new AccountAutoEnableListener(redisMessageListenerContainer, accountStatusManager);
        log.info("Bean [OAuth2 Account Lock Status Listener] Auto Configure.");
        return listener;
    }

    @Bean
    @ConditionalOnMissingBean
    public AuthenticationFailureListener authenticationFailureListener(
            SignInFailureLimitedStampManager stampManager,
            OAuth2AccountStatusManager accountLockService) {
        AuthenticationFailureListener listener =
                new AuthenticationFailureListener(stampManager, accountLockService);
        log.info("Bean [OAuth2 Authentication Failure Listener] Auto Configure.");
        return listener;
    }
}
