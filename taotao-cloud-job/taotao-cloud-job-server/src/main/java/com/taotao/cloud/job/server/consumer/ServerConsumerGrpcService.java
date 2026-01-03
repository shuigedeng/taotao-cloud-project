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

package com.taotao.cloud.job.server.consumer;

import com.taotao.cloud.job.remote.protos.CommonCausa;
import com.taotao.cloud.job.remote.protos.MqCausa;
import com.taotao.cloud.job.server.consumer.entity.Response;
import com.taotao.cloud.job.server.consumer.entity.ResponseEnum;
import com.taotao.cloud.remote.api.MqGrpc;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;

/**
 * ServerConsumerGrpcService
 *
 * @author shuigedeng
 * @version 2026.02
 * @since 2025-12-19 09:30:45
 */
@GrpcService
@Slf4j
public class ServerConsumerGrpcService extends MqGrpc.MqImplBase {

    DefaultMessageStore defaultMessageStore = new DefaultMessageStore();

    ServerConsumerGrpcService( Consumer consumer ) {
        defaultMessageStore.startWatcher(consumer);
        DelayedQueueManager.init(consumer);
    }

    @Override
    public void send(
            MqCausa.Message request, StreamObserver<CommonCausa.Response> responseObserver ) {
        defaultMessageStore.writeToCommitLog(
                request,
                new RemotingResponseCallback() {
                    @Override
                    public void callback( Response response ) {
                        if (response.getRes().equals(ResponseEnum.SUCCESS)) {
                            CommonCausa.Response build =
                                    CommonCausa.Response.newBuilder().setCode(200).build();
                            responseObserver.onNext(build);
                            responseObserver.onCompleted();
                        } else {
                            log.error(response.getRes().getV());
                            CommonCausa.Response build =
                                    CommonCausa.Response.newBuilder().setCode(500).build();
                            responseObserver.onNext(build);
                            responseObserver.onCompleted();
                        }
                    }
                });
    }
}
