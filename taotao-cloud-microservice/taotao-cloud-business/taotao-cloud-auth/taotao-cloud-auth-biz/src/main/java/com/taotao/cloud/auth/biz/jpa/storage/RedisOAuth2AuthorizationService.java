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

package com.taotao.cloud.auth.biz.jpa.storage;

import com.taotao.boot.cache.redis.repository.RedisRepository;
import com.taotao.boot.common.utils.log.LogUtils;
import com.taotao.cloud.auth.biz.jpa.service.TtcAuthorizationService;
import java.util.concurrent.TimeUnit;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;

/**
 * An {@link OAuth2AuthorizationService} that stores {@link OAuth2Authorization}'s in-memory.
 *
 * <p>
 * <b>NOTE:</b> This implementation should ONLY be used during development/testing.
 *
 * @author shuigedeng
 * @version 2023.07
 * @since 2023-07-10 17:10:58
 */
public final class RedisOAuth2AuthorizationService extends JpaOAuth2AuthorizationService {

    /**
     * 根据 id 查询时放入Redis中的部分 key
     */
    public static final String OAUTH2_AUTHORIZATION_ID = ":oauth2_authorization:id:";

    /**
     * 根据 token类型、token 查询时放入Redis中的部分 key
     */
    public static final String OAUTH2_AUTHORIZATION_TOKEN_TYPE = ":oauth2_authorization:tokenType:";

    /**
     * 授权超时
     */
    public static final long AUTHORIZATION_TIMEOUT = 300L;

    /**
     * 前缀
     */
    public static final String PREFIX = "spring-authorization-server";

    /**
     * redis存储库
     */
    private final RedisRepository redisRepository;

    /**
     * redis oauth2授权服务
     *
     * @param ttcAuthorizationService 希罗多德授权服务
     * @param registeredClientRepository    注册客户端存储库
     * @param redisRepository               redis存储库
     * @return
     * @since 2023-07-10 17:10:59
     */
    public RedisOAuth2AuthorizationService(
            TtcAuthorizationService ttcAuthorizationService,
            RegisteredClientRepository registeredClientRepository,
            RedisRepository redisRepository) {
        super(ttcAuthorizationService, registeredClientRepository);
        this.redisRepository = redisRepository;
    }

    /**
     * 保存
     *
     * @param authorization 授权
     * @since 2023-07-10 17:10:59
     */
    @Override
    public void save(OAuth2Authorization authorization) {
        if (authorization != null) {
            set(authorization, AUTHORIZATION_TIMEOUT, TimeUnit.SECONDS);
            super.save(authorization);
        }
    }

    /**
     * 移除
     *
     * @param authorization 授权
     * @since 2023-07-10 17:10:59
     */
    @Override
    public void remove(OAuth2Authorization authorization) {
        if (authorization != null) {
            redisRepository.del(PREFIX + OAUTH2_AUTHORIZATION_ID + authorization.getId());

            OAuth2Authorization.Token<OAuth2AccessToken> accessToken =
                    authorization.getAccessToken();
            if (accessToken != null) {
                OAuth2AccessToken token = accessToken.getToken();
                String tokenValue = token.getTokenValue();
                redisRepository.del(
                        PREFIX
                                + OAUTH2_AUTHORIZATION_TOKEN_TYPE
                                + OAuth2TokenType.ACCESS_TOKEN.getValue()
                                + ":"
                                + tokenValue);
            }

            OAuth2Authorization.Token<OAuth2RefreshToken> refreshToken =
                    authorization.getRefreshToken();
            if (refreshToken != null) {
                OAuth2RefreshToken token = refreshToken.getToken();
                String tokenValue = token.getTokenValue();
                redisRepository.del(
                        PREFIX
                                + OAUTH2_AUTHORIZATION_TOKEN_TYPE
                                + OAuth2TokenType.REFRESH_TOKEN.getValue()
                                + ":"
                                + tokenValue);
            }

            super.remove(authorization);
        }
    }

