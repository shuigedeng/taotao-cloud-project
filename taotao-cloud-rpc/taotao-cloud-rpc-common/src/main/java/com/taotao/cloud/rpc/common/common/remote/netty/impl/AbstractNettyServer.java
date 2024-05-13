package com.github.houbb.rpc.common.remote.netty.impl;

import com.github.houbb.rpc.common.remote.netty.NettyServer;
import io.netty.channel.ChannelHandler;

import java.util.concurrent.Executors;

/**
 * netty 网络服务端
 * @author shuigedeng
 * @since 0.0.8
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
