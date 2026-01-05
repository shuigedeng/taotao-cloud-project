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

package com.taotao.cloud.mq.broker.api;

import com.taotao.cloud.mq.broker.dto.ChannelGroupNameDto;
import com.taotao.cloud.mq.broker.dto.ServiceEntry;
import com.taotao.cloud.mq.broker.dto.consumer.ConsumerSubscribeBo;
import com.taotao.cloud.mq.broker.dto.consumer.ConsumerSubscribeReq;
import com.taotao.cloud.mq.broker.dto.consumer.ConsumerUnSubscribeReq;
import com.taotao.cloud.mq.common.balance.LoadBalance;
import com.taotao.cloud.mq.common.dto.req.MqHeartBeatReq;
import com.taotao.cloud.mq.common.dto.req.MqMessage;
import com.taotao.cloud.mq.common.dto.resp.MqCommonResp;
import io.netty.channel.Channel;
import java.util.List;

/**
 * <p> 消费者注册服务类 </p>
 *
 * @since 2024.05
 */
public interface BrokerConsumerService {

    /**
     * 设置负载均衡策略
     *
     * @param loadBalance 负载均衡
     * @since 2024.05
     */
    void loadBalance( LoadBalance<ConsumerSubscribeBo> loadBalance);

    /**
     * 注册当前服务信息 （1）将该服务通过 {@link ServiceEntry#getGroupName()} 进行分组 订阅了这个 serviceId 的所有客户端
     *
     * @param serviceEntry 注册当前服务信息
     * @param channel      channel
     * @since 2024.05
     */
    MqCommonResp register(final ServiceEntry serviceEntry, Channel channel);

    /**
     * 注销当前服务信息
     *
     * @param serviceEntry 注册当前服务信息
     * @param channel      channel
     * @since 2024.05
     */
    MqCommonResp unRegister(final ServiceEntry serviceEntry, Channel channel);

    /**
     * 监听服务信息 （1）监听之后，如果有任何相关的机器信息发生变化，则进行推送。 （2）内置的信息，需要传送 ip 信息到注册中心。
     *
     * @param serviceEntry  客户端明细信息
     * @param clientChannel 客户端 channel 信息
     * @since 2024.05
     */
    MqCommonResp subscribe(final ConsumerSubscribeReq serviceEntry, final Channel clientChannel);

    /**
     * 取消监听服务信息 （1）监听之后，如果有任何相关的机器信息发生变化，则进行推送。 （2）内置的信息，需要传送 ip 信息到注册中心。
     *
     * @param serviceEntry  客户端明细信息
     * @param clientChannel 客户端 channel 信息
     * @since 2024.05
     */
    MqCommonResp unSubscribe(
            final ConsumerUnSubscribeReq serviceEntry, final Channel clientChannel);

    /**
     * 获取所有匹配的消费者-主动推送 1. 同一个 groupName 只返回一个，注意负载均衡 2. 返回匹配当前消息的消费者通道
     *
     * @param mqMessage 消息体
     * @return 结果
     */
    List<ChannelGroupNameDto> getPushSubscribeList(MqMessage mqMessage);

    /**
     * 心跳
     *
     * @param mqHeartBeatReq 入参
     * @param channel        渠道
     * @since 2024.05
     */
    void heartbeat(final MqHeartBeatReq mqHeartBeatReq, Channel channel);

    /**
     * 校验有效性
     *
     * @param channelId 通道唯一标识
     * @since 2024.05
     */
    void checkValid(final String channelId);
}
