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

package com.taotao.cloud.rpc.registry.simple.server;

import com.taotao.cloud.rpc.registry.domain.entry.ServiceEntry;
import io.netty.channel.Channel;
import java.util.Collection;
import java.util.List;

/**
 * <p> 服务端注册服务类 </p>
 * @since 2024.06
 */
public interface RegisterServerService {

    /**
     * 注册当前服务信息
     * （1）将该服务通过 {@link ServiceEntry#serviceId()} 进行分组
     * 订阅了这个 serviceId 的所有客户端
     * @param serviceEntry 注册当前服务信息
     * @param channel channel
     * @since 2024.06
     * @return 更新后的服务信息列表
     */
    List<ServiceEntry> register(final ServiceEntry serviceEntry, Channel channel);

    /**
     * 注销当前服务信息
     * @param serviceEntry 注册当前服务信息
     * @since 2024.06
     * @return 更新后的服务信息列表
     */
    List<ServiceEntry> unRegister(final ServiceEntry serviceEntry);

    /**
     * 根据服务标识发现对应的服务器信息
     * （1）如果对应的列表为空，则返回空列表。
     * @param serviceId 服务标识
     * @return 服务信息列表
     * @since 2024.06
     */
    List<ServiceEntry> lookUp(final String serviceId);

    /**
     * channel 列表
     * @return 列表
     * @since 0.1.8
     */
    Collection<Channel> channels();

    /**
     * 所有的服务明细
     * @return 结果
     * @since 0.2.0
     */
    Collection<ServiceEntry> serviceEntries();

    /**
     * 所有的指定地址端口的服务明细
     * @param ipPort 地址
     * @return 结果
     * @since 0.2.0
     */
    Collection<ServiceEntry> serviceEntries(String ipPort);
}
