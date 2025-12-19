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

package com.taotao.cloud.netty.itcast.client;

import com.taotao.cloud.netty.itcast.client.handler.RpcResponseMessageHandler;
import com.taotao.cloud.netty.itcast.message.RpcRequestMessage;
import com.taotao.cloud.netty.itcast.protocol.MessageCodecSharable;
import com.taotao.cloud.netty.itcast.protocol.ProcotolFrameDecoder;
import com.taotao.cloud.netty.itcast.protocol.SequenceIdGenerator;
import com.taotao.cloud.netty.itcast.server.service.HelloService;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.concurrent.DefaultPromise;

import java.lang.reflect.Proxy;

import lombok.extern.slf4j.Slf4j;

/**
 * RpcClientManager
 *
 * @author shuigedeng
 * @version 2026.01
 * @since 2025-12-19 09:30:45
 */
@Slf4j
public class RpcClientManager {

    public static void main( String[] args ) {
        HelloService service = getProxyService(HelloService.class);
        System.out.println(service.sayHello("zhangsan"));
        //        System.out.println(service.sayHello("lisi"));
        //        System.out.println(service.sayHello("wangwu"));
    }

    // 创建代理类
    public static <T> T getProxyService( Class<T> serviceClass ) {
        ClassLoader loader = serviceClass.getClassLoader();
        Class<?>[] interfaces = new Class[]{serviceClass};
        //                                                            sayHello  "张三"
        Object o =
                Proxy.newProxyInstance(
                        loader,
                        interfaces,
                        ( proxy, method, args ) -> {
                            // 1. 将方法调用转换为 消息对象
                            int sequenceId = SequenceIdGenerator.nextId();
                            RpcRequestMessage msg =
                                    new RpcRequestMessage(
                                            sequenceId,
                                            serviceClass.getName(),
                                            method.getName(),
                                            method.getReturnType(),
                                            method.getParameterTypes(),
                                            args);
                            // 2. 将消息对象发送出去
                            getChannel().writeAndFlush(msg);

                            // 3. 准备一个空 Promise 对象，来接收结果             指定 promise 对象异步接收结果线程
                            DefaultPromise<Object> promise =
                                    new DefaultPromise<>(getChannel().eventLoop());
                            RpcResponseMessageHandler.PROMISES.put(sequenceId, promise);

                            //            promise.addListener(future -> {
                            //                // 线程
                            //            });

                            // 4. 等待 promise 结果
                            promise.await();
                            if (promise.isSuccess()) {
                                // 调用正常
                                return promise.getNow();
                            } else {
                                // 调用失败
                                throw new RuntimeException(promise.cause());
                            }
                        });
        return (T) o;
    }

    private static Channel channel = null;
    private static final Object LOCK = new Object();

    // 获取唯一的 channel 对象
    public static Channel getChannel() {
        if (channel != null) {
            return channel;
        }
        synchronized (LOCK) { //  t2
            if (channel != null) { // t1
                return channel;
            }
            initChannel();
            return channel;
        }
    }

    // 初始化 channel 方法
    private static void initChannel() {
        NioEventLoopGroup group = new NioEventLoopGroup();
        LoggingHandler LOGGING_HANDLER = new LoggingHandler(LogLevel.DEBUG);
        MessageCodecSharable MESSAGE_CODEC = new MessageCodecSharable();
        RpcResponseMessageHandler RPC_HANDLER = new RpcResponseMessageHandler();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.group(group);
        bootstrap.handler(
                new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel( SocketChannel ch ) throws Exception {
                        ch.pipeline().addLast(new ProcotolFrameDecoder());
                        ch.pipeline().addLast(LOGGING_HANDLER);
                        ch.pipeline().addLast(MESSAGE_CODEC);
                        ch.pipeline().addLast(RPC_HANDLER);
                    }
                });
        try {
            channel = bootstrap.connect("localhost", 8080).sync().channel();
            channel.closeFuture()
                    .addListener(
                            future -> {
                                group.shutdownGracefully();
                            });
        } catch (Exception e) {
            log.error("client error", e);
        }
    }
}
