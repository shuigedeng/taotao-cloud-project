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

package com.taotao.cloud.auth.biz.demo.compliance.configuration;

import cn.herodotus.engine.oauth2.compliance.annotation.ConditionalOnAutoUnlockUserAccount;
import cn.herodotus.engine.oauth2.compliance.definition.AccountStatusChangeService;
import cn.herodotus.engine.oauth2.compliance.listener.AccountStatusListener;
import cn.herodotus.engine.oauth2.compliance.listener.AuthenticationFailureListener;
import cn.herodotus.engine.oauth2.compliance.listener.AuthenticationSuccessListener;
import cn.herodotus.engine.oauth2.compliance.service.OAuth2AccountStatusService;
import cn.herodotus.engine.oauth2.compliance.service.OAuth2ComplianceService;
import cn.herodotus.engine.oauth2.compliance.stamp.SignInFailureLimitedStampManager;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

/**
 * Description: OAuth2 应用安全合规配置
 *
 * @author : gengwei.zheng
 * @date : 2022/7/11 10:20
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(AccountStatusChangeService.class)
@EntityScan(basePackages = {"cn.herodotus.engine.oauth2.compliance.entity"})
@EnableJpaRepositories(
        basePackages = {
            "cn.herodotus.engine.oauth2.compliance.repository",
        })
@ComponentScan(
        basePackages = {
            "cn.herodotus.engine.oauth2.compliance.stamp",
            "cn.herodotus.engine.oauth2.compliance.service",
            "cn.herodotus.engine.oauth2.compliance.controller",
        })
public class OAuth2ComplianceConfiguration {

    private static final Logger log = LoggerFactory.getLogger(OAuth2ComplianceConfiguration.class);

    @PostConstruct
    public void postConstruct() {
        log.debug("[Herodotus] |- SDK [OAuth2 Compliance] Auto Configure.");
    }

    @Bean
    @ConditionalOnAutoUnlockUserAccount
    public AccountStatusListener accountLockStatusListener(
            RedisMessageListenerContainer redisMessageListenerContainer,
            OAuth2AccountStatusService accountLockService) {
        AccountStatusListener lockStatusListener =
                new AccountStatusListener(redisMessageListenerContainer, accountLockService);
        log.trace("[Herodotus] |- Bean [OAuth2 Account Lock Status Listener] Auto Configure.");
        return lockStatusListener;
    }

    @Bean
    @ConditionalOnMissingBean
    public AuthenticationFailureListener authenticationFailureListener(
            SignInFailureLimitedStampManager stampManager,
            OAuth2AccountStatusService accountLockService) {
        AuthenticationFailureListener authenticationFailureListener =
                new AuthenticationFailureListener(stampManager, accountLockService);
        log.trace("[Herodotus] |- Bean [OAuth2 Authentication Failure Listener] Auto Configure.");
        return authenticationFailureListener;
    }

    @Bean
    @ConditionalOnMissingBean
    public AuthenticationSuccessListener authenticationSuccessListener(
            SignInFailureLimitedStampManager stampManager,
            OAuth2ComplianceService complianceService) {
        AuthenticationSuccessListener authenticationSuccessListener =
                new AuthenticationSuccessListener(stampManager, complianceService);
        log.trace("[Herodotus] |- Bean [OAuth2 Authentication Success Listener] Auto Configure.");
        return authenticationSuccessListener;
    }
}
