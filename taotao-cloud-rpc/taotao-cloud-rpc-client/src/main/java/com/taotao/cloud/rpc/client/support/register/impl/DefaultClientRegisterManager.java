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

package com.taotao.cloud.rpc.client.support.register.impl;

import com.taotao.cloud.rpc.client.handler.RpcClientHandler;
import com.taotao.cloud.rpc.client.handler.RpcClientRegisterHandler;
import com.taotao.cloud.rpc.client.model.ClientQueryServerChannelConfig;
import com.taotao.cloud.rpc.client.support.register.ClientRegisterManager;
import com.taotao.cloud.rpc.common.common.config.component.RpcAddress;
import com.taotao.cloud.rpc.common.common.exception.RpcRuntimeException;
import com.taotao.cloud.rpc.common.common.remote.netty.handler.ChannelHandlerFactory;
import com.taotao.cloud.rpc.common.common.remote.netty.handler.ChannelHandlers;
import com.taotao.cloud.rpc.common.common.rpc.domain.RpcChannelFuture;
import com.taotao.cloud.rpc.common.common.rpc.domain.RpcResponse;
import com.taotao.cloud.rpc.common.common.rpc.domain.impl.RpcResponses;
import com.taotao.cloud.rpc.common.common.support.invoke.InvokeManager;
import com.taotao.cloud.rpc.common.common.support.resource.ResourceManager;
import com.taotao.cloud.rpc.registry.domain.entry.ServiceEntry;
import com.taotao.cloud.rpc.registry.domain.entry.impl.ServiceEntryBuilder;
import com.taotao.cloud.rpc.registry.domain.message.NotifyMessage;
import com.taotao.cloud.rpc.registry.domain.message.body.RegisterCenterAddNotifyBody;
import com.taotao.cloud.rpc.registry.domain.message.body.RegisterCenterRemoveNotifyBody;
import com.taotao.cloud.rpc.registry.domain.message.impl.NotifyMessages;
import com.taotao.cloud.rpc.registry.simple.constant.MessageTypeConst;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p> 默认客户端注册中心实现类 </p>
 *
 * @since 2024.06
 */
public class DefaultClientRegisterManager implements ClientRegisterManager {

    private static final Logger log = LoggerFactory.getLogger(DefaultClientRegisterManager.class);

    /**
     * 服务调用信息管理类
     *
     * @since 2024.06
     */
    private final InvokeManager invokeManager;

    /**
     * 资源管理类
     *
     * @since 0.1.3
     */
    private final ResourceManager resourceManager;

    /**
     * 注册中心超时时间
     *
     * @since 2024.06
     */
    private final long registerCenterTimeOut;

    /**
     * 查询配置信息
     *
     * @since 0.1.8
     */
    private final Map<String, ClientQueryServerChannelConfig> queryConfigMap;

    /**
     * 服务端 channel 列表信息
     *
     * @since 0.1.8
     */
    private final Map<String, List<RpcChannelFuture>> serverChannelFutureMap;

    /**
     * 注册中心对应的 channel 信息
     *
     * @since 0.1.8
     */
    private final Map<String, RpcChannelFuture> registerCenterChannelMap;

    /**
     * 关注的服务标识集合
     *
     * @since 0.1.8
     */
    private final Set<String> subscribeServerIdSet;

    /**
     * 单线程定时处理任务
     *
     * @since 0.1.8
     */
    private static final ScheduledExecutorService EXECUTOR_SERVICE =
            Executors.newSingleThreadScheduledExecutor();

    public DefaultClientRegisterManager(
            InvokeManager invokeManager, ResourceManager resourceManager ) {
        this.invokeManager = invokeManager;
        this.resourceManager = resourceManager;

        this.registerCenterTimeOut = 60 * 1000;

        this.queryConfigMap = new ConcurrentHashMap<>();
        this.serverChannelFutureMap = new ConcurrentHashMap<>();
        this.registerCenterChannelMap = new ConcurrentHashMap<>();
        this.subscribeServerIdSet = new HashSet<>();

        final Runnable runnable = new LocalChannelFutureThread();
        EXECUTOR_SERVICE.schedule(runnable, 60, TimeUnit.SECONDS);
    }

