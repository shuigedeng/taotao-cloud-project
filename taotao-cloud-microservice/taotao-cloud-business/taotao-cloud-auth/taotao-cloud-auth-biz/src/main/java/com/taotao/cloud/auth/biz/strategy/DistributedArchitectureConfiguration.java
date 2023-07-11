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

package com.taotao.cloud.auth.biz.strategy;

import com.taotao.cloud.auth.biz.authentication.login.oauth2.social.handler.SocialAuthenticationHandler;
import com.taotao.cloud.auth.biz.strategy.local.HerodotusLocalPermissionDetailsService;
import com.taotao.cloud.auth.biz.strategy.local.HerodotusLocalUserDetailsService;
import com.taotao.cloud.auth.biz.strategy.local.SysPermissionService;
import com.taotao.cloud.auth.biz.strategy.local.SysUserService;
import com.taotao.cloud.auth.biz.strategy.remote.HerodotusRemotePermissionDetailsService;
import com.taotao.cloud.auth.biz.strategy.remote.HerodotusRemoteUserDetailsService;
import com.taotao.cloud.sys.api.feign.IFeignUserApi;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>Description: 分布式架构配置 </p>
 *
 * @author shuigedeng
 * @version 2023.07
 * @since 2023-07-04 11:43:44
 */
@Configuration(proxyBeanMethods = false)
public class DistributedArchitectureConfiguration {

    private static final Logger log = LoggerFactory.getLogger(DistributedArchitectureConfiguration.class);

    @PostConstruct
    public void postConstruct() {
        log.debug("[Herodotus] |- Module [Distributed Architecture] Auto Configure.");
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnProperty(prefix = "taotao.cloud.auth.local", name = "enabled", havingValue = "true")
    static class DataAccessStrategyLocalConfiguration {

        @Bean
        @ConditionalOnMissingBean
        public StrategyUserDetailsService herodotusLocalUserDetailsService(
                SysUserService sysUserService, SocialAuthenticationHandler socialAuthenticationHandler) {
            log.debug("[Herodotus] |- Strategy [Local User Details Service] Auto Configure.");
            return new HerodotusLocalUserDetailsService(sysUserService, socialAuthenticationHandler);
        }

        @Bean
        @ConditionalOnMissingBean
        public StrategyPermissionDetailsService herodotusLocalPermissionDetailsService(
                SysPermissionService sysPermissionService) {
            HerodotusLocalPermissionDetailsService herodotusLocalPermissionDetailsService =
                    new HerodotusLocalPermissionDetailsService(sysPermissionService);
            log.debug("[Herodotus] |- Strategy [Local Permission Details Service] Auto Configure.");
            return herodotusLocalPermissionDetailsService;
        }
    }

    // 默认使用feign方式
    @Configuration(proxyBeanMethods = false)
    @ConditionalOnProperty(
            prefix = "taotao.cloud.auth.remote",
            name = "enabled",
            havingValue = "true",
            matchIfMissing = true)
    static class DataAccessStrategyRemoteConfiguration {

        @Bean
        @ConditionalOnMissingBean
        public StrategyUserDetailsService herodotusRemoteUserDetailsService(IFeignUserApi userApi) {
            log.debug("[Herodotus] |- Strategy [Remote User Details Service] Auto Configure.");
            return new HerodotusRemoteUserDetailsService(userApi);
        }

        @Bean
        @ConditionalOnMissingBean
        public StrategyPermissionDetailsService herodotusRemotePermissionDetailsService() {
            HerodotusRemotePermissionDetailsService herodotusRemotePermissionDetailsService =
                    new HerodotusRemotePermissionDetailsService();
            log.debug("[Herodotus] |- Strategy [Remote Permission Details Service] Auto Configure.");
            return herodotusRemotePermissionDetailsService;
        }
    }
}
