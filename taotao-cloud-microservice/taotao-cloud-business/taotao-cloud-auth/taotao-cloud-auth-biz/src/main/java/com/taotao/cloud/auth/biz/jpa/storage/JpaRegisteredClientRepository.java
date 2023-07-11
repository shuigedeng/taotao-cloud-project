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

package com.taotao.cloud.auth.biz.jpa.storage;

import com.taotao.cloud.auth.biz.jpa.converter.HerodotusToOAuth2RegisteredClientConverter;
import com.taotao.cloud.auth.biz.jpa.converter.OAuth2ToHerodotusRegisteredClientConverter;
import com.taotao.cloud.auth.biz.jpa.entity.HerodotusRegisteredClient;
import com.taotao.cloud.auth.biz.jpa.jackson2.OAuth2JacksonProcessor;
import com.taotao.cloud.auth.biz.jpa.service.HerodotusRegisteredClientService;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;

/**
 * <p>Description: 基于Jpa 的 RegisteredClient服务 </p>
 *
 * @author shuigedeng
 * @version 2023.07
 * @since 2023-07-10 17:10:47
 */
public class JpaRegisteredClientRepository implements RegisteredClientRepository {

    /**
     * 日志
     */
    private static final Logger log = LoggerFactory.getLogger(JpaRegisteredClientRepository.class);

    /**
     * 希罗多德注册客户服务
     */
    private final HerodotusRegisteredClientService herodotusRegisteredClientService;
    /**
     * 希罗多德到oauth2转换器
     */
    private final Converter<HerodotusRegisteredClient, RegisteredClient> herodotusToOAuth2Converter;
    /**
     * oauth2到希罗多德转换器
     */
    private final Converter<RegisteredClient, HerodotusRegisteredClient> oauth2ToHerodotusConverter;

    /**
     * jpa注册客户端存储库
     *
     * @param herodotusRegisteredClientService 希罗多德注册客户服务
     * @param passwordEncoder                  密码编码器
     * @return
     * @since 2023-07-10 17:10:47
     */
    public JpaRegisteredClientRepository(
            HerodotusRegisteredClientService herodotusRegisteredClientService, PasswordEncoder passwordEncoder) {
        this.herodotusRegisteredClientService = herodotusRegisteredClientService;
        OAuth2JacksonProcessor jacksonProcessor = new OAuth2JacksonProcessor();
        this.herodotusToOAuth2Converter = new HerodotusToOAuth2RegisteredClientConverter(jacksonProcessor);
        this.oauth2ToHerodotusConverter =
                new OAuth2ToHerodotusRegisteredClientConverter(jacksonProcessor, passwordEncoder);
    }

    /**
     * 保存
     *
     * @param registeredClient 注册客户
     * @since 2023-07-10 17:10:48
     */
    @Override
    public void save(RegisteredClient registeredClient) {
        log.info("Jpa Registered Client Repository save entity.");
        this.herodotusRegisteredClientService.save(toEntity(registeredClient));
    }

    /**
     * 按id查找
     *
     * @param id id
     * @return {@link RegisteredClient }
     * @since 2023-07-10 17:10:48
     */
    @Override
    public RegisteredClient findById(String id) {
        log.info("Jpa Registered Client Repository findById.");
        HerodotusRegisteredClient herodotusRegisteredClient = this.herodotusRegisteredClientService.findById(id);
        if (ObjectUtils.isNotEmpty(herodotusRegisteredClient)) {
            return toObject(herodotusRegisteredClient);
        }
        return null;
    }

    /**
     * 按客户id查找
     *
     * @param clientId 客户端id
     * @return {@link RegisteredClient }
     * @since 2023-07-10 17:10:48
     */
    @Override
    public RegisteredClient findByClientId(String clientId) {
        log.info("Jpa Registered Client Repository findByClientId.");
        return this.herodotusRegisteredClientService
                .findByClientId(clientId)
                .map(this::toObject)
                .orElse(null);
    }

    /**
     * 移除
     *
     * @param id id
     * @since 2023-07-10 17:10:48
     */
    public void remove(String id) {
        log.info("Jpa Registered Client Repository remove.");
        this.herodotusRegisteredClientService.deleteById(id);
    }

    /**
     * 反对
     *
     * @param herodotusRegisteredClient 希罗多德注册客户
     * @return {@link RegisteredClient }
     * @since 2023-07-10 17:10:48
     */
    private RegisteredClient toObject(HerodotusRegisteredClient herodotusRegisteredClient) {
        return herodotusToOAuth2Converter.convert(herodotusRegisteredClient);
    }

    /**
     * 对实体
     *
     * @param registeredClient 注册客户
     * @return {@link HerodotusRegisteredClient }
     * @since 2023-07-10 17:10:48
     */
    private HerodotusRegisteredClient toEntity(RegisteredClient registeredClient) {
        return oauth2ToHerodotusConverter.convert(registeredClient);
    }
}
