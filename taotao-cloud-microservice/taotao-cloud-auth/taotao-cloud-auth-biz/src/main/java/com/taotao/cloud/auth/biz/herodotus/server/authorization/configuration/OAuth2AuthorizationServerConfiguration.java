/*
 * Copyright (c) 2020-2030 ZHENGGENGWEI(码匠君)<herodotus@aliyun.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Eurynome Cloud 采用APACHE LICENSE 2.0开源协议，您在使用过程中，需要注意以下几点：
 *
 * 1.请不要删除和修改根目录下的LICENSE文件。
 * 2.请不要删除和修改 Eurynome Cloud 源码头部的版权声明。
 * 3.请保留源码和相关描述文件的项目出处，作者声明等。
 * 4.分发源码时候，请注明软件出处 https://gitee.com/herodotus/eurynome-cloud
 * 5.在修改包名，模块名称，项目代码等时，请注明软件出处 https://gitee.com/herodotus/eurynome-cloud
 * 6.若您的项目无法满足以上几点，可申请商业授权
 */

package com.taotao.cloud.auth.biz.herodotus.server.authorization.configuration;

import cn.herodotus.engine.oauth2.authorization.properties.OAuth2UiProperties;
import cn.herodotus.engine.oauth2.core.properties.OAuth2Properties;
import cn.herodotus.engine.oauth2.data.jpa.configuration.OAuth2DataJpaConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.annotation.PostConstruct;

/**
 * <p>Description: OAuth2 Manager 模块配置 </p>
 *
 * @author : gengwei.zheng
 * @date : 2022/2/26 12:35
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties({OAuth2Properties.class, OAuth2UiProperties.class})
@Import({OAuth2DataJpaConfiguration.class})
@EntityScan(basePackages = {
        "cn.herodotus.engine.oauth2.server.authorization.entity"
})
@EnableJpaRepositories(basePackages = {
        "cn.herodotus.engine.oauth2.server.authorization.repository",
})
@ComponentScan(basePackages = {
        "cn.herodotus.engine.oauth2.server.authorization.service",
        "cn.herodotus.engine.oauth2.server.authorization.controller",
})
public class OAuth2AuthorizationServerConfiguration {

    private static final Logger log = LoggerFactory.getLogger(OAuth2AuthorizationServerConfiguration.class);

    @PostConstruct
    public void postConstruct() {
        log.debug("[Herodotus] |- SDK [Engine OAuth2 Authorization Server] Auto Configure.");
    }
}
