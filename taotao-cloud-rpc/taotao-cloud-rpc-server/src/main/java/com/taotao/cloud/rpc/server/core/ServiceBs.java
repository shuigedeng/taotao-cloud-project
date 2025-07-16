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

package com.taotao.cloud.rpc.server.core;

import com.taotao.cloud.rpc.common.common.config.component.RpcAddress;
import com.taotao.cloud.rpc.common.common.config.component.RpcAddressBuilder;
import com.taotao.cloud.rpc.common.common.config.protocol.ProtocolConfig;
import com.taotao.cloud.rpc.common.common.exception.RpcRuntimeException;
import com.taotao.cloud.rpc.common.common.remote.netty.NettyServer;
import com.taotao.cloud.rpc.common.common.remote.netty.handler.ChannelHandlers;
import com.taotao.cloud.rpc.common.common.remote.netty.impl.DefaultNettyClient;
import com.taotao.cloud.rpc.common.common.remote.netty.impl.DefaultNettyServer;
import com.taotao.cloud.rpc.common.common.support.delay.DelayExecutor;
import com.taotao.cloud.rpc.common.common.support.delay.DelayQueueExecutor;
import com.taotao.cloud.rpc.common.common.support.hook.ShutdownHooks;
import com.taotao.cloud.rpc.common.common.support.invoke.InvokeManager;
import com.taotao.cloud.rpc.common.common.support.invoke.impl.DefaultInvokeManager;
import com.taotao.cloud.rpc.common.common.support.resource.ResourceManager;
import com.taotao.cloud.rpc.common.common.support.resource.impl.DefaultResourceManager;
import com.taotao.cloud.rpc.common.common.support.status.enums.StatusEnum;
import com.taotao.cloud.rpc.common.common.support.status.service.StatusManager;
import com.taotao.cloud.rpc.common.common.support.status.service.impl.DefaultStatusManager;
import com.taotao.cloud.rpc.registry.domain.entry.ServiceEntry;
import com.taotao.cloud.rpc.registry.domain.entry.impl.ServiceEntryBuilder;
import com.taotao.cloud.rpc.server.config.service.DefaultServiceConfig;
import com.taotao.cloud.rpc.server.config.service.ServiceConfig;
import com.taotao.cloud.rpc.server.handler.RpcServerHandler;
import com.taotao.cloud.rpc.server.handler.RpcServerRegisterHandler;
import com.taotao.cloud.rpc.server.registry.ServiceRegistry;
import com.taotao.cloud.rpc.server.service.impl.DefaultServiceFactory;
import com.taotao.cloud.rpc.server.support.hook.DefaultServerShutdownHook;
import com.taotao.cloud.rpc.server.support.register.DefaultServerRegisterLocalManager;
import com.taotao.cloud.rpc.server.support.register.ServerRegisterManager;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 默认服务端注册类
 *
 * @author shuigedeng
 * @since 2024.06
 */
public class ServiceBs implements ServiceRegistry {

    /**
     * 日志信息
     *
     * @since 2024.06
     */
    private static final Logger LOG = LoggerFactory.getLogger(ServiceBs.class);

    /**
     * 单例信息
     *
     * @since 2024.06
     */
    private static final ServiceBs INSTANCE = new ServiceBs();

    /**
     * rpc 服务端端口号
     *
     * @since 2024.06
     */
    private int rpcPort;

    /**
     * 协议配置
     * （1）默认只实现 tcp
     * （2）后期可以拓展实现 web-service/http/https 等等。
     *
     * @since 2024.06
     */
    private ProtocolConfig protocolConfig;

    /**
     * 服务配置列表
     *
     * @since 2024.06
     */
    private List<ServiceConfig> serviceConfigList;

    /**
     * 注册中心地址列表
     *
     * @since 2024.06
     */
    private List<RpcAddress> registerCenterList;

    /**
     * 状态管理类
     * @since 0.1.3
     */
    private StatusManager statusManager;

    /**
     * 资源管理类
     * @since 0.1.3
     */
    private ResourceManager resourceManager;

    /**
     * 调用管理类
     * @since 0.1.3
     */
    private InvokeManager invokeManager;

