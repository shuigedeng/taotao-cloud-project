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

import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.channel.embedded.EmbeddedChannel;
import lombok.extern.slf4j.Slf4j;

/**
 * TestEmbeddedChannel
 *
 * @author shuigedeng
 * @version 2026.03
 * @since 2025-12-19 09:30:45
 */
@Slf4j
public class TestEmbeddedChannel {

    public static void main( String[] args ) {
        ChannelInboundHandlerAdapter h1 =
                new ChannelInboundHandlerAdapter() {
                    @Override
                    public void channelRead( ChannelHandlerContext ctx, Object msg )
                            throws Exception {
                        log.debug("1");
                        super.channelRead(ctx, msg);
                    }
                };
        ChannelInboundHandlerAdapter h2 =
                new ChannelInboundHandlerAdapter() {
                    @Override
                    public void channelRead( ChannelHandlerContext ctx, Object msg )
                            throws Exception {
                        log.debug("2");
                        super.channelRead(ctx, msg);
                    }
                };
        ChannelOutboundHandlerAdapter h3 =
                new ChannelOutboundHandlerAdapter() {
                    @Override
                    public void write( ChannelHandlerContext ctx, Object msg, ChannelPromise promise )
                            throws Exception {
                        log.debug("3");
                        super.write(ctx, msg, promise);
                    }
                };
        ChannelOutboundHandlerAdapter h4 =
                new ChannelOutboundHandlerAdapter() {
                    @Override
                    public void write( ChannelHandlerContext ctx, Object msg, ChannelPromise promise )
                            throws Exception {
                        log.debug("4");
                        super.write(ctx, msg, promise);
                    }
                };
        EmbeddedChannel channel = new EmbeddedChannel(h1, h2, h3, h4);
        // 模拟入站操作
        //
        // channel.writeInbound(ByteBufAllocator.DEFAULT.buffer().writeBytes("hello".getBytes()));
        // 模拟出站操作
        channel.writeOutbound(ByteBufAllocator.DEFAULT.buffer().writeBytes("world".getBytes()));
    }
}
