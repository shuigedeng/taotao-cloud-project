/*
 * Copyright (c) 2020-2030 ZHENGGENGWEI(码匠君)<herodotus@aliyun.com>
 *
 * Dante Engine Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
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
 * 2.请不要删除和修改 Dante Engine 源码头部的版权声明。
 * 3.请保留源码和相关描述文件的项目出处，作者声明等。
 * 4.分发源码时候，请注明软件出处 https://gitee.com/herodotus/dante-engine
 * 5.在修改包名，模块名称，项目代码等时，请注明软件出处 https://gitee.com/herodotus/dante-engine
 * 6.若您的项目无法满足以上几点，可申请商业授权
 */

package com.taotao.cloud.auth.biz.oauth2.oauth2_server.controller;

import com.taotao.cloud.auth.biz.oauth2.oauth2_server.entity.OAuth2Application;
import com.taotao.cloud.auth.biz.oauth2.oauth2_server.entity.OAuth2Scope;
import com.taotao.cloud.auth.biz.oauth2.oauth2_server.service.OAuth2ApplicationService;
import com.taotao.cloud.auth.biz.oauth2.oauth2_server.service.OAuth2ScopeService;
import com.taotao.cloud.common.constant.SymbolConstants;
import java.security.Principal;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
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
 * @author gengwei.zheng
 * @date 2022-03-01
 * @see org.springframework.security.oauth2.server.authorization.web.OAuth2AuthorizationEndpointFilter
 */
@Controller
public class ConsentController {

	private final OAuth2ApplicationService applicationService;
	private final OAuth2AuthorizationConsentService authorizationConsentService;
	private final OAuth2ScopeService scopeService;

	private Map<String, OAuth2Scope> dictionaries;

	public ConsentController(OAuth2ApplicationService applicationService,
		OAuth2AuthorizationConsentService authorizationConsentService,
		OAuth2ScopeService scopeService) {
		this.applicationService = applicationService;
		this.authorizationConsentService = authorizationConsentService;
		this.scopeService = scopeService;
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
	@GetMapping(value = "/oauth2/consent")
	public String consent(Principal principal, Model model,
		@RequestParam(OAuth2ParameterNames.CLIENT_ID) String clientId,
		@RequestParam(OAuth2ParameterNames.SCOPE) String scope,
		@RequestParam(OAuth2ParameterNames.STATE) String state) {

		/** 移除已经授权过的scope */
		//待授权的scope
		Set<String> scopesToApprove = new HashSet<>();
		//之前已经授权过的scope
		Set<String> previouslyApprovedScopes = new HashSet<>();
		//获取客户端注册信息
		OAuth2Application application = this.applicationService.findByClientId(clientId);
		//获取当前Client下用户之前的consent信息
		OAuth2AuthorizationConsent currentAuthorizationConsent =
			this.authorizationConsentService.findById(clientId, principal.getName());
		//当前Client下用户已经授权的scope
		Set<String> authorizedScopes = Optional.ofNullable(currentAuthorizationConsent)
			.map(OAuth2AuthorizationConsent::getScopes)
			.orElse(Collections.emptySet());
		//遍历请求的scope，提取之前已授权过 和 待授权的scope
		for (String requestedScope : StringUtils.delimitedListToStringArray(scope,
			SymbolConstants.SPACE)) {
			if (authorizedScopes.contains(requestedScope)) {
				previouslyApprovedScopes.add(requestedScope);
			} else if (!OidcScopes.OPENID.equals(requestedScope)) {
				scopesToApprove.add(requestedScope);
			}
		}

		//输出信息指consent页面
		model.addAttribute("clientId", clientId);
		model.addAttribute("state", state);
		model.addAttribute("scopes", withDescription(scopesToApprove));
		model.addAttribute("previouslyApprovedScopes", withDescription(previouslyApprovedScopes));
		model.addAttribute("principalName", principal.getName());
		model.addAttribute("applicationName", application.getApplicationName());
		model.addAttribute("logo", application.getLogo());
		return "consent";
	}

	private void initDictionaries() {
		List<OAuth2Scope> scopes = scopeService.findAll();
		if (CollectionUtils.isNotEmpty(scopes)) {
			if (MapUtils.isEmpty(dictionaries) || scopes.size() != dictionaries.size()) {
				dictionaries = scopes.stream()
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
	private Set<OAuth2Scope> withDescription(Set<String> scopes) {
		if (CollectionUtils.isNotEmpty(scopes)) {
			return scopes.stream().map(item -> dictionaries.get(item)).collect(Collectors.toSet());
		} else {
			return new HashSet<>();
		}
	}
}
