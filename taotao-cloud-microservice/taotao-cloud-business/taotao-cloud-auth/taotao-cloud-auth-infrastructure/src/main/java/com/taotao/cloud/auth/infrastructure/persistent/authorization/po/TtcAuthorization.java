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

package com.taotao.cloud.auth.infrastructure.persistent.authorization.po;

import com.google.common.base.MoreObjects;
import com.taotao.cloud.auth.infrastructure.persistent.authorization.generator.TtcAuthorizationUuidGenerator;
import com.taotao.boot.data.jpa.tenant.AbstractEntity;
import com.taotao.boot.security.spring.constants.OAuth2Constants;
import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * <p>OAuth2 认证信息 </p>
 *
 * @author shuigedeng
 * @version 2023.07
 * @since 2023-07-10 17:11:47
 */
@Entity
@Table(
	name = "oauth2_authorization",
	indexes = {
		@Index(name = "oauth2_authorization_id_idx", columnList = "id"),
		@Index(name = "oauth2_authorization_rci_idx", columnList = "registered_client_id"),
		@Index(name = "oauth2_authorization_pn_idx", columnList = "principal_name")
	})
@Cacheable
@org.hibernate.annotations.Cache(
	usage = CacheConcurrencyStrategy.READ_WRITE,
	region = OAuth2Constants.REGION_OAUTH2_AUTHORIZATION)
public class TtcAuthorization extends AbstractEntity {

	/**
	 * id
	 */
	@Id
	@TtcAuthorizationUuidGenerator
	@Column(name = "id", nullable = false, length = 100)
	private String id;

	/**
	 * 注册客户端id
	 */
	@Column(name = "registered_client_id", nullable = false, length = 100)
	private String registeredClientId;

	/**
	 * 主体名称
	 */
	@Column(name = "principal_name", nullable = false, length = 200)
	private String principalName;

	/**
	 * 授权授权类型
	 */
	@Column(name = "authorization_grant_type", nullable = false, length = 100)
	private String authorizationGrantType;

	/**
	 * 授权范围
	 */
	@Column(name = "authorized_scopes", length = 1000)
	private String authorizedScopes;

	/**
	 * 属性
	 */
	@Column(name = "attributes", columnDefinition = "TEXT")
	private String attributes;

	/**
	 * 州
	 */
	@Column(name = "state", length = 500)
	private String state;

	/**
	 * 授权代码值
	 */
	@Column(name = "authorization_code_value", columnDefinition = "TEXT")
	private String authorizationCodeValue;

	/**
	 * 授权码发布于
	 */
	@Column(name = "authorization_code_issued_at")
	private LocalDateTime authorizationCodeIssuedAt;

	/**
	 * 授权码在
	 */
	@Column(name = "authorization_code_expires_at")
	private LocalDateTime authorizationCodeExpiresAt;

	/**
	 * 授权码元数据
	 */
	@Column(name = "authorization_code_metadata", columnDefinition = "TEXT")
	private String authorizationCodeMetadata;

	/**
	 * 访问令牌值
	 */
	@Column(name = "access_token_value", columnDefinition = "TEXT")
	private String accessTokenValue;

	/**
	 * 访问令牌在
	 */
	@Column(name = "access_token_issued_at")
	private LocalDateTime accessTokenIssuedAt;

	/**
	 * 访问令牌在
	 */
	@Column(name = "access_token_expires_at")
	private LocalDateTime accessTokenExpiresAt;

	/**
	 * 访问令牌元数据
	 */
	@Column(name = "access_token_metadata", columnDefinition = "TEXT")
	private String accessTokenMetadata;

	/**
	 * 访问令牌类型
	 */
	@Column(name = "access_token_type", length = 100)
	private String accessTokenType;

	/**
	 * 访问令牌范围
	 */
	@Column(name = "access_token_scopes", length = 1000)
	private String accessTokenScopes;

	/**
	 * oidc id令牌值
	 */
	@Column(name = "oidc_id_token_value", columnDefinition = "TEXT")
	private String oidcIdTokenValue;

	/**
	 * oidc id令牌在
	 */
	@Column(name = "oidc_id_token_issued_at")
	private LocalDateTime oidcIdTokenIssuedAt;

