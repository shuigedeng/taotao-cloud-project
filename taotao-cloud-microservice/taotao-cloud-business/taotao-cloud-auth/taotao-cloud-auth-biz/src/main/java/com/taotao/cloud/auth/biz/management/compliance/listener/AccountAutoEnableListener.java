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

package com.taotao.cloud.auth.biz.management.compliance.listener;

import com.taotao.boot.security.spring.constants.OAuth2Constants;
import com.taotao.boot.security.spring.constants.SymbolConstants;
import com.taotao.cloud.auth.biz.management.compliance.OAuth2AccountStatusManager;
import java.nio.charset.StandardCharsets;
import com.taotao.boot.common.utils.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

/**
 * <p>账户锁定状态监听 </p>
 */
public class AccountAutoEnableListener extends KeyExpirationEventMessageListener {

    private static final Logger log = LoggerFactory.getLogger(AccountAutoEnableListener.class);

    private final OAuth2AccountStatusManager accountStatusManager;

    public AccountAutoEnableListener(
            RedisMessageListenerContainer listenerContainer,
            OAuth2AccountStatusManager accountStatusManager) {
        super(listenerContainer);
        this.accountStatusManager = accountStatusManager;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String key = new String(message.getBody(), StandardCharsets.UTF_8);
        if (StringUtils.contains(key, OAuth2Constants.CACHE_NAME_TOKEN_LOCKED_USER_DETAIL)) {
            String userId = StringUtils.substringAfterLast(key, SymbolConstants.COLON);
            log.info(" Parse the user [{}] at expired redis cache key [{}]", userId, key);
            if (StringUtils.isNotBlank(userId)) {
                log.debug(" Automatically unlock user account [{}]", userId);
                accountStatusManager.enable(userId);
            }
        }
    }
}
