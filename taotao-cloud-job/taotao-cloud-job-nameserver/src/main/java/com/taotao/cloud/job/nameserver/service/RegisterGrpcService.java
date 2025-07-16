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

package com.taotao.cloud.job.nameserver.service;

import com.taotao.cloud.job.common.constant.RemoteConstant;
import com.taotao.cloud.job.nameserver.core.ServerIpAddressManager;
import com.taotao.cloud.job.nameserver.core.distro.ClientStatusManager;
import com.taotao.cloud.job.nameserver.core.distro.DistroClientDataProcessor;
import com.taotao.cloud.job.nameserver.module.ClientHeartbeat;
import com.taotao.cloud.job.nameserver.module.ReBalanceInfo;
import com.taotao.cloud.job.nameserver.module.sync.ServerRegisterSyncInfo;
import com.taotao.cloud.job.nameserver.module.sync.WorkerSubscribeSyncInfo;
import com.taotao.cloud.job.remote.protos.CommonCausa;
import com.taotao.cloud.job.remote.protos.RegisterCausa;
import com.taotao.cloud.remote.api.RegisterToNameServerGrpc;
import io.grpc.stub.StreamObserver;
import java.util.ArrayList;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * handle schedule and worker register/subscribe
 * register/subscribe also a heartbeat
 */
@GrpcService
public class RegisterGrpcService extends RegisterToNameServerGrpc.RegisterToNameServerImplBase {
    @Autowired ServerIpAddressManager service;
    @Autowired DistroClientDataProcessor processor;
    @Autowired ClientStatusManager clientStatusManager;

    /**
     * register when ScheduleServer
     * also schedule heartbeat
     *
     * @param request
     * @param responseObserver
     */
    @Override
    public void serverRegister(
            RegisterCausa.ServerRegisterReporter request,
            StreamObserver<CommonCausa.Response> responseObserver) {
        service.add2ServerAddressSet(request.getServerIpAddress());

        // heartbeat
        clientStatusManager.updateStatus(
                new ClientHeartbeat(
                        request.getServerIpAddress(),
                        RemoteConstant.SERVER,
                        request.getRegisterTimestamp(),
                        null));
        // sync
        processor.handleSync(
                new ServerRegisterSyncInfo(request.getServerIpAddress()),
                RemoteConstant.INCREMENTAL_ADD_SERVER);
        CommonCausa.Response build = CommonCausa.Response.newBuilder().build();
        responseObserver.onNext(build);
        responseObserver.onCompleted();
    }

    /**
     * worker subscribe at fixed rate,and update scheduleTimes
     * also worker heartbeat
     *
     * @param request
     * @param responseObserver
     */
    @Override
    public void workerSubscribe(
            RegisterCausa.WorkerSubscribeReq request,
            StreamObserver<CommonCausa.Response> responseObserver) {
        service.addAppName2WorkerNumMap(request.getWorkerIpAddress(), request.getAppName());
        service.addScheduleTimes(request.getServerIpAddress(), request.getScheduleTime());
        // heartbeat
        clientStatusManager.updateStatus(
                new ClientHeartbeat(
                        request.getWorkerIpAddress(),
                        RemoteConstant.WORKER,
                        request.getSubscribeTimestamp(),
                        request.getAppName()));
        // sync
        processor.handleSync(
                new WorkerSubscribeSyncInfo(
                        request.getWorkerIpAddress(),
                        request.getAppName(),
                        request.getScheduleTime(),
                        request.getServerIpAddress()),
                RemoteConstant.INCREMENTAL_ADD_WORKER);

        ReBalanceInfo info =
                service.getServerAddressReBalanceList(
                        request.getServerIpAddress(), request.getAppName());

        RegisterCausa.WorkerSubscribeResponse build =
                RegisterCausa.WorkerSubscribeResponse.newBuilder()
                        .addAllServerAddressIpLists(info.getServerIpList())
                        .setIsSplit(info.isSplit())
                        .setIsChangeServer(info.isChangeServer())
                        .setSubAppName(info.getSubAppName())
                        .build();
        CommonCausa.Response build1 =
                CommonCausa.Response.newBuilder().setWorkerSubscribeResponse(build).build();
        responseObserver.onNext(build1);
        responseObserver.onCompleted();
    }

    /**
     * producer get serverList at fixed rate
     *
     * @param request
     * @param responseObserver
     */
    @Override
    public void fetchServerList(
            RegisterCausa.FetchServerAddressListReq request,
            StreamObserver<CommonCausa.Response> responseObserver) {
        ArrayList<String> serverAddressList = new ArrayList<>(service.getServerAddressSet());
        RegisterCausa.ServerAddressList builder =
                RegisterCausa.ServerAddressList.newBuilder()
                        .addAllServerAddressList(serverAddressList)
                        .build();
        CommonCausa.Response build1 =
                CommonCausa.Response.newBuilder().setServerAddressList(builder).build();
        responseObserver.onNext(build1);
        responseObserver.onCompleted();
    }
}
