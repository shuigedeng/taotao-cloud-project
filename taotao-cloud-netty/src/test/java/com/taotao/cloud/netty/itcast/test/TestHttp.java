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

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.util.List;

import lombok.extern.slf4j.Slf4j;

/**
 * TestHttp
 *
 * @author shuigedeng
 * @version 2026.03
 * @since 2025-12-19 09:30:45
 */
@Slf4j
public class TestHttp {

    public static void main( String[] args ) {
        NioEventLoopGroup boss = new NioEventLoopGroup();
        NioEventLoopGroup worker = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.channel(NioServerSocketChannel.class);
            serverBootstrap.group(boss, worker);
            serverBootstrap.childHandler(
                    new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel( SocketChannel ch ) throws Exception {
                            ch.pipeline().addLast(new LoggingHandler(LogLevel.DEBUG));
                            ch.pipeline().addLast(new HttpServerCodec());
                            ch.pipeline()
                                    .addLast(
                                            new SimpleChannelInboundHandler<DefaultHttpRequest>() {
                                                @Override
                                                protected void channelRead0(
                                                        ChannelHandlerContext ctx,
                                                        DefaultHttpRequest msg )
                                                        throws Exception {
                                                    log.debug("{}", msg.uri());
                                                    QueryStringDecoder decoder =
                                                            new QueryStringDecoder(msg.uri());
                                                    List<String> name =
                                                            decoder.parameters().get("name");
                                                    DefaultFullHttpResponse response =
                                                            new DefaultFullHttpResponse(
                                                                    msg.protocolVersion(),
                                                                    HttpResponseStatus.OK);
                                                    byte[] bytes =
                                                            ( "<h1>hello!" + name.get(0) + "</h1>" )
                                                                    .getBytes();
                                                    response.headers()
                                                            .set(
                                                                    HttpHeaderNames.CONTENT_TYPE,
                                                                    "text/html");
                                                    response.headers()
                                                            .setInt(
                                                                    HttpHeaderNames.CONTENT_LENGTH,
                                                                    bytes.length);
                                                    response.content().writeBytes(bytes);
                                                    ctx.writeAndFlush(response);
                                                }
                                            });
                        }
                    });
            ChannelFuture channelFuture = serverBootstrap.bind(8080).sync();
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            log.error("server error", e);
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }
}