	/**
	 * oidc id令牌在
	 */
	@Column(name = "oidc_id_token_expires_at")
	private LocalDateTime oidcIdTokenExpiresAt;

	/**
	 * oidc id令牌元数据
	 */
	@Column(name = "oidc_id_token_metadata", columnDefinition = "TEXT")
	private String oidcIdTokenMetadata;

	/**
	 * oidc id令牌声明
	 */
	@Column(name = "oidc_id_token_claims", length = 2000)
	private String oidcIdTokenClaims;

	/**
	 * 刷新令牌值
	 */
	@Column(name = "refresh_token_value", columnDefinition = "TEXT")
	private String refreshTokenValue;

	/**
	 * 刷新在
	 */
	@Column(name = "refresh_token_issued_at")
	private LocalDateTime refreshTokenIssuedAt;

	/**
	 * 刷新令牌在
	 */
	@Column(name = "refresh_token_expires_at")
	private LocalDateTime refreshTokenExpiresAt;

	/**
	 * 刷新令牌元数据
	 */
	@Column(name = "refresh_token_metadata", columnDefinition = "TEXT")
	private String refreshTokenMetadata;

	/**
	 * 用户代码值
	 */
	@Column(name = "user_code_value", columnDefinition = "TEXT")
	private String userCodeValue;

	/**
	 * 用户代码发布于
	 */
	@Column(name = "user_code_issued_at")
	private LocalDateTime userCodeIssuedAt;

	/**
	 * 用户代码在
	 */
	@Column(name = "user_code_expires_at")
	private LocalDateTime userCodeExpiresAt;

	/**
	 * 用户代码元数据
	 */
	@Column(name = "user_code_metadata", columnDefinition = "TEXT")
	private String userCodeMetadata;

	/**
	 * 设备代码值
	 */
	@Column(name = "device_code_value", columnDefinition = "TEXT")
	private String deviceCodeValue;

	/**
	 * 设备代码发布于
	 */
	@Column(name = "device_code_issued_at")
	private LocalDateTime deviceCodeIssuedAt;

	/**
	 * 设备代码在
	 */
	@Column(name = "device_code_expires_at")
	private LocalDateTime deviceCodeExpiresAt;

	/**
	 * 设备代码元数据
	 */
	@Column(name = "device_code_metadata", columnDefinition = "TEXT")
	private String deviceCodeMetadata;

	/**
	 * 获取id
	 *
	 * @return {@link String }
	 * @since 2023-07-10 17:11:47
	 */
	public String getId() {
		return id;
	}

	/**
	 * 设置id
	 *
	 * @param id id
	 * @since 2023-07-10 17:11:48
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * 获取注册客户端id
	 *
	 * @return {@link String }
	 * @since 2023-07-10 17:11:48
	 */
	public String getRegisteredClientId() {
		return registeredClientId;
	}

	/**
	 * 设置注册客户端id
	 *
	 * @param registeredClientId 注册客户端id
	 * @since 2023-07-10 17:11:48
	 */
	public void setRegisteredClientId(String registeredClientId) {
		this.registeredClientId = registeredClientId;
	}

	/**
	 * 获取主体名称
	 *
	 * @return {@link String }
	 * @since 2023-07-10 17:11:48
	 */
	public String getPrincipalName() {
		return principalName;
	}

	/**
	 * 设置主体名称
	 *
	 * @param principalName 主体名称
	 * @since 2023-07-10 17:11:49
	 */
	public void setPrincipalName(String principalName) {
		this.principalName = principalName;
	}

	/**
	 * 获取授权授予类型
	 *
	 * @return {@link String }
	 * @since 2023-07-10 17:11:49
	 */
	public String getAuthorizationGrantType() {
		return authorizationGrantType;
	}

	/**
	 * 设置授权授权类型
	 *
	 * @param authorizationGrantType 授权授权类型
	 * @since 2023-07-10 17:11:50
	 */
	public void setAuthorizationGrantType(String authorizationGrantType) {
		this.authorizationGrantType = authorizationGrantType;
	}

	/**
	 * 获取授权范围
	 *
	 * @return {@link String }
	 * @since 2023-07-10 17:11:50
	 */
	public String getAuthorizedScopes() {
		return authorizedScopes;
	}

