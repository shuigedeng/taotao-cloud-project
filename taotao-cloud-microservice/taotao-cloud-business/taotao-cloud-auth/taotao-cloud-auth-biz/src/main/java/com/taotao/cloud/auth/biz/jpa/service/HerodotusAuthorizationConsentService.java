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

package com.taotao.cloud.auth.biz.jpa.service;

import com.taotao.cloud.auth.biz.jpa.entity.HerodotusAuthorizationConsent;
import com.taotao.cloud.auth.biz.jpa.repository.HerodotusAuthorizationConsentRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>Description: HerodotusAuthorizationConsentService </p>
 * <p>
 * 这里命名没有按照统一的习惯，主要是为了防止与 spring-authorization-server 已有类的同名而导致Bean注入失败
 *
 *
 */
@Service
public class HerodotusAuthorizationConsentService {

    private static final Logger log = LoggerFactory.getLogger(HerodotusAuthorizationConsentService.class);

    private final HerodotusAuthorizationConsentRepository authorizationConsentRepository;

    @Autowired
    public HerodotusAuthorizationConsentService(
            HerodotusAuthorizationConsentRepository authorizationConsentRepository) {
        this.authorizationConsentRepository = authorizationConsentRepository;
    }

    public Optional<HerodotusAuthorizationConsent> findByRegisteredClientIdAndPrincipalName(
            String registeredClientId, String principalName) {
        Optional<HerodotusAuthorizationConsent> result =
                this.authorizationConsentRepository.findByRegisteredClientIdAndPrincipalName(
                        registeredClientId, principalName);
        log.info("HerodotusAuthorizationConsent Service findByRegisteredClientIdAndPrincipalName.");
        return result;
    }

    public void deleteByRegisteredClientIdAndPrincipalName(String registeredClientId, String principalName) {
        this.authorizationConsentRepository.deleteByRegisteredClientIdAndPrincipalName(
                registeredClientId, principalName);
        log.info("HerodotusAuthorizationConsent Service deleteByRegisteredClientIdAndPrincipalName.");
    }

    public void save(HerodotusAuthorizationConsent entity) {
        authorizationConsentRepository.save(entity);
    }
}
