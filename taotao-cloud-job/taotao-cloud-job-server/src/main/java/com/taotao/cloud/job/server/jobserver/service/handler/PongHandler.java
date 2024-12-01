package com.taotao.cloud.job.server.jobserver.service.handler;

import io.grpc.stub.StreamObserver;
import org.springframework.stereotype.Component;

@Component
public class PongHandler implements RpcHandler{
    @Override
    public void handle(Object req, StreamObserver<CommonCausa.Response> responseObserver) {
        CommonCausa.Response build = CommonCausa.Response.newBuilder().setCode(RemoteConstant.SUCCESS).build();
        responseObserver.onNext(build);
        responseObserver.onCompleted();
    }
}
