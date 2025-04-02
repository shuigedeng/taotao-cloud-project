package com.taotao.cloud.job.server.service.handler;

import com.taotao.cloud.job.remote.protos.CommonCausa;
import io.grpc.stub.StreamObserver;

public interface RpcHandler {
     void handle(Object req,StreamObserver<CommonCausa.Response> responseObserver);
}
