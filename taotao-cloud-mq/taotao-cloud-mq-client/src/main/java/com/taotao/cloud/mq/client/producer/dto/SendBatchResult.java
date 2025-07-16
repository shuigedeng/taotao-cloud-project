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

package com.taotao.cloud.mq.client.producer.dto;

import com.taotao.cloud.mq.client.producer.constant.SendStatus;
import java.util.List;

/**
 * 批量发送结果
 *
 * @author shuigedeng
 * @since 2024.05
 */
public class SendBatchResult {

    /**
     * 消息唯一标识
     */
    private List<String> messageIds;

    /**
     * 发送状态
     */
    private SendStatus status;

    public static SendBatchResult of(List<String> messageIds, SendStatus status) {
        SendBatchResult result = new SendBatchResult();
        result.setMessageIds(messageIds);
        result.setStatus(status);

        return result;
    }

    public List<String> getMessageIds() {
        return messageIds;
    }

    public void setMessageIds(List<String> messageIds) {
        this.messageIds = messageIds;
    }

    public SendStatus getStatus() {
        return status;
    }

    public void setStatus(SendStatus status) {
        this.status = status;
    }
}
