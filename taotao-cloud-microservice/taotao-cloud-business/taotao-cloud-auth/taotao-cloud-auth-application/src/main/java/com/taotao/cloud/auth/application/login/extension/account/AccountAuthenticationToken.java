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

package com.taotao.cloud.auth.application.login.extension.account;

import java.io.Serial;
import java.util.Collection;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityCoreVersion;
import org.springframework.util.Assert;

/**
 * 账户认证令牌
 *
 * @author shuigedeng
 * @version 2023.04
 * @since 2023-06-29 11:00:19
 */
public class AccountAuthenticationToken extends AbstractAuthenticationToken {

	@Serial
	private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

	// principal：能唯一标识用户身份的属性，一个用户可以有多个principal
	// 如登录的唯一标识，用户可以使用用户名或手机或邮箱进行登录，这些principal是让别人知道的
	private final Object principal;
	// credential：凭证，用户才知道的，简单的说就是密码
	// 如：手机开锁，可以使用屏幕密码也可以使用人脸识别，屏幕密码和人脸是你个人（用户）才拥有的；
	// principals 和 credentials 组合就是用户名 / 密码了。
	private String password;
	private String type;

	/**
	 * 此构造函数用来初始化未授信凭据.
	 *
	 * @param principal the principal
	 * @param password  the captcha
	 */
	public AccountAuthenticationToken(Object principal, String password, String type) {
		super(null);
		this.principal = principal;
		this.password = password;
		this.type = type;

		setAuthenticated(false);
	}

	/**
	 * 此构造函数用来初始化授信凭据.
	 *
	 * @param principal   the principal
	 * @param password    the captcha
	 * @param authorities the authorities
	 */
	public AccountAuthenticationToken(
		Object principal, String password, String type,
		Collection<? extends GrantedAuthority> authorities) {
		super(authorities);
		this.principal = principal;
		this.password = password;
		this.type = type;

		// must use super, as we override
		super.setAuthenticated(true);
	}

	public static AccountAuthenticationToken unauthenticated(Object principal, String password,
		String type) {
		return new AccountAuthenticationToken(principal, password, type);
	}

	public static AccountAuthenticationToken authenticated(
		Object principal, String password, String type,
		Collection<? extends GrantedAuthority> authorities) {
		return new AccountAuthenticationToken(principal, password, type, authorities);
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
		Assert.isTrue(
			!isAuthenticated,
			"Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
		super.setAuthenticated(false);
	}

	@Override
	public void eraseCredentials() {
		super.eraseCredentials();
		this.password = null;
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

	public void setType(String type) {
		this.type = type;
	}
}
