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

package com.taotao.cloud.auth.biz.management.compliance.listener;

import com.taotao.cloud.auth.biz.authentication.stamp.SignInFailureLimitedStampManager;
import com.taotao.cloud.auth.biz.management.service.OAuth2ComplianceService;
import com.taotao.cloud.security.springsecurity.core.definition.domain.PrincipalDetails;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.dromara.hutool.crypto.SecureUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationToken;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * <p>Description: 登录成功事件监听 </p>
 *
 *
 * @date : 2022/7/7 20:58
 */
public class AuthenticationSuccessListener implements ApplicationListener<AuthenticationSuccessEvent> {

	private static final Logger log = LoggerFactory.getLogger(AuthenticationSuccessListener.class);

	private final SignInFailureLimitedStampManager stampManager;
	private final OAuth2ComplianceService complianceService;

	public AuthenticationSuccessListener(SignInFailureLimitedStampManager stampManager, OAuth2ComplianceService complianceService) {
		this.stampManager = stampManager;
		this.complianceService = complianceService;
	}

	@Override
	public void onApplicationEvent(AuthenticationSuccessEvent event) {

		log.info("[Herodotus] |- Authentication Success Listener!");

		Authentication authentication = event.getAuthentication();

		if (authentication instanceof OAuth2AccessTokenAuthenticationToken authenticationToken) {
			Object details = authentication.getDetails();

			String username = null;
			if (ObjectUtils.isNotEmpty(details) && details instanceof PrincipalDetails user) {
				username = user.getUserName();
			}

			String clientId = authenticationToken.getRegisteredClient().getId();

			RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
			if (ObjectUtils.isNotEmpty(requestAttributes) && requestAttributes instanceof ServletRequestAttributes servletRequestAttributes) {
				HttpServletRequest request = servletRequestAttributes.getRequest();

				if (ObjectUtils.isNotEmpty(request) && StringUtils.isNotBlank(username)) {
					complianceService.save(username, clientId, "用户登录", request);
					String key = SecureUtil.md5(username);
					boolean hasKey = stampManager.containKey(key);
					if (hasKey) {
						stampManager.delete(key);
					}
				}
			} else {
				log.info("[Herodotus] |- Can not get request and username, skip!");
			}
		}
	}
}
