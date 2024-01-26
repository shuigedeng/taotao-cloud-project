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

package com.taotao.cloud.auth.biz.jpa.service;

import com.taotao.cloud.auth.biz.jpa.entity.HerodotusRegisteredClient;
import com.taotao.cloud.auth.biz.jpa.repository.HerodotusRegisteredClientRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>HerodotusRegisteredClientService </p>
 * <p>
 * 这里命名没有按照统一的习惯，主要是为了防止与 spring-authorization-server 已有类的同名而导致Bean注入失败
 *
 * @author shuigedeng
 * @version 2023.07
 * @since 2023-07-10 17:10:22
 */
@Service
public class HerodotusRegisteredClientService {

    /**
     * 日志
     */
    private static final Logger log = LoggerFactory.getLogger(HerodotusRegisteredClientService.class);

    /**
     * 注册客户端存储库
     */
    private final HerodotusRegisteredClientRepository registeredClientRepository;

    /**
     * 希罗多德注册客户服务
     *
     * @param registeredClientRepository 注册客户端存储库
     * @return
     * @since 2023-07-10 17:10:23
     */
    @Autowired
    public HerodotusRegisteredClientService(HerodotusRegisteredClientRepository registeredClientRepository) {
        this.registeredClientRepository = registeredClientRepository;
    }

    /**
     * 按客户id查找
     *
     * @param clientId 客户端id
     * @return {@link Optional }<{@link HerodotusRegisteredClient }>
     * @since 2023-07-10 17:10:23
     */
    public Optional<HerodotusRegisteredClient> findByClientId(String clientId) {
        Optional<HerodotusRegisteredClient> result = this.registeredClientRepository.findByClientId(clientId);
        log.info("HerodotusRegisteredClient Service findByClientId.");
        return result;
    }

    /**
     * 保存
     *
     * @param entity 实体
     * @since 2023-07-10 17:10:23
     */
    public void save(HerodotusRegisteredClient entity) {
        registeredClientRepository.save(entity);
    }

    /**
     * 按id查找
     *
     * @param id id
     * @return {@link HerodotusRegisteredClient }
     * @since 2023-07-10 17:10:23
     */
    public HerodotusRegisteredClient findById(String id) {
        return registeredClientRepository.findById(id).get();
    }

    /**
     * 按id删除
     *
     * @param id id
     * @since 2023-07-10 17:10:23
     */
    public void deleteById(String id) {
        registeredClientRepository.deleteById(id);
    }
}
