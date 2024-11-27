package com.taotao.cloud.job.core.worker.service;

import io.grpc.stub.StreamObserver;
import com.taotao.cloud.remote.api.ScheduleGrpc;
import com.taotao.cloud.remote.protos.CommonCausa;
import com.taotao.cloud.remote.protos.ScheduleCausa;
import com.taotao.cloud.worker.common.TtcJobWorkerConfig;
import com.taotao.cloud.worker.service.handler.ScheduleJobHandler;
import org.springframework.beans.factory.annotation.Autowired;

public class WorkerScheduleGrpcService extends ScheduleGrpc.ScheduleImplBase {
    ScheduleJobHandler scheduleJobHandler;

    public WorkerScheduleGrpcService(TtcJobWorkerConfig kJobWorkerConfig) {
        this.scheduleJobHandler = new ScheduleJobHandler(kJobWorkerConfig);
    }

    @Override
    public void serverScheduleJob(ScheduleCausa.ServerScheduleJobReq request, StreamObserver<CommonCausa.Response> responseObserver) {
        scheduleJobHandler.handle(request, responseObserver);
    }
}
