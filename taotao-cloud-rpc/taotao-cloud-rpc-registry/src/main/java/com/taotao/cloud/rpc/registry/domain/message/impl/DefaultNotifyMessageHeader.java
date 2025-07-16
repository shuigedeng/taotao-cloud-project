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

package com.taotao.cloud.rpc.registry.domain.message.impl;

import com.taotao.cloud.rpc.registry.domain.message.NotifyMessageHeader;

/**
 * 默认通知消息頭
 * @author shuigedeng
 * @since 2024.06
 */
class DefaultNotifyMessageHeader implements NotifyMessageHeader {

    private static final long serialVersionUID = -5742810870688287022L;

    /**
     * 消息类型
     * @since 2024.06
     */
    private String type;

    @Override
    public String type() {
        return type;
    }

    public DefaultNotifyMessageHeader type(String type) {
        this.type = type;
        return this;
    }

    @Override
    public String toString() {
        return "DefaultRegisterMessageHeader{" + "type=" + type + '}';
    }
}
