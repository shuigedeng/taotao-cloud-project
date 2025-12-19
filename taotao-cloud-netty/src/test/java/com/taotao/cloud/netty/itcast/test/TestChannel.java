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

package com.taotao.cloud.netty.itcast.test;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.channel.nio.NioEventLoopGroup;

import java.io.IOException;

import lombok.extern.slf4j.Slf4j;

/**
 * TestChannel
 *
 * @author shuigedeng
 * @version 2026.01
 * @since 2025-12-19 09:30:45
 */
@Slf4j
public class TestChannel {

    public static void main( String[] args ) throws IOException {
        NioEventLoopGroup group = new NioEventLoopGroup();
        EmbeddedChannel c1 =
                new EmbeddedChannel(
                        false,
                        false,
                        new ChannelInboundHandlerAdapter() {
                            @Override
                            public void channelRead( ChannelHandlerContext ctx, Object msg )
                                    throws Exception {
                                log.debug("{}", msg);
                                super.channelRead(ctx, msg);
                            }
                        });
        EmbeddedChannel c2 =
                new EmbeddedChannel(
                        new ChannelInboundHandlerAdapter() {
                            @Override
                            public void channelRead( ChannelHandlerContext ctx, Object msg )
                                    throws Exception {
                                log.debug("{}", msg);
                                super.channelRead(ctx, msg);
                            }
                        });
        EmbeddedChannel c3 =
                new EmbeddedChannel(
                        new ChannelInboundHandlerAdapter() {
                            @Override
                            public void channelRead( ChannelHandlerContext ctx, Object msg )
                                    throws Exception {
                                log.debug("{}", msg);
                                super.channelRead(ctx, msg);
                            }
                        });

        group.next().register(c1);
        group.next().register(c2);
        group.next().register(c3);
        c1.writeInbound("1");
        c2.writeInbound("2");
        c3.writeInbound("3");
        c1.writeInbound("1");
        c2.writeInbound("2");
        c3.writeInbound("3");
        System.in.read();
    }
}
