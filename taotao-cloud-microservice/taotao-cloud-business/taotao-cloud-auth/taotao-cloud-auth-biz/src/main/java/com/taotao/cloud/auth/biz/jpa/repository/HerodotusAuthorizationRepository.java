/*
 * Copyright (c) 2020-2030 ZHENGGENGWEI(码匠君)<herodotus@aliyun.com>
 *
 * Dante Engine licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * <http://www.apache.org/licenses/LICENSE-2.0>
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
 * 4.分发源码时候，请注明软件出处 <https://gitee.com/herodotus/dante-engine>
 * 5.在修改包名，模块名称，项目代码等时，请注明软件出处 <https://gitee.com/herodotus/dante-engine>
 * 6.若您的项目无法满足以上几点，可申请商业授权
 */

package com.taotao.cloud.auth.biz.jpa.repository;

import com.taotao.cloud.auth.biz.jpa.entity.HerodotusAuthorization;
import com.taotao.cloud.data.jpa.base.repository.BaseRepository;
import jakarta.persistence.QueryHint;
import org.hibernate.jpa.AvailableHints;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * <p>Description: HerodotusAuthorizationRepository </p>
 *
 * 
 * @date : 2022/2/25 21:05
 */
public interface HerodotusAuthorizationRepository extends BaseRepository<HerodotusAuthorization, String> {

	/**
	 * 根据 State 查询 OAuth2 认证信息
	 *
	 * @param state OAuth2 Authorization Code 模式参数 State
	 * @return OAuth2 认证信息 {@link HerodotusAuthorization}
	 */
	@QueryHints(@QueryHint(name = AvailableHints.HINT_CACHEABLE, value = "true"))
	Optional<HerodotusAuthorization> findByState(String state);

	/**
	 * 根据 authorizationCode 查询 OAuth2 认证信息
	 *
	 * @param authorizationCode OAuth2 Authorization Code 模式参数 code
	 * @return OAuth2 认证信息 {@link HerodotusAuthorization}
	 */
	@QueryHints(@QueryHint(name = AvailableHints.HINT_CACHEABLE, value = "true"))
	Optional<HerodotusAuthorization> findByAuthorizationCodeValue(String authorizationCode);

	/**
	 * 根据 Access Token 查询 OAuth2 认证信息
	 *
	 * @param accessToken OAuth2 accessToken
	 * @return OAuth2 认证信息 {@link HerodotusAuthorization}
	 */
	@QueryHints(@QueryHint(name = AvailableHints.HINT_CACHEABLE, value = "true"))
	Optional<HerodotusAuthorization> findByAccessTokenValue(String accessToken);

	/**
	 * 根据 Refresh Token 查询 OAuth2 认证信息
	 *
	 * @param refreshToken OAuth2 refreshToken
	 * @return OAuth2 认证信息 {@link HerodotusAuthorization}
	 */
	@QueryHints(@QueryHint(name = AvailableHints.HINT_CACHEABLE, value = "true"))
	Optional<HerodotusAuthorization> findByRefreshTokenValue(String refreshToken);

	/**
	 * 根据 Id Token 查询 OAuth2 认证信息
	 *
	 * @param idToken OAuth2 idToken
	 * @return OAuth2 认证信息 {@link HerodotusAuthorization}
	 */
	@QueryHints(@QueryHint(name = AvailableHints.HINT_CACHEABLE, value = "true"))
	Optional<HerodotusAuthorization> findByOidcIdTokenValue(String idToken);

	/**
	 * 根据 User Code 查询 OAuth2 认证信息
	 *
	 * @param userCode OAuth2 userCode
	 * @return OAuth2 认证信息 {@link HerodotusAuthorization}
	 */
	@QueryHints(@QueryHint(name = AvailableHints.HINT_CACHEABLE, value = "true"))
	Optional<HerodotusAuthorization> findByUserCodeValue(String userCode);

	/**
	 * 根据 Device Code 查询 OAuth2 认证信息
	 *
	 * @param deviceCode OAuth2 deviceCode
	 * @return OAuth2 认证信息 {@link HerodotusAuthorization}
	 */
	@QueryHints(@QueryHint(name = AvailableHints.HINT_CACHEABLE, value = "true"))
	Optional<HerodotusAuthorization> findByDeviceCodeValue(String deviceCode);

