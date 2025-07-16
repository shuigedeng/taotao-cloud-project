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

package com.taotao.cloud.rpc.registry.domain.entry;

import java.io.Serializable;

/**
 * <p> 注册服务信息 </p>
 *
 * （1）每一个 serviceId 是可以对应多台 ip:port 信息的。
 * @since 2024.06
 */
public interface ServiceEntry extends Serializable {

    /**
     * 服务标识
     * @return 服务标识
     * @since 2024.06
     */
    String serviceId();

    /**
     * 服务描述
     * @return 服务描述
     * @since 2024.06
     */
    String description();

    /**
     * 机器 ip 信息
     *
     * <pre>
     *     InetSocketAddress insocket = (InetSocketAddress) ctx.channel().remoteAddress();
     *     String clientIP = insocket.getAddress().getHostAddress();
     * </pre>
     *
     * @return 机器 ip 信息
     * @since 2024.06
     */
    String ip();

    /**
     * 端口信息
     * @return 端口信息
     * @since 2024.06
     */
    int port();

    /**
     * 权重信息
     * @return 权重信息
     * @since 2024.06
     */
    int weight();
}
