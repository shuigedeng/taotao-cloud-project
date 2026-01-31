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

package com.taotao.cloud.job.worker.common.grpc.strategies.strategy;

import com.taotao.cloud.job.remote.protos.CommonCausa;
import com.taotao.cloud.job.remote.protos.ScheduleCausa;
import com.taotao.cloud.job.worker.common.constant.TransportTypeEnum;
import com.taotao.cloud.job.worker.common.grpc.RpcInitializer;
import com.taotao.cloud.job.worker.common.grpc.strategies.GrpcStrategy;
import com.taotao.cloud.remote.api.ScheduleGrpc;
import io.grpc.ManagedChannel;

import java.util.HashMap;

/**
 * HeartHealthReportRpcService
 *
 * @author shuigedeng
 * @version 2026.03
 * @since 2025-12-19 09:30:45
 */
public class HeartHealthReportRpcService implements GrpcStrategy<TransportTypeEnum> {

    HashMap<String, ScheduleGrpc.ScheduleBlockingStub> ip2Stubs = new HashMap<>();

    @Override
    public void init() {
        HashMap<String, ManagedChannel> ip2ChannelsMap = RpcInitializer.getIp2ChannelsMap();
        for (String ip : ip2ChannelsMap.keySet()) {
            ip2Stubs.put(ip, ScheduleGrpc.newBlockingStub(ip2ChannelsMap.get(ip)));
        }
    }

    @Override
    public Object execute( Object params ) {
        ScheduleCausa.WorkerHeartbeat workerHeartbeat = (ScheduleCausa.WorkerHeartbeat) params;
        ScheduleGrpc.ScheduleBlockingStub stub = ip2Stubs.get(workerHeartbeat.getServerIpAddress());
        CommonCausa.Response response = stub.reportWorkerHeartbeat(workerHeartbeat);
        return null;
    }

    @Override
    public TransportTypeEnum getTypeEnumFromStrategyClass() {
        return TransportTypeEnum.HEARTBEAT_HEALTH_REPORT;
    }
}
