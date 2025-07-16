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

package com.taotao.cloud.rpc.server.support.register;

import com.taotao.cloud.rpc.common.common.config.component.RpcAddress;
import com.taotao.cloud.rpc.registry.domain.entry.ServiceEntry;
import com.taotao.cloud.rpc.registry.domain.message.NotifyMessage;
import com.taotao.cloud.rpc.registry.domain.message.body.ServerHeartbeatBody;
import com.taotao.cloud.rpc.registry.domain.message.impl.NotifyMessages;
import com.taotao.cloud.rpc.registry.simple.constant.MessageTypeConst;
import io.netty.channel.Channel;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 默认的服务端注册管理实现
 *
 * @since 0.1.8
 */
public class DefaultServerRegisterLocalManager implements ServerRegisterManager {

    private static final Logger LOG =
            LoggerFactory.getLogger(DefaultServerRegisterLocalManager.class);

    /**
     * 存放对应的 map 信息
     *
     * @since 0.1.8
     */
    private final Map<String, ServiceEntry> serviceEntryMap;

    /**
     * 注册中心的 channel 列表
     *
     * @since 0.1.8
     */
    private final Map<String, Channel> registerCenterChannelMap;

    /**
     * 服务端端口号
     *
     * @since 0.2.0
     */
    private int port;

    /**
     * 心跳执行器
     *
     * @since 0.2.0
     */
    private final ScheduledExecutorService heartbeatExecutor;

    public DefaultServerRegisterLocalManager() {
        this.serviceEntryMap = new HashMap<>();
        this.registerCenterChannelMap = new HashMap<>();

        heartbeatExecutor = Executors.newSingleThreadScheduledExecutor();
        heartbeatExecutor.scheduleAtFixedRate(new HeartbeatThread(), 5, 2, TimeUnit.SECONDS);
    }

    private class HeartbeatThread implements Runnable {

        @Override
        public void run() {
            // 1. 每 2 秒钟，向 channel 发送请求信息
            LOG.debug(("[HEARTBEAT] 开始定时执行心跳"));
            //			String currentIp = NetUtil.getLocalIp();
            ServerHeartbeatBody body = new ServerHeartbeatBody();
            body.ip("currentIp");
            body.port(port);
            body.time(System.currentTimeMillis());

            NotifyMessage notifyMessage =
                    NotifyMessages.of(MessageTypeConst.SERVER_HEARTBEAT_REQ, body);
            for (Channel channel : registerCenterChannelMap.values()) {
                channel.writeAndFlush(notifyMessage);
            }
            LOG.debug("[HEARTBEAT] 完成定时执行心跳");
        }
    }

    @Override
    public DefaultServerRegisterLocalManager port(int port) {
        this.port = port;
        return this;
    }

    @Override
    public void register(ServiceEntry serviceEntry) {
        paramCheck(serviceEntry);
        //		log.info("[Rpc Server] add service: {}", serviceEntry);

        final String serviceId = serviceEntry.serviceId();
        serviceEntryMap.put(serviceId, serviceEntry);

        // 通知注册中心
        final NotifyMessage notifyMessage = buildRegisterMessage(serviceEntry);
        for (Channel channel : registerCenterChannelMap.values()) {
            //			log.info("[Rpc Server] register to service center: {}", notifyMessage);
            channel.writeAndFlush(notifyMessage);
        }
    }

    @Override
    public ServiceEntry unRegister(String serviceId) {
        //		ArgUtil.notEmpty(serviceId, "serviceId");

        // 本地信息清空
        ServiceEntry serviceEntry = serviceEntryMap.remove(serviceId);
        //		log.warn("[Rpc Server] 移除服务 {} 对应的服务对象为 {}", serviceId, serviceEntry);
        if (serviceEntry == null) {
            //			log.warn("[Rpc Server] serviceId: {} 对应的服务信息不存在", serviceId);
            return serviceEntry;
        }

        // 通知注册中心
        final NotifyMessage notifyMessage = buildUnRegisterMessage(serviceEntry);
        for (Channel channel : registerCenterChannelMap.values()) {
            //			log.info("[Rpc Server] unRegister to service center: {}", notifyMessage);
            channel.writeAndFlush(notifyMessage);
        }

        return serviceEntry;
    }

