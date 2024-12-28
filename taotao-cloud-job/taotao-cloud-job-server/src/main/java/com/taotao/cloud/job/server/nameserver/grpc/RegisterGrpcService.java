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

package com.taotao.cloud.job.server.nameserver.grpc;

import com.taotao.cloud.job.server.nameserver.balance.ServerIpAddressManagerService;
import com.taotao.cloud.job.server.nameserver.module.ReBalanceInfo;
import com.taotao.cloud.remote.api.RegisterToNameServerGrpc;
import com.taotao.cloud.remote.protos.CommonCausa;
import com.taotao.cloud.remote.protos.RegisterCausa;
import io.grpc.stub.StreamObserver;
import java.util.ArrayList;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

@GrpcService
public class RegisterGrpcService extends RegisterToNameServerGrpc.RegisterToNameServerImplBase {
    @Autowired
    ServerIpAddressManagerService service;

    @Override
    public void serverRegister(
            RegisterCausa.ServerRegisterReporter request, StreamObserver<CommonCausa.Response> responseObserver) {
        if (!service.getServerAddressSet().contains(request.getServerIpAddress())) {
            service.add2ServerAddressSet(request);
        }
        CommonCausa.Response build = CommonCausa.Response.newBuilder().build();
        responseObserver.onNext(build);
        responseObserver.onCompleted();
    }

    @Override
    public void workerSubscribe(
            RegisterCausa.WorkerSubscribeReq request, StreamObserver<CommonCausa.Response> responseObserver) {
        service.addAppName2WorkerNumMap(request.getWorkerIpAddress(), request.getAppName());
        service.addScheduleTimes(request.getServerIpAddress(), request.getScheduleTime());
        ReBalanceInfo info = service.getServerAddressReBalanceList(request.getServerIpAddress(), request.getAppName());

        RegisterCausa.WorkerSubscribeResponse build = RegisterCausa.WorkerSubscribeResponse.newBuilder()
                .addAllServerAddressIpLists(info.getServerIpList())
                .setIsSplit(info.isSplit())
                .setIsChangeServer(info.isChangeServer())
                .setSubAppName(info.getSubAppName())
                .build();
        CommonCausa.Response build1 = CommonCausa.Response.newBuilder()
                .setWorkerSubscribeResponse(build)
                .build();
        responseObserver.onNext(build1);
        responseObserver.onCompleted();
    }

    @Override
    public void fetchServerList(
            RegisterCausa.FetchServerAddressListReq request, StreamObserver<CommonCausa.Response> responseObserver) {
        ArrayList<String> serverAddressList = new ArrayList<>(service.getServerAddressSet());
        RegisterCausa.ServerAddressList builder = RegisterCausa.ServerAddressList.newBuilder()
                .addAllServerAddressList(serverAddressList)
                .build();
        CommonCausa.Response build1 =
                CommonCausa.Response.newBuilder().setServerAddressList(builder).build();
        responseObserver.onNext(build1);
        responseObserver.onCompleted();
    }
}
