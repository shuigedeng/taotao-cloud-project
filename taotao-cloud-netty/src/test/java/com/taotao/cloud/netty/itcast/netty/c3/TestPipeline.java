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
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * TestPipeline
 *
 * @author shuigedeng
 * @version 2026.01
 * @since 2025-12-19 09:30:45
 */
@Slf4j
public class TestPipeline {

    public static void main( String[] args ) {
        new ServerBootstrap()
                .group(new NioEventLoopGroup())
                .channel(NioServerSocketChannel.class)
                .childHandler(
                        new ChannelInitializer<NioSocketChannel>() {
                            @Override
                            protected void initChannel( NioSocketChannel ch ) throws Exception {
                                // 1. 通过 channel 拿到 pipeline
                                ChannelPipeline pipeline = ch.pipeline();
                                // 2. 添加处理器 head ->  h1 -> h2 ->  h4 -> h3 -> h5 -> h6 -> tail
                                pipeline.addLast(
                                        "h1",
                                        new ChannelInboundHandlerAdapter() {
                                            @Override
                                            public void channelRead(
                                                    ChannelHandlerContext ctx, Object msg )
                                                    throws Exception {
                                                log.debug("1");
                                                super.channelRead(ctx, msg);
                                            }
                                        });
                                pipeline.addLast(
                                        "h2",
                                        new ChannelInboundHandlerAdapter() {
                                            @Override
                                            public void channelRead(
                                                    ChannelHandlerContext ctx, Object name )
                                                    throws Exception {
                                                log.debug("2");
                                                super.channelRead(
                                                        ctx,
                                                        name); // 将数据传递给下个 handler，如果不调用，调用链会断开 或者调用
                                                // ctx.fireChannelRead(student);
                                            }
                                        });

                                pipeline.addLast(
                                        "h3",
                                        new ChannelInboundHandlerAdapter() {
                                            @Override
                                            public void channelRead(
                                                    ChannelHandlerContext ctx, Object msg )
                                                    throws Exception {
                                                log.debug("3");
                                                ctx.writeAndFlush(
                                                        ctx.alloc()
                                                                .buffer()
                                                                .writeBytes(
                                                                        "server...".getBytes()));
                                                //
                                                // ch.writeAndFlush(ctx.alloc().buffer().writeBytes("server...".getBytes()));
                                            }
                                        });
                                pipeline.addLast(
                                        "h4",
                                        new ChannelOutboundHandlerAdapter() {
                                            @Override
                                            public void write(
                                                    ChannelHandlerContext ctx,
                                                    Object msg,
                                                    ChannelPromise promise )
                                                    throws Exception {
                                                log.debug("4");
                                                super.write(ctx, msg, promise);
                                            }
                                        });
                                pipeline.addLast(
                                        "h5",
                                        new ChannelOutboundHandlerAdapter() {
                                            @Override
                                            public void write(
                                                    ChannelHandlerContext ctx,
                                                    Object msg,
                                                    ChannelPromise promise )
                                                    throws Exception {
                                                log.debug("5");
                                                super.write(ctx, msg, promise);
                                            }
                                        });
                                pipeline.addLast(
                                        "h6",
                                        new ChannelOutboundHandlerAdapter() {
                                            @Override
                                            public void write(
                                                    ChannelHandlerContext ctx,
                                                    Object msg,
                                                    ChannelPromise promise )
                                                    throws Exception {
                                                log.debug("6");
                                                super.write(ctx, msg, promise);
                                            }
                                        });
                            }
                        })
                .bind(8080);
    }

    @Data
    @AllArgsConstructor
    static class Student {

        private String name;
    }
}
