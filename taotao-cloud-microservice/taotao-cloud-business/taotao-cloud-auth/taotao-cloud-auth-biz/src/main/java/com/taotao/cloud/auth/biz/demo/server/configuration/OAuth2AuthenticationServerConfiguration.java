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

package com.taotao.cloud.auth.biz.demo.server.configuration;

import cn.herodotus.engine.oauth2.authentication.properties.OAuth2UiProperties;
import cn.herodotus.engine.oauth2.core.properties.OAuth2ComplianceProperties;
import cn.herodotus.engine.oauth2.core.properties.OAuth2Properties;
import cn.herodotus.engine.oauth2.data.jpa.configuration.OAuth2DataJpaConfiguration;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Description: OAuth2 Manager 模块配置 {@link
 * org.springframework.security.oauth2.jwt.JwtTimestampValidator}
 *
 * @author : gengwei.zheng
 * @date : 2022/2/26 12:35
 */
@AutoConfiguration
@EnableConfigurationProperties({OAuth2Properties.class, OAuth2UiProperties.class, OAuth2ComplianceProperties.class})
@Import({OAuth2DataJpaConfiguration.class})
@EntityScan(basePackages = {"cn.herodotus.engine.oauth2.server.authentication.entity"})
@EnableJpaRepositories(
        basePackages = {
            "cn.herodotus.engine.oauth2.server.authentication.repository",
        })
@ComponentScan(
        basePackages = {
            "cn.herodotus.engine.oauth2.server.authentication.service",
            "cn.herodotus.engine.oauth2.server.authentication.controller",
        })
public class OAuth2AuthenticationServerConfiguration {

    private static final Logger log = LoggerFactory.getLogger(OAuth2AuthenticationServerConfiguration.class);

    @PostConstruct
    public void postConstruct() {
        log.debug("[Herodotus] |- SDK [OAuth2 Authorization Server] Auto Configure.");
    }
}
