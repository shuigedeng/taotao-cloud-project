///*
// * Copyright 2002-2021 the original author or authors.
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *      https://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//package com.taotao.cloud.security.token;
//
//import org.springframework.security.authentication.AbstractAuthenticationToken;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.SpringSecurityCoreVersion;
//
//import java.util.Collection;
//
///**
// * 手机认证token
// *
// * @author dengtao
// * @version 1.0.0
// * @since 2020/4/29 20:26
// */
//public class TaotaoCloudAuthenticationToken extends AbstractAuthenticationToken {
//
//	private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;
//
//	/**
//	 * 1-手机号码 2-qrCode 3-(username/手机号码/邮箱/nickname)
//	 */
//	private final Object principal;
//	/**
//	 * 1-手机验证码值 2-null 3-密码值
//	 */
//	private final Object credentials;
//	/**
//	 * 会员用户登录类型 手机验证码登录-phone 二维码扫码登陆-qr 账号密码登陆-password
//	 */
//	private final String grantType;
//	/**
//	 * 登录类型 后台管理用户backend 会员用户member(只提供password登录)
//	 */
//	private final String userType;
//
//	public TaotaoCloudAuthenticationToken(Object principal,
//		Object credentials,
//		String grantType,
//		String userType) {
//		super(null);
//		this.principal = principal;
//		this.credentials = credentials;
//		this.grantType = grantType;
//		this.userType = userType;
//		setAuthenticated(false);
//	}
//
//	public TaotaoCloudAuthenticationToken(Object principal,
//		Object credentials,
//		String grantType,
//		String userType,
//		Collection<? extends GrantedAuthority> authorities) {
//		super(authorities);
//		this.principal = principal;
//		this.credentials = credentials;
//		this.grantType = grantType;
//		this.userType = userType;
//		super.setAuthenticated(true);
//	}
//
//	@Override
//	public Object getCredentials() {
//		return credentials;
//	}
//
//	@Override
//	public Object getPrincipal() {
//		return this.principal;
//	}
//
//	@Override
//	public void setAuthenticated(boolean isAuthenticated) {
//		if (isAuthenticated) {
//			throw new IllegalArgumentException(
//				"Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
//		}
//		super.setAuthenticated(false);
//	}
//
//	@Override
//	public String getName() {
//		return null == this.principal ? null : this.principal.toString();
//	}
//
//	@Override
//	public void eraseCredentials() {
//		super.eraseCredentials();
//	}
//
//	public String getGrantType() {
//		return grantType;
//	}
//
//	public String getUserType() {
//		return userType;
//	}
//}