	/**
	 * 根据客户端ID和用户名查询未过期Token
	 *
	 * @param registeredClientId 客户端ID
	 * @param principalName      用户名称
	 * @param localDateTime      时间
	 * @return 认证信息列表
	 */
	List<HerodotusAuthorization> findAllByRegisteredClientIdAndPrincipalNameAndAccessTokenExpiresAtAfter(String registeredClientId, String principalName, LocalDateTime localDateTime);

	/**
	 * 根据 RefreshToken 过期时间，清理历史 Token信息
	 * <p>
	 * OAuth2Authorization 表中存在 AccessToken、OidcToken、RefreshToken 等三个过期时间。
	 * 正常的删除逻辑应该是三个过期时间都已经过期才行。但是有特殊情况：
	 * 1. OidcToken 的过期时间有可能为空，这就增加了 SQL 处理的复杂度。
	 * 2. 逻辑上 RefreshToken 的过期应该是最长的(这是默认配置正确的情况)
	 * 因此，目前就简单的根据 RefreshToken过期时间进行处理
	 *
	 * @param localDateTime 时间
	 */
	@Modifying
	@Transactional
	void deleteByRefreshTokenExpiresAtBefore(LocalDateTime localDateTime);


	@Modifying
	@Transactional
	@Query(
		"""
			  update HerodotusAuthorization set
				registeredClientId = :#{#herodotusAuthorization.registeredClientId} ,
				principalName = :#{#herodotusAuthorization.principalName} ,
				authorizationGrantType = :#{#herodotusAuthorization.authorizationGrantType} ,
				authorizedScopes = :#{#herodotusAuthorization.authorizedScopes} ,
				attributes = :#{#herodotusAuthorization.attributes} ,
				state = :#{#herodotusAuthorization.state} ,
				authorizationCodeValue = :#{#herodotusAuthorization.authorizationCodeValue} ,
				authorizationCodeIssuedAt = :#{#herodotusAuthorization.authorizationCodeIssuedAt} ,
				authorizationCodeExpiresAt = :#{#herodotusAuthorization.authorizationCodeExpiresAt} ,
				authorizationCodeMetadata = :#{#herodotusAuthorization.authorizationCodeMetadata} ,
				accessTokenValue = :#{#herodotusAuthorization.accessTokenValue} ,
				accessTokenIssuedAt = :#{#herodotusAuthorization.accessTokenIssuedAt} ,
				accessTokenExpiresAt = :#{#herodotusAuthorization.accessTokenExpiresAt} ,
				accessTokenMetadata = :#{#herodotusAuthorization.accessTokenMetadata} ,
				accessTokenType = :#{#herodotusAuthorization.accessTokenType} ,
				accessTokenScopes = :#{#herodotusAuthorization.accessTokenScopes} ,
				oidcIdTokenValue = :#{#herodotusAuthorization.oidcIdTokenValue} ,
				oidcIdTokenIssuedAt = :#{#herodotusAuthorization.oidcIdTokenIssuedAt} ,
				oidcIdTokenExpiresAt = :#{#herodotusAuthorization.oidcIdTokenExpiresAt} ,
				oidcIdTokenMetadata = :#{#herodotusAuthorization.oidcIdTokenMetadata} ,
				oidcIdTokenClaims = :#{#herodotusAuthorization.oidcIdTokenClaims} ,
				refreshTokenValue = :#{#herodotusAuthorization.refreshTokenValue} ,
				refreshTokenIssuedAt = :#{#herodotusAuthorization.refreshTokenIssuedAt} ,
				refreshTokenExpiresAt = :#{#herodotusAuthorization.refreshTokenExpiresAt} ,
				refreshTokenMetadata = :#{#herodotusAuthorization.refreshTokenMetadata} ,
				userCodeValue = :#{#herodotusAuthorization.userCodeValue} ,
				userCodeIssuedAt = :#{#herodotusAuthorization.userCodeIssuedAt} ,
				userCodeExpiresAt = :#{#herodotusAuthorization.userCodeExpiresAt} ,
				userCodeMetadata = :#{#herodotusAuthorization.userCodeMetadata} ,
				deviceCodeValue = :#{#herodotusAuthorization.deviceCodeValue} ,
				deviceCodeIssuedAt = :#{#herodotusAuthorization.deviceCodeIssuedAt} ,
				deviceCodeExpiresAt = :#{#herodotusAuthorization.deviceCodeExpiresAt} ,
				deviceCodeMetadata =  :#{#herodotusAuthorization.deviceCodeMetadata}
				where id = :#{#herodotusAuthorization.id}
			""")
	Integer updateBy(@Param("herodotusAuthorization") HerodotusAuthorization herodotusAuthorization);
}
