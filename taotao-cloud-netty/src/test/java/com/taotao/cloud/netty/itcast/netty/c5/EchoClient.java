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

package com.taotao.cloud.netty.itcast.netty.c5;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;

import java.nio.charset.Charset;
import java.util.Scanner;

/**
 * EchoClient
 *
 * @author shuigedeng
 * @version 2026.01
 * @since 2025-12-19 09:30:45
 */
public class EchoClient {

    public static void main( String[] args ) throws InterruptedException {
        NioEventLoopGroup group = new NioEventLoopGroup();
        Channel channel =
                new Bootstrap()
                        .group(group)
                        .channel(NioSocketChannel.class)
                        .handler(
                                new ChannelInitializer<NioSocketChannel>() {
                                    @Override
                                    protected void initChannel( NioSocketChannel ch )
                                            throws Exception {
                                        ch.pipeline().addLast(new StringEncoder());
                                        ch.pipeline()
                                                .addLast(
                                                        new ChannelInboundHandlerAdapter() {
                                                            @Override
                                                            public void channelRead(
                                                                    ChannelHandlerContext ctx,
                                                                    Object msg )
                                                                    throws Exception {
                                                                ByteBuf buffer = (ByteBuf) msg;
                                                                System.out.println(
                                                                        buffer.toString(
                                                                                Charset
                                                                                        .defaultCharset()));

                                                                // 思考：需要释放 buffer 吗
                                                            }
                                                        });
                                    }
                                })
                        .connect("127.0.0.1", 8080)
                        .sync()
                        .channel();
        channel.closeFuture()
                .addListener(
                        future -> {
                            group.shutdownGracefully();
                        });
        new Thread(
                () -> {
                    Scanner scanner = new Scanner(System.in);
                    while (true) {
                        String line = scanner.nextLine();
                        if ("q".equals(line)) {
                            channel.close();
                            break;
                        }
                        channel.writeAndFlush(line);
                    }
                })
                .start();
    }
}
