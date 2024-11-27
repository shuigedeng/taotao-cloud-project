package com.taotao.cloud.job.server.jobserver.service.handler;

import io.grpc.stub.StreamObserver;
import com.taotao.cloud.remote.protos.CommonCausa;
import com.taotao.cloud.remote.protos.ServerDiscoverCausa;

public interface RpcHandler {
     void handle(Object req,StreamObserver<CommonCausa.Response> responseObserver);
}
