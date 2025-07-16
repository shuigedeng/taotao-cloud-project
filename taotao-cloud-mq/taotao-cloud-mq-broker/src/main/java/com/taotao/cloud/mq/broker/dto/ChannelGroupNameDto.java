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

package com.taotao.cloud.mq.broker.dto;

import io.netty.channel.Channel;

/**
 * @author shuigedeng
 * @since 2024.05
 */
public class ChannelGroupNameDto {

    /**
     * 分组名称
     */
    private String consumerGroupName;

    /**
     * 通道
     */
    private Channel channel;

    public static ChannelGroupNameDto of(String consumerGroupName, Channel channel) {
        ChannelGroupNameDto dto = new ChannelGroupNameDto();
        dto.setConsumerGroupName(consumerGroupName);
        dto.setChannel(channel);
        return dto;
    }

    public String getConsumerGroupName() {
        return consumerGroupName;
    }

    public void setConsumerGroupName(String consumerGroupName) {
        this.consumerGroupName = consumerGroupName;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }
}
