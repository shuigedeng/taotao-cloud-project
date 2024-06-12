package com.taotao.cloud.rpc.common.common.remote.netty.impl;

import com.taotao.cloud.rpc.common.common.remote.netty.NettyServer;
import io.netty.channel.ChannelHandler;

import java.util.concurrent.Executors;

/**
 * netty 网络服务端
 * @author shuigedeng
 * @since 2024.06
 */
public abstract class AbstractNettyServer implements NettyServer {

    protected int port;

    protected ChannelHandler channelHandler;

    public AbstractNettyServer(int port, ChannelHandler channelHandler) {
        this.port = port;
        this.channelHandler = channelHandler;
    }

    @Override
    public void init() {

    }

    @Override
    public void destroy() {

    }

    @Override
    public void asyncRun() {
        Executors.newSingleThreadExecutor().submit(this);
    }

}
