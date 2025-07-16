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

package com.taotao.cloud.auth.biz.management.service;

import com.taotao.cloud.auth.biz.jpa.repository.TtcRegisteredClientRepository;
import com.taotao.cloud.auth.biz.management.converter.OAuth2DeviceToRegisteredClientConverter;
import com.taotao.cloud.auth.biz.management.converter.RegisteredClientToOAuth2DeviceConverter;
import com.taotao.cloud.auth.biz.management.entity.OAuth2Device;
import com.taotao.cloud.auth.biz.management.entity.OAuth2Scope;
import com.taotao.cloud.auth.biz.management.repository.OAuth2DeviceRepository;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.oidc.OidcClientRegistration;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>OAuth2DeviceService </p>
 *
 *
 * @since : 2023/5/15 16:36
 */
@Service
public class OAuth2DeviceService {

    private static final Logger log = LoggerFactory.getLogger(OAuth2ApplicationService.class);

    private final RegisteredClientRepository registeredClientRepository;
    private final TtcRegisteredClientRepository ttcRegisteredClientRepository;
    private final OAuth2DeviceRepository deviceRepository;
    private final Converter<OAuth2Device, RegisteredClient> oauth2DeviceToRegisteredClientConverter;
    private final Converter<RegisteredClient, OAuth2Device> registeredClientToOAuth2DeviceConverter;

    public OAuth2DeviceService(
            RegisteredClientRepository registeredClientRepository,
            TtcRegisteredClientRepository ttcRegisteredClientRepository,
            OAuth2DeviceRepository deviceRepository,
            OAuth2ScopeService scopeService) {
        this.registeredClientRepository = registeredClientRepository;
        this.ttcRegisteredClientRepository = ttcRegisteredClientRepository;
        this.deviceRepository = deviceRepository;
        this.oauth2DeviceToRegisteredClientConverter =
                new OAuth2DeviceToRegisteredClientConverter();
        this.registeredClientToOAuth2DeviceConverter =
                new RegisteredClientToOAuth2DeviceConverter(scopeService);
    }

    @Transactional(rollbackFor = RuntimeException.class)
    public OAuth2Device saveAndFlush(OAuth2Device entity) {
        OAuth2Device device = deviceRepository.saveAndFlush(entity);
        if (ObjectUtils.isNotEmpty(device)) {
            registeredClientRepository.save(
                    oauth2DeviceToRegisteredClientConverter.convert(device));
            return device;
        } else {
            log.error("OAuth2DeviceService saveOrUpdate error, rollback data!");
            throw new NullPointerException("save or update OAuth2DeviceService failed");
        }
    }

    @Transactional(rollbackFor = RuntimeException.class)
    public void deleteById(String id) {
        deviceRepository.deleteById(id);
        ttcRegisteredClientRepository.deleteById(id);
    }

    @Transactional(rollbackFor = RuntimeException.class)
    public OAuth2Device authorize(String deviceId, String[] scopeIds) {

        Set<OAuth2Scope> scopes = new HashSet<>();
        for (String scopeId : scopeIds) {
            OAuth2Scope scope = new OAuth2Scope();
            scope.setScopeId(scopeId);
            scopes.add(scope);
        }

        OAuth2Device oldDevice = deviceRepository.findById(deviceId).get();
        oldDevice.setScopes(scopes);

        return saveAndFlush(oldDevice);
    }

    /**
     * 客户端自动注册是将信息存储在 oauth2_registered_client 中。
     * 为了方便管理，将该条数据同步至 oauth2_device 表中。
     *
     * @param oidcClientRegistration {@link OidcClientRegistration}
     * @return 是否同步成功
     */
    public boolean sync(OidcClientRegistration oidcClientRegistration) {
        RegisteredClient registeredClient =
                registeredClientRepository.findByClientId(oidcClientRegistration.getClientId());

        if (ObjectUtils.isNotEmpty(registeredClient)) {
            OAuth2Device oauth2Device =
                    registeredClientToOAuth2DeviceConverter.convert(registeredClient);
            if (ObjectUtils.isNotEmpty(oauth2Device)) {
                OAuth2Device result = deviceRepository.save(oauth2Device);
                return ObjectUtils.isNotEmpty(result);
            }
        }
        return false;
    }

    public boolean activate(String clientId, boolean isActivated) {
        int result = deviceRepository.activate(clientId, isActivated);
        return result != 0;
    }
}
