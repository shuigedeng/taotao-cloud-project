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

package com.taotao.cloud.rpc.core.net.netty.server;

import com.taotao.cloud.rpc.common.exception.RpcException;
import com.taotao.cloud.rpc.common.idworker.utils.LRedisHelper;
import com.taotao.cloud.rpc.common.serializer.CommonSerializer;
import com.taotao.cloud.rpc.common.util.IpUtils;
import com.taotao.cloud.rpc.common.util.PropertiesConstants;
import com.taotao.cloud.rpc.core.codec.CommonDecoder;
import com.taotao.cloud.rpc.core.codec.CommonEncoder;
import com.taotao.cloud.rpc.core.hook.ServerShutdownHook;
import com.taotao.cloud.rpc.core.net.AbstractRpcServer;
import com.taotao.cloud.rpc.core.provider.ServiceProvider;
import com.taotao.cloud.rpc.core.registry.ServiceRegistry;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.MissingResourceException;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.ServiceLoader;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;

/**
 * netty服务器
 */
@Slf4j
public class NettyServer extends AbstractRpcServer {

    /**
     * 不放到 抽象层 是 因为 不希望 被继承，子类会 破坏 CommonSerializer 的 完整性 使用时，主机名一般运行在项目所在服务器上，推荐 localhost 和
     * 127.0.0.1 或者 公网 主机名
     */
    private final CommonSerializer serializer;

    /**
     * Netty 服务端 连接监听 和 业务 事件循环组 考虑到 一个进程中 只创建一个 NettyServer 为了共享 EventLoopGroup 和 优雅 善后处理 使用 static
     * final 修饰 final 只是 引用 对象的地址 不可变，内容 成员还是可以变的
     */
    private static final EventLoopGroup bossGroup = new NioEventLoopGroup();

    private static final EventLoopGroup workerGroup = new NioEventLoopGroup();

    private static String redisServerWay = "";

    /**
     * 服务器启动时 优先做一些预加载
     */
    static {
        // 使用InPutStream流读取properties文件
        String currentWorkPath = System.getProperty("user.dir");
        PropertyResourceBundle configResource = null;
        try (BufferedReader bufferedReader =
                new BufferedReader(
                        new FileReader(currentWorkPath + "/config/resource.properties")); ) {

            configResource = new PropertyResourceBundle(bufferedReader);
            redisServerWay = configResource.getString(PropertiesConstants.REDIS_SERVER_WAY);

            if ("lettuce".equals(redisServerWay)) {
                LRedisHelper.preLoad();
            }

        } catch (MissingResourceException redisServerWayException) {
            log.warn("redis client way attribute is missing");
            log.info("use default redis client default way: jedis");
            redisServerWay = "jedis";
        } catch (IOException ioException) {
            log.info(
                    "not found resource from resource path: {}",
                    currentWorkPath + "/config/resource.properties");
            try {
                ResourceBundle resource = ResourceBundle.getBundle("resource");
                redisServerWay = resource.getString(PropertiesConstants.REDIS_SERVER_WAY);
                if ("lettuce".equals(redisServerWay)) {
                    LRedisHelper.preLoad();
                }

            } catch (MissingResourceException resourceException) {
                log.info("not found resource from resource path: {}", "resource.properties");
                log.info("use default redis client way: jedis");
                redisServerWay = "jedis";
            }
            log.info("read resource from resource path: {}", "resource.properties");
        }
        /**
         * 其他 预加载选项
         */
        NettyChannelDispatcher.init();
    }

    /**
     * @param hostName       启动服务所在机器的主机号，可以是私网或者公网
     * @param port           启动服务所在机器的端口号
     * @param serializerCode 序列化代码
     * @throws RpcException
     */
    public NettyServer(String hostName, int port, Integer serializerCode) throws RpcException {
        this.hostName =
                hostName.equals("localhost") || hostName.equals("127.0.0.1")
                        ? IpUtils.getPubIpAddr()
                        : hostName;
        log.info("start with host: {}, port: {}", this.hostName, port);
        this.port = port;
        // serviceRegistry = new NacosServiceRegistry();
        // serviceProvider = new DefaultServiceProvider();
        /**
         * 使用 SPI 机制，接口与实现类解耦到配置文件
         */
        serviceRegistry = ServiceLoader.load(ServiceRegistry.class).iterator().next();
        serviceProvider = ServiceLoader.load(ServiceProvider.class).iterator().next();

        serializer = CommonSerializer.getByCode(serializerCode);
        // 扫描 @ServiceScan 包下的 所有 @Service类，并 注册它们
        scanServices();
    }

    @Override
    public void start() {
        /**
         *  封装了 之前 使用的 线程吃 和 任务队列
         *  实现了 ExecutorService 接口
         */
        ServerShutdownHook.getShutdownHook().addClearAllHook();

        try {
            /**
             *  启动服务
             */
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap
                    .group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .option(ChannelOption.SO_BACKLOG, 256)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT) // 缓冲池
                    .childHandler(
                            new ChannelInitializer<SocketChannel>() {
                                @Override
                                protected void initChannel(SocketChannel ch) throws Exception {
                                    ChannelPipeline pipeline = ch.pipeline();
                                    /**
                                     * 读 超时 触发, WriteIdleTime 和 allIdleTime 为 0 表示不做处理
                                     */
                                    pipeline.addLast(
                                            new IdleStateHandler(30, 0, 0, TimeUnit.SECONDS));
                                    pipeline.addLast(new CommonEncoder(serializer));
                                    pipeline.addLast(new CommonDecoder());
                                    pipeline.addLast(new NettyServerHandler());
                                }
                            });
            ChannelFuture future = serverBootstrap.bind(port).sync();
            future.channel().closeFuture().sync();

        } catch (Exception e) {
            log.error("Error occurred while starting server! {}", e);
        } finally {
            // 如果服务器 直接通过 关闭 来断开 finally 及 后面的代码将 无法执行，搬迁 到 关闭钩子 优雅关闭
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
        // 无法执行
    }

    public static void shutdownAll() {
        log.info("close all EventLoopGroup now ...");
        try {
            bossGroup.shutdownGracefully().sync();
            log.info(
                    "close Netty Server Boss EventLoopGroup [{}] [{}]",
                    bossGroup.getClass(),
                    bossGroup.isTerminated());
        } catch (InterruptedException e) {
            log.error("close thread was interrupted: ", e);
        }
        try {
            workerGroup.shutdownGracefully().sync();
            log.info(
                    "close Netty Server Worker EventLoopGroup [{}] [{}]",
                    workerGroup.getClass(),
                    bossGroup.isTerminated());
        } catch (InterruptedException e) {
            log.error("close thread was interrupted: ", e);
        }
        try {
            bossGroup.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            log.error("failed to close Netty Server Boss EventLoopGroup: ", e);
            bossGroup.shutdownNow();
        }
        try {
            workerGroup.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            log.error("failed to close Netty Server Boss EventLoopGroup: ", e);
            workerGroup.shutdownNow();
        }
        log.info("Netty Server EventLoopGroup closed successfully");
    }
}
