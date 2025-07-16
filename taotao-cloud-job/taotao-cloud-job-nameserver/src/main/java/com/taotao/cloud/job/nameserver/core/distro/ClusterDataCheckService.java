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

import com.taotao.cloud.job.common.constant.RemoteConstant;
import com.taotao.cloud.job.common.utils.net.MyNetUtil;
import com.taotao.cloud.job.nameserver.config.TtcJobNameServerConfig;
import com.taotao.cloud.job.nameserver.core.GrpcClient;
import com.taotao.cloud.job.nameserver.core.ServerIpAddressManager;
import com.taotao.cloud.job.nameserver.module.sync.FullSyncInfo;
import java.util.List;
import java.util.concurrent.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * dataCheck
 */
@Component
public class ClusterDataCheckService {
    @Value("${grpc.server.port}")
    private String port;

    private final List<String> clusterNodes;
    private final String curServerIp;
    private final ServerIpAddressManager serverIpAddressManager;
    private final GrpcClient grpcClient;
    private final Executor executor = Executors.newFixedThreadPool(10);

    public ClusterDataCheckService(
            ServerIpAddressManager serverIpAddressManager,
            TtcJobNameServerConfig ttcJobNameServerConfig,
            GrpcClient grpcClient) {
        this.curServerIp = MyNetUtil.address;
        this.grpcClient = grpcClient;
        this.clusterNodes = ttcJobNameServerConfig.getServerAddressList();
        this.serverIpAddressManager = serverIpAddressManager;

        // 启动定时心跳任务
        ScheduledExecutorService heartbeatScheduler = Executors.newScheduledThreadPool(1);
        heartbeatScheduler.scheduleAtFixedRate(this::sendHeartbeat, 5, 10, TimeUnit.SECONDS);
    }

    // 发送心跳
    private void sendHeartbeat() {
        String checksum = serverIpAddressManager.calculateChecksum();
        FullSyncInfo info = serverIpAddressManager.getClientAllInfo();
        for (String node : clusterNodes) {
            if (!node.contains(curServerIp)
                    && !node.equals(RemoteConstant.LOOPBACKIP + ":" + port)) { // 不发给自身
                grpcClient.dataCheck(checksum, node, info, executor);
            }
        }
    }
}
