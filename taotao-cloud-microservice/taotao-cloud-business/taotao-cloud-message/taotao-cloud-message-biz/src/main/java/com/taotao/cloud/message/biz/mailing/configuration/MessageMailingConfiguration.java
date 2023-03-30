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

package com.taotao.cloud.message.biz.mailing.configuration;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/** 消息互动 */
@Configuration(proxyBeanMethods = false)
@EntityScan(basePackages = {"com.taotao.cloud.message.mailing.entity"})
@EnableJpaRepositories(
        basePackages = {
            "com.taotao.cloud.message.biz.mailing.repository",
        })
@ComponentScan(
        basePackages = {
            "com.taotao.cloud.message.biz.mailing.service",
            "com.taotao.cloud.message.biz.mailing.controller",
        })
public class MessageMailingConfiguration {

    private static final Logger log = LoggerFactory.getLogger(MessageMailingConfiguration.class);

    @PostConstruct
    public void postConstruct() {
        log.debug("[Websocket] |- SDK [Message Mailing] Auto Configure.");
    }
}