    /**
     * LocalChannelFutureThread
     *
     * @author shuigedeng
     * @version 2026.03
     * @since 2025-12-19 09:30:45
     */
    private class LocalChannelFutureThread implements Runnable {

        @Override
        public void run() {
            log.info("开始定时更新本地的服务端连接信息");
            for (Map.Entry<String, List<RpcChannelFuture>> entry :
                    serverChannelFutureMap.entrySet()) {
                String serviceId = entry.getKey();
                //				log.info("开始定时更新本地的服务端 {} 连接信息", serviceId);
                ClientQueryServerChannelConfig config = queryConfigMap.get(serviceId);
                if (config == null) {
                    //					log.warn("serviceId: {} 对应的查询配置为空，忽略查询。", serviceId);
                    continue;
                }

                // 1. 是否连接配置信息
                final List<RpcAddress> rpcAddresses = config.rpcAddresses();
                final boolean subscribe = config.subscribe();
                //				if (CollectionUtil.isNotEmpty(rpcAddresses) || !subscribe) {
                //					log.warn("serviceId: {} 对应的配置固定，无需定时更新。", serviceId);
                //					continue;
                //				}

                // 2. 查询对应的配置信息
                List<RpcChannelFuture> resultFutureList = new ArrayList<>();
                List<RpcChannelFuture> oldFutureList = entry.getValue();
                List<RpcAddress> lastRpcAddressList = getRpcAddresses(config);
                // 2.1 以前没有，现在有的
                // 2.2 以前现在都有的
                for (RpcAddress newAddress : lastRpcAddressList) {
                    for (RpcChannelFuture oldFuture : oldFutureList) {
                        RpcAddress oldAddress = oldFuture.address();
                        // 以前现在都有的
                        if (oldAddress.equals(newAddress)) {
                            resultFutureList.add(oldFuture);
                        } else {
                            // 新增的
                            RpcChannelFuture future =
                                    ChannelHandlers.channelFuture(
                                            newAddress,
                                            new ChannelHandlerFactory() {
                                                @Override
                                                public ChannelHandler handler() {
                                                    final ChannelHandler channelHandler =
                                                            new RpcClientHandler(invokeManager);
                                                    return ChannelHandlers.objectCodecHandler(
                                                            channelHandler);
                                                }
                                            });

                            resourceManager.addDestroy(future.destroyable());
                            resultFutureList.add(oldFuture);
                        }
                    }
                }

                // 2.3 以前有，现在没有的
                // 不用处理，这里直接以最新的为准，没有添加。
                serverChannelFutureMap.put(serviceId, resultFutureList);
                //				log.info("完成定时更新本地的服务端 {} 连接信息", serviceId);
            }

            log.info("完成定时更新本地的服务端连接信息");
        }
    }

    /**
     * 查询服务端地址信息列表 （1）registerCenterTimeOut 超时时间内部直接指定
     *
     * @param serviceId 服务唯一标识
     * @param registerCenterList 注册中心列表
     * @param config 配置信息
     * @return 服务端地址信息列表
     * @since 2024.06
     */
    private List<RpcAddress> queryServerAddressList(
            String serviceId,
            List<RpcAddress> registerCenterList,
            ClientQueryServerChannelConfig config ) {
        // 1. 参数校验
        //		ArgUtil.notEmpty(serviceId, "serviceId");
        //		ArgUtil.notEmpty(registerCenterList, "registerCenterList");

        // 2. 查询服务信息
        List<ServiceEntry> serviceEntries =
                lookUpServiceEntryList(serviceId, registerCenterList, config);
        //		log.info("[Client] register center serviceEntries: {}", serviceEntries);

        // 3. 结果转换
        //		return CollectionUtil.toList(serviceEntries, new IHandler<ServiceEntry, RpcAddress>() {
        //			@Override
        //			public RpcAddress handle(ServiceEntry serviceEntry) {
        //				return new RpcAddress(serviceEntry.ip(),
        //					serviceEntry.port(), serviceEntry.weight());
        //			}
        //		});
        return null;
    }

