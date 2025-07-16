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

package com.taotao.cloud.job.server.service;

import com.taotao.cloud.job.remote.protos.CommonCausa;
import com.taotao.cloud.job.remote.protos.ScheduleCausa;
import com.taotao.cloud.job.server.service.handler.HeartHealthReportHandler;
import com.taotao.cloud.remote.api.ScheduleGrpc;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

@GrpcService
public class ServerScheduleGrpcService extends ScheduleGrpc.ScheduleImplBase {
    @Autowired HeartHealthReportHandler heartHealthReportHandler;

    @Override
    public void reportWorkerHeartbeat(
            ScheduleCausa.WorkerHeartbeat request,
            StreamObserver<CommonCausa.Response> responseObserver) {
        heartHealthReportHandler.handle(request, responseObserver);
    }
}
