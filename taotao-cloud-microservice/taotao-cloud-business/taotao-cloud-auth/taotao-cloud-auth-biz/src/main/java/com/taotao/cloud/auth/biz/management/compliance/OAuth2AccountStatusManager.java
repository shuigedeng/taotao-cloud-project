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

package com.taotao.cloud.auth.biz.management.compliance;

import com.taotao.boot.common.enums.DataItemStatus;
import com.taotao.boot.security.spring.core.userdetails.TtcUser;
import com.taotao.boot.security.spring.event.domain.UserStatus;
import com.taotao.cloud.auth.biz.authentication.stamp.LockedUserDetailsStampManager;
import com.taotao.cloud.auth.biz.management.compliance.processor.changer.AccountStatusChanger;
import com.taotao.cloud.auth.biz.management.processor.EnhanceUserDetailsService;
import org.apache.commons.lang3.ObjectUtils;
import com.taotao.boot.common.utils.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * <p>账户锁定处理服务 </p>
 */
public class OAuth2AccountStatusManager {

    private static final Logger log = LoggerFactory.getLogger(OAuth2AccountStatusManager.class);

    private final UserDetailsService userDetailsService;
    private final AccountStatusChanger accountStatusChanger;
    private final LockedUserDetailsStampManager lockedUserDetailsStampManager;

    public OAuth2AccountStatusManager(
            UserDetailsService userDetailsService,
            AccountStatusChanger accountStatusChanger,
            LockedUserDetailsStampManager lockedUserDetailsStampManager) {
        this.userDetailsService = userDetailsService;
        this.lockedUserDetailsStampManager = lockedUserDetailsStampManager;
        this.accountStatusChanger = accountStatusChanger;
    }

    private EnhanceUserDetailsService getUserDetailsService() {
        return (EnhanceUserDetailsService) userDetailsService;
    }

    private String getUserId(String username) {
        EnhanceUserDetailsService enhanceUserDetailsService = getUserDetailsService();
        TtcUser user = enhanceUserDetailsService.loadTtcUserByUsername(username);
        if (ObjectUtils.isNotEmpty(user)) {
            return user.getUserId();
        }

        log.warn(" Can not found the userid for [{}]", username);
        return null;
    }

    public void lock(String username) {
        String userId = getUserId(username);
        if (ObjectUtils.isNotEmpty(userId)) {
            accountStatusChanger.process(new UserStatus(userId, DataItemStatus.LOCKING.name()));
            lockedUserDetailsStampManager.put(userId, username);
            log.info(" User count [{}] has been locked, and record into cache!", username);
        }
    }

    public void enable(String userId) {
        if (ObjectUtils.isNotEmpty(userId)) {
            accountStatusChanger.process(new UserStatus(userId, DataItemStatus.ENABLE.name()));
        }
    }

    public void releaseFromCache(String username) {
        String userId = getUserId(username);
        if (ObjectUtils.isNotEmpty(userId)) {
            String value = (String) lockedUserDetailsStampManager.get(userId);
            if (StringUtils.isNotEmpty(value)) {
                this.lockedUserDetailsStampManager.delete(userId);
                log.info(" User count [{}] locked info has been release!", username);
            }
        }
    }
}