    @Override
    public void unRegisterAll() {
        Collection<ServiceEntry> serviceEntrySet = serviceEntryMap.values();

        //		if (CollectionUtil.isEmpty(serviceEntrySet)) {
        //			// 服务列表为空
        //			log.warn("[Rpc Server] remove service set is empty.");
        //			return;
        //		}

        for (ServiceEntry serviceEntry : serviceEntrySet) {
            this.unRegister(serviceEntry.serviceId());
        }
    }

    @Override
    public void addRegisterChannel(RpcAddress rpcAddress, Channel channel) {
        //		ArgUtil.notNull(rpcAddress, "rpcAddress");
        //		ArgUtil.notNull(channel, "channel");

        String key = buildKey(rpcAddress);
        // 首先判断是否包含，如果不包含，则需要把当前的 service 全部注册一遍
        boolean contains = registerCenterChannelMap.containsKey(key);
        if (contains) {
            //			log.info("[Rpc Server] 已包含添加注册中心地址：{}，不做重复添加", rpcAddress);
            return;
        }

        registerCenterChannelMap.put(key, channel);
        //		log.info("[Rpc Server] 添加注册中心地址：{} 对应的 channel 信息", rpcAddress);

        // 针对这一个心的机器，重新注册服务
        Collection<ServiceEntry> serviceEntries = serviceEntryMap.values();
        //		if (CollectionUtil.isEmpty(serviceEntries)) {
        //			log.info("[Rpc Server] 对应的服务列表为空，无需通知注册中心。");
        //			return;
        //		}
        for (ServiceEntry serviceEntry : serviceEntries) {
            final NotifyMessage notifyMessage = buildRegisterMessage(serviceEntry);
            //			log.info("[Rpc Server] register to new register center: {}", notifyMessage);
            channel.writeAndFlush(notifyMessage);
        }
    }

    @Override
    public void removeRegisterChannel(RpcAddress rpcAddress) {
        //		ArgUtil.notNull(rpcAddress, "rpcAddress");

        String key = buildKey(rpcAddress);
        Channel channel = registerCenterChannelMap.remove(key);
        //		log.info("[Rpc Server] 移除注册中心地址：{}", rpcAddress);
    }

    @Override
    public void clearRegisterChannel() {
        LOG.info("[Rpc Server] 开始清空对应的注册中心 channel future");
        this.registerCenterChannelMap.clear();
        LOG.info("[Rpc Server] 完成清空对应的注册中心 channel future");
    }

    /**
     * 构建键值
     *
     * @param rpcAddress 地址
     * @return 结果
     * @since 0.1.8
     */
    private String buildKey(final RpcAddress rpcAddress) {
        String key = rpcAddress.address() + ":" + rpcAddress.port();

        return key;
    }

    /**
     * 参数校验
     *
     * @param serviceEntry 服务明细
     * @since 0.1.8
     */
    private void paramCheck(final ServiceEntry serviceEntry) {
        //		ArgUtil.notNull(serviceEntry, "serviceEntry");
        final String serviceId = serviceEntry.serviceId();
        //		ArgUtil.notEmpty(serviceId, "serviceId");
    }

    /**
     * 构建注册信息配置
     *
     * @param serviceEntry 配置信息
     * @return 注册信息
     * @since 0.1.8
     */
    private NotifyMessage buildRegisterMessage(final ServiceEntry serviceEntry) {
        return NotifyMessages.of(MessageTypeConst.SERVER_REGISTER_REQ, serviceEntry);
    }

    /**
     * 构建取消注册信息配置
     *
     * @param serviceEntry 配置信息
     * @return 注册信息
     * @since 0.1.8
     */
    private NotifyMessage buildUnRegisterMessage(final ServiceEntry serviceEntry) {
        return NotifyMessages.of(MessageTypeConst.SERVER_UN_REGISTER_REQ, serviceEntry);
    }
}
