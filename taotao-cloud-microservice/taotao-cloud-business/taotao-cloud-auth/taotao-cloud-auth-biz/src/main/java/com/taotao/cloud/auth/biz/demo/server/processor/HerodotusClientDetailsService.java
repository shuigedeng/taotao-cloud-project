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

package com.taotao.cloud.auth.biz.demo.server.processor;

import cn.herodotus.engine.oauth2.core.definition.domain.HerodotusGrantedAuthority;
import cn.herodotus.engine.oauth2.core.definition.service.EnhanceClientDetailsService;
import cn.herodotus.engine.oauth2.server.authentication.entity.OAuth2Application;
import cn.herodotus.engine.oauth2.server.authentication.entity.OAuth2Authority;
import cn.herodotus.engine.oauth2.server.authentication.entity.OAuth2Scope;
import cn.herodotus.engine.oauth2.server.authentication.service.OAuth2ApplicationService;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;

/**
 * Description: 客户端交互处理器
 *
 * @author : gengwei.zheng
 * @date : 2022/4/1 15:21
 */
public class HerodotusClientDetailsService implements EnhanceClientDetailsService {

    private final OAuth2ApplicationService applicationService;

    public HerodotusClientDetailsService(OAuth2ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @Override
    public Set<HerodotusGrantedAuthority> findAuthoritiesById(String clientId) {

        OAuth2Application application = applicationService.findByClientId(clientId);
        if (ObjectUtils.isNotEmpty(application)) {
            Set<OAuth2Scope> scopes = application.getScopes();
            Set<HerodotusGrantedAuthority> result = new HashSet<>();
            if (CollectionUtils.isNotEmpty(scopes)) {
                for (OAuth2Scope scope : scopes) {
                    Set<OAuth2Authority> authorities = scope.getAuthorities();
                    if (CollectionUtils.isNotEmpty(authorities)) {
                        Set<HerodotusGrantedAuthority> grantedAuthorities = authorities.stream()
                                .map(item -> new HerodotusGrantedAuthority(item.getAuthorityCode()))
                                .collect(Collectors.toSet());
                        result.addAll(grantedAuthorities);
                    }
                }
            }
            return result;
        }

        return new HashSet<>();
    }
}