	/**
	 * 设置授权范围
	 *
	 * @param authorizedScopes 授权范围
	 * @since 2023-07-10 17:11:51
	 */
	public void setAuthorizedScopes(String authorizedScopes) {
		this.authorizedScopes = authorizedScopes;
	}

	/**
	 * 获取属性
	 *
	 * @return {@link String }
	 * @since 2023-07-10 17:11:51
	 */
	public String getAttributes() {
		return attributes;
	}

	/**
	 * 设置属性
	 *
	 * @param attributes 属性
	 * @since 2023-07-10 17:11:52
	 */
	public void setAttributes(String attributes) {
		this.attributes = attributes;
	}

	/**
	 * 获取状态
	 *
	 * @return {@link String }
	 * @since 2023-07-10 17:11:52
	 */
	public String getState() {
		return state;
	}

	/**
	 * 设置状态
	 *
	 * @param state 州
	 * @since 2023-07-10 17:11:52
	 */
	public void setState(String state) {
		this.state = state;
	}

	/**
	 * 获取授权代码值
	 *
	 * @return {@link String }
	 * @since 2023-07-10 17:11:53
	 */
	public String getAuthorizationCodeValue() {
		return authorizationCodeValue;
	}

	/**
	 * 设置授权代码值
	 *
	 * @param authorizationCodeValue 授权代码值
	 * @since 2023-07-10 17:11:53
	 */
	public void setAuthorizationCodeValue(String authorizationCodeValue) {
		this.authorizationCodeValue = authorizationCodeValue;
	}

	/**
	 * 获取授权码
	 *
	 * @return {@link LocalDateTime }
	 * @since 2023-07-10 17:11:54
	 */
	public LocalDateTime getAuthorizationCodeIssuedAt() {
		return authorizationCodeIssuedAt;
	}

	/**
	 * 设置在
	 *
	 * @param authorizationCodeIssuedAt 授权码发布于
	 * @since 2023-07-10 17:11:55
	 */
	public void setAuthorizationCodeIssuedAt(LocalDateTime authorizationCodeIssuedAt) {
		this.authorizationCodeIssuedAt = authorizationCodeIssuedAt;
	}

	/**
	 * 获取授权码过期时间为
	 *
	 * @return {@link LocalDateTime }
	 * @since 2023-07-10 17:11:55
	 */
	public LocalDateTime getAuthorizationCodeExpiresAt() {
		return authorizationCodeExpiresAt;
	}

	/**
	 * 设置授权代码在
	 *
	 * @param authorizationCodeExpiresAt 授权码在
	 * @since 2023-07-10 17:11:56
	 */
	public void setAuthorizationCodeExpiresAt(LocalDateTime authorizationCodeExpiresAt) {
		this.authorizationCodeExpiresAt = authorizationCodeExpiresAt;
	}

	/**
	 * 获取授权码元数据
	 *
	 * @return {@link String }
	 * @since 2023-07-10 17:11:56
	 */
	public String getAuthorizationCodeMetadata() {
		return authorizationCodeMetadata;
	}

	/**
	 * 设置授权码元数据
	 *
	 * @param authorizationCodeMetadata 授权码元数据
	 * @since 2023-07-10 17:11:57
	 */
	public void setAuthorizationCodeMetadata(String authorizationCodeMetadata) {
		this.authorizationCodeMetadata = authorizationCodeMetadata;
	}

	/**
	 * 获取访问令牌值
	 *
	 * @return {@link String }
	 * @since 2023-07-10 17:11:57
	 */
	public String getAccessTokenValue() {
		return accessTokenValue;
	}

	/**
	 * 设置访问令牌值
	 *
	 * @param accessTokenValue 访问令牌值
	 * @since 2023-07-10 17:11:58
	 */
	public void setAccessTokenValue(String accessTokenValue) {
		this.accessTokenValue = accessTokenValue;
	}

	/**
	 * 获取访问令牌发布于
	 *
	 * @return {@link LocalDateTime }
	 * @since 2023-07-10 17:11:58
	 */
	public LocalDateTime getAccessTokenIssuedAt() {
		return accessTokenIssuedAt;
	}

