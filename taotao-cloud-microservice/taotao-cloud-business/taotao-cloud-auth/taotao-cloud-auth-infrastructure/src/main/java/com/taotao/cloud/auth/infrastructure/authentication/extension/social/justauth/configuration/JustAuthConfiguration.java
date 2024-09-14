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

package com.taotao.cloud.auth.infrastructure.authentication.extension.social.justauth.configuration;

import com.taotao.cloud.auth.infrastructure.authentication.extension.social.all.enums.AccountType;
import com.taotao.cloud.auth.infrastructure.authentication.extension.social.justauth.processor.JustAuthAccessHandler;
import com.taotao.cloud.auth.infrastructure.authentication.extension.social.justauth.processor.JustAuthProcessor;
import com.taotao.cloud.auth.infrastructure.authentication.extension.social.justauth.properties.JustAuthProperties;
import com.taotao.cloud.auth.infrastructure.authentication.extension.social.justauth.stamp.JustAuthStateStampManager;
import com.taotao.boot.cache.redis.repository.RedisRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>JustAuth配置 </p>
 * <p>
 * Ttc.platform.social.justauth.configs配置的情况下才注入
 *
 *
 * @since : 2021/5/22 11:25
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(JustAuthProperties.class)
public class JustAuthConfiguration {

    private static final Logger log = LoggerFactory.getLogger(JustAuthConfiguration.class);

    @PostConstruct
    public void init() {
        log.debug("SDK [Access Just Auth] Auto Configure.");
    }

    @Bean
    @ConditionalOnMissingBean
    public JustAuthStateStampManager justAuthStateStampManager(
            RedisRepository redisRepository, JustAuthProperties justAuthProperties) {
        JustAuthStateStampManager justAuthStateStampManager =
                new JustAuthStateStampManager(redisRepository, justAuthProperties);
        log.trace("Bean [Just Auth State Redis Cache] Auto Configure.");
        return justAuthStateStampManager;
    }

    @Bean
    @ConditionalOnBean(JustAuthStateStampManager.class)
    @ConditionalOnMissingBean
    public JustAuthProcessor justAuthProcessor(
            JustAuthStateStampManager justAuthStateStampManager, JustAuthProperties justAuthProperties) {
        JustAuthProcessor justAuthProcessor = new JustAuthProcessor();
        justAuthProcessor.setJustAuthStateRedisCache(justAuthStateStampManager);
        justAuthProcessor.setJustAuthProperties(justAuthProperties);
        log.trace("Bean [Just Auth Request Generator] Auto Configure.");
        return justAuthProcessor;
    }

    @Bean(AccountType.JUST_AUTH_HANDLER)
    @ConditionalOnBean(JustAuthProcessor.class)
    @ConditionalOnMissingBean
    public JustAuthAccessHandler justAuthAccessHandler(JustAuthProcessor justAuthProcessor) {
        JustAuthAccessHandler justAuthAccessHandler = new JustAuthAccessHandler(justAuthProcessor);
        log.debug("Bean [Just Auth Access Handler] Auto Configure.");
        return justAuthAccessHandler;
    }
}
