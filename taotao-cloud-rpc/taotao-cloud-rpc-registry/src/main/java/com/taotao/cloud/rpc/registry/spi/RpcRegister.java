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

package com.taotao.cloud.rpc.registry.spi;

import com.taotao.cloud.rpc.registry.domain.entry.ServiceEntry;
import com.taotao.cloud.rpc.registry.domain.message.body.ServerHeartbeatBody;
import io.netty.channel.Channel;

/**
 * <p> 注册中心接口 </p>
 * @since 2024.06
 */
public interface RpcRegister {

    /**
     * 注册当前服务信息
     * 订阅了这个 serviceId 的所有客户端
     * @param serviceEntry 注册当前服务信息
     * @param channel channel
     * @since 2024.06
     */
    void register(final ServiceEntry serviceEntry, Channel channel);

    /**
     * 注销当前服务信息
     * @param serviceEntry 注册当前服务信息
     * @since 2024.06
     */
    void unRegister(final ServiceEntry serviceEntry);

    /**
     * 监听服务信息
     * （1）监听之后，如果有任何相关的机器信息发生变化，则进行推送。
     * （2）内置的信息，需要传送 ip 信息到注册中心。
     *
     * @param serviceEntry 客户端明细信息
     * @param channel 频道信息
     * @since 2024.06
     */
    void subscribe(final ServiceEntry serviceEntry, final Channel channel);

    /**
     * 取消监听服务信息
     *
     * （1）将改服务从客户端的监听列表中移除即可。
     *
     * @param server 客户端明细信息
     * @param channel 频道信息
     * @since 2024.06
     */
    void unSubscribe(final ServiceEntry server, final Channel channel);

    /**
     * 启动时查询 serviceId 对应的所有服务端信息
     * @param seqId 请求标识
     * @param clientEntry 客户端查询明细
     * @param channel 频道信息
     * @since 2024.06
     */
    void lookUp(String seqId, ServiceEntry clientEntry, final Channel channel);

    /**
     * 服务端心跳检测
     * @param heartbeatBody 心跳对象
     * @param channel 频道信息
     * @since 0.2.0
     */
    void serverHeartbeat(final ServerHeartbeatBody heartbeatBody, final Channel channel);
}
