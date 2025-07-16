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

import com.taotao.cloud.auth.biz.strategy.local.LocalPermissionDetailsService;
import com.taotao.cloud.auth.biz.strategy.local.LocalUserDetailsService;
import com.taotao.cloud.auth.biz.strategy.local.SysPermissionService;
import com.taotao.cloud.auth.biz.strategy.local.SysUserService;
import com.taotao.cloud.auth.biz.strategy.remote.RemotePermissionDetailsService;
import com.taotao.cloud.auth.biz.strategy.remote.RemoteUserDetailsService;
import com.taotao.cloud.sys.api.feign.UserApi;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>分布式架构配置 </p>
 *
 * @author shuigedeng
 * @version 2023.07
 * @since 2023-07-04 11:43:44
 */
@Configuration(proxyBeanMethods = false)
public class DistributedArchitectureConfiguration {

    private static final Logger log =
            LoggerFactory.getLogger(DistributedArchitectureConfiguration.class);

    @PostConstruct
    public void postConstruct() {
        log.debug(" Module [Distributed Architecture] Auto Configure.");
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnProperty(
            prefix = "taotao.cloud.auth.local",
            name = "enabled",
            havingValue = "true")
    static class DataAccessStrategyLocalConfiguration {

        @Bean
        @ConditionalOnMissingBean
        public StrategyUserDetailsService localUserDetailsService(
                SysUserService sysUserService,
                SocialAuthenticationHandler socialAuthenticationHandler) {
            log.debug(" Strategy [Local User Details Service] Auto Configure.");
            return new LocalUserDetailsService(sysUserService, socialAuthenticationHandler);
        }

        @Bean
        @ConditionalOnMissingBean
        public StrategyPermissionDetailsService localPermissionDetailsService(
                SysPermissionService sysPermissionService) {
            LocalPermissionDetailsService localPermissionDetailsService =
                    new LocalPermissionDetailsService(sysPermissionService);
            log.debug(" Strategy [Local Permission Details Service] Auto Configure.");
            return localPermissionDetailsService;
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
        public StrategyUserDetailsService remoteUserDetailsService(UserApi userApi) {
            log.debug(" Strategy [Remote User Details Service] Auto Configure.");
            return new RemoteUserDetailsService(userApi);
        }

        @Bean
        @ConditionalOnMissingBean
        public StrategyPermissionDetailsService remotePermissionDetailsService() {
            RemotePermissionDetailsService remotePermissionDetailsService =
                    new RemotePermissionDetailsService();
            log.debug(" Strategy [Remote Permission Details Service] Auto Configure.");
            return remotePermissionDetailsService;
        }
    }
}
