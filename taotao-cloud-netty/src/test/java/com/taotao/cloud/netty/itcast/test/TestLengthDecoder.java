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

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.nio.charset.Charset;

/**
 * TestLengthDecoder
 *
 * @author shuigedeng
 * @version 2026.02
 * @since 2025-12-19 09:30:45
 */
public class TestLengthDecoder {

    public static void main( String[] args ) {
        EmbeddedChannel channel =
                new EmbeddedChannel(
                        new LengthFieldBasedFrameDecoder(1024, 0, 4, 0, 4),
                        new LoggingHandler(LogLevel.DEBUG),
                        new ChannelInboundHandlerAdapter() {
                            @Override
                            public void channelRead( ChannelHandlerContext ctx, Object msg )
                                    throws Exception {
                                ByteBuf buf = (ByteBuf) msg;
                                System.out.println(buf.toString(Charset.defaultCharset()));
                            }
                        });

        ByteBuf buf = ByteBufAllocator.DEFAULT.buffer();
        buf.writeInt(12);
        buf.writeBytes("hello, world".getBytes());
        buf.writeInt(6);
        buf.writeBytes("你好".getBytes());
        channel.writeInbound(buf);
    }
}
