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

import com.taotao.cloud.rpc.registry.domain.message.NotifyMessage;
import com.taotao.cloud.rpc.registry.domain.message.NotifyMessageHeader;

/**
 * 默认注册消息
 * @author shuigedeng
 * @since 2024.06
 */
class DefaultNotifyMessage implements NotifyMessage {

    private static final long serialVersionUID = 3979588494064088927L;

    /**
     * 唯一序列号标识
     * @since 2024.06
     */
    private String seqId;

    /**
     * 头信息
     * @since 2024.06
     */
    private NotifyMessageHeader header;

    /**
     * 消息信息体
     * @since 2024.06
     */
    private Object body;

    @Override
    public String seqId() {
        return seqId;
    }

    @Override
    public DefaultNotifyMessage seqId(String seqId) {
        this.seqId = seqId;
        return this;
    }

    @Override
    public NotifyMessageHeader header() {
        return header;
    }

    public DefaultNotifyMessage header(NotifyMessageHeader header) {
        this.header = header;
        return this;
    }

    @Override
    public Object body() {
        return body;
    }

    public DefaultNotifyMessage body(Object body) {
        this.body = body;
        return this;
    }

    @Override
    public String toString() {
        return "DefaultRegisterMessage{" + "header=" + header + ", body=" + body + '}';
    }
}
