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

package com.taotao.cloud.auth.biz.demo.compliance.listener;

import cn.herodotus.engine.assistant.core.definition.constants.SymbolConstants;
import cn.herodotus.engine.oauth2.compliance.service.OAuth2AccountStatusService;
import cn.herodotus.engine.oauth2.core.constants.OAuth2Constants;
import java.nio.charset.StandardCharsets;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

/**
 * Description: 账户锁定状态监听
 *
 * @author : gengwei.zheng
 * @date : 2022/7/8 11:11
 */
public class AccountStatusListener extends KeyExpirationEventMessageListener {

    private static final Logger log = LoggerFactory.getLogger(AccountStatusListener.class);

    private final OAuth2AccountStatusService accountLockService;

    public AccountStatusListener(
            RedisMessageListenerContainer listenerContainer, OAuth2AccountStatusService accountLockService) {
        super(listenerContainer);
        this.accountLockService = accountLockService;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String key = new String(message.getBody(), StandardCharsets.UTF_8);
        if (StringUtils.contains(key, OAuth2Constants.CACHE_NAME_TOKEN_LOCKED_USER_DETAIL)) {
            String userId = StringUtils.substringAfterLast(key, SymbolConstants.COLON);
            log.info("[Herodotus] |- Parse the user [{}] at expired redis cache key [{}]", userId, key);
            if (StringUtils.isNotBlank(userId)) {
                log.debug("[Herodotus] |- Automatically unlock user account [{}]", userId);
                accountLockService.enable(userId);
            }
        }
    }
}
