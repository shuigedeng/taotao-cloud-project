/*
 * Copyright (c) 2020-2030 ZHENGGENGWEI(码匠君)<herodotus@aliyun.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Eurynome Cloud 采用APACHE LICENSE 2.0开源协议，您在使用过程中，需要注意以下几点：
 *
 * 1.请不要删除和修改根目录下的LICENSE文件。
 * 2.请不要删除和修改 Eurynome Cloud 源码头部的版权声明。
 * 3.请保留源码和相关描述文件的项目出处，作者声明等。
 * 4.分发源码时候，请注明软件出处 https://gitee.com/herodotus/eurynome-cloud
 * 5.在修改包名，模块名称，项目代码等时，请注明软件出处 https://gitee.com/herodotus/eurynome-cloud
 * 6.若您的项目无法满足以上几点，可申请商业授权
 */

package com.taotao.cloud.auth.biz.herodotus.jpa.service;

import cn.herodotus.engine.data.core.repository.BaseRepository;
import cn.herodotus.engine.data.core.service.BaseLayeredService;
import cn.herodotus.engine.oauth2.data.jpa.repository.HerodotusRegisteredClientRepository;
import cn.herodotus.engine.oauth2.data.jpa.entity.HerodotusRegisteredClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * <p>Description: HerodotusRegisteredClientService </p>
 * <p>
 * 这里命名没有按照统一的习惯，主要是为了防止与 spring-authorization-server 已有类的同名而导致Bean注入失败
 *
 * @author : gengwei.zheng
 * @date : 2022/2/25 21:06
 */
@Service
public class HerodotusRegisteredClientService extends BaseLayeredService<HerodotusRegisteredClient, String> {

    private static final Logger log = LoggerFactory.getLogger(HerodotusRegisteredClientService.class);

    private final HerodotusRegisteredClientRepository registeredClientRepository;

    @Autowired
    public HerodotusRegisteredClientService(HerodotusRegisteredClientRepository registeredClientRepository) {
        this.registeredClientRepository = registeredClientRepository;
    }

    @Override
    public BaseRepository<HerodotusRegisteredClient, String> getRepository() {
        return registeredClientRepository;
    }

    public Optional<HerodotusRegisteredClient> findByClientId(String clientId) {
        Optional<HerodotusRegisteredClient> result = this.registeredClientRepository.findByClientId(clientId);
        log.debug("[Herodotus] |- HerodotusRegisteredClient Service findByClientId.");
        return result;
    }
}
