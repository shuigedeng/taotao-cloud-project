/*
 * Copyright (c)  2019. houbinbin Inc.
 * rpc All rights reserved.
 */

package com.github.houbb.rpc.common.remote.netty.handler;

import io.netty.channel.ChannelHandler;

/**
 * <p> 用户构建 channel handler </p>
 *
 * <pre> Created: 2019/10/26 11:01 上午  </pre>
 * <pre> Project: rpc  </pre>
 *
 * @author houbinbin
 * @since 0.0.9
 */
public interface ChannelHandlerFactory {

    /**
     * 构建 handler 信息
     * @return ChannelHandler
     * @since 0.0.9
     */
    ChannelHandler handler();

}
