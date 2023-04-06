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

package com.taotao.cloud.auth.biz.demo.jpa.repository;

import cn.herodotus.engine.data.core.repository.BaseRepository;
import cn.herodotus.engine.oauth2.data.jpa.entity.HerodotusAuthorization;
import jakarta.persistence.QueryHint;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.hibernate.jpa.AvailableHints;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.transaction.annotation.Transactional;

/**
 * Description: HerodotusAuthorizationRepository
 *
 * @author : gengwei.zheng
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
    Optional<HerodotusAuthorization> findByAuthorizationCode(String authorizationCode);

    /**
     * 根据 Access Token 查询 OAuth2 认证信息
     *
     * @param accessToken OAuth2 accessToken
     * @return OAuth2 认证信息 {@link HerodotusAuthorization}
     */
    @QueryHints(@QueryHint(name = AvailableHints.HINT_CACHEABLE, value = "true"))
    Optional<HerodotusAuthorization> findByAccessToken(String accessToken);

    /**
     * 根据 Refresh Token 查询 OAuth2 认证信息
     *
     * @param refreshToken OAuth2 refreshToken
     * @return OAuth2 认证信息 {@link HerodotusAuthorization}
     */
    @QueryHints(@QueryHint(name = AvailableHints.HINT_CACHEABLE, value = "true"))
    Optional<HerodotusAuthorization> findByRefreshToken(String refreshToken);

    /**
     * 根据客户端ID和用户名查询未过期Token
     *
     * @param registeredClientId 客户端ID
     * @param principalName 用户名称
     * @param localDateTime 时间
     * @return 认证信息列表
     */
    List<HerodotusAuthorization> findAllByRegisteredClientIdAndPrincipalNameAndAccessTokenExpiresAtAfter(
            String registeredClientId, String principalName, LocalDateTime localDateTime);

    /**
     * 根据 RefreshToken 过期时间，清理历史 Token信息
     *
     * <p>OAuth2Authorization 表中存在 AccessToken、OidcToken、RefreshToken 等三个过期时间。
     * 正常的删除逻辑应该是三个过期时间都已经过期才行。但是有特殊情况： 1. OidcToken 的过期时间有可能为空，这就增加了 SQL 处理的复杂度。 2. 逻辑上
     * RefreshToken 的过期应该是最长的(这是默认配置正确的情况) 因此，目前就简单的根据 RefreshToken过期时间进行处理
     *
     * @param localDateTime 时间
     */
    @Modifying
    @Transactional
    void deleteByRefreshTokenExpiresAtBefore(LocalDateTime localDateTime);
}
