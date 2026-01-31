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

package com.taotao.cloud.netty.itcast.advance.c1;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Client1
 *
 * @author shuigedeng
 * @version 2026.03
 * @since 2025-12-19 09:30:45
 */
public class Client1 {

    static final Logger log = LoggerFactory.getLogger(Client1.class);

    public static void main( String[] args ) {
        for (int i = 0; i < 10; i++) {
            send();
        }
        System.out.println("finish");
    }

    private static void send() {
        NioEventLoopGroup worker = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.group(worker);
            bootstrap.handler(
                    new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel( SocketChannel ch ) {
                            ch.pipeline()
                                    .addLast(
                                            new ChannelInboundHandlerAdapter() {
                                                // 会在连接 channel 建立成功后，会触发 active 事件
                                                @Override
                                                public void channelActive(
                                                        ChannelHandlerContext ctx ) {
                                                    ByteBuf buf = ctx.alloc().buffer(16);
                                                    buf.writeBytes(
                                                            new byte[]{
                                                                    0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10,
                                                                    11, 12, 13, 14, 15, 16, 17
                                                            });
                                                    ctx.writeAndFlush(buf);
                                                    ctx.channel().close();
                                                }
                                            });
                        }
                    });
            ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 8080).sync();
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            log.error("client error", e);
        } finally {
            worker.shutdownGracefully();
        }
    }
}
