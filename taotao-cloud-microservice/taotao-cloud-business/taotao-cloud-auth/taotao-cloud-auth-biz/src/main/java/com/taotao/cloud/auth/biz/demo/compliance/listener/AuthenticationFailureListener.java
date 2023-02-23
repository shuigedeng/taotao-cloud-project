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

package com.taotao.cloud.auth.biz.demo.compliance.listener;

import cn.herodotus.engine.cache.core.exception.MaximumLimitExceededException;
import cn.herodotus.engine.oauth2.compliance.service.OAuth2AccountStatusService;
import cn.herodotus.engine.oauth2.compliance.stamp.SignInFailureLimitedStampManager;
import java.time.Duration;
import java.util.Map;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationGrantAuthenticationToken;

/**
 * <p>Description: 登出成功监听 </p>
 *
 * @author : gengwei.zheng
 * @date : 2021/12/18 17:58
 */
public class AuthenticationFailureListener implements
	ApplicationListener<AbstractAuthenticationFailureEvent> {

	private static final Logger log = LoggerFactory.getLogger(AuthenticationFailureListener.class);

	private final SignInFailureLimitedStampManager stampManager;
	private final OAuth2AccountStatusService accountLockService;

	public AuthenticationFailureListener(SignInFailureLimitedStampManager stampManager,
		OAuth2AccountStatusService accountLockService) {
		this.stampManager = stampManager;
		this.accountLockService = accountLockService;
	}

	@Override
	public void onApplicationEvent(AbstractAuthenticationFailureEvent event) {

		log.debug("[Herodotus] |- User sign in catch failure event : [{}].",
			event.getClass().getName());

		if (event instanceof AuthenticationFailureBadCredentialsEvent) {
			Authentication authentication = event.getAuthentication();

			String username = null;

			if (authentication instanceof OAuth2AuthorizationGrantAuthenticationToken) {

				log.debug(
					"[Herodotus] |- Toke object in failure event  is OAuth2AuthorizationGrantAuthenticationToken");

				OAuth2AuthorizationGrantAuthenticationToken token = (OAuth2AuthorizationGrantAuthenticationToken) authentication;
				Map<String, Object> params = token.getAdditionalParameters();
				username = getPrincipal(params);
			}

			if (authentication instanceof UsernamePasswordAuthenticationToken) {

				log.debug(
					"[Herodotus] |- Toke object in failure event  is UsernamePasswordAuthenticationToken");

				UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;
				Object principal = token.getPrincipal();
				if (principal instanceof String) {
					username = (String) principal;
				}
			}

			if (StringUtils.isNotBlank(username)) {

				log.debug("[Herodotus] |- Parse the username in failure event is [{}].", username);

				int maxTimes = stampManager.getComplianceProperties().getSignInFailureLimited()
					.getMaxTimes();
				Duration expire = stampManager.getComplianceProperties().getSignInFailureLimited()
					.getExpire();
				try {
					int times = stampManager.counting(username, maxTimes, expire, true,
						"AuthenticationFailureListener");
					log.debug("[Herodotus] |- Sign in user input password error [{}] items", times);
				} catch (MaximumLimitExceededException e) {
					log.warn("[Herodotus] |- User [{}] password error [{}] items, LOCK ACCOUNT!",
						username, maxTimes);
					accountLockService.lock(username);
				}
			}
		}
	}

	private String getPrincipal(Map<String, Object> params) {
		if (MapUtils.isNotEmpty(params)) {
			if (params.containsKey(OAuth2ParameterNames.USERNAME)) {
				Object value = params.get(OAuth2ParameterNames.USERNAME);
				if (ObjectUtils.isNotEmpty(value)) {
					return (String) value;
				}
			}
		}

		return null;
	}
}
