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

package com.taotao.cloud.auth.biz.dante.jpa.storage;

import com.taotao.cloud.auth.biz.dante.jpa.adapter.HerodotusRegisteredClientAdapter;
import com.taotao.cloud.auth.biz.dante.jpa.service.HerodotusRegisteredClientService;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;

/**
 * <p>Description: 基于Jpa 的 RegisteredClient服务 </p>
 *
 * @author : gengwei.zheng
 * @date : 2022/2/25 21:27
 */
public class JpaRegisteredClientRepository implements RegisteredClientRepository {

    private static final Logger log = LoggerFactory.getLogger(JpaRegisteredClientRepository.class);

    private final HerodotusRegisteredClientService herodotusRegisteredClientService;

    private final HerodotusRegisteredClientAdapter herodotusRegisteredClientAdapter;

    public JpaRegisteredClientRepository(HerodotusRegisteredClientService herodotusRegisteredClientService, PasswordEncoder passwordEncoder) {
        this.herodotusRegisteredClientService = herodotusRegisteredClientService;
        this.herodotusRegisteredClientAdapter = new HerodotusRegisteredClientAdapter(passwordEncoder);
    }

    @Override
    public void save(RegisteredClient registeredClient) {
        log.debug("[Herodotus] |- Jpa Registered Client Repository save entity.");
        this.herodotusRegisteredClientService.save(toEntity(registeredClient));
    }

    @Override
    public RegisteredClient findById(String id) {
        log.debug("[Herodotus] |- Jpa Registered Client Repository findById.");
        HerodotusRegisteredClient herodotusRegisteredClient = this.herodotusRegisteredClientService.findById(id);
        if (ObjectUtils.isNotEmpty(herodotusRegisteredClient)) {
            return toObject(herodotusRegisteredClient);
        }
        return null;
    }

    @Override
    public RegisteredClient findByClientId(String clientId) {
        log.debug("[Herodotus] |- Jpa Registered Client Repository findByClientId.");
        return this.herodotusRegisteredClientService.findByClientId(clientId).map(this::toObject).orElse(null);
    }

    public void remove(String id) {
        log.debug("[Herodotus] |- Jpa Registered Client Repository remove.");
        this.herodotusRegisteredClientService.deleteById(id);
    }

    private RegisteredClient toObject(HerodotusRegisteredClient herodotusRegisteredClient) {
        return herodotusRegisteredClientAdapter.toObject(herodotusRegisteredClient);
    }

    private HerodotusRegisteredClient toEntity(RegisteredClient registeredClient) {
        return herodotusRegisteredClientAdapter.toEntity(registeredClient);
    }
}
