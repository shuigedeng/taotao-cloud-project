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

package com.taotao.cloud.job.server.service.handler;

import com.taotao.cloud.job.common.domain.WorkerHeartbeat;
import com.taotao.cloud.job.remote.protos.CommonCausa;
import com.taotao.cloud.job.server.remote.worker.WorkerClusterManagerService;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

/**
 * HeartHealthReportHandler
 *
 * @author shuigedeng
 * @version 2026.01
 * @since 2025-12-19 09:30:45
 */
@Component
@Slf4j
public class HeartHealthReportHandler implements RpcHandler {

    @Override
    public void handle( Object req, StreamObserver<CommonCausa.Response> responseObserver ) {
        WorkerHeartbeat workerHeartbeat = new WorkerHeartbeat();
        BeanUtils.copyProperties(req, workerHeartbeat);
        WorkerClusterManagerService.updateStatus(workerHeartbeat);
        responseObserver.onNext(null);
        responseObserver.onCompleted();
    }
}
