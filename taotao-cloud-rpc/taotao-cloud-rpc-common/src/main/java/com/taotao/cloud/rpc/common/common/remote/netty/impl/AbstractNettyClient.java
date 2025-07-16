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

package com.taotao.cloud.rpc.common.common.remote.netty.impl;

import com.taotao.cloud.rpc.common.common.remote.netty.NettyClient;
import io.netty.channel.ChannelHandler;

/**
 * netty 网络服务端
 * @author shuigedeng
 * @since 2024.06
 * @param <V> 泛型
 */
public abstract class AbstractNettyClient<V> implements NettyClient<V> {

    /**
     * ip 信息
     * @since 2024.06
     */
    protected String ip;

    /**
     * 端口信息
     * @since 2024.06
     */
    protected int port;

    /**
     * channel handler
     * @since 2024.06
     */
    protected ChannelHandler channelHandler;

    public AbstractNettyClient(String ip, int port, ChannelHandler channelHandler) {
        this.ip = ip;
        this.port = port;
        this.channelHandler = channelHandler;
    }

    @Override
    public void init() {}

    @Override
    public void destroy() {}
}
