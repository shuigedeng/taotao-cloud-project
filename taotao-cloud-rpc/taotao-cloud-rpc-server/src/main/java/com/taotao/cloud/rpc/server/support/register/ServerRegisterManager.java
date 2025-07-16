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

package com.taotao.cloud.rpc.server.support.register;

import com.taotao.cloud.rpc.common.common.config.component.RpcAddress;
import com.taotao.cloud.rpc.registry.domain.entry.ServiceEntry;
import io.netty.channel.Channel;

/**
 * 服务端注册中心本地管理类
 * @since 0.1.8
 */
public interface ServerRegisterManager {

    /**
     * 设置端口号
     * @param port 端口号
     * @return 结果
     * @since 0.2.0
     */
    ServerRegisterManager port(final int port);

    /**
     * 注册当前服务信息
     * （1）将该服务通过 {@link ServiceEntry#serviceId()} 进行分组
     * 订阅了这个 serviceId 的所有客户端
     * @param serviceEntry 注册当前服务信息
     * @since 2024.06
     */
    void register(final ServiceEntry serviceEntry);

    /**
     * 注销当前服务信息
     * @param serviceId 服务标识
     * @since 2024.06
     * @return 服务明细
     */
    ServiceEntry unRegister(final String serviceId);

    /**
     * 注销当前所有服务信息
     * @since 0.1.8
     */
    void unRegisterAll();

    /**
     * 添加注册中心的 channel
     * @param rpcAddress 地址
     * @param channel channel
     * @since 0.1.8
     */
    void addRegisterChannel(RpcAddress rpcAddress, Channel channel);

    /**
     * 移除注册中心的 channel
     * @param rpcAddress 地址
     * @since 0.1.8
     */
    void removeRegisterChannel(RpcAddress rpcAddress);

    /**
     * 清空注册中心的 channel
     * @since 0.1.8
     */
    void clearRegisterChannel();
}