    @Override
    public void initServerChannelFutureList( ClientQueryServerChannelConfig config ) {
        // 1. 查询
        List<RpcAddress> serverAddressList = getRpcAddresses(config);

        // 2. 构建对象
        List<RpcChannelFuture> channelFutureList =
                this.initChannelFutureList(serverAddressList, config);

        // 2.2 循环将 nettyClient 信息放入到资源列表中
        for (RpcChannelFuture channelFuture : channelFutureList) {
            this.resourceManager.addDestroy(channelFuture.destroyable());
        }

        // 3. 存放到本地 map 中，用于本地定时更新
        String serviceId = config.serviceId();
        queryConfigMap.put(serviceId, config);
        serverChannelFutureMap.put(serviceId, channelFutureList);
    }

    @Override
    public List<RpcChannelFuture> queryServerChannelFutures( String serviceId ) {
        return serverChannelFutureMap.get(serviceId);
    }

    @Override
    public void unSubscribeServerAll() {
        log.info("开始取消订阅所有服务端信息");

        // TODO: 后续可以优化，添加对应的批处理。
        //		if (CollectionUtil.isEmpty(subscribeServerIdSet)) {
        //			log.info("订阅服务器的集合为空，忽略处理");
        //		}
        //		else {
        //			for (String serviceId : subscribeServerIdSet) {
        //				for (RpcChannelFuture rpcChannelFuture : this.registerCenterChannelMap.values()) {
        //					ServiceEntry serviceEntry = ServiceEntryBuilder.of(serviceId);
        //					NotifyMessage subscribeMessage = NotifyMessages.of(
        //						MessageTypeConst.CLIENT_UN_SUBSCRIBE_REQ, serviceEntry);
        //					// 这里可以直接 oneway
        //					ChannelFuture future = rpcChannelFuture.channelFuture();
        //					future.channel().writeAndFlush(subscribeMessage);
        ////					log.info("客户端 unSubscribe serviceId {} for register center address {}",
        ////						serviceId,
        ////						rpcChannelFuture.address());
        //				}
        //			}
        //		}

        log.info("完成取消订阅所有服务端信息");
    }

    @Override
    public void subscribeServer( String serviceId ) {
        Collection<RpcChannelFuture> registerChannelFutureList = registerCenterChannelMap.values();
        //		if (CollectionUtil.isEmpty(registerChannelFutureList)) {
        //			log.warn("注册中心连接列表为空，忽略处理。");
        //			return;
        //		}

        if (subscribeServerIdSet.contains(serviceId)) {
            //			log.info("serviceId: {} 对应的 subscribe 已经设置，本次忽略处理", serviceId);
        } else {
            for (RpcChannelFuture rpcChannelFuture : registerChannelFutureList) {
                subscribeServer(serviceId, rpcChannelFuture);
            }

            // 设置
            subscribeServerIdSet.add(serviceId);
        }
    }

    /**
     * 关注服务
     *
     * @param serviceId 服务标识
     * @param rpcChannelFuture 注册中心的 channel
     * @since 0.1.8
     */
    private void subscribeServer( String serviceId, RpcChannelFuture rpcChannelFuture ) {
        //		log.info("开始推送关注，serviceId: {}, rpcAddress: {}", serviceId,
        //			rpcChannelFuture.address());
        ServiceEntry serviceEntry = ServiceEntryBuilder.of(serviceId);
        NotifyMessage subscribeMessage =
                NotifyMessages.of(MessageTypeConst.CLIENT_SUBSCRIBE_REQ, serviceEntry);
        // 这里可以直接 oneway
        ChannelFuture future = rpcChannelFuture.channelFuture();
        future.channel().writeAndFlush(subscribeMessage);
        //		log.info("客户端 subscribe serviceId {} for register center address {}", serviceId,
        //			rpcChannelFuture.address());
    }

