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

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.nio.charset.Charset;

import lombok.extern.slf4j.Slf4j;

/**
 * EventLoopServer
 *
 * @author shuigedeng
 * @version 2026.02
 * @since 2025-12-19 09:30:45
 */
@Slf4j
public class EventLoopServer {

    public static void main( String[] args ) {
        // 细分2：创建一个独立的 EventLoopGroup
        EventLoopGroup group = new DefaultEventLoopGroup();
        new ServerBootstrap()
                // boss 和 worker
                // 细分1：boss 只负责 ServerSocketChannel 上 accept 事件     worker 只负责 socketChannel 上的读写
                .group(new NioEventLoopGroup(), new NioEventLoopGroup(2))
                .channel(NioServerSocketChannel.class)
                .childHandler(
                        new ChannelInitializer<NioSocketChannel>() {
                            @Override
                            protected void initChannel( NioSocketChannel ch ) throws Exception {
                                ch.pipeline()
                                        .addLast(
                                                "handler1",
                                                new ChannelInboundHandlerAdapter() {
                                                    @Override // ByteBuf
                                                    public void channelRead(
                                                            ChannelHandlerContext ctx, Object msg )
                                                            throws Exception {
                                                        ByteBuf buf = (ByteBuf) msg;
                                                        log.debug(
                                                                buf.toString(
                                                                        Charset.defaultCharset()));
                                                        ctx.fireChannelRead(
                                                                msg); // 让消息传递给下一个handler
                                                    }
                                                });
                                /*.addLast(group, "handler2", new ChannelInboundHandlerAdapter() {
                                    @Override                                         // ByteBuf
                                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                        ByteBuf buf = (ByteBuf) msg;
                                        log.debug(buf.toString(Charset.defaultCharset()));
                                    }
                                });*/
                            }
                        })
                .bind(8080);
    }
}
