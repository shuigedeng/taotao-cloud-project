package com.taotao.cloud.job.server.jobserver.common.grpc;

import com.taotao.cloud.common.constant.RemoteConstant;
import com.taotao.cloud.remote.api.ScheduleGrpc;
import com.taotao.cloud.remote.protos.CommonCausa;
import com.taotao.cloud.remote.protos.ScheduleCausa;
import com.taotao.cloud.server.extension.singletonpool.GrpcStubSingletonPool;
import org.springframework.stereotype.Component;

@Component
public class ServerScheduleJobRpcClient implements RpcServiceCaller{
    @Override
    public Object call(Object params) {
        ScheduleCausa.ServerScheduleJobReq req = (ScheduleCausa.ServerScheduleJobReq) params;
        ScheduleGrpc.ScheduleBlockingStub stubSingleton = GrpcStubSingletonPool.getStubSingleton(req.getWorkerAddress(), ScheduleGrpc.class, ScheduleGrpc.ScheduleBlockingStub.class, RemoteConstant.WORKER);
        CommonCausa.Response response = stubSingleton.serverScheduleJob(req);
        return response;

    }
}
