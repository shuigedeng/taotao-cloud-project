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

package com.taotao.cloud.gateway.authentication;

import com.taotao.boot.common.utils.log.LogUtils;
import org.springframework.context.event.EventListener;
import org.springframework.security.authorization.event.AuthorizationDeniedEvent;
import org.springframework.stereotype.Component;

/**
 * 授权拒绝事件侦听器
 *
 * @author shuigedeng
 * @version 2023.07
 * @since 2023-07-12 11:06:59
 */
@Component
public class AuthorizationDeniedEventListener {

    @EventListener(AuthorizationDeniedEvent.class)
    public void authorizationDeniedEvent(AuthorizationDeniedEvent<?> event) {
        LogUtils.error("AuthorizationDeniedEvent", event.getAuthorizationResult());
    }
}
