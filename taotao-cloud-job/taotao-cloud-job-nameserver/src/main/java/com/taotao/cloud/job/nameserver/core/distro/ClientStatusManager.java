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

package com.taotao.cloud.job.nameserver.core.distro;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.taotao.cloud.job.common.constant.RemoteConstant;
import com.taotao.cloud.job.nameserver.module.ClientHeartbeat;
import com.taotao.cloud.job.nameserver.module.ClientNodeInfo;
import com.taotao.cloud.job.nameserver.module.sync.ServerRemoveSyncInfo;
import com.taotao.cloud.job.nameserver.module.sync.WorkerRemoveSyncInfo;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * hold client status through heartbeat between server and client
 */
@Component
@Slf4j
public class ClientStatusManager {
    Map<String, ClientNodeInfo> address2ClientNodeMap = Maps.newConcurrentMap();
    DistroClientDataProcessor processor;

    ClientStatusManager(DistroClientDataProcessor processor) {
        this.processor = processor;
        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);
        executor.scheduleAtFixedRate(this::cleanClientNode, 1000, 5000, TimeUnit.MILLISECONDS);
    }

    public void updateStatus(ClientHeartbeat heartbeat) {
        String clientIp = heartbeat.getIp();
        String type = heartbeat.getClientType();
        long heartbeatTime = heartbeat.getHeartbeatTime();
        String appName = heartbeat.getAppName();

        ClientNodeInfo c =
                address2ClientNodeMap.computeIfAbsent(
                        clientIp + ":" + type,
                        ignore -> {
                            ClientNodeInfo info = new ClientNodeInfo();
                            info.setType(type);
                            info.refresh(heartbeat);
                            return info;
                        });
        long oldTime = c.getLastActiveTime();
        if (heartbeatTime < oldTime) {
            log.warn(
                    "[ClientStatusManager] receive the expired heartbeat from {}, serverTime: {}, heartTime: {}",
                    clientIp,
                    System.currentTimeMillis(),
                    heartbeat.getHeartbeatTime());
            return;
        }
        c.refresh(heartbeat);
    }

    private void cleanClientNode() {
        List<String> timeoutAddress = Lists.newLinkedList();
        address2ClientNodeMap.forEach(
                (addr, clientInfo) -> {
                    if (clientInfo.timeout()) {
                        timeoutAddress.add(addr);
                    }
                });
        if (!timeoutAddress.isEmpty()) {
            log.info(
                    "[ClientStatusManager] detective timeout client({}), try to release their infos.",
                    timeoutAddress);
            timeoutAddress.forEach(this::handleClean);
        }
    }

    private void handleClean(String clientIp) {
        String type = address2ClientNodeMap.get(clientIp).getType();
        String appName = address2ClientNodeMap.get(clientIp).getAppName();
        if (type.equals(RemoteConstant.SERVER)) {
            processor.handleSync(
                    new ServerRemoveSyncInfo(clientIp), RemoteConstant.INCREMENTAL_REMOVE_SERVER);
        } else {
            processor.handleSync(
                    new WorkerRemoveSyncInfo(clientIp, appName),
                    RemoteConstant.INCREMENTAL_REMOVE_WORKER);
        }
        address2ClientNodeMap.remove(clientIp);
    }
}
