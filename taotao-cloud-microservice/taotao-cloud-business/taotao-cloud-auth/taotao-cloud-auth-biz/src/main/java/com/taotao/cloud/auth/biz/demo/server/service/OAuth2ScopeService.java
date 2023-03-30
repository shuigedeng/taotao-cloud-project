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

package com.taotao.cloud.auth.biz.demo.server.service;

import cn.herodotus.engine.data.core.repository.BaseRepository;
import cn.herodotus.engine.data.core.service.BaseLayeredService;
import cn.herodotus.engine.oauth2.server.authentication.entity.OAuth2Authority;
import cn.herodotus.engine.oauth2.server.authentication.entity.OAuth2Scope;
import cn.herodotus.engine.oauth2.server.authentication.repository.OAuth2ScopeRepository;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Description : OauthScopeService
 *
 * @author : gengwei.zheng
 * @date : 2020/3/19 17:00
 */
@Service
public class OAuth2ScopeService extends BaseLayeredService<OAuth2Scope, String> {

    private static final Logger log = LoggerFactory.getLogger(OAuth2ScopeService.class);

    private final OAuth2ScopeRepository oauthScopesRepository;

    @Autowired
    public OAuth2ScopeService(OAuth2ScopeRepository oauthScopesRepository) {
        this.oauthScopesRepository = oauthScopesRepository;
    }

    @Override
    public BaseRepository<OAuth2Scope, String> getRepository() {
        return oauthScopesRepository;
    }

    public OAuth2Scope authorize(String scopeId, Set<OAuth2Authority> authorities) {

        OAuth2Scope oldScope = findById(scopeId);
        oldScope.setAuthorities(authorities);

        OAuth2Scope newScope = saveOrUpdate(oldScope);
        log.debug("[Herodotus] |- OAuth2ScopeService assign.");
        return newScope;
    }

    public OAuth2Scope findByScopeCode(String scopeCode) {
        OAuth2Scope scope = oauthScopesRepository.findByScopeCode(scopeCode);
        log.debug("[Herodotus] |- OAuth2ScopeService findByScopeCode.");
        return scope;
    }
}
