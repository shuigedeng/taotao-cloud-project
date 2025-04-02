package com.taotao.cloud.job.worker.service;

import com.taotao.cloud.job.remote.protos.CommonCausa;
import com.taotao.cloud.job.remote.protos.ScheduleCausa;
import com.taotao.cloud.job.worker.common.KJobWorkerConfig;
import com.taotao.cloud.job.worker.service.handler.ScheduleJobHandler;
import com.taotao.cloud.remote.api.ScheduleGrpc;
import io.grpc.stub.StreamObserver;
import org.springframework.beans.factory.annotation.Autowired;

public class WorkerScheduleGrpcService extends ScheduleGrpc.ScheduleImplBase {
    ScheduleJobHandler scheduleJobHandler;

    public WorkerScheduleGrpcService(KJobWorkerConfig kJobWorkerConfig) {
        this.scheduleJobHandler = new ScheduleJobHandler(kJobWorkerConfig);
    }

    @Override
    public void serverScheduleJob(ScheduleCausa.ServerScheduleJobReq request, StreamObserver<CommonCausa.Response> responseObserver) {
        scheduleJobHandler.handle(request, responseObserver);
    }
}
