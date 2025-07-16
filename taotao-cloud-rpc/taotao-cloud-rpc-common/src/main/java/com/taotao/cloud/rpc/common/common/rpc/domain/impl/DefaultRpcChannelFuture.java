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

package com.taotao.cloud.rpc.common.common.rpc.domain.impl;

import com.taotao.cloud.rpc.common.common.api.Destroyable;
import com.taotao.cloud.rpc.common.common.config.component.RpcAddress;
import com.taotao.cloud.rpc.common.common.rpc.domain.RpcChannelFuture;
import com.taotao.cloud.rpc.common.common.util.IpUtils;
import io.netty.channel.ChannelFuture;

/**
 * <p> 默认的实现 </p>
 * @since 2024.06
 */
public class DefaultRpcChannelFuture implements RpcChannelFuture {

    /**
     * channel future 信息
     * @since 2024.06
     */
    private ChannelFuture channelFuture;

    /**
     * 对应的地址信息
     * @since 2024.06
     */
    private RpcAddress address;

    /**
     * 权重信息
     * @since 2024.06
     */
    private int weight;

    /**
     * 可销毁的对象
     * @since 0.1.3
     */
    private Destroyable destroyable;

    public static DefaultRpcChannelFuture newInstance() {
        return new DefaultRpcChannelFuture();
    }

    @Override
    public ChannelFuture channelFuture() {
        return channelFuture;
    }

    public DefaultRpcChannelFuture channelFuture(ChannelFuture channelFuture) {
        this.channelFuture = channelFuture;
        return this;
    }

    @Override
    public RpcAddress address() {
        return address;
    }

    public DefaultRpcChannelFuture address(RpcAddress address) {
        this.address = address;
        return this;
    }

    @Override
    public String url() {
        RpcAddress rpcAddress = this.address;
        return IpUtils.ipPort(rpcAddress.address(), rpcAddress.port());
    }

    @Override
    public int weight() {
        return weight;
    }

    public DefaultRpcChannelFuture weight(int weight) {
        this.weight = weight;
        return this;
    }

    @Override
    public Destroyable destroyable() {
        return destroyable;
    }

    public DefaultRpcChannelFuture destroyable(Destroyable destroyable) {
        this.destroyable = destroyable;
        return this;
    }

    @Override
    public String toString() {
        return "DefaultRpcChannelFuture{"
                + "channelFuture="
                + channelFuture
                + ", address="
                + address
                + ", weight="
                + weight
                + ", destroyable="
                + destroyable
                + '}';
    }
}
