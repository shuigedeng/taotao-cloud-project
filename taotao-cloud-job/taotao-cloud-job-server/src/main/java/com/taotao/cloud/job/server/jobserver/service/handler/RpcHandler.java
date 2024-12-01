package com.taotao.cloud.job.server.jobserver.service.handler;

import io.grpc.stub.StreamObserver;

public interface RpcHandler {
     void handle(Object req,StreamObserver<CommonCausa.Response> responseObserver);
}