    @Override
    public void serverRegisterNotify( ServiceEntry serviceEntry ) {
        // 新增
        //		log.info("客户端接收到服务端注册通知:{}", serviceEntry);

        // 如果已经包含
        final String serviceId = serviceEntry.serviceId();
        boolean contains = containsServerEntry(serviceEntry);
        if (contains) {
            //			log.warn("已经包含当前服务 {}, 跳过处理。", serviceEntry);
            return;
        }

        // 构建，并且添加到列表中
        // 新增的
        RpcAddress newAddress = new RpcAddress(serviceEntry.ip(), serviceEntry.port());
        RpcChannelFuture future =
                ChannelHandlers.channelFuture(
                        newAddress,
                        new ChannelHandlerFactory() {
                            @Override
                            public ChannelHandler handler() {
                                final ChannelHandler channelHandler =
                                        new RpcClientHandler(invokeManager);
                                return ChannelHandlers.objectCodecHandler(channelHandler);
                            }
                        });

        resourceManager.addDestroy(future.destroyable());
        List<RpcChannelFuture> futureList = serverChannelFutureMap.get(serviceId);
        futureList.add(future);
        serverChannelFutureMap.put(serviceId, futureList);
    }

    /**
     * 是否包含服务明细
     *
     * @param serviceEntry 服务明细
     * @return 是否
     * @since 0.1.8
     */
    private boolean containsServerEntry( ServiceEntry serviceEntry ) {
        // 移除
        String serviceId = serviceEntry.serviceId();

        List<RpcChannelFuture> futureList = serverChannelFutureMap.get(serviceId);
        //		if (CollectionUtil.isEmpty(futureList)) {
        //			return false;
        //		}

        for (RpcChannelFuture rpcChannelFuture : futureList) {
            RpcAddress rpcAddress = rpcChannelFuture.address();
            if (isTheSameIpPort(rpcAddress, serviceEntry)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void serverUnRegisterNotify( ServiceEntry serviceEntry ) {
        //		log.info("客户端接收到服务端注销通知 :{}", serviceEntry);
        // 移除
        String serviceId = serviceEntry.serviceId();

        List<RpcChannelFuture> futureList = serverChannelFutureMap.get(serviceId);
        //		if (CollectionUtil.isEmpty(futureList)) {
        //			log.warn("serviceId: {} 对应的服务列表为空，无需处理。", serviceId);
        //			return;
        //		}

        // 遍历移除
        Iterator<RpcChannelFuture> iterator = futureList.listIterator();
        while (iterator.hasNext()) {
            RpcChannelFuture rpcChannelFuture = iterator.next();
            RpcAddress rpcAddress = rpcChannelFuture.address();

            // 是否相同
            boolean isTheSame = isTheSameIpPort(rpcAddress, serviceEntry);
            if (isTheSame) {
                //				log.info("移除服务端 {} 对应的 channel 信息", serviceEntry);
                iterator.remove();
            }
        }

        // 更新
        serverChannelFutureMap.put(serviceId, futureList);
    }

    @Override
    public void addRegisterChannel( RegisterCenterAddNotifyBody body, Channel channel ) {
        //		log.info("接收到注册中心新增机器的通知 {}", body);
        String ip = body.ip();
        int port = body.port();

        boolean contains = containsIpPort(ip, port);
        if (contains) {
            log.info("当前注册中心的服务信息已包含，不用处理。");
            return;
        }

        // 重新创建
        RpcAddress rpcAddress = new RpcAddress(ip, port);
        final String key = buildKey(rpcAddress);
        RpcChannelFuture newChannel = createNewRegisterChannel(rpcAddress);
        registerCenterChannelMap.put(key, newChannel);

        // 推送所有关注的服务标识
        for (String serviceId : subscribeServerIdSet) {
            subscribeServer(serviceId, newChannel);
        }
    }

    @Override
    public void removeRegisterChannel( RegisterCenterRemoveNotifyBody body ) {
        //		log.info("接收到注册中心移除机器的通知 {}", body);
        String ip = body.ip();
        int port = body.port();
        for (Map.Entry<String, RpcChannelFuture> entry : registerCenterChannelMap.entrySet()) {
            RpcChannelFuture future = entry.getValue();

            if (isSameIpPort(future, ip, port)) {
                //				log.info("开始移除注册中心注销的机器 ip: {}, port: {}", ip, port);
                String key = entry.getKey();
                future.destroyable().destroy();
                registerCenterChannelMap.remove(key);
                //				log.info("完成移除注册中心注销的机器 ip: {}, port: {}", ip, port);
            }
        }
    }

    /**
     * 是否包含指定的地址和端口
     *
     * @param ip 地址
     * @param port 端口
     * @return 是否
     * @since 0.1.8
     */
    private boolean containsIpPort( String ip, int port ) {
        Collection<RpcChannelFuture> futures = registerCenterChannelMap.values();
        for (RpcChannelFuture future : futures) {
            if (isSameIpPort(future, ip, port)) {
                return true;
            }
        }

        return false;
    }

    /**
     * 是否为相同的 ip
     *
     * @param rpcChannelFuture channel
     * @param ip 地址
     * @param port 端口
     * @return 是否
     * @since 0.1.8
     */
    private boolean isSameIpPort( RpcChannelFuture rpcChannelFuture, String ip, int port ) {
        RpcAddress rpcAddress = rpcChannelFuture.address();
        if (rpcAddress.address().equals(ip) && rpcAddress.port() == port) {
            return true;
        }
        return false;
    }

    /**
     * 是否为相同的地址端口
     *
     * @param rpcAddress 地址
     * @param serviceEntry 服务信息
     * @return 结果
     * @since 0.1.8
     */
    private boolean isTheSameIpPort( final RpcAddress rpcAddress, final ServiceEntry serviceEntry ) {
        String ipPort = rpcAddress.address() + ":" + rpcAddress.port();
        String ipPortEntry = serviceEntry.ip() + ":" + serviceEntry.port();

        return ipPort.equals(ipPortEntry);
    }

    /**
     * 初始化列表
     *
     * @param rpcAddressList 地址
     * @param config 配置
     * @return 结果
     * @since 0.1.6
     */
    private List<RpcChannelFuture> initChannelFutureList(
            List<RpcAddress> rpcAddressList, ClientQueryServerChannelConfig config ) {
        final boolean check = config.check();
        // 检测可用性
        if (check) {
            // 1. 列表为空
            //			if (CollectionUtil.isEmpty(rpcAddressList)) {
            //				log.error("[Rpc Client] rpc address list is empty!");
            //				throw new RpcRuntimeException();
            //			}

            // 2. 初始化
            return ChannelHandlers.channelFutureList(
                    rpcAddressList,
                    new ChannelHandlerFactory() {
                        @Override
                        public ChannelHandler handler() {
                            final ChannelHandler channelHandler =
                                    new RpcClientHandler(invokeManager);
                            return ChannelHandlers.objectCodecHandler(channelHandler);
                        }
                    });
        }

        // 如果不检测可用性
        List<RpcChannelFuture> resultList = new ArrayList<>();
        //		if (CollectionUtil.isEmpty(rpcAddressList)) {
        //			log.warn("[Rpc Client] rpc address list is empty, without check init.");
        //			return resultList;
        //		}
        // 如果异常，则捕获
        for (RpcAddress rpcAddress : rpcAddressList) {
            try {
                RpcChannelFuture future =
                        ChannelHandlers.channelFuture(
                                rpcAddress,
                                new ChannelHandlerFactory() {
                                    @Override
                                    public ChannelHandler handler() {
                                        final ChannelHandler channelHandler =
                                                new RpcClientHandler(invokeManager);
                                        return ChannelHandlers.objectCodecHandler(channelHandler);
                                    }
                                });
                resultList.add(future);
            } catch (Exception exception) {
                //				log.error("[Rpc Client] rpc address init failed, without check init. {}",
                //					rpcAddress, exception);
            }
        }

        return resultList;
    }

    /**
     * 获取 rpc 地址信息列表 （1）默认直接通过指定的地址获取 （2）如果指定列表为空，且
     *
     * @return rpc 地址信息列表
     * @since 2024.06
     */
    private List<RpcAddress> getRpcAddresses( ClientQueryServerChannelConfig config ) {
        final List<RpcAddress> rpcAddresses = config.rpcAddresses();
        final List<RpcAddress> registerCenterList = config.registerCenterList();
        final String serviceId = config.serviceId();

        // 0. 快速返回
        //		if (CollectionUtil.isNotEmpty(rpcAddresses)) {
        //			return rpcAddresses;
        //		}

        // 1. 信息检查
        registerCenterParamCheck(config);

        // 2. 查询服务信息
        return this.queryServerAddressList(serviceId, registerCenterList, config);
    }

    /**
     * 注册中心参数检查 （1）如果可用列表为空，且没有指定自动发现，这个时候服务已经不可用了。
     *
     * @since 2024.06
     */
    private void registerCenterParamCheck( ClientQueryServerChannelConfig config ) {
        final boolean subscribe = config.subscribe();
        final String serviceId = config.serviceId();
        if (!subscribe) {
            //			log.error("[Rpc Client] no available services found for serviceId:{}", serviceId);
            throw new RpcRuntimeException();
        }
    }

    /**
     * 查询服务信息列表
     * <p>
     * 1. 将需要 subscribe 的方法进行记录。 set 2. 获取对应的集合
     *
     * @return 服务明细列表
     * @since 2024.06
     */
    @SuppressWarnings("unchecked")
    private List<ServiceEntry> lookUpServiceEntryList(
            final String serviceId,
            final List<RpcAddress> registerCenterList,
            ClientQueryServerChannelConfig config ) {
        // 1. 连接到注册中心
        List<RpcChannelFuture> channelFutureList = connectRegisterCenter(registerCenterList);

        // 2. 选择一个
        // 从前往后遍历，成功则直接返回
        final boolean subscribe = config.subscribe();
        if (subscribe) {
            this.subscribeServer(serviceId);
        }

        for (RpcChannelFuture rpcChannelFuture : channelFutureList) {
            try {
                ChannelFuture channelFuture = rpcChannelFuture.channelFuture();
                // 3. 发送查询请求
                ServiceEntry serviceEntry = ServiceEntryBuilder.of(serviceId);
                NotifyMessage lookUpMessage =
                        NotifyMessages.of(MessageTypeConst.CLIENT_LOOK_UP_SERVER_REQ, serviceEntry);

                final String seqId = lookUpMessage.seqId();
                invokeManager.addRequest(seqId, registerCenterTimeOut);
                channelFuture.channel().writeAndFlush(lookUpMessage);

                // 4. 等待查询结果
                RpcResponse rpcResponse = invokeManager.getResponse(seqId);
                return (List<ServiceEntry>) RpcResponses.getResult(rpcResponse);
            } catch (Exception exception) {
                log.warn("注册中心查询异常，继续尝试其他服务器。", exception);
            }
        }

        throw new RpcRuntimeException("服务端发现失败，请检查注册中心服务是否正常。");
    }

    /**
     * 连接到注册中心
     *
     * @param registerCenterList 注册中心地址列表
     * @return 对应的结果列表
     * @since 2024.06
     */
    private List<RpcChannelFuture> connectRegisterCenter(
            final List<RpcAddress> registerCenterList ) {
        List<RpcChannelFuture> rpcChannelFutures = new ArrayList<>();

        for (RpcAddress rpcAddress : registerCenterList) {
            String key = buildKey(rpcAddress);

            RpcChannelFuture oldChannel = registerCenterChannelMap.get(key);
            if (oldChannel != null) {
                // 已经存在，直接复用
                rpcChannelFutures.add(oldChannel);
            } else {
                RpcChannelFuture newChannel = createNewRegisterChannel(rpcAddress);
                rpcChannelFutures.add(newChannel);
            }
        }

        return rpcChannelFutures;
    }

    /**
     * 创建新的注册中心对象
     *
     * @param rpcAddress 地址
     * @return 结果
     * @since 0.1.8
     */
    private RpcChannelFuture createNewRegisterChannel( final RpcAddress rpcAddress ) {
        final ChannelHandler channelHandler = new RpcClientRegisterHandler(invokeManager, this);
        // 重新创建
        RpcChannelFuture channelFuture =
                ChannelHandlers.channelFuture(
                        rpcAddress,
                        new ChannelHandlerFactory() {
                            @Override
                            public ChannelHandler handler() {
                                return ChannelHandlers.objectCodecHandler(channelHandler);
                            }
                        });
        // 添加资源回收管理
        resourceManager.addDestroy(channelFuture.destroyable());

        final String key = buildKey(rpcAddress);
        // 放入便于复用
        registerCenterChannelMap.put(key, channelFuture);

        return channelFuture;
    }

    private String buildKey( final RpcAddress rpcAddress ) {
        return rpcAddress.address() + ":" + rpcAddress.port();
    }
}
