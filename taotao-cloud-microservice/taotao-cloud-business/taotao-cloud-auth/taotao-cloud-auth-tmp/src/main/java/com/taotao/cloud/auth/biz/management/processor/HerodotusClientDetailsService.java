/*
 * Copyright (c) 2020-2030 ZHENGGENGWEI(码匠君)<herodotus@aliyun.com>
 *
 * Dante Engine licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Dante Engine 采用APACHE LICENSE 2.0开源协议，您在使用过程中，需要注意以下几点：
 *
 * 1.请不要删除和修改根目录下的LICENSE文件。
 * 2.请不要删除和修改 Dante Cloud 源码头部的版权声明。
 * 3.请保留源码和相关描述文件的项目出处，作者声明等。
 * 4.分发源码时候，请注明软件出处 
 * 5.在修改包名，模块名称，项目代码等时，请注明软件出处 
 * 6.若您的项目无法满足以上几点，可申请商业授权
 */

package com.taotao.cloud.auth.biz.management.processor;

import com.taotao.cloud.auth.biz.management.entity.OAuth2Application;
import com.taotao.cloud.auth.biz.management.entity.OAuth2Permission;
import com.taotao.cloud.auth.biz.management.entity.OAuth2Scope;
import com.taotao.cloud.auth.biz.management.service.OAuth2ApplicationService;
import com.taotao.cloud.security.springsecurity.core.definition.domain.HerodotusGrantedAuthority;
import com.taotao.cloud.security.springsecurity.core.definition.service.EnhanceClientDetailsService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>Description: 客户端交互处理器 </p>
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
                    Set<OAuth2Permission> permissions = scope.getPermissions();
                    if (CollectionUtils.isNotEmpty(permissions)) {
                        Set<HerodotusGrantedAuthority> grantedAuthorities = permissions.stream().map(item -> new HerodotusGrantedAuthority(item.getPermissionCode())).collect(Collectors.toSet());
                        result.addAll(grantedAuthorities);
                    }
                }
            }
            return result;
        }

        return new HashSet<>();
    }
}
