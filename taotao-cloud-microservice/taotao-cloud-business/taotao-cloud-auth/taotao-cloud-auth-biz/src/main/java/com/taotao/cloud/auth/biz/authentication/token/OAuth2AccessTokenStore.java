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

package com.taotao.cloud.auth.biz.authentication.token;

import com.taotao.boot.cache.redis.repository.RedisRepository;
import java.util.concurrent.TimeUnit;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.stereotype.Component;

/**
 * <p>OAuth 2.0 Endpoint 工具方法类 </p>
 * <p>
 * 新版 spring-security-oauth2-authorization-server 很多代码都是“包”级可访问的，外部无法使用。为了方便扩展将其提取出来，便于使用。
 * <p>
 * 代码内容与原包代码基本一致。
 *
 * @author shuigedeng
 * @version 2023.07
 * @since 2023-07-12 09:10:51
 */
@Component
public class OAuth2AccessTokenStore {
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

    private final RedisRepository redisRepository;

    public OAuth2AccessTokenStore(RedisRepository redisRepository) {
        this.redisRepository = redisRepository;
    }

    public void addToken(
            UserDetails userDetails,
            OAuth2AccessTokenResponse accessTokenResponse,
            long timeout,
            TimeUnit unit) {
        OAuth2AccessToken accessToken = accessTokenResponse.getAccessToken();
        if (accessToken != null) {
            String tokenValue = accessToken.getTokenValue();

            redisRepository.set(tokenValue, userDetails);

            redisRepository
                    .opsForValue()
                    .set(
                            PREFIX
                                    + OAUTH2_AUTHORIZATION_TOKEN_TYPE
                                    + OAuth2TokenType.ACCESS_TOKEN.getValue()
                                    + ":"
                                    + tokenValue,
                            tokenValue,
                            timeout,
                            unit);
        }

        OAuth2RefreshToken refreshToken = accessTokenResponse.getRefreshToken();
        if (refreshToken != null) {
            String tokenValue = refreshToken.getTokenValue();
            redisRepository
                    .opsForValue()
                    .set(
                            PREFIX
                                    + OAUTH2_AUTHORIZATION_TOKEN_TYPE
                                    + OAuth2TokenType.REFRESH_TOKEN.getValue()
                                    + ":"
                                    + tokenValue,
                            tokenValue,
                            timeout,
                            unit);
        }
    }

    public OAuth2AccessTokenResponse freshToken(String freshToken) {

        return null;
    }

    public String findByToken(String token) {
        return "";
    }
}
