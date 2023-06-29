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

package com.taotao.cloud.auth.biz.authentication.login.extension.oneClick;

import com.taotao.cloud.auth.biz.authentication.login.extension.oneClick.service.OneClickLoginService;
import com.taotao.cloud.auth.biz.authentication.login.extension.oneClick.service.OneClickJustAuthUserDetailsService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.Assert;

import java.util.Map;

import static java.util.Objects.nonNull;

/** 基于阿里云app手机号码一键登录 */
public class OneClickLoginAuthenticationProvider implements AuthenticationProvider, InitializingBean, MessageSourceAware {

    private MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();

	private final OneClickJustAuthUserDetailsService oneClickUserDetailsService;
	private final OneClickLoginService oneClickLoginService;

	public OneClickLoginAuthenticationProvider(OneClickJustAuthUserDetailsService oneClickUserDetailsService,
                                               OneClickLoginService oneClickLoginService) {
		this.oneClickUserDetailsService = oneClickUserDetailsService;
		this.oneClickLoginService = oneClickLoginService;
	}

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Assert.isInstanceOf(
                OneClickLoginAuthenticationToken.class,
                authentication,
                () -> messages.getMessage(
                        "AccountVerificationAuthenticationProvider.onlySupports",
                        "Only AccountVerificationAuthenticationProvider is supported"));

		if (!supports(authentication.getClass())) {
			return null;
		}
		OneClickLoginAuthenticationToken authenticationToken = (OneClickLoginAuthenticationToken) authentication;

		if (authentication.isAuthenticated()) {
			return authentication;
		}

		UserDetails user;
		try {
			user = this.oneClickUserDetailsService.loadUserByOneClick((String) authenticationToken.getPrincipal());
		}
		catch (UsernameNotFoundException e) {
			user = null;
		}

		Map<String, String> otherParamMap;
		if (user == null) {
			user = this.oneClickUserDetailsService.registerUser((String) authenticationToken.getPrincipal());
		}

		// 一键登录的其他参数处理
		otherParamMap = authenticationToken.getOtherParamMap();
		if (nonNull(otherParamMap) && !otherParamMap.isEmpty()) {
			this.oneClickLoginService.otherParamsHandler(user, otherParamMap);
		}
		OneClickLoginAuthenticationToken authenticationResult =
			new OneClickLoginAuthenticationToken(user,
				otherParamMap,
				user.getAuthorities());
		authenticationResult.setDetails(authenticationToken.getDetails());
		return authenticationResult;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return OneClickLoginAuthenticationToken.class.isAssignableFrom(authentication);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(oneClickUserDetailsService, "oneClickUserDetailsService must not be null");
    }

    @Override
    public void setMessageSource(MessageSource messageSource) {
        this.messages = new MessageSourceAccessor(messageSource);
    }


}
