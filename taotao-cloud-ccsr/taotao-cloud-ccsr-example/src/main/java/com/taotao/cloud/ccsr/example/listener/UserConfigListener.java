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

package com.taotao.cloud.ccsr.example.listener;

import com.taotao.cloud.ccsr.api.event.EventType;
import com.taotao.cloud.ccsr.client.listener.AbstractConfigListener;
import com.taotao.cloud.ccsr.common.log.Log;
import com.taotao.cloud.ccsr.example.dto.User;
import com.taotao.cloud.ccsr.spi.Join;

@Join
public class UserConfigListener extends AbstractConfigListener<User> {
    @Override
    public void receive(String dataStr, User data, EventType eventType) {
        // TODO: Implement the logic to handle the received data
        Log.print("客户端收到配置变更推送: eventType=%s, dataStr=%s", eventType, dataStr);
    }
}
