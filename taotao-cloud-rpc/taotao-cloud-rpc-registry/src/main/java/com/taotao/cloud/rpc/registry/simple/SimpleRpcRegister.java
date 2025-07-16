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

package com.taotao.cloud.rpc.registry.simple;

import com.taotao.cloud.rpc.common.common.rpc.domain.RpcResponse;
import com.taotao.cloud.rpc.common.common.rpc.domain.impl.DefaultRpcResponse;
import com.taotao.cloud.rpc.common.common.util.IpUtils;
import com.taotao.cloud.rpc.registry.domain.entry.ServiceEntry;
import com.taotao.cloud.rpc.registry.domain.message.NotifyMessage;
import com.taotao.cloud.rpc.registry.domain.message.body.ServerHeartbeatBody;
import com.taotao.cloud.rpc.registry.domain.message.impl.NotifyMessages;
import com.taotao.cloud.rpc.registry.simple.client.RegisterClientService;
import com.taotao.cloud.rpc.registry.simple.constant.MessageTypeConst;
import com.taotao.cloud.rpc.registry.simple.server.RegisterServerService;
import com.taotao.cloud.rpc.registry.simple.server.impl.DefaultRegisterServerService;
import com.taotao.cloud.rpc.registry.spi.RpcRegister;
import io.netty.channel.Channel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p> 简单的 rpc 注册 </p>
 * （1）各种关系的关系服务类 （2）各种关系之间的通讯类 （3）domain 层
 *
 * @since 2024.06
 */
public class SimpleRpcRegister implements RpcRegister {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultRegisterServerService.class);

    /**
     * 服务端信息管理
     *
     * @since 2024.06
     */
    private final RegisterServerService registerServerService;

    /**
     * 客户端信息管理
     *
     * @since 2024.06
     */
    private final RegisterClientService registerClientService;

    /**
     * 服务端心跳 map
     *
     * @since 0.2.0
     */
    private final Map<String, Long> serverHeartbeatMap;

    /**
     * 服务端心跳定时处理
     *
     * @since 0.2.0
     */
    private final ScheduledExecutorService serverHeartBeatExecutor;

    public SimpleRpcRegister(
            RegisterServerService registerServerService,
            RegisterClientService registerClientService) {
        this.registerServerService = registerServerService;
        this.registerClientService = registerClientService;

        this.serverHeartbeatMap = new ConcurrentHashMap<>();
        this.serverHeartBeatExecutor = Executors.newSingleThreadScheduledExecutor();

        final Runnable runnable = new ServerHeartBeatThread();
        serverHeartBeatExecutor.scheduleAtFixedRate(runnable, 60, 60, TimeUnit.SECONDS);
    }

    /**
     * 心跳线程
     *
     * @since 0.2.0
     */
    private class ServerHeartBeatThread implements Runnable {

        /**
         * 限制毫秒数
         *
         * @since 0.2.0
         */
        private final long limitMills = 120 * 1000;

        @Override
        public void run() {
            // 遍历集合，找到过期的服务端
            List<String> expiredServices = getExpiredServiceList(limitMills);
            //			if (CollectionUtil.isEmpty(expiredServices)) {
            //				log.debug("[HEARTBEAT] 本次过期服务检测不存在，忽略。");
            //				return;
            //			}

            // 遍历过期的服务端，执行
            for (String ipPort : expiredServices) {
                Collection<ServiceEntry> serviceEntries =
                        registerServerService.serviceEntries(ipPort);
                //				if (CollectionUtil.isEmpty(serviceEntries)) {
                //					log.debug("[HEARTBEAT] ipPort {} 本次过期服务列表不存在，忽略。", ipPort);
                //					continue;
                //				}

                //				for (ServiceEntry serviceEntry : serviceEntries) {
                //					log.debug("[HEARTBEAT] serviceEntry {} 已过期，开始取消注册处理。", serviceEntry);
                //					unRegister(serviceEntry);
                //					log.debug("[HEARTBEAT] serviceEntry {} 已过期，完成取消注册处理。", serviceEntry);
                //				}
            }
        }
    }

    /**
     * 获取过期的服务列表
     *
     * @param limitMills 超时时间
     * @return 结果
     * @since 0.2.0
     */
    private List<String> getExpiredServiceList(final long limitMills) {
        // 遍历集合，找到过期的服务端
        List<String> expiredServices = new ArrayList<>();
        long currentMills = System.currentTimeMillis();
        for (Map.Entry<String, Long> entry : serverHeartbeatMap.entrySet()) {
            String key = entry.getKey();
            long time = entry.getValue();

            long differMills = currentMills - time;
            if (differMills > limitMills) {
                //				log.debug("[HEARTBEAT] ip:port {} has been expired for {}", differMills);
                expiredServices.add(key);
            }
        }
        return expiredServices;
    }

    @Override
    public void register(ServiceEntry serviceEntry, Channel channel) {
        List<ServiceEntry> serviceEntryList = registerServerService.register(serviceEntry, channel);

        // 通知监听者
        registerClientService.registerNotify(serviceEntry.serviceId(), serviceEntry);
    }

    @Override
    public void unRegister(ServiceEntry serviceEntry) {
        List<ServiceEntry> serviceEntryList = registerServerService.unRegister(serviceEntry);

        // 通知监听者
        registerClientService.unRegisterNotify(serviceEntry.serviceId(), serviceEntry);
    }

    @Override
    public void subscribe(ServiceEntry clientEntry, final Channel channel) {
        registerClientService.subscribe(clientEntry, channel);
    }

    @Override
    public void unSubscribe(ServiceEntry clientEntry, Channel channel) {
        registerClientService.unSubscribe(clientEntry, channel);
    }

    @Override
    public void lookUp(String seqId, ServiceEntry clientEntry, Channel channel) {
        final String serviceId = clientEntry.serviceId();
        List<ServiceEntry> serviceEntryList = registerServerService.lookUp(serviceId);

        // 回写
        // 为了复用原先的相应结果，此处直接使用 rpc response
        RpcResponse rpcResponse =
                DefaultRpcResponse.newInstance().seqId(seqId).result(serviceEntryList);
        NotifyMessage notifyMessage =
                NotifyMessages.of(MessageTypeConst.CLIENT_LOOK_UP_SERVER_RESP, seqId, rpcResponse);
        channel.writeAndFlush(notifyMessage);
    }

    @Override
    public void serverHeartbeat(ServerHeartbeatBody heartbeatBody, Channel channel) {
        String ip = heartbeatBody.ip();
        int port = heartbeatBody.port();

        // 存储当前的时间
        String key = IpUtils.ipPort(ip, port);
        long time = heartbeatBody.time();
        serverHeartbeatMap.put(key, time);
        //		log.debug("[HEARTBEAT] 接收到服务端的心跳 {}", heartbeatBody);
    }
}
