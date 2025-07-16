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
import com.taotao.cloud.auth.biz.jpa.service.TtcAuthorizationConsentService;
import java.util.concurrent.TimeUnit;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsent;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;

/**
 * An {@link OAuth2AuthorizationConsentService} that stores {@link OAuth2AuthorizationConsent}'s
 * in-memory.
 *
 * <p>
 * <b>NOTE:</b> This implementation should ONLY be used during development/testing.
 *
 * @author shuigedeng
 * @version 2023.07
 * @since 2023-07-10 17:10:53
 */
public final class RedisOAuth2AuthorizationConsentService
        extends JpaOAuth2AuthorizationConsentService {

    /**
     * 查询时放入Redis中的部分 key
     */
    public static final String OAUTH2_AUTHORIZATION_CONSENT = ":oauth2_authorization_consent:";

    /**
     * 前缀
     */
    public static final String PREFIX = "spring-authorization-server";

    /**
     * 授权同意超时
     */
    public static final long AUTHORIZATION_CONSENT_TIMEOUT = 300L;

    /**
     * redis存储库
     */
    private final RedisRepository redisRepository;

    /**
     * redis oauth2授权同意服务
     *
     * @param ttcAuthorizationConsentService 希罗多德授权同意服务
     * @param registeredClientRepository           注册客户端存储库
     * @param redisRepository                      redis存储库
     * @return
     * @since 2023-07-10 17:10:53
     */
    public RedisOAuth2AuthorizationConsentService(
            TtcAuthorizationConsentService ttcAuthorizationConsentService,
            RegisteredClientRepository registeredClientRepository,
            RedisRepository redisRepository) {
        super(ttcAuthorizationConsentService, registeredClientRepository);
        this.redisRepository = redisRepository;
    }

    /**
     * 保存
     *
     * @param authorizationConsent 授权同意书
     * @since 2023-07-10 17:10:53
     */
    @Override
    public void save(OAuth2AuthorizationConsent authorizationConsent) {
        if (authorizationConsent != null) {
            set(authorizationConsent, AUTHORIZATION_CONSENT_TIMEOUT, TimeUnit.SECONDS);
            super.save(authorizationConsent);
        }
    }

    /**
     * 移除
     *
     * @param authorizationConsent 授权同意书
     * @since 2023-07-10 17:10:53
     */
    @Override
    public void remove(OAuth2AuthorizationConsent authorizationConsent) {
        if (authorizationConsent != null) {
            String registeredClientId = authorizationConsent.getRegisteredClientId();
            String principalName = authorizationConsent.getPrincipalName();
            redisRepository.del(
                    principalName
                            + OAUTH2_AUTHORIZATION_CONSENT
                            + registeredClientId
                            + ":"
                            + principalName);
            super.remove(authorizationConsent);
        }
    }

    /**
     * 按id查找
     *
     * @param registeredClientId 注册客户端id
     * @param principalName      主体名称
     * @return {@link OAuth2AuthorizationConsent }
     * @since 2023-07-10 17:10:53
     */
    @Override
    public OAuth2AuthorizationConsent findById(String registeredClientId, String principalName) {

        OAuth2AuthorizationConsent oauth2AuthorizationConsentRedis =
                (OAuth2AuthorizationConsent)
                        redisRepository
                                .opsForValue()
                                .get(
                                        PREFIX
                                                + OAUTH2_AUTHORIZATION_CONSENT
                                                + registeredClientId
                                                + ":"
                                                + principalName);

        OAuth2AuthorizationConsent oauth2AuthorizationResult;
        OAuth2AuthorizationConsent oauth2AuthorizationByDatabase;

        if (oauth2AuthorizationConsentRedis == null) {
            oauth2AuthorizationByDatabase = super.findById(registeredClientId, principalName);
            LogUtils.debug(
                    "根据 registeredClientId：{}、principalName：{} 直接查询数据库中的授权：{}",
                    registeredClientId,
                    principalName,
                    oauth2AuthorizationByDatabase);
            if (oauth2AuthorizationByDatabase != null) {
                set(oauth2AuthorizationByDatabase, AUTHORIZATION_CONSENT_TIMEOUT, TimeUnit.SECONDS);
            }
            oauth2AuthorizationResult = oauth2AuthorizationByDatabase;
        } else {
            LogUtils.debug(
                    "根据 registeredClientId：{}、principalName：{} 直接查询Redis中的授权：{}",
                    registeredClientId,
                    principalName,
                    oauth2AuthorizationConsentRedis);
            oauth2AuthorizationResult = oauth2AuthorizationConsentRedis;
        }

        return oauth2AuthorizationResult;
    }

    /**
     * 设置
     *
     * @param authorizationConsent 授权同意书
     * @param timeout              超时
     * @param unit                 单位
     * @since 2023-07-10 17:10:54
     */
    public void set(OAuth2AuthorizationConsent authorizationConsent, long timeout, TimeUnit unit) {
        String registeredClientId = authorizationConsent.getRegisteredClientId();
        String principalName = authorizationConsent.getPrincipalName();

        redisRepository
                .opsForValue()
                .set(
                        PREFIX
                                + OAUTH2_AUTHORIZATION_CONSENT
                                + registeredClientId
                                + ":"
                                + principalName,
                        authorizationConsent,
                        timeout,
                        unit);
    }
}