	/**
	 * 设置在
	 *
	 * @param accessTokenIssuedAt 访问令牌在
	 * @since 2023-07-10 17:11:59
	 */
	public void setAccessTokenIssuedAt(LocalDateTime accessTokenIssuedAt) {
		this.accessTokenIssuedAt = accessTokenIssuedAt;
	}

	/**
	 * 获取访问令牌在
	 *
	 * @return {@link LocalDateTime }
	 * @since 2023-07-10 17:11:59
	 */
	public LocalDateTime getAccessTokenExpiresAt() {
		return accessTokenExpiresAt;
	}

	/**
	 * 设置访问令牌在
	 *
	 * @param accessTokenExpiresAt 访问令牌在
	 * @since 2023-07-10 17:12:00
	 */
	public void setAccessTokenExpiresAt(LocalDateTime accessTokenExpiresAt) {
		this.accessTokenExpiresAt = accessTokenExpiresAt;
	}

	/**
	 * 获取访问令牌元数据
	 *
	 * @return {@link String }
	 * @since 2023-07-10 17:12:01
	 */
	public String getAccessTokenMetadata() {
		return accessTokenMetadata;
	}

	/**
	 * 设置访问令牌元数据
	 *
	 * @param accessTokenMetadata 访问令牌元数据
	 * @since 2023-07-10 17:12:01
	 */
	public void setAccessTokenMetadata(String accessTokenMetadata) {
		this.accessTokenMetadata = accessTokenMetadata;
	}

	/**
	 * 获取访问令牌类型
	 *
	 * @return {@link String }
	 * @since 2023-07-10 17:12:02
	 */
	public String getAccessTokenType() {
		return accessTokenType;
	}

	/**
	 * 设置访问令牌类型
	 *
	 * @param accessTokenType 访问令牌类型
	 * @since 2023-07-10 17:12:02
	 */
	public void setAccessTokenType(String accessTokenType) {
		this.accessTokenType = accessTokenType;
	}

	/**
	 * 获取访问令牌范围
	 *
	 * @return {@link String }
	 * @since 2023-07-10 17:12:03
	 */
	public String getAccessTokenScopes() {
		return accessTokenScopes;
	}

	/**
	 * 设置访问令牌范围
	 *
	 * @param accessTokenScopes 访问令牌范围
	 * @since 2023-07-10 17:12:04
	 */
	public void setAccessTokenScopes(String accessTokenScopes) {
		this.accessTokenScopes = accessTokenScopes;
	}

	/**
	 * 获取oidc id令牌值
	 *
	 * @return {@link String }
	 * @since 2023-07-10 17:12:04
	 */
	public String getOidcIdTokenValue() {
		return oidcIdTokenValue;
	}

	/**
	 * 设置oidc id令牌值
	 *
	 * @param oidcIdTokenValue oidc id令牌值
	 * @since 2023-07-10 17:12:05
	 */
	public void setOidcIdTokenValue(String oidcIdTokenValue) {
		this.oidcIdTokenValue = oidcIdTokenValue;
	}

	/**
	 * 获取oidc id令牌，发布时间为
	 *
	 * @return {@link LocalDateTime }
	 * @since 2023-07-10 17:12:05
	 */
	public LocalDateTime getOidcIdTokenIssuedAt() {
		return oidcIdTokenIssuedAt;
	}

	/**
	 * 设置oidc id令牌，在
	 *
	 * @param oidcIdTokenIssuedAt oidc id令牌在
	 * @since 2023-07-10 17:12:06
	 */
	public void setOidcIdTokenIssuedAt(LocalDateTime oidcIdTokenIssuedAt) {
		this.oidcIdTokenIssuedAt = oidcIdTokenIssuedAt;
	}

	/**
	 * 获取oidc id令牌过期时间为
	 *
	 * @return {@link LocalDateTime }
	 * @since 2023-07-10 17:12:06
	 */
	public LocalDateTime getOidcIdTokenExpiresAt() {
		return oidcIdTokenExpiresAt;
	}

	/**
	 * 设置oidc id令牌在
	 *
	 * @param oidcIdTokenExpiresAt oidc id令牌在
	 * @since 2023-07-10 17:12:07
	 */
	public void setOidcIdTokenExpiresAt(LocalDateTime oidcIdTokenExpiresAt) {
		this.oidcIdTokenExpiresAt = oidcIdTokenExpiresAt;
	}

