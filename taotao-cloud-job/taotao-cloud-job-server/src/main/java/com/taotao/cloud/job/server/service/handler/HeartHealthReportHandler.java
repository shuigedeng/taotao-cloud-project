package com.taotao.cloud.job.server.service.handler;

import com.taotao.cloud.job.remote.protos.CommonCausa;
import com.taotao.cloud.job.server.remote.worker.WorkerClusterManagerService;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class HeartHealthReportHandler implements RpcHandler{

    @Override
    public void handle(Object req, StreamObserver<CommonCausa.Response> responseObserver) {
        WorkerHeartbeat workerHeartbeat = new WorkerHeartbeat();
        BeanUtils.copyProperties(req, workerHeartbeat);
        WorkerClusterManagerService.updateStatus(workerHeartbeat);
        responseObserver.onNext(null);
        responseObserver.onCompleted();
    }
}
