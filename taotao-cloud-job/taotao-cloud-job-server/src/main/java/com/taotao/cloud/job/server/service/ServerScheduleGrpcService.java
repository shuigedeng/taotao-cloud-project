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
    @Autowired
	HeartHealthReportHandler heartHealthReportHandler;
    @Override
    public void reportWorkerHeartbeat(ScheduleCausa.WorkerHeartbeat request, StreamObserver<CommonCausa.Response> responseObserver) {
        heartHealthReportHandler.handle(request, responseObserver);
    }
}
