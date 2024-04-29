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

package com.taotao.cloud.auth.infrastructure.authentication.service;

import com.taotao.cloud.auth.infrastructure.persistent.management.po.OAuth2Permission;
import com.taotao.cloud.auth.infrastructure.persistent.management.po.OAuth2Scope;
import com.taotao.cloud.auth.infrastructure.persistent.management.repository.OAuth2ScopeRepository;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Service;

/**
 * <p> Description : OauthScopeService </p>
 *
 *
 * @since : 2020/3/19 17:00
 */
@Service
public class OAuth2ScopeService {

    private final OAuth2ScopeRepository oauthScopesRepository;

    public OAuth2ScopeService(OAuth2ScopeRepository oauthScopesRepository) {
        this.oauthScopesRepository = oauthScopesRepository;
    }

    public OAuth2Scope assigned(String scopeId, Set<OAuth2Permission> permissions) {

        OAuth2Scope oldScope = oauthScopesRepository.findById(scopeId).get();
        oldScope.setPermissions(permissions);

        return oauthScopesRepository.saveAndFlush(oldScope);
    }

    public OAuth2Scope findByScopeCode(String scopeCode) {
        return oauthScopesRepository.findByScopeCode(scopeCode);
    }

    public List<OAuth2Scope> findByScopeCodeIn(List<String> scopeCodes) {
        return oauthScopesRepository.findByScopeCodeIn(scopeCodes);
    }

    public List<OAuth2Scope> findAll() {

        return oauthScopesRepository.findAll();
    }
}
