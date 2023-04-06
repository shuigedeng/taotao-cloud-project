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

package com.taotao.cloud.auth.biz.demo.compliance.service;

import cn.herodotus.engine.data.core.enums.DataItemStatus;
import cn.herodotus.engine.oauth2.compliance.definition.AccountStatusChangeService;
import cn.herodotus.engine.oauth2.compliance.stamp.LockedUserDetailsStampManager;
import cn.herodotus.engine.oauth2.core.definition.domain.HerodotusUser;
import cn.herodotus.engine.oauth2.core.definition.service.EnhanceUserDetailsService;
import cn.herodotus.engine.web.core.domain.UserStatus;
import jodd.util.StringUtil;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

/**
 * Description: 账户锁定处理服务
 *
 * @author : gengwei.zheng
 * @date : 2022/7/8 19:25
 */
@Service
public class OAuth2AccountStatusService {

    private static final Logger log = LoggerFactory.getLogger(OAuth2AccountStatusService.class);

    private final UserDetailsService userDetailsService;
    private final AccountStatusChangeService accountStatusChangeService;
    private final LockedUserDetailsStampManager userDetailsStampManager;

    @Autowired
    public OAuth2AccountStatusService(
            UserDetailsService userDetailsService,
            AccountStatusChangeService accountStatusChangeService,
            LockedUserDetailsStampManager userDetailsStampManager) {
        this.userDetailsService = userDetailsService;
        this.userDetailsStampManager = userDetailsStampManager;
        this.accountStatusChangeService = accountStatusChangeService;
    }

    private EnhanceUserDetailsService getUserDetailsService() {
        return (EnhanceUserDetailsService) userDetailsService;
    }

    private String getUserId(String username) {
        EnhanceUserDetailsService enhanceUserDetailsService = getUserDetailsService();
        HerodotusUser user = enhanceUserDetailsService.loadHerodotusUserByUsername(username);
        if (ObjectUtils.isNotEmpty(user)) {
            return user.getUserId();
        }

        log.warn("[Herodotus] |- Can not found the userid for [{}]", username);
        return null;
    }

    public void lock(String username) {
        String userId = getUserId(username);
        if (ObjectUtils.isNotEmpty(userId)) {
            accountStatusChangeService.process(new UserStatus(userId, DataItemStatus.LOCKING.name()));
            userDetailsStampManager.put(userId, username);
            log.info("[Herodotus] |- User count [{}] has been locked, and record into cache!", username);
        }
    }

    public void enable(String userId) {
        if (ObjectUtils.isNotEmpty(userId)) {
            accountStatusChangeService.process(new UserStatus(userId, DataItemStatus.ENABLE.name()));
        }
    }

    public void releaseFromCache(String username) {
        String userId = getUserId(username);
        if (ObjectUtils.isNotEmpty(userId)) {
            String value = userDetailsStampManager.get(userId);
            if (StringUtil.isNotEmpty(value)) {
                this.userDetailsStampManager.delete(userId);
                log.info("[Herodotus] |- User count [{}] locked info has been release!", username);
            }
        }
    }
}
