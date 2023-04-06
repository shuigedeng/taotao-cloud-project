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

package com.taotao.cloud.auth.biz.demo.starter.processor;

import cn.herodotus.dante.module.common.ServiceNameConstants;
import cn.herodotus.engine.event.core.local.LocalChangeUserStatusEvent;
import cn.herodotus.engine.event.security.remote.RemoteChangeUserStatusEvent;
import cn.herodotus.engine.oauth2.compliance.definition.AccountStatusChangeService;
import cn.herodotus.engine.web.core.context.ServiceContext;
import cn.herodotus.engine.web.core.domain.UserStatus;

/**
 * Description: 用户状态变更处理器
 *
 * @author : gengwei.zheng
 * @date : 2022/7/10 17:25
 */
public class HerodotusAccountStatusChangeService implements AccountStatusChangeService {

    @Override
    public String getDestinationServiceName() {
        return ServiceNameConstants.SERVICE_NAME_UPMS;
    }

    @Override
    public void postLocalProcess(UserStatus data) {
        ServiceContext.getInstance().publishEvent(new LocalChangeUserStatusEvent(data));
    }

    @Override
    public void postRemoteProcess(String data, String originService, String destinationService) {
        ServiceContext.getInstance()
                .publishEvent(new RemoteChangeUserStatusEvent(data, originService, destinationService));
    }
}