    /**
     * 延迟执行器
     * @since 0.1.7
     */
    private DelayExecutor delayExecutor;

    /**
     * 服务注册中心管理
     * @since 0.1.8
     */
    private ServerRegisterManager serverRegisterManager;

    private ServiceBs() {
        // 初始化默认参数
        this.serviceConfigList = new ArrayList<>();
        this.rpcPort = 9527;
        //        this.registerCenterList = Guavas.newArrayList();

        // manager 初始化
        this.statusManager = new DefaultStatusManager();
        this.resourceManager = new DefaultResourceManager();
        this.invokeManager = new DefaultInvokeManager();
        this.serverRegisterManager = new DefaultServerRegisterLocalManager();
        serverRegisterManager.port(rpcPort);

        this.delayExecutor = new DelayQueueExecutor();
    }

    public static ServiceBs getInstance() {
        return INSTANCE;
    }

    @Override
    public ServiceRegistry port(int port) {
        //        ArgUtil.positive(port, "port");

        this.rpcPort = port;
        serverRegisterManager.port(rpcPort);
        return this;
    }

    /**
     * 注册服务实现
     * （1）主要用于后期服务调用
     * （2）如何根据 id 获取实现？非常简单，id 是唯一的。
     * 有就是有，没有就抛出异常，直接返回。
     * （3）如果根据 {@link RpcRequest} 获取对应的方法。
     * <p>
     * 3.1 根据 serviceId 获取唯一的实现
     * 3.2 根据 {@link Class#getMethod(String, Class[])} 方法名称+参数类型唯一获取方法
     * 3.3 根据 {@link java.lang.reflect.Method#invoke(Object, Object...)} 执行方法
     *
     * @param serviceId   服务标识
     * @param serviceImpl 服务实现
     * @return this
     * @since 2024.06
     */
    @Override
    @SuppressWarnings("unchecked")
    public synchronized ServiceBs register(final String serviceId, final Object serviceImpl) {
        //        ArgUtil.notEmpty(serviceId, "serviceId");
        //        ArgUtil.notNull(serviceImpl, "serviceImpl");

        // 构建对应的其他信息
        ServiceConfig serviceConfig = new DefaultServiceConfig();
        serviceConfig.id(serviceId).reference(serviceImpl).register(true).delay(0);

        addServiceConfig(serviceConfig);

        return this;
    }

    /**
     * 注册服务实现
     * （1）主要用于后期服务调用
     * （2）如何根据 id 获取实现？非常简单，id 是唯一的。
     * 有就是有，没有就抛出异常，直接返回。
     * （3）如果根据 {@link RpcRequest} 获取对应的方法。
     * <p>
     * 3.1 根据 serviceId 获取唯一的实现
     * 3.2 根据 {@link Class#getMethod(String, Class[])} 方法名称+参数类型唯一获取方法
     * 3.3 根据 {@link java.lang.reflect.Method#invoke(Object, Object...)} 执行方法
     *
     * @param serviceConfig  服务配置信息
     * @return this
     * @since 0.1.7
     */
    @SuppressWarnings("all")
    public synchronized ServiceBs register(final ServiceConfig serviceConfig) {
        //        ArgUtil.notNull(serviceConfig, "serviceConfig");
        //        ArgUtil.notNull(serviceConfig.reference(), "serviceConfig.reference");
        //        ArgUtil.notNull(serviceConfig.id(), "serviceConfig.id");

        // 构建对应的其他信息
        addServiceConfig(serviceConfig);

        return this;
    }

    @Override
    public ServiceRegistry expose() {
        // 1. 注册所有服务信息
        DefaultServiceFactory.getInstance().registerServicesLocal(serviceConfigList);
        LOG.info("server register local finish.");

        // 2. 启动 netty server 信息
        final ChannelHandler channelHandler =
                ChannelHandlers.objectCodecHandler(
                        new RpcServerHandler(invokeManager, statusManager));
        NettyServer nettyServer = DefaultNettyServer.newInstance(rpcPort, channelHandler);
        nettyServer.asyncRun();
        LOG.info("server service start finish.");
        this.resourceManager.addDestroy(nettyServer);

        // 3. 注册到配置中心
        this.registerServiceCenter();
        LOG.info("server service register finish.");

        return this;
    }

