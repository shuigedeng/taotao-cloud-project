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

package com.taotao.cloud.netty.itcast.source;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * TestByteBuf
 *
 * @author shuigedeng
 * @version 2026.03
 * @since 2025-12-19 09:30:45
 */
@Slf4j
public class TestByteBuf {

    public static void main( String[] args ) {
        new ServerBootstrap()
                .group(new NioEventLoopGroup())
                .channel(NioServerSocketChannel.class)
                .childHandler(
                        new ChannelInitializer<NioSocketChannel>() {
                            @Override
                            protected void initChannel( NioSocketChannel ch ) {
                                ch.pipeline().addLast(new LoggingHandler());
                                ch.pipeline()
                                        .addLast(
                                                new ChannelInboundHandlerAdapter() {
                                                    @Override
                                                    public void channelRead(
                                                            ChannelHandlerContext ctx, Object msg )
                                                            throws Exception {
                                                        //                                ByteBuf
                                                        // buf = ctx.alloc().buffer();
                                                        //
                                                        // log.debug("alloc buf {}", buf);

                                                        log.debug("receive buf {}", msg);
                                                        System.out.println("");
                                                    }
                                                });
                            }
                        })
                .bind(8080);
    }
}
