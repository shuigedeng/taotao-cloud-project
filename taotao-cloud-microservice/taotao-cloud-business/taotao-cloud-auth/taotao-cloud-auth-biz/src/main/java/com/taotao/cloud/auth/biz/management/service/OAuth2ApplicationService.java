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
import com.taotao.cloud.auth.biz.management.converter.OAuth2ApplicationToRegisteredClientConverter;
import com.taotao.cloud.auth.biz.management.entity.OAuth2Application;
import com.taotao.cloud.auth.biz.management.entity.OAuth2Scope;
import com.taotao.cloud.auth.biz.management.repository.OAuth2ApplicationRepository;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>OAuth2ApplicationService </p>
 *
 *
 * @since : 2022/3/1 18:06
 */
@Service
public class OAuth2ApplicationService {

    private static final Logger log = LoggerFactory.getLogger(OAuth2ApplicationService.class);

    private final RegisteredClientRepository registeredClientRepository;
    private final TtcRegisteredClientRepository ttcRegisteredClientRepository;
    private final OAuth2ApplicationRepository applicationRepository;
    private final Converter<OAuth2Application, RegisteredClient> objectConverter;

    public OAuth2ApplicationService(
            RegisteredClientRepository registeredClientRepository,
            TtcRegisteredClientRepository ttcRegisteredClientRepository,
            OAuth2ApplicationRepository applicationRepository) {
        this.registeredClientRepository = registeredClientRepository;
        this.ttcRegisteredClientRepository = ttcRegisteredClientRepository;
        this.applicationRepository = applicationRepository;
        this.objectConverter = new OAuth2ApplicationToRegisteredClientConverter();
    }

    public OAuth2Application saveAndFlush(OAuth2Application entity) {
        OAuth2Application application = applicationRepository.saveAndFlush(entity);
        if (ObjectUtils.isNotEmpty(application)) {
            registeredClientRepository.save(objectConverter.convert(application));
            log.info("OAuth2ApplicationService saveOrUpdate.");
            return application;
        } else {
            log.error("OAuth2ApplicationService saveOrUpdate error, rollback data!");
            throw new NullPointerException("save or update OAuth2Application failed");
        }
    }

    @Transactional(rollbackFor = RuntimeException.class)
    public void deleteById(String id) {
        applicationRepository.deleteById(id);
        ttcRegisteredClientRepository.deleteById(id);
    }

    @Transactional(rollbackFor = RuntimeException.class)
    public OAuth2Application authorize(String applicationId, String[] scopeIds) {

        Set<OAuth2Scope> scopes = new HashSet<>();
        for (String scopeId : scopeIds) {
            OAuth2Scope scope = new OAuth2Scope();
            scope.setScopeId(scopeId);
            scopes.add(scope);
        }

        OAuth2Application oldApplication = applicationRepository.findById(applicationId).get();
        oldApplication.setScopes(scopes);

        return saveAndFlush(oldApplication);
    }

    public OAuth2Application findByClientId(String clientId) {
        return applicationRepository.findByClientId(clientId);
    }
}
