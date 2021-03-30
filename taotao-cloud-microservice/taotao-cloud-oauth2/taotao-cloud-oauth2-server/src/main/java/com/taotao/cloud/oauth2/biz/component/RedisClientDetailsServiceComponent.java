/*
 * Copyright 2002-2021 the original author or authors.
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
package com.taotao.cloud.oauth2.biz.component;

import com.taotao.cloud.common.constant.SecurityConstant;
import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.redis.repository.RedisRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.oauth2.common.exceptions.InvalidClientException;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.NoSuchClientException;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.sql.DataSource;
import java.util.List;

/**
 * 将oauth_client_details表数据缓存到redis，这里做个缓存优化
 * 后台管理模块中有对oauth_client_details的crud， 注意同步redis的数据
 * 注意对oauth_client_details清除redis db部分数据的清空
 *
 * @author dengtao
 * @since 2020/4/29 17:40
 * @version 1.0.0
 */
@Component
public class RedisClientDetailsServiceComponent extends JdbcClientDetailsService {

    private final RedisRepository redisRepository;

    public RedisClientDetailsServiceComponent(@Qualifier("dataSource") DataSource dataSource,
                                              RedisRepository redisRepository) {
        super(dataSource);
        this.redisRepository = redisRepository;
    }

    @Override
    public ClientDetails loadClientByClientId(String clientId) {
        ClientDetails clientDetails = (ClientDetails) redisRepository.get(clientRedisKey(clientId));
        if (clientDetails == null) {
            clientDetails = cacheAndGetClient(clientId);
        }
        return clientDetails;
    }

    /**
     * 缓存client并返回client
     *
     * @param clientId clientId
     * @return org.springframework.security.oauth2.provider.ClientDetails
     * @author dengtao
     * @since 2020/4/29 17:42
     */
    private ClientDetails cacheAndGetClient(String clientId) {
        ClientDetails clientDetails = null;
        try {
            clientDetails = super.loadClientByClientId(clientId);
            if (null != clientDetails) {
                redisRepository.setExpire(clientRedisKey(clientId), clientDetails, 30 * 24 * 24 * 60);
                LogUtil.info("缓存clientId:{0},{1}", clientId, clientDetails);
            }
        } catch (NoSuchClientException e) {
            LogUtil.error("未查询到clientId:{0}", e, clientId);
        } catch (InvalidClientException e) {
            LogUtil.error("验证客户端失败:{}", e, clientId);
        }
        return clientDetails;
    }

    @Override
    public void updateClientDetails(ClientDetails clientDetails) {
        super.updateClientDetails(clientDetails);
        cacheAndGetClient(clientDetails.getClientId());
    }

    @Override
    public void updateClientSecret(String clientId, String secret) {
        super.updateClientSecret(clientId, secret);
        cacheAndGetClient(clientId);
    }

    @Override
    public void removeClientDetails(String clientId) {
        super.removeClientDetails(clientId);
        removeRedisCache(clientId);
    }

    /**
     * 删除redis缓存
     *
     * @param clientId clientId
     * @return void
     * @author dengtao
     * @since 2020/4/29 17:44
     */
    private void removeRedisCache(String clientId) {
        redisRepository.del(clientRedisKey(clientId));
    }

    /**
     * 将oauth_client_details全表刷入redis
     *
     * @return void
     * @author dengtao
     * @since 2020/4/29 17:45
     */
    public void loadAllClientToCache() {
        List<ClientDetails> list = super.listClientDetails();
        if (CollectionUtils.isEmpty(list)) {
            LogUtil.info("oauth_client_details表数据为空，请检查");
            return;
        }
        list.parallelStream().forEach(client -> redisRepository.set(clientRedisKey(client.getClientId()), client));
    }

    private String clientRedisKey(String clientId) {
        return SecurityConstant.CACHE_CLIENT_KEY + ":" + clientId;
    }
}
