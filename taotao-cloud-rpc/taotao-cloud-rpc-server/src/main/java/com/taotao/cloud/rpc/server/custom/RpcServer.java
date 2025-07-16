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

package com.taotao.cloud.rpc.server.custom;

import com.taotao.cloud.rpc.common.common.RpcDecoder;
import com.taotao.cloud.rpc.common.common.RpcEncoder;
import com.taotao.cloud.rpc.common.common.RpcReponse;
import com.taotao.cloud.rpc.common.common.RpcRequest;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * rpc 核心处理器<br>
 *
 * @author shuigedeng
 * @version v1.0.0
 */
public class RpcServer implements InitializingBean, ApplicationContextAware {
    public static final Logger logger = LoggerFactory.getLogger(RpcServer.class);

    private String serverAddress;
    //    private ServiceRegistry serviceRegistry;
    private Map<String, Object> handleMap = new ConcurrentHashMap<>();

    public RpcServer(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    //    public RpcServer(String serverAddress, ServiceRegistry serviceRegistry) {
    //        this.serverAddress = serverAddress;
    //        this.serviceRegistry = serviceRegistry;
    //    }

    @Override
    public void afterPropertiesSet() throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap
                    .group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(
                            new ChannelInitializer<SocketChannel>() {
                                @Override
                                protected void initChannel(SocketChannel ch) throws Exception {
                                    ch.pipeline()
                                            .addLast(new RpcDecoder(RpcRequest.class))
                                            .addLast(new RpcEncoder(RpcReponse.class))
                                            .addLast(new RpcHandler(handleMap));
                                }
                            })
                    .childOption(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            String[] addressSpilt = serverAddress.split(":");
            String ip = addressSpilt[0];
            int port = Integer.parseInt(addressSpilt[1]);

            // 启动netty服务器
            ChannelFuture channelFuture = serverBootstrap.bind(ip, port).sync();
            //            if (null != serviceRegistry) {
            //                // 服务的地址注册到 zk上 以供客户端去获取服务地址
            //                serviceRegistry.registry(serverAddress);
            //            }

            channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, Object> beansWithAnnotation =
                applicationContext.getBeansWithAnnotation(RpcService.class);
        //        if (MapUtils.isNotEmpty(beansWithAnnotation)) {
        //            for (Object clazz : beansWithAnnotation.values()) {
        //                String name =
        // clazz.getClass().getAnnotation(RpcService.class).value().getName();
        //                if (null == name) {
        //                    name = clazz.getClass().getName();
        //                }
        //                // name表示value的直  value表示注解类的对象
        //                handleMap.put(name, clazz);
        //            }
        //        }
    }
}
