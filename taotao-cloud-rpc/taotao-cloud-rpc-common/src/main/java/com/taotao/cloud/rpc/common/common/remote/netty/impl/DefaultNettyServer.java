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

import com.taotao.cloud.rpc.common.common.exception.RpcRuntimeException;
import com.taotao.cloud.rpc.common.common.remote.netty.NettyServer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * netty 网络服务端
 * @author shuigedeng
 * @since 2024.06
 */
public class DefaultNettyServer extends AbstractNettyServer {

    /**
     * 日志信息
     * @since 2024.06
     */
    private static final Logger LOG = LoggerFactory.getLogger(DefaultNettyServer.class);

    /**
     * channel 信息
     * @since 2024.06
     */
    private ChannelFuture channelFuture;

    /**
     * boss 线程池
     * @since 2024.06
     */
    private EventLoopGroup bossGroup;

    /**
     * worker 线程池
     * @since 2024.06
     */
    private EventLoopGroup workerGroup;

    private DefaultNettyServer(int port, ChannelHandler channelHandler) {
        super(port, channelHandler);
    }

    public static NettyServer newInstance(int port, ChannelHandler channelHandler) {
        return new DefaultNettyServer(port, channelHandler);
    }

    @Override
    public void run() {
        //        LOG.info("[Netty Server] start with port: {} and channelHandler: {} ",
        //                port, channelHandler.getClass().getSimpleName());

        bossGroup = new NioEventLoopGroup();
        workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap
                    .group(workerGroup, bossGroup)
                    .channel(NioServerSocketChannel.class)
                    // 打印日志
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(channelHandler)
                    // 这个参数影响的是还没有被accept 取出的连接
                    .option(ChannelOption.SO_BACKLOG, 128)
                    // 这个参数只是过一段时间内客户端没有响应，服务端会发送一个 ack 包，以判断客户端是否还活着。
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            // 绑定端口，开始接收进来的链接
            channelFuture = serverBootstrap.bind(port).syncUninterruptibly();
            LOG.info("[Netty Server] 启动完成，监听【" + port + "】端口");

        } catch (Exception e) {
            LOG.error("[Netty Server] 服务启动异常", e);
            throw new RpcRuntimeException(e);
        }
    }

    @Override
    public void destroy() {
        try {
            LOG.info("[Netty Server] 开始关闭");
            channelFuture.channel().close();
            LOG.info("[Netty Server] 完成关闭");
        } catch (Exception e) {
            LOG.error("[Netty Server] 关闭服务异常", e);
            throw new RpcRuntimeException(e);
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
}
