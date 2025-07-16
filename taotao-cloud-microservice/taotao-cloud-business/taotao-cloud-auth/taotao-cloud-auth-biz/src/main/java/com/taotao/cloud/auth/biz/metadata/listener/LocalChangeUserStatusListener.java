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

package com.taotao.cloud.auth.biz.metadata.listener;

import com.taotao.boot.common.enums.DataItemStatus;
import com.taotao.boot.security.spring.event.LocalChangeUserStatusEvent;
import com.taotao.boot.security.spring.event.domain.UserStatus;
import com.taotao.cloud.auth.biz.strategy.local.SysUserService;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * <p>本地用户状态变更监听 </p>
 */
@Component
public class LocalChangeUserStatusListener
        implements ApplicationListener<LocalChangeUserStatusEvent> {

    private static final Logger log = LoggerFactory.getLogger(LocalChangeUserStatusListener.class);
    private final SysUserService sysUserService;

    public LocalChangeUserStatusListener(SysUserService sysUserService) {
        this.sysUserService = sysUserService;
    }

    @Override
    public void onApplicationEvent(LocalChangeUserStatusEvent event) {
        log.info(" Change user status gather LOCAL listener, response event!");

        UserStatus userStatus = event.getData();
        if (ObjectUtils.isNotEmpty(userStatus)) {
            DataItemStatus dataItemStatus = DataItemStatus.valueOf(userStatus.getStatus());
            if (ObjectUtils.isNotEmpty(dataItemStatus)) {
                sysUserService.changeStatus(userStatus.getUserId(), dataItemStatus);
            }
        }
    }
}
