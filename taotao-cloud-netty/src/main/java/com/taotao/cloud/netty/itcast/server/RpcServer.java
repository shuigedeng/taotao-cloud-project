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

package com.taotao.cloud.netty.itcast.server;

import com.taotao.cloud.netty.itcast.protocol.MessageCodecSharable;
import com.taotao.cloud.netty.itcast.protocol.ProcotolFrameDecoder;
import com.taotao.cloud.netty.itcast.server.handler.RpcRequestMessageHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.MultiThreadIoEventLoopGroup;
import io.netty.channel.nio.NioIoHandler;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * RpcServer
 *
 * @author shuigedeng
 * @version 2026.03
 * @since 2025-12-19 09:30:45
 */
@Slf4j
public class RpcServer {

    public static void main( String[] args ) {
		// NIO事件循环组
		// BossGroup：专门处理连接请求，线程数通常为1（足够应对万级连接）
		MultiThreadIoEventLoopGroup bossGroup =
			new MultiThreadIoEventLoopGroup(1, NioIoHandler.newFactory());

		// WorkerGroup：处理IO读写，线程数 = CPU核心数 * 2
		int workerThreads = Runtime.getRuntime().availableProcessors() * 2;
		MultiThreadIoEventLoopGroup workerGroup =
			new MultiThreadIoEventLoopGroup(workerThreads, NioIoHandler.newFactory());
        LoggingHandler LOGGING_HANDLER = new LoggingHandler(LogLevel.DEBUG);
        MessageCodecSharable MESSAGE_CODEC = new MessageCodecSharable();
        RpcRequestMessageHandler RPC_HANDLER = new RpcRequestMessageHandler();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.channel(NioServerSocketChannel.class);
            serverBootstrap.group(bossGroup, workerGroup);
            serverBootstrap.childHandler(
                    new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel( SocketChannel ch ) throws Exception {
                            ch.pipeline().addLast(new ProcotolFrameDecoder());
                            ch.pipeline().addLast(LOGGING_HANDLER);
                            ch.pipeline().addLast(MESSAGE_CODEC);
                            ch.pipeline().addLast(RPC_HANDLER);
                        }
                    });
            Channel channel = serverBootstrap.bind(8080).sync().channel();
            channel.closeFuture().sync();
        } catch (InterruptedException e) {
            log.error("server error", e);
        } finally {
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
        }
    }
}
