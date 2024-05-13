/*
 * Copyright (c)  2019. houbinbin Inc.
 * rpc All rights reserved.
 */

package com.github.houbb.rpc.common.rpc.domain;

import com.github.houbb.load.balance.support.server.IServer;
import com.github.houbb.rpc.common.api.Destroyable;
import com.github.houbb.rpc.common.config.component.RpcAddress;

import io.netty.channel.ChannelFuture;

/**
 * <p> rpc channel future 接口</p>
 *
 * <pre> Created: 2019/10/26 9:39 上午  </pre>
 * <pre> Project: rpc  </pre>
 *
 * @author houbinbin
 * @since 0.0.9
 */
public interface RpcChannelFuture extends IServer {

    /**
     * channel future 信息
     * @return ChannelFuture
     * @since 0.0.9
     */
    ChannelFuture channelFuture();

    /**
     * 对应的地址信息
     * @return 地址信息
     * @since 0.0.9
     */
    RpcAddress address();

    /**
     * 可销毁的对象
     * @return 可销毁的信息
     * @since 0.1.3
     */
    Destroyable destroyable();

}
