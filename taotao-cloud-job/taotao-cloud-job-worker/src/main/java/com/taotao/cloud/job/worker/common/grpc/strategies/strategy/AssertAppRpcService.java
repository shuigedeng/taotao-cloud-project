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

import com.taotao.cloud.job.common.constant.RemoteConstant;
import com.taotao.cloud.job.common.exception.TtcJobException;
import com.taotao.cloud.job.common.utils.CommonUtils;
import com.taotao.cloud.job.remote.protos.CommonCausa;
import com.taotao.cloud.job.remote.protos.ServerDiscoverCausa;
import com.taotao.cloud.job.worker.common.constant.TransportTypeEnum;
import com.taotao.cloud.job.worker.common.grpc.RpcInitializer;
import com.taotao.cloud.job.worker.common.grpc.strategies.GrpcStrategy;
import com.taotao.cloud.job.worker.subscribe.WorkerSubscribeManager;
import com.taotao.cloud.remote.api.ServerDiscoverGrpc;
import io.grpc.ManagedChannel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

/**
 * AssertAppRpcService
 *
 * @author shuigedeng
 * @version 2026.02
 * @since 2025-12-19 09:30:45
 */
@Slf4j
public class AssertAppRpcService implements GrpcStrategy<TransportTypeEnum> {

    List<ServerDiscoverGrpc.ServerDiscoverBlockingStub> serverDiscoverStubs = new ArrayList<>();

    @Override
    public void init() {
        HashMap<String, ManagedChannel> ip2ChannelsMap = RpcInitializer.getIp2ChannelsMap();
        for (ManagedChannel channel : ip2ChannelsMap.values()) {
            serverDiscoverStubs.add(ServerDiscoverGrpc.newBlockingStub(channel));
        }
    }

    @Override
    public Object execute( Object params ) {
        ServerDiscoverCausa.AppName appNameInfo = (ServerDiscoverCausa.AppName) params;
        for (ServerDiscoverGrpc.ServerDiscoverBlockingStub serverDiscoverStub :
                serverDiscoverStubs) {
            try {
                if (WorkerSubscribeManager.isSplit()) {
                    // 需要分组，依附于新的server，优先选择最小连接的server
                    appNameInfo =
                            ServerDiscoverCausa.AppName.newBuilder()
                                    .setAppName(appNameInfo.getAppName())
                                    .setSubAppName(WorkerSubscribeManager.getSubAppName())
                                    .setTargetServer(
                                            WorkerSubscribeManager.getServerIpList().get(0))
                                    .build();
                    log.info("change server to ip:{}", appNameInfo.getTargetServer());
                }

                // 重置状态，防止多次分组
                WorkerSubscribeManager.setSplitStatus(false);

                ServerDiscoverCausa.AppName finalAppNameInfo = appNameInfo;
                CommonCausa.Response response =
                        CommonUtils.executeWithRetry0(
                                () -> serverDiscoverStub.assertApp(finalAppNameInfo));
                if (response.getCode() == RemoteConstant.SUCCESS) {
                    return response.getWorkInfo();
                } else {
                    log.error(
                            "[TtcJobWorker] assert appName failed, this appName is invalid, please register the appName  first.");
                    throw new TtcJobException(response.getMessage());
                }
            } catch (Exception e) {
                log.error("[TtcJobWorker] grpc error");
            }
        }
        log.error("[TtcJobWorker] no available server");
        throw new TtcJobException("no server available");
    }

    @Override
    public TransportTypeEnum getTypeEnumFromStrategyClass() {
        return TransportTypeEnum.ASSERT_APP;
    }
}
