package com.taotao.cloud.job.core.worker.service.handler;

import io.grpc.stub.StreamObserver;
import com.taotao.cloud.remote.protos.CommonCausa;

public interface RpcHandler {
     void handle(Object req,StreamObserver<CommonCausa.Response> responseObserver);
}
