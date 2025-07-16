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

package com.taotao.cloud.auth.biz.management.controller;

import com.taotao.boot.security.spring.constants.DefaultConstants;
import com.taotao.boot.security.spring.constants.SymbolConstants;
import com.taotao.boot.security.spring.properties.OAuth2EndpointProperties;
import com.taotao.cloud.auth.biz.management.dto.Option;
import com.taotao.cloud.auth.biz.management.entity.OAuth2Application;
import com.taotao.cloud.auth.biz.management.entity.OAuth2Scope;
import com.taotao.cloud.auth.biz.management.service.OAuth2ApplicationService;
import com.taotao.cloud.auth.biz.management.service.OAuth2ScopeService;
import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsent;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * OAuth2 授权确认页面 - controller
 *
 * @since 2022-03-01
 * @see org.springframework.security.oauth2.server.authorization.web.OAuth2AuthorizationEndpointFilter
 */
@Controller
public class ConsentController {

    private final OAuth2ApplicationService applicationService;
    private final OAuth2AuthorizationConsentService authorizationConsentService;
    private final OAuth2ScopeService scopeService;
    private final OAuth2EndpointProperties oAuth2EndpointProperties;

    private Map<String, OAuth2Scope> dictionaries;

    public ConsentController(
            OAuth2ApplicationService applicationService,
            OAuth2AuthorizationConsentService authorizationConsentService,
            OAuth2ScopeService scopeService,
            OAuth2EndpointProperties oAuth2EndpointProperties) {
        this.applicationService = applicationService;
        this.authorizationConsentService = authorizationConsentService;
        this.scopeService = scopeService;
        this.oAuth2EndpointProperties = oAuth2EndpointProperties;
        initDictionaries();
    }

    /**
     * Consent页面（确认请求scope的页面）
     *
     * @param principal 用户信息
     * @param model     model
     * @param clientId  客户端ID
     * @param scope     请求范围
     * @param state     state参数
     * @return Consent页面
     */
    @GetMapping(DefaultConstants.AUTHORIZATION_CONSENT_URI)
    public String consent(
            Principal principal,
            Model model,
            @RequestParam(OAuth2ParameterNames.CLIENT_ID) String clientId,
            @RequestParam(OAuth2ParameterNames.SCOPE) String scope,
            @RequestParam(OAuth2ParameterNames.STATE) String state,
            @RequestParam(value = OAuth2ParameterNames.USER_CODE, required = false)
                    String userCode) {

        // 待授权的scope
        Set<String> scopesToApprove = new HashSet<>();
        // 之前已经授权过的scope
        Set<String> previouslyApprovedScopes = new HashSet<>();
        // 获取客户端注册信息
        OAuth2Application application = this.applicationService.findByClientId(clientId);
        // 获取当前Client下用户之前的consent信息
        OAuth2AuthorizationConsent currentAuthorizationConsent =
                this.authorizationConsentService.findById(clientId, principal.getName());
        // 当前Client下用户已经授权的scope
        Set<String> authorizedScopes =
                Optional.ofNullable(currentAuthorizationConsent)
                        .map(OAuth2AuthorizationConsent::getScopes)
                        .orElse(Collections.emptySet());

        // 遍历请求的scope，提取之前已授权过 和 待授权的scope
        for (String requestedScope :
                StringUtils.delimitedListToStringArray(scope, SymbolConstants.SPACE)) {
            if (OidcScopes.OPENID.equals(requestedScope)) {
                continue;
            }

            if (authorizedScopes.contains(requestedScope)) {
                previouslyApprovedScopes.add(requestedScope);
            } else {
                scopesToApprove.add(requestedScope);
            }
        }

        Set<String> redirectUris =
                StringUtils.commaDelimitedListToSet(application.getRedirectUris());

        // 输出信息指consent页面
        model.addAttribute("clientId", clientId);
        model.addAttribute("state", state);
        model.addAttribute("scopesToAuthorize", withDescription(scopesToApprove));
        model.addAttribute("scopesPreviouslyAuthorized", withDescription(previouslyApprovedScopes));
        model.addAttribute("principalName", principal.getName());
        model.addAttribute("applicationName", application.getApplicationName());
        model.addAttribute("logo", application.getLogo());
        model.addAttribute("redirectUri", redirectUris.iterator().next());
        model.addAttribute("userCode", userCode);
        String action = oAuth2EndpointProperties.getAuthorizationEndpoint();
        if (StringUtils.hasText(userCode)) {
            action = oAuth2EndpointProperties.getDeviceVerificationEndpoint();
        }
        model.addAttribute("action", action);
        return "consent";
    }

    private void initDictionaries() {
        List<OAuth2Scope> scopes = scopeService.findAll();
        if (CollectionUtils.isNotEmpty(scopes)) {
            if (MapUtils.isEmpty(dictionaries) || scopes.size() != dictionaries.size()) {
                dictionaries =
                        scopes.stream()
                                .collect(Collectors.toMap(OAuth2Scope::getScopeCode, item -> item));
            }
        }
    }

    /**
     * 根据scope生成相关权限描述
     *
     * @param scopes scope集合
     * @return scope描述集合
     */
    private Set<Option> withDescription(Set<String> scopes) {
        if (CollectionUtils.isNotEmpty(scopes)) {
            return scopes.stream()
                    .map(item -> scopeToOption(dictionaries.get(item)))
                    .collect(Collectors.toSet());
        } else {
            return new HashSet<>();
        }
    }

    private Option scopeToOption(OAuth2Scope scope) {
        Option option = new Option();
        String label =
                scope.getDescription() == null ? scope.getScopeName() : scope.getDescription();
        option.setLabel(label);
        option.setValue(scope.getScopeCode());
        return option;
    }
}
