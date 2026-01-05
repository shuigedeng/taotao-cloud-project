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

package com.taotao.cloud.mq.broker.support.persist;

import com.taotao.cloud.mq.broker.dto.persist.MqMessagePersistPut;
import com.taotao.cloud.mq.common.dto.req.MqConsumerPullReq;
import com.taotao.cloud.mq.common.dto.req.component.MqConsumerUpdateStatusDto;
import com.taotao.cloud.mq.common.dto.resp.MqCommonResp;
import com.taotao.cloud.mq.common.dto.resp.MqConsumerPullResp;
import io.netty.channel.Channel;
import java.util.List;

/**
 * @author shuigedeng
 * @since 2024.05
 */
public interface MqBrokerPersist {

    /**
     * 保存消息
     *
     * @param mqMessage 消息
     * @return 响应
     * @since 2024.05
     */
    MqCommonResp put(final MqMessagePersistPut mqMessage);

    /**
     * 保存消息-批量
     *
     * @param putList 消息
     * @return 响应
     * @since 2024.05
     */
    MqCommonResp putBatch(final List<MqMessagePersistPut> putList);

    /**
     * 更新状态
     *
     * @param messageId         消息唯一标识
     * @param consumerGroupName 消费者分组名称
     * @param status            状态
     * @return 结果
     * @since 2024.05
     */
    MqCommonResp updateStatus(
            final String messageId, final String consumerGroupName, final String status);

    /**
     * 更新状态-批量
     *
     * @param statusDtoList 状态列表
     * @return 结果
     * @since 2024.05
     */
    MqCommonResp updateStatusBatch(List<MqConsumerUpdateStatusDto> statusDtoList);

    /**
     * 拉取消息
     *
     * @param pull    拉取消息
     * @param channel 通道
     * @return 结果
     */
    MqConsumerPullResp pull(final MqConsumerPullReq pull, final Channel channel);
}
