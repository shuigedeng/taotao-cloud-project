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

package com.taotao.cloud.auth.biz.management.compliance.processor.changer;

import com.taotao.cloud.common.utils.context.ContextUtils;
import com.taotao.cloud.security.springsecurity.event.LocalChangeUserStatusEvent;
import com.taotao.cloud.security.springsecurity.event.RemoteChangeUserStatusEvent;
import com.taotao.cloud.security.springsecurity.event.domain.UserStatus;

/**
 * <p>用户状态变更处理器 </p>
 *
 */
public class TtcAccountStatusChanger implements AccountStatusChanger {
    @Override
    public String getDestinationServiceName() {
        return "taotao-cloud-sys";
    }

    @Override
    public void postLocalProcess(UserStatus data) {
        ContextUtils.getApplicationContext().publishEvent(new LocalChangeUserStatusEvent(data));
    }

    @Override
    public void postRemoteProcess(String data, String originService, String destinationService) {
        ContextUtils.getApplicationContext().publishEvent(new RemoteChangeUserStatusEvent(data));
    }
}
