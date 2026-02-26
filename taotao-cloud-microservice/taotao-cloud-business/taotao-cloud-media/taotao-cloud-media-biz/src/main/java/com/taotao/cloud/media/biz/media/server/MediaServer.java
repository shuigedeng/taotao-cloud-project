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

package com.taotao.cloud.media.biz.media.server;

import com.taotao.boot.common.utils.log.LogUtils;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioIoHandler;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.unix.PreferredDirectByteBufAllocator;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.codec.http.cors.CorsConfig;
import io.netty.handler.codec.http.cors.CorsConfigBuilder;
import io.netty.handler.codec.http.cors.CorsHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import java.net.InetSocketAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/** flv流媒体服务 */
@Component
public class MediaServer {

    @Autowired
    private FlvHandler flvHandler;

    public void start(InetSocketAddress socketAddress) {
        // new 一个主线程组
//        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
//        // new 一个工作线程组
//        EventLoopGroup workGroup = new NioEventLoopGroup(200);

		// NIO事件循环组
		// BossGroup：专门处理连接请求，线程数通常为1（足够应对万级连接）
		MultiThreadIoEventLoopGroup bossGroup =
			new MultiThreadIoEventLoopGroup(1, NioIoHandler.newFactory());

		// WorkerGroup：处理IO读写，线程数 = CPU核心数 * 2
		int workerThreads = Runtime.getRuntime().availableProcessors() * 2;
		MultiThreadIoEventLoopGroup workerGroup =
			new MultiThreadIoEventLoopGroup(workerThreads, NioIoHandler.newFactory());
        ServerBootstrap bootstrap = new ServerBootstrap()
                .group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        CorsConfig corsConfig = CorsConfigBuilder.forAnyOrigin()
                                .allowNullOrigin()
                                .allowCredentials()
                                .build();

                        socketChannel
                                .pipeline()
                                .addLast(new HttpResponseEncoder())
                                .addLast(new HttpRequestDecoder())
                                .addLast(new ChunkedWriteHandler())
                                .addLast(new HttpObjectAggregator(64 * 1024))
                                .addLast(new CorsHandler(corsConfig))
                                .addLast(flvHandler);
                    }
                })
                .localAddress(socketAddress)
                .option(ChannelOption.SO_BACKLOG, 128)
                // 首选直接内存
                .option(ChannelOption.ALLOCATOR, PreferredDirectByteBufAllocator.DEFAULT)
                // 设置队列大小
                //                .option(ChannelOption.SO_BACKLOG, 1024)
                // 两小时内没有数据的通信时,TCP会自动发送一个活动探测数据报文
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childOption(ChannelOption.SO_RCVBUF, 128 * 1024)
                .childOption(ChannelOption.SO_SNDBUF, 1024 * 1024)
                .childOption(
                        ChannelOption.WRITE_BUFFER_WATER_MARK, new WriteBufferWaterMark(1024 * 1024 / 2, 1024 * 1024));
        // 绑定端口,开始接收进来的连接
        try {
            ChannelFuture future = bootstrap.bind(socketAddress).sync();
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            LogUtils.error(e);
        } finally {
            // 关闭主线程组
            bossGroup.shutdownGracefully();
            // 关闭工作线程组
            workerGroup.shutdownGracefully();
        }
    }
}
