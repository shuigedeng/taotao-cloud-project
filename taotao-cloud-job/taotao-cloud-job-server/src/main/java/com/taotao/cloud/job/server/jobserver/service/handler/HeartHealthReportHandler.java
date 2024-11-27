package com.taotao.cloud.job.server.jobserver.service.handler;

import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import com.taotao.cloud.common.domain.WorkerHeartbeat;
import com.taotao.cloud.remote.protos.CommonCausa;
import com.taotao.cloud.remote.protos.ScheduleCausa;
import com.taotao.cloud.remote.protos.ServerDiscoverCausa;
import com.taotao.cloud.server.remote.worker.WorkerClusterManagerService;
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
