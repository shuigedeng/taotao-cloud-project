package com.taotao.cloud.job.server.jobserver.service;

import com.taotao.cloud.job.server.jobserver.service.handler.HeartHealthReportHandler;
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
