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

package com.taotao.cloud.netty.itcast.netty.c3;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;

import java.net.InetSocketAddress;

import lombok.extern.slf4j.Slf4j;

/**
 * EventLoopClient
 *
 * @author shuigedeng
 * @version 2026.03
 * @since 2025-12-19 09:30:45
 */
@Slf4j
public class EventLoopClient {

    public static void main( String[] args ) throws InterruptedException {
        // 2. 带有 Future，Promise 的类型都是和异步方法配套使用，用来处理结果
        ChannelFuture channelFuture =
                new Bootstrap()
                        .group(new NioEventLoopGroup())
                        .channel(NioSocketChannel.class)
                        .handler(
                                new ChannelInitializer<NioSocketChannel>() {
                                    @Override // 在连接建立后被调用
                                    protected void initChannel( NioSocketChannel ch )
                                            throws Exception {
                                        ch.pipeline().addLast(new StringEncoder());
                                    }
                                })
                        // 1. 连接到服务器
                        // 异步非阻塞, main 发起了调用，真正执行 connect 是 nio 线程
                        .connect(new InetSocketAddress("localhost", 8080)); // 1s 秒后

        // 2.1 使用 sync 方法同步处理结果
        /*channelFuture.sync(); // 阻塞住当前线程，直到nio线程连接建立完毕
        Channel channel = channelFuture.channel();
        log.debug("{}", channel);
        channel.writeAndFlush("hello, world");*/

        // 2.2 使用 addListener(回调对象) 方法异步处理结果
        channelFuture.addListener(
                new ChannelFutureListener() {
                    @Override
                    // 在 nio 线程连接建立好之后，会调用 operationComplete
                    public void operationComplete( ChannelFuture future ) throws Exception {
                        Channel channel = future.channel();
                        log.debug("{}", channel);
                        channel.writeAndFlush("hello, world");
                    }
                });
    }
}
