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

package com.taotao.cloud.auth.biz.controller.oauth2;

import java.security.Principal;
import java.util.Collections;
import java.util.Set;
import lombok.AllArgsConstructor;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsent;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.web.OAuth2AuthorizationEndpointFilter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 自定义用户确认页
 *
 * @author felord.cn
 */
@Controller
@AllArgsConstructor
public class AuthorizationConsentController {
    private final RegisteredClientRepository registeredClientRepository;
    private final OAuth2AuthorizationConsentService authorizationConsentService;
    // private final OAuth2ScopeService oAuth2ScopeService;
    private final AuthorizationServerSettings authorizationServerSettings;

    /**
     * {@link OAuth2AuthorizationEndpointFilter} 会302重定向到{@code /oauth2/consent}并携带入参
     *
     * @param principal 当前用户
     * @param model 视图模型
     * @param clientId oauth2 client id
     * @param scope 请求授权的scope
     * @param state state 值
     * @return 自定义授权确认页面 consent.html
     */
    @GetMapping(value = "/oauth2/consent")
    public String consent(
            Principal principal,
            Model model,
            @RequestParam(OAuth2ParameterNames.CLIENT_ID) String clientId,
            @RequestParam(OAuth2ParameterNames.SCOPE) String scope,
            @RequestParam(OAuth2ParameterNames.STATE) String state) {

        RegisteredClient registeredClient = this.registeredClientRepository.findByClientId(clientId);
        String id = registeredClient.getId();
        OAuth2AuthorizationConsent currentAuthorizationConsent =
                this.authorizationConsentService.findById(id, principal.getName());

        Set<String> authorizedScopes =
                currentAuthorizationConsent != null ? currentAuthorizationConsent.getScopes() : Collections.emptySet();

        // Set<OAuth2Scope> scopesToApproves = new HashSet<>();
        // Set<OAuth2Scope> previouslyApprovedScopesSet = new HashSet<>();
        //
        // String[] scopes = StringUtils.delimitedListToStringArray(scope, " ");
        //
        // List<OAuth2Scope> oAuth2Scopes = oAuth2ScopeService.findByClientIdAndScope(clientId,
        // Arrays.asList(scopes));
        //       oAuth2Scopes.forEach(oAuth2Scope -> {
        //           if (authorizedScopes.contains(oAuth2Scope.getScope())) {
        //               previouslyApprovedScopesSet.add(oAuth2Scope);
        //           } else {
        //               scopesToApproves.add(oAuth2Scope);
        //           }
        //       });

        String clientName = registeredClient.getClientName();

        model.addAttribute("authorizationEndpoint", authorizationServerSettings.getAuthorizationEndpoint());
        model.addAttribute("clientId", clientId);
        model.addAttribute("clientName", clientName);
        model.addAttribute("state", state);
        // model.addAttribute("scopes", scopesToApproves);
        // model.addAttribute("previouslyApprovedScopes", previouslyApprovedScopesSet);
        model.addAttribute("principalName", principal.getName());

        return "consent";
    }
}
