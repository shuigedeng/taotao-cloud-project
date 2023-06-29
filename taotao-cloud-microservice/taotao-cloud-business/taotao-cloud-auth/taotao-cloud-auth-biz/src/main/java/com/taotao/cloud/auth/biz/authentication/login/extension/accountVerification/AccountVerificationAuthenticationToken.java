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

package com.taotao.cloud.auth.biz.authentication.login.extension.accountVerification;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityCoreVersion;
import org.springframework.util.Assert;

import java.io.Serial;
import java.util.Collection;

/**
 * 帐户验证身份验证令牌
 *
 * @author shuigedeng
 * @version 2023.04
 * @since 2023-06-29 14:05:37
 */
public class AccountVerificationAuthenticationToken extends AbstractAuthenticationToken {

	@Serial
	private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

	private final Object principal;
	private String password;
	private String verificationCode;
	private String type;

	/**
	 * 此构造函数用来初始化未授信凭据.
	 *
	 * @param principal the principal
	 * @param password  the captcha
	 */
	public AccountVerificationAuthenticationToken(
		Object principal, String password, String verificationCode, String type) {
		super(null);
		this.principal = principal;
		this.password = password;
		this.verificationCode = verificationCode;
		this.type = type;
		setAuthenticated(false);
	}

	/**
	 * 此构造函数用来初始化授信凭据.
	 *
	 * @param principal        the principal
	 * @param password         the captcha
	 * @param verificationCode the verificationCode
	 * @param authorities      the authorities
	 */
	public AccountVerificationAuthenticationToken(
		Object principal,
		String password,
		String verificationCode,
		String type,
		Collection<? extends GrantedAuthority> authorities) {
		super(authorities);
		this.principal = principal;
		this.password = password;
		this.verificationCode = verificationCode;
		this.type = type;
		// must use super, as we override
		super.setAuthenticated(true);
	}

	public static AccountVerificationAuthenticationToken unauthenticated(Object principal, String password, String verificationCode, String type) {
		return new AccountVerificationAuthenticationToken(principal, password, verificationCode, type);
	}

	public static AccountVerificationAuthenticationToken authenticated(Object principal,
																	   String password,
																	   String verificationCode,
																	   String type,
																	   Collection<? extends GrantedAuthority> authorities) {
		return new AccountVerificationAuthenticationToken(principal, password, verificationCode, type, authorities);
	}

	@Override
	public Object getCredentials() {
		return this.password;
	}

	@Override
	public Object getPrincipal() {
		return this.principal;
	}

	@Override
	public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
		Assert.isTrue(!isAuthenticated, "Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");

		super.setAuthenticated(false);
	}

	@Override
	public void eraseCredentials() {
		super.eraseCredentials();
		this.password = null;
	}

	public String getVerificationCode() {
		return verificationCode;
	}

	public String getType() {
		return type;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setVerificationCode(String verificationCode) {
		this.verificationCode = verificationCode;
	}

	public void setType(String type) {
		this.type = type;
	}
}
