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

package com.taotao.cloud.auth.infrastructure.authentication.extension.social;

import static com.taotao.boot.security.spring.oauth2.TtcAuthorizationGrantType.SOCIAL;

import com.taotao.boot.security.spring.oauth2.TtcAuthorizationGrantType;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationGrantAuthenticationToken;
import org.springframework.util.Assert;

/**
 * <p>自定义社会化登录认证Token </p>
 *
 * @since : 2022/3/31 14:54
 */
public class OAuth2SocialCredentialsAuthenticationToken extends
	OAuth2AuthorizationGrantAuthenticationToken {

	private final Set<String> scopes;

	public OAuth2SocialCredentialsAuthenticationToken(
		Authentication clientPrincipal,
		@Nullable Set<String> scopes,
		@Nullable Map<String, Object> additionalParameters) {
		super(TtcAuthorizationGrantType.SOCIAL, clientPrincipal, additionalParameters);
		Assert.notNull(clientPrincipal, "clientPrincipal cannot be null");
		this.scopes = Collections.unmodifiableSet(
			CollectionUtils.isNotEmpty(scopes) ? new HashSet<>(scopes) : Collections.emptySet());
	}

	public Set<String> getScopes() {
		return scopes;
	}
}