    /**
     * 按id查找
     *
     * @param id id
     * @return {@link OAuth2Authorization }
     * @since 2023-07-10 17:10:59
     */
    @Override
    public OAuth2Authorization findById(String id) {

        OAuth2Authorization oauth2AuthorizationRedis =
                (OAuth2Authorization)
                        redisRepository.opsForValue().get(PREFIX + OAUTH2_AUTHORIZATION_ID + id);

        OAuth2Authorization oauth2AuthorizationResult;
        OAuth2Authorization oauth2AuthorizationByDatabase;

        if (oauth2AuthorizationRedis == null) {
            oauth2AuthorizationByDatabase = super.findById(id);
            LogUtils.debug("根据 id：{} 直接查询数据库中的授权：{}", id, oauth2AuthorizationByDatabase);

            if (oauth2AuthorizationByDatabase != null) {
                set(oauth2AuthorizationByDatabase, AUTHORIZATION_TIMEOUT, TimeUnit.SECONDS);
            }

            oauth2AuthorizationResult = oauth2AuthorizationByDatabase;
        } else {
            LogUtils.debug("根据 id：{} 直接查询Redis中的授权：{}", id, oauth2AuthorizationRedis);
            oauth2AuthorizationResult = oauth2AuthorizationRedis;
        }

        return oauth2AuthorizationResult;
    }

    /**
     * 按令牌查找
     *
     * @param token     令牌
     * @param tokenType 令牌类型
     * @return {@link OAuth2Authorization }
     * @since 2023-07-10 17:10:59
     */
    @Override
    public OAuth2Authorization findByToken(String token, OAuth2TokenType tokenType) {
        assert tokenType != null;
        String tokenTypeValue = tokenType.getValue();

        OAuth2Authorization oauth2AuthorizationByRedis =
                (OAuth2Authorization)
                        redisRepository
                                .opsForValue()
                                .get(
                                        PREFIX
                                                + OAUTH2_AUTHORIZATION_TOKEN_TYPE
                                                + tokenTypeValue
                                                + ":"
                                                + token);

        OAuth2Authorization oauth2AuthorizationResult;
        OAuth2Authorization oauth2AuthorizationByDatabase;

        if (oauth2AuthorizationByRedis == null) {
            oauth2AuthorizationByDatabase = super.findByToken(token, tokenType);
            LogUtils.debug(
                    "根据 token：{}、tokenType：{} 直接查询数据库中的客户：{}",
                    token,
                    tokenType,
                    oauth2AuthorizationByDatabase);

            if (oauth2AuthorizationByDatabase != null) {
                set(oauth2AuthorizationByDatabase, AUTHORIZATION_TIMEOUT, TimeUnit.SECONDS);
            }

            oauth2AuthorizationResult = oauth2AuthorizationByDatabase;
        } else {
            LogUtils.debug(
                    "根据 token：{}、tokenType：{} 直接查询Redis中的客户：{}",
                    token,
                    tokenType,
                    oauth2AuthorizationByRedis);
            oauth2AuthorizationResult = oauth2AuthorizationByRedis;
        }

        return oauth2AuthorizationResult;
    }

    /**
     * 设置
     *
     * @param authorization 授权
     * @param timeout       超时
     * @param unit          单位
     * @since 2023-07-10 17:10:59
     */
    public void set(OAuth2Authorization authorization, long timeout, TimeUnit unit) {
        redisRepository
                .opsForValue()
                .set(
                        PREFIX + OAUTH2_AUTHORIZATION_ID + authorization.getId(),
                        authorization,
                        timeout,
                        unit);

        OAuth2Authorization.Token<OAuth2AccessToken> accessToken = authorization.getAccessToken();
        if (accessToken != null) {
            OAuth2AccessToken token = accessToken.getToken();
            if (token != null) {
                String tokenValue = token.getTokenValue();
                redisRepository
                        .opsForValue()
                        .set(
                                PREFIX
                                        + OAUTH2_AUTHORIZATION_TOKEN_TYPE
                                        + OAuth2TokenType.ACCESS_TOKEN.getValue()
                                        + ":"
                                        + tokenValue,
                                authorization,
                                timeout,
                                unit);
            }
        }

        OAuth2Authorization.Token<OAuth2RefreshToken> refreshToken =
                authorization.getRefreshToken();
        if (refreshToken != null) {
            OAuth2RefreshToken token = refreshToken.getToken();
            if (token != null) {
                String tokenValue = token.getTokenValue();
                redisRepository
                        .opsForValue()
                        .set(
                                PREFIX
                                        + OAUTH2_AUTHORIZATION_TOKEN_TYPE
                                        + OAuth2TokenType.REFRESH_TOKEN.getValue()
                                        + ":"
                                        + tokenValue,
                                authorization,
                                timeout,
                                unit);
            }
        }
    }
}
