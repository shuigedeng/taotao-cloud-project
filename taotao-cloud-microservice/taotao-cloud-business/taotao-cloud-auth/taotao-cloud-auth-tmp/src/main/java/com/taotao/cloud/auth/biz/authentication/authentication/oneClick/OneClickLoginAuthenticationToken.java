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

package com.taotao.cloud.auth.biz.authentication.authentication.oneClick;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityCoreVersion;
import org.springframework.web.context.request.ServletWebRequest;

import java.util.Collection;
import java.util.Map;

public class OneClickLoginAuthenticationToken extends AbstractAuthenticationToken {

    private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;


	private final Object principal;

	private final Map<String, String> otherParamMap;

	private ServletWebRequest request;


	/**
	 * This constructor can be safely used by any codes that wishes to create a
	 * <codes>OneClickLoginAuthenticationToken</codes>, as the {@link #isAuthenticated()}
	 * will return <codes>false</codes>.
	 *
	 */
	public OneClickLoginAuthenticationToken(@NonNull String mobile, @Nullable Map<String, String> otherParamMap) {
		this(mobile, otherParamMap, (ServletWebRequest) null);
	}

	public OneClickLoginAuthenticationToken(@NonNull String mobile,
											@Nullable Map<String, String> otherParamMap,
											@Nullable ServletWebRequest request) {
		super(null);
		this.principal = mobile;
		this.otherParamMap = otherParamMap;
		this.request = request;
		setAuthenticated(false);
	}

	/**
	 * This constructor should only be used by <codes>AuthenticationManager</codes> or
	 * <codes>AuthenticationProvider</codes> implementations that are satisfied with
	 * producing a trusted (i.e. {@link #isAuthenticated()} = <codes>true</codes>)
	 * auth token.
	 *
	 * @param principal     principal
	 * @param otherParamMap other param map, map(paramName, paramValue)
	 * @param authorities   authorities
	 */
	public OneClickLoginAuthenticationToken(@NonNull Object principal,
											@Nullable Map<String, String> otherParamMap,
											@NonNull Collection<? extends GrantedAuthority> authorities) {
		super(authorities);
		this.principal = principal;
		this.otherParamMap = otherParamMap;
		this.request = null;
		// must use super, as we override
		super.setAuthenticated(true);
	}


	@Override
	public Object getCredentials() {
		return null;
	}

	@Override
	public Object getPrincipal() {
		return this.principal;
	}

	@Override
	public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
		if (isAuthenticated) {
			throw new IllegalArgumentException(
				"Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
		}

		super.setAuthenticated(false);
	}

	@Override
	public void eraseCredentials() {
		super.eraseCredentials();
	}

	@NonNull
	public ServletWebRequest getRequest() {
		return request;
	}

	@Nullable
	public Map<String, String> getOtherParamMap() {
		return otherParamMap;
	}
}