	/**
	 * 获取oidc id令牌元数据
	 *
	 * @return {@link String }
	 * @since 2023-07-10 17:12:07
	 */
	public String getOidcIdTokenMetadata() {
		return oidcIdTokenMetadata;
	}

	/**
	 * 设置oidc id令牌元数据
	 *
	 * @param oidcIdTokenMetadata oidc id令牌元数据
	 * @since 2023-07-10 17:12:08
	 */
	public void setOidcIdTokenMetadata(String oidcIdTokenMetadata) {
		this.oidcIdTokenMetadata = oidcIdTokenMetadata;
	}

	/**
	 * 获取oidc id令牌声明
	 *
	 * @return {@link String }
	 * @since 2023-07-10 17:12:09
	 */
	public String getOidcIdTokenClaims() {
		return oidcIdTokenClaims;
	}

	/**
	 * 设置oidc id令牌声明
	 *
	 * @param oidcIdTokenClaims oidc id令牌声明
	 * @since 2023-07-10 17:12:09
	 */
	public void setOidcIdTokenClaims(String oidcIdTokenClaims) {
		this.oidcIdTokenClaims = oidcIdTokenClaims;
	}

	/**
	 * 获取刷新令牌值
	 *
	 * @return {@link String }
	 * @since 2023-07-10 17:12:10
	 */
	public String getRefreshTokenValue() {
		return refreshTokenValue;
	}

	/**
	 * 设置刷新令牌值
	 *
	 * @param refreshTokenValue 刷新令牌值
	 * @since 2023-07-10 17:12:11
	 */
	public void setRefreshTokenValue(String refreshTokenValue) {
		this.refreshTokenValue = refreshTokenValue;
	}

	/**
	 * 获取在
	 *
	 * @return {@link LocalDateTime }
	 * @since 2023-07-10 17:12:11
	 */
	public LocalDateTime getRefreshTokenIssuedAt() {
		return refreshTokenIssuedAt;
	}

	/**
	 * 设置在发出刷新令牌
	 *
	 * @param refreshTokenIssuedAt 刷新在
	 * @since 2023-07-10 17:12:12
	 */
	public void setRefreshTokenIssuedAt(LocalDateTime refreshTokenIssuedAt) {
		this.refreshTokenIssuedAt = refreshTokenIssuedAt;
	}

	/**
	 * 获取刷新令牌在
	 *
	 * @return {@link LocalDateTime }
	 * @since 2023-07-10 17:12:12
	 */
	public LocalDateTime getRefreshTokenExpiresAt() {
		return refreshTokenExpiresAt;
	}

	/**
	 * 设置刷新令牌在
	 *
	 * @param refreshTokenExpiresAt 刷新令牌在
	 * @since 2023-07-10 17:12:13
	 */
	public void setRefreshTokenExpiresAt(LocalDateTime refreshTokenExpiresAt) {
		this.refreshTokenExpiresAt = refreshTokenExpiresAt;
	}

	/**
	 * 获取刷新令牌元数据
	 *
	 * @return {@link String }
	 * @since 2023-07-10 17:12:13
	 */
	public String getRefreshTokenMetadata() {
		return refreshTokenMetadata;
	}

	/**
	 * 设置刷新令牌元数据
	 *
	 * @param refreshTokenMetadata 刷新令牌元数据
	 * @since 2023-07-10 17:12:14
	 */
	public void setRefreshTokenMetadata(String refreshTokenMetadata) {
		this.refreshTokenMetadata = refreshTokenMetadata;
	}

	/**
	 * 获取用户代码值
	 *
	 * @return {@link String }
	 * @since 2023-07-10 17:12:14
	 */
	public String getUserCodeValue() {
		return userCodeValue;
	}

	/**
	 * 设置用户代码值
	 *
	 * @param userCodeValue 用户代码值
	 * @since 2023-07-10 17:12:15
	 */
	public void setUserCodeValue(String userCodeValue) {
		this.userCodeValue = userCodeValue;
	}

