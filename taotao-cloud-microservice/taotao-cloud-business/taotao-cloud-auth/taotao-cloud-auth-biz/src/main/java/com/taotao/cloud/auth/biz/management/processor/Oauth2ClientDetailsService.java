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

package com.taotao.cloud.auth.biz.management.processor;

import com.taotao.boot.security.spring.core.authority.TtcGrantedAuthority;
import com.taotao.cloud.auth.biz.management.entity.OAuth2Application;
import com.taotao.cloud.auth.biz.management.entity.OAuth2Permission;
import com.taotao.cloud.auth.biz.management.entity.OAuth2Scope;
import com.taotao.cloud.auth.biz.management.service.OAuth2ApplicationService;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;

/**
 * <p>客户端交互处理器 </p>
 *
 * @author shuigedeng
 * @version 2023.07
 * @since 2023-07-04 10:52:02
 */
public class Oauth2ClientDetailsService implements EnhanceClientDetailsService {

    private final OAuth2ApplicationService applicationService;

    public Oauth2ClientDetailsService(OAuth2ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @Override
    public Set<TtcGrantedAuthority> findAuthoritiesById(String clientId) {
        OAuth2Application application = applicationService.findByClientId(clientId);
        if (ObjectUtils.isNotEmpty(application)) {
            Set<OAuth2Scope> scopes = application.getScopes();
            Set<TtcGrantedAuthority> result = new HashSet<>();
            if (CollectionUtils.isNotEmpty(scopes)) {
                for (OAuth2Scope scope : scopes) {
                    Set<OAuth2Permission> permissions = scope.getPermissions();
                    if (CollectionUtils.isNotEmpty(permissions)) {
                        Set<TtcGrantedAuthority> grantedAuthorities =
                                permissions.stream()
                                        .map(
                                                item ->
                                                        new TtcGrantedAuthority(
                                                                item.getPermissionCode()))
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
