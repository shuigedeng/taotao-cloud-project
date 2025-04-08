package com.taotao.cloud.job.worker.service;

import com.taotao.cloud.job.remote.protos.CommonCausa;
import com.taotao.cloud.job.remote.protos.ScheduleCausa;
import com.taotao.cloud.job.worker.common.TtcJobWorkerConfig;
import com.taotao.cloud.job.worker.service.handler.ScheduleJobHandler;
import com.taotao.cloud.remote.api.ScheduleGrpc;
import io.grpc.stub.StreamObserver;

public class WorkerScheduleGrpcService extends ScheduleGrpc.ScheduleImplBase {
    ScheduleJobHandler scheduleJobHandler;

    public WorkerScheduleGrpcService(TtcJobWorkerConfig ttcJobWorkerConfig) {
        this.scheduleJobHandler = new ScheduleJobHandler(ttcJobWorkerConfig);
    }

    @Override
    public void serverScheduleJob(ScheduleCausa.ServerScheduleJobReq request, StreamObserver<CommonCausa.Response> responseObserver) {
        scheduleJobHandler.handle(request, responseObserver);
    }
}
