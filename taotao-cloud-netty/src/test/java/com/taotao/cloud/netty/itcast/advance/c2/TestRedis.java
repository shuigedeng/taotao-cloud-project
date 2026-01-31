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

package com.taotao.cloud.netty.itcast.advance.c2;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LoggingHandler;

import java.nio.charset.Charset;

import lombok.extern.slf4j.Slf4j;

/**
 * TestRedis
 *
 * @author shuigedeng
 * @version 2026.03
 * @since 2025-12-19 09:30:45
 */
@Slf4j
public class TestRedis {

    /*
    set name zhangsan
    *3
    $3
    set
    $4
    name
    $8
    zhangsan
     */
    public static void main( String[] args ) {
        final byte[] LINE = {13, 10};
        NioEventLoopGroup worker = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.group(worker);
            bootstrap.handler(
                    new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel( SocketChannel ch ) {
                            ch.pipeline().addLast(new LoggingHandler());
                            ch.pipeline()
                                    .addLast(
                                            new ChannelInboundHandlerAdapter() {
                                                @Override
                                                public void channelActive(
                                                        ChannelHandlerContext ctx ) {
                                                    ByteBuf buf = ctx.alloc().buffer();
                                                    buf.writeBytes("*3".getBytes());
                                                    buf.writeBytes(LINE);
                                                    buf.writeBytes("$3".getBytes());
                                                    buf.writeBytes(LINE);
                                                    buf.writeBytes("set".getBytes());
                                                    buf.writeBytes(LINE);
                                                    buf.writeBytes("$4".getBytes());
                                                    buf.writeBytes(LINE);
                                                    buf.writeBytes("name".getBytes());
                                                    buf.writeBytes(LINE);
                                                    buf.writeBytes("$8".getBytes());
                                                    buf.writeBytes(LINE);
                                                    buf.writeBytes("zhangsan".getBytes());
                                                    buf.writeBytes(LINE);
                                                    ctx.writeAndFlush(buf);
                                                }

                                                @Override
                                                public void channelRead(
                                                        ChannelHandlerContext ctx, Object msg )
                                                        throws Exception {
                                                    ByteBuf buf = (ByteBuf) msg;
                                                    System.out.println(
                                                            buf.toString(Charset.defaultCharset()));
                                                }
                                            });
                        }
                    });
            ChannelFuture channelFuture = bootstrap.connect("localhost", 6379).sync();
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            log.error("client error", e);
        } finally {
            worker.shutdownGracefully();
        }
    }
}
