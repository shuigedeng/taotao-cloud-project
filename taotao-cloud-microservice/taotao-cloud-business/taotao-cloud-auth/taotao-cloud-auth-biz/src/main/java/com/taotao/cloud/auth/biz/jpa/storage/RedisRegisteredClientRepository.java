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
import com.taotao.cloud.auth.biz.jpa.service.TtcRegisteredClientService;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;

/**
 * <p>基于Jpa 的 RegisteredClient服务 </p>
 *
 * @author shuigedeng
 * @version 2023.07
 * @since 2023-07-10 17:11:02
 */
public class RedisRegisteredClientRepository extends JpaRegisteredClientRepository {

    private static final Logger log =
            LoggerFactory.getLogger(RedisRegisteredClientRepository.class);

    /**
     * 根据 id 查询时放入Redis中的部分 key
     */
    public static final String REGISTERED_CLIENT_ID = ":registered_client:id:";

    /**
     * 根据 clientId 查询时放入Redis中的部分 key
     */
    public static final String REGISTERED_CLIENT_CLIENT_ID = ":registered_client:clientId:";

    /**
     * 前缀
     */
    public static final String PREFIX = "spring-authorization-server";

    /**
     * 注册客户端超时
     */
    public static final long REGISTERED_CLIENT_TIMEOUT = 300L;

    /**
     * redis存储库
     */
    private final RedisRepository redisRepository;

    /**
     * redis注册客户端存储库
     *
     * @param ttcRegisteredClientService 希罗多德注册客户服务
     * @param passwordEncoder                  密码编码器
     * @param redisRepository                  redis存储库
     * @return
     * @since 2023-07-10 17:11:03
     */
    public RedisRegisteredClientRepository(
            TtcRegisteredClientService ttcRegisteredClientService,
            PasswordEncoder passwordEncoder,
            RedisRepository redisRepository) {
        super(ttcRegisteredClientService, passwordEncoder);
        this.redisRepository = redisRepository;
    }

    /**
     * 保存
     *
     * @param registeredClient 注册客户
     * @since 2023-07-10 17:11:03
     */
    @Override
    public void save(RegisteredClient registeredClient) {
        if (registeredClient != null) {
            set(registeredClient, 300L, TimeUnit.SECONDS);
            super.save(registeredClient);
        }
    }

    /**
     * 按id查找
     *
     * @param id id
     * @return {@link RegisteredClient }
     * @since 2023-07-10 17:11:03
     */
    @Override
    public RegisteredClient findById(String id) {
        RegisteredClient registeredClientByRedis =
                (RegisteredClient)
                        redisRepository.opsForValue().get(PREFIX + REGISTERED_CLIENT_ID + id);

        RegisteredClient registeredClientResult;
        RegisteredClient registeredClientByDatabase;

        if (registeredClientByRedis == null) {
            registeredClientByDatabase = super.findById(id);
            log.debug("根据 id：{} 直接查询数据库中的客户：{}", id, registeredClientByDatabase);
            if (registeredClientByDatabase != null) {
                set(registeredClientByDatabase, REGISTERED_CLIENT_TIMEOUT, TimeUnit.SECONDS);
            }
            registeredClientResult = registeredClientByDatabase;
        } else {
            log.debug("根据 id：{} 直接查询Redis中的客户：{}", id, registeredClientByRedis);
            registeredClientResult = registeredClientByRedis;
        }
        return registeredClientResult;
    }

    /**
     * 按客户id查找
     *
     * @param clientId 客户端id
     * @return {@link RegisteredClient }
     * @since 2023-07-10 17:11:03
     */
    @Override
    public RegisteredClient findByClientId(String clientId) {
        RegisteredClient registeredClientByRedis =
                (RegisteredClient)
                        redisRepository
                                .opsForValue()
                                .get(PREFIX + REGISTERED_CLIENT_CLIENT_ID + clientId);

        RegisteredClient registeredClientResult;
        RegisteredClient registeredClientByDatabase;

        if (registeredClientByRedis == null) {
            registeredClientByDatabase = super.findByClientId(clientId);
            log.debug("根据 clientId：{} 直接查询数据库中的客户：{}", clientId, registeredClientByDatabase);

            if (registeredClientByDatabase != null) {
                set(registeredClientByDatabase, REGISTERED_CLIENT_TIMEOUT, TimeUnit.SECONDS);
            }
            registeredClientResult = registeredClientByDatabase;
        } else {
            log.debug("根据 clientId：{} 直接查询Redis中的客户：{}", clientId, registeredClientByRedis);
            registeredClientResult = registeredClientByRedis;
        }
        return registeredClientResult;
    }

    /**
     * 设置
     *
     * @param registeredClient 注册客户
     * @param timeout          超时
     * @param unit             单位
     * @since 2023-07-10 17:11:03
     */
    public void set(RegisteredClient registeredClient, long timeout, TimeUnit unit) {
        redisRepository
                .opsForValue()
                .set(
                        PREFIX + REGISTERED_CLIENT_ID + registeredClient.getId(),
                        registeredClient,
                        timeout,
                        unit);
        redisRepository
                .opsForValue()
                .set(
                        PREFIX + REGISTERED_CLIENT_CLIENT_ID + registeredClient.getClientId(),
                        registeredClient,
                        timeout,
                        unit);
    }
}
