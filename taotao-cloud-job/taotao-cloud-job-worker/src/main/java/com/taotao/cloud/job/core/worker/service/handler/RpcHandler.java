package com.taotao.cloud.job.core.worker.service.handler;

import com.taotao.cloud.remote.protos.CommonCausa;
import io.grpc.stub.StreamObserver;

public interface RpcHandler {
	void handle(Object req, StreamObserver<CommonCausa.Response> responseObserver);
}