	/**
	 * 获取用户代码
	 *
	 * @return {@link LocalDateTime }
	 * @since 2023-07-10 17:12:15
	 */
	public LocalDateTime getUserCodeIssuedAt() {
		return userCodeIssuedAt;
	}

	/**
	 * 设置在以下位置发布用户代码
	 *
	 * @param userCodeIssuedAt 用户代码发布于
	 * @since 2023-07-10 17:12:16
	 */
	public void setUserCodeIssuedAt(LocalDateTime userCodeIssuedAt) {
		this.userCodeIssuedAt = userCodeIssuedAt;
	}

	/**
	 * 获取用户代码过期时间为
	 *
	 * @return {@link LocalDateTime }
	 * @since 2023-07-10 17:12:16
	 */
	public LocalDateTime getUserCodeExpiresAt() {
		return userCodeExpiresAt;
	}

	/**
	 * 设置用户代码在
	 *
	 * @param userCodeExpiresAt 用户代码在
	 * @since 2023-07-10 17:12:17
	 */
	public void setUserCodeExpiresAt(LocalDateTime userCodeExpiresAt) {
		this.userCodeExpiresAt = userCodeExpiresAt;
	}

	/**
	 * 获取用户代码元数据
	 *
	 * @return {@link String }
	 * @since 2023-07-10 17:12:18
	 */
	public String getUserCodeMetadata() {
		return userCodeMetadata;
	}

	/**
	 * 设置用户代码元数据
	 *
	 * @param userCodeMetadata 用户代码元数据
	 * @since 2023-07-10 17:12:18
	 */
	public void setUserCodeMetadata(String userCodeMetadata) {
		this.userCodeMetadata = userCodeMetadata;
	}

	/**
	 * 获取设备代码值
	 *
	 * @return {@link String }
	 * @since 2023-07-10 17:12:19
	 */
	public String getDeviceCodeValue() {
		return deviceCodeValue;
	}

	/**
	 * 设置设备代码值
	 *
	 * @param deviceCodeValue 设备代码值
	 * @since 2023-07-10 17:12:19
	 */
	public void setDeviceCodeValue(String deviceCodeValue) {
		this.deviceCodeValue = deviceCodeValue;
	}

	/**
	 * 获取设备代码
	 *
	 * @return {@link LocalDateTime }
	 * @since 2023-07-10 17:12:20
	 */
	public LocalDateTime getDeviceCodeIssuedAt() {
		return deviceCodeIssuedAt;
	}

	/**
	 * 设置在以下位置发布设备代码
	 *
	 * @param deviceCodeIssuedAt 设备代码发布于
	 * @since 2023-07-10 17:12:20
	 */
	public void setDeviceCodeIssuedAt(LocalDateTime deviceCodeIssuedAt) {
		this.deviceCodeIssuedAt = deviceCodeIssuedAt;
	}

	/**
	 * 获取设备代码在
	 *
	 * @return {@link LocalDateTime }
	 * @since 2023-07-10 17:12:21
	 */
	public LocalDateTime getDeviceCodeExpiresAt() {
		return deviceCodeExpiresAt;
	}

	/**
	 * 设置设备代码在
	 *
	 * @param deviceCodeExpiresAt 设备代码在
	 * @since 2023-07-10 17:12:21
	 */
	public void setDeviceCodeExpiresAt(LocalDateTime deviceCodeExpiresAt) {
		this.deviceCodeExpiresAt = deviceCodeExpiresAt;
	}

	/**
	 * 获取设备代码元数据
	 *
	 * @return {@link String }
	 * @since 2023-07-10 17:12:22
	 */
	public String getDeviceCodeMetadata() {
		return deviceCodeMetadata;
	}

	/**
	 * 设置设备代码元数据
	 *
	 * @param deviceCodeMetadata 设备代码元数据
	 * @since 2023-07-10 17:12:23
	 */
	public void setDeviceCodeMetadata(String deviceCodeMetadata) {
		this.deviceCodeMetadata = deviceCodeMetadata;
	}

	/**
	 * 要字符串
	 *
	 * @return {@link String }
	 * @since 2023-07-10 17:12:23
	 */
	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
			.add("id", id)
			.add("registeredClientId", registeredClientId)
			.add("principalName", principalName)
			.add("attributes", attributes)
			.add("state", state)
			.toString();
	}
}
