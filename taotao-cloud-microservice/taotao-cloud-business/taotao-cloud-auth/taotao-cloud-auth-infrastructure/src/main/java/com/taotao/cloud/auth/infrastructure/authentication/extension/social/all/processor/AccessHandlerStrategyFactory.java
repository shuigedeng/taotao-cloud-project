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

package com.taotao.cloud.auth.infrastructure.authentication.extension.social.all.processor;

import com.taotao.cloud.auth.infrastructure.authentication.extension.social.all.enums.AccountType;
import com.taotao.cloud.auth.infrastructure.authentication.extension.social.core.definition.AccessHandler;
import com.taotao.cloud.auth.infrastructure.authentication.extension.social.core.definition.AccessResponse;
import com.taotao.cloud.auth.infrastructure.authentication.extension.social.core.definition.AccessUserDetails;
import com.taotao.cloud.auth.infrastructure.authentication.extension.social.core.exception.AccessHandlerNotFoundException;
import com.taotao.cloud.auth.infrastructure.authentication.extension.social.core.exception.IllegalAccessArgumentException;
import com.taotao.boot.security.spring.core.AccessPrincipal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * <p>Access Handler 工厂 </p>
 * <p>
 * 通过该工厂模式，对接入的常规操作进行封装。避免导入引用各个组件，导致耦合性增大
 * <p>
 * 本处使用基于Spring Boot 的工厂模式
 * {@see :https://www.pianshen.com/article/466978086/}
 *
 *
 * @since : 2021/4/4 17:40
 */
public class AccessHandlerStrategyFactory {

    private static final Logger log = LoggerFactory.getLogger(AccessHandlerStrategyFactory.class);

    @Autowired
    private final Map<String, AccessHandler> handlers = new ConcurrentHashMap<>();

    public AccessResponse preProcess(String source, String core, String... params) {
        AccessHandler socialAuthenticationHandler = this.getAccessHandler(source);
        return socialAuthenticationHandler.preProcess(core, params);
    }

    public AccessResponse preProcess(AccountType accountType, String core, String... params) {
        AccessHandler socialAuthenticationHandler = this.getAccessHandler(accountType);
        return socialAuthenticationHandler.preProcess(core, params);
    }

    public AccessUserDetails findAccessUserDetails(String source, AccessPrincipal accessPrincipal) {
        AccessHandler socialAuthenticationHandler = this.getAccessHandler(source);
        AccessUserDetails accessUserDetails = socialAuthenticationHandler.loadUserDetails(source, accessPrincipal);

        log.debug("AccessHandlerFactory findAccessUserDetails.");
        return accessUserDetails;
    }

    public AccessHandler getAccessHandler(String source) {
        if (ObjectUtils.isEmpty(source)) {
            throw new IllegalAccessArgumentException("Cannot found SocialProvider");
        }

        AccountType accountType = AccountType.getAccountType(source);
        if (ObjectUtils.isEmpty(accountType)) {
            throw new IllegalAccessArgumentException("Cannot parse the source parameter.");
        }

        return getAccessHandler(accountType);
    }

    public AccessHandler getAccessHandler(AccountType accountType) {
        String handlerName = accountType.getHandler();
        AccessHandler socialAuthenticationHandler = handlers.get(handlerName);
        if (ObjectUtils.isNotEmpty(socialAuthenticationHandler)) {
            return socialAuthenticationHandler;
        } else {
            throw new AccessHandlerNotFoundException("Can not found Social Handler for " + handlerName);
        }
    }
}
