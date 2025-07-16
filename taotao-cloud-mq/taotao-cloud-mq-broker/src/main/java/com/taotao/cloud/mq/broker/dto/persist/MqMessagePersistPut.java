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

package com.taotao.cloud.mq.broker.dto.persist;

import com.taotao.cloud.mq.common.dto.req.MqMessage;
import com.taotao.cloud.mq.common.rpc.RpcAddress;

/**
 * @author shuigedeng
 * @since 2024.05
 */
public class MqMessagePersistPut {

    /**
     * 消息体
     */
    private MqMessage mqMessage;

    /**
     * 地址信息
     */
    private RpcAddress rpcAddress;

    /**
     * 消息状态
     */
    private String messageStatus;

    public MqMessage getMqMessage() {
        return mqMessage;
    }

    public void setMqMessage(MqMessage mqMessage) {
        this.mqMessage = mqMessage;
    }

    public RpcAddress getRpcAddress() {
        return rpcAddress;
    }

    public void setRpcAddress(RpcAddress rpcAddress) {
        this.rpcAddress = rpcAddress;
    }

    public String getMessageStatus() {
        return messageStatus;
    }

    public void setMessageStatus(String messageStatus) {
        this.messageStatus = messageStatus;
    }
}
