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

package com.taotao.cloud.auth.biz.authentication.token;

import com.taotao.boot.security.spring.constants.BaseConstants;
import com.taotao.boot.security.spring.core.userdetails.TtcUser;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationToken;

/**
 * <p>TokenCustomizer 通用处理 </p>
 *
 * @author shuigedeng
 * @version 2023.07
 * @since 2023-07-10 17:25:30
 */
public abstract class AbstractTokenCustomizer {

    /**
     * 追加全部
     *
     * @param attributes       属性
     * @param authentication   身份验证
     * @param authorizedScopes 授权范围
     * @since 2023-07-10 17:25:30
     */
    protected void appendAll(
            Map<String, Object> attributes,
            Authentication authentication,
            Set<String> authorizedScopes) {
        appendAuthorities(attributes, authentication);
        appendCommons(attributes, authentication, authorizedScopes);
    }

    /**
     * 附加权限
     *
     * @param attributes     属性
     * @param authentication 身份验证
     * @since 2023-07-10 17:25:31
     */
    protected void appendAuthorities(
            Map<String, Object> attributes, Authentication authentication) {
        if (CollectionUtils.isNotEmpty(authentication.getAuthorities())) {
            Set<String> authorities =
                    authentication.getAuthorities().stream()
                            .map(GrantedAuthority::getAuthority)
                            .collect(Collectors.toSet());
            attributes.put(BaseConstants.AUTHORITIES, authorities);
        }
    }

    /**
     * 追加公地
     *
     * @param attributes       属性
     * @param authentication   身份验证
     * @param authorizedScopes 授权范围
     * @since 2023-07-10 17:25:31
     */
    protected void appendCommons(
            Map<String, Object> attributes,
            Authentication authentication,
            Set<String> authorizedScopes) {

        if (CollectionUtils.isNotEmpty(authorizedScopes)) {
            attributes.put(OAuth2ParameterNames.SCOPE, authorizedScopes);
        }

        if (authentication instanceof UsernamePasswordAuthenticationToken) {
            TtcUser principal = (TtcUser) authentication.getPrincipal();
            putUserInfo(attributes, principal);
        }

        if (authentication instanceof OAuth2AccessTokenAuthenticationToken) {
            Object details = authentication.getDetails();
            if (ObjectUtils.isNotEmpty(details) && details instanceof TtcUser) {
                TtcUser principal = (TtcUser) details;
                putUserInfo(attributes, principal);
            }
        }

        attributes.put("license", "Apache-2.0 Licensed | Copyright © 2020-2023 shuigedeng");
    }

    /**
     * 放置用户信息
     *
     * @param attributes 属性
     * @param principal  校长
     * @since 2023-07-10 17:25:32
     */
    private void putUserInfo(Map<String, Object> attributes, TtcUser principal) {
        attributes.put(BaseConstants.OPEN_ID, principal.getUserId());
        attributes.put(BaseConstants.ROLES, principal.getRoles());
        attributes.put(BaseConstants.AVATAR, principal.getAvatar());
        attributes.put(BaseConstants.EMPLOYEE_ID, principal.getEmployeeId());
    }
}
