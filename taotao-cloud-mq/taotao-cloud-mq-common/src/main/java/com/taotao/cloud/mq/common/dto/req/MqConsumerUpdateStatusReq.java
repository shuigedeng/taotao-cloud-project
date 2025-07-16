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

package com.taotao.cloud.mq.common.dto.req;

/**
 * @author shuigedeng
 * @since 2024.05
 */
public class MqConsumerUpdateStatusReq extends MqCommonReq {

    /**
     * 消息唯一标识
     */
    private String messageId;

    /**
     * 消息状态
     */
    private String messageStatus;

    /**
     * 消费者分组名称
     */
    private String consumerGroupName;

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getMessageStatus() {
        return messageStatus;
    }

    public void setMessageStatus(String messageStatus) {
        this.messageStatus = messageStatus;
    }

    public String getConsumerGroupName() {
        return consumerGroupName;
    }

    public void setConsumerGroupName(String consumerGroupName) {
        this.consumerGroupName = consumerGroupName;
    }

    @Override
    public String toString() {
        return "MqConsumerUpdateStatusReq{"
                + "messageId='"
                + messageId
                + '\''
                + ", messageStatus='"
                + messageStatus
                + '\''
                + ", consumerGroupName='"
                + consumerGroupName
                + '\''
                + "} "
                + super.toString();
    }
}
