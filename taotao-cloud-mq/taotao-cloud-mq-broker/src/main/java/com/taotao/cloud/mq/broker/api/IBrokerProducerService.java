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

import com.taotao.cloud.mq.broker.dto.ServiceEntry;
import com.taotao.cloud.mq.common.dto.resp.MqCommonResp;
import io.netty.channel.Channel;

/**
 * <p> 生产者注册服务类 </p>
 *
 * @since 2024.05
 */
public interface IBrokerProducerService {

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
     * @param channel      通道
     * @since 2024.05
     */
    MqCommonResp unRegister(final ServiceEntry serviceEntry, Channel channel);

    /**
     * 获取服务地址信息
     *
     * @param channelId channel
     * @return 结果
     * @since 2024.05
     */
    ServiceEntry getServiceEntry(final String channelId);

    /**
     * 校验有效性
     *
     * @param channelId 通道唯一标识
     * @since 2024.05
     */
    void checkValid(final String channelId);
}