    @Override
    public ServiceRegistry registerCenter(String addresses) {
        this.registerCenterList = RpcAddressBuilder.of(addresses);
        return this;
    }

    /**
     * 注冊服務到注册中心
     * （1）循环服务列表注册到配置中心列表
     * （2）如果 register 为 false，则不进行注册
     * （3）后期可以添加延迟暴露，但是感觉意义不大。
     *
     * @since 2024.06
     */
    private void registerServiceCenter() {
        // 初始化服务端到注册中心的连接信息
        for (RpcAddress rpcAddress : registerCenterList) {
            RpcServerRegisterHandler rpcServerRegisterHandler =
                    new RpcServerRegisterHandler(serverRegisterManager);
            ChannelHandler registerHandler =
                    ChannelHandlers.objectCodecHandler(rpcServerRegisterHandler);
            //            LOG.info("[Rpc Server] start register to {}:{}", rpcAddress.address(),
            //                    rpcAddress.port());
            // TODO: 针对配置中心可以进一步细化，比如某一个 ip 变更，做对应的销毁，但是没有必要，一般配置中心变动的可能性较小。
            DefaultNettyClient nettyClient =
                    DefaultNettyClient.newInstance(
                            rpcAddress.address(), rpcAddress.port(), registerHandler);
            ChannelFuture channelFuture = nettyClient.call();
            resourceManager.addDestroy(nettyClient);

            // 添加到服务端管理中
            serverRegisterManager.addRegisterChannel(rpcAddress, channelFuture.channel());
        }

        // 注册到配置中心
        // 初期简单点，直接循环调用即可
        // 循环服务信息
        for (final ServiceConfig config : this.serviceConfigList) {
            boolean register = config.register();
            final String serviceId = config.id();
            if (!register) {
                //                LOG.info("[Rpc Server] serviceId: {} register config is false.",
                //                        serviceId);
                continue;
            }

            // 兼容小于 0 的情况
            long delayMills = config.delay();
            if (delayMills <= 0) {
                delayMills = 0;
            }

            // ps: 这里也可以把不延迟的同步执行。
            // 统一写可以保证逻辑的一致性
            delayExecutor.delay(
                    delayMills,
                    new Runnable() {
                        @Override
                        public void run() {
                            //                    LOG.info("[Rpc Server] serviceId: {} delay init
                            // start.", serviceId);
                            // 服务端通知到注册中心
                            //                    final String hostIp = NetUtil.getLocalHost();
                            ServiceEntry serviceEntry =
                                    ServiceEntryBuilder.of(config.id(), "hostIp", rpcPort);
                            serverRegisterManager.register(serviceEntry);

                            // 4. 添加服务端钩子函数
                            statusManager.status(StatusEnum.ENABLE.code());
                            //                    LOG.info("[Rpc Server] serviceId: {} delay init
                            // end.", serviceId);
                        }
                    });
        }

        // 统一添加钩子函数
        final DefaultServerShutdownHook rpcShutdownHook = new DefaultServerShutdownHook();
        rpcShutdownHook.statusManager(statusManager);
        rpcShutdownHook.invokeManager(invokeManager);
        rpcShutdownHook.resourceManager(resourceManager);
        rpcShutdownHook.serverRegisterManager(serverRegisterManager);
        ShutdownHooks.rpcShutdownHook(rpcShutdownHook);
    }

    /**
     * 添加服务配置
     * @param serviceConfig 服务配置
     * @since 0.1.7
     */
    @SuppressWarnings("all")
    private void addServiceConfig(final ServiceConfig serviceConfig) {
        // 判断是否存在重复的 id
        final String id = serviceConfig.id();
        for (ServiceConfig config : serviceConfigList) {
            if (config.id().equals(id)) {
                //                LOG.error("serviceConfig id has been registered, please check for
                // id: {}", id);
                throw new RpcRuntimeException();
            }
        }
        this.serviceConfigList.add(serviceConfig);
    }
}
