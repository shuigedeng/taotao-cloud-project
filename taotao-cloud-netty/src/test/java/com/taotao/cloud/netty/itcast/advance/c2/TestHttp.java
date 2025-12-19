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

import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_LENGTH;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * TestHttp
 *
 * @author shuigedeng
 * @version 2026.01
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
                                            new SimpleChannelInboundHandler<HttpRequest>() {
                                                @Override
                                                protected void channelRead0(
                                                        ChannelHandlerContext ctx, HttpRequest msg )
                                                        throws Exception {
                                                    // 获取请求
                                                    log.debug(msg.uri());

                                                    // 返回响应
                                                    DefaultFullHttpResponse response =
                                                            new DefaultFullHttpResponse(
                                                                    msg.protocolVersion(),
                                                                    HttpResponseStatus.OK);

                                                    byte[] bytes =
                                                            "<h1>Hello, world!</h1>".getBytes();

                                                    response.headers()
                                                            .setInt(CONTENT_LENGTH, bytes.length);
                                                    response.content().writeBytes(bytes);

                                                    // 写回响应
                                                    ctx.writeAndFlush(response);
                                                }
                                            });
                            /*ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                                @Override
                                public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                    log.debug("{}", msg.getClass());

                                    if (msg instanceof HttpRequest) { // 请求行，请求头

                                    } else if (msg instanceof HttpContent) { //请求体

                                    }
                                }
                            });*/
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
