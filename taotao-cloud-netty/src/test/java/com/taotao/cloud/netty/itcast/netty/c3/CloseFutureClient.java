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
import java.util.Scanner;

import lombok.extern.slf4j.Slf4j;

/**
 * CloseFutureClient
 *
 * @author shuigedeng
 * @version 2026.03
 * @since 2025-12-19 09:30:45
 */
@Slf4j
public class CloseFutureClient {

    public static void main( String[] args ) throws InterruptedException {
        NioEventLoopGroup group = new NioEventLoopGroup();
        ChannelFuture channelFuture =
                new Bootstrap()
                        .group(group)
                        .channel(NioSocketChannel.class)
                        .handler(
                                new ChannelInitializer<NioSocketChannel>() {
                                    @Override // 在连接建立后被调用
                                    protected void initChannel( NioSocketChannel ch )
                                            throws Exception {
                                        //                        ch.pipeline().addLast(new
                                        // LoggingHandler(LogLevel.DEBUG));
                                        ch.pipeline().addLast(new StringEncoder());
                                    }
                                })
                        .connect(new InetSocketAddress("localhost", 8080));
        System.out.println(channelFuture.getClass());
        Channel channel = channelFuture.sync().channel();
        log.debug("{}", channel);
        new Thread(
                () -> {
                    Scanner scanner = new Scanner(System.in);
                    while (true) {
                        String line = scanner.nextLine();
                        if ("q".equals(line)) {
                            channel.close(); // close 异步操作 1s 之后
                            //                    log.debug("处理关闭之后的操作"); // 不能在这里善后
                            break;
                        }
                        channel.writeAndFlush(line);
                    }
                },
                "input")
                .start();

        // 获取 CloseFuture 对象， 1) 同步处理关闭， 2) 异步处理关闭
        ChannelFuture closeFuture = channel.closeFuture();
        /*log.debug("waiting close...");
        closeFuture.sync();
        log.debug("处理关闭之后的操作");*/
        System.out.println(closeFuture.getClass());
        closeFuture.addListener(
                (ChannelFutureListener)
                        future -> {
                            log.debug("处理关闭之后的操作");
                            group.shutdownGracefully();
                        });
    }
}
