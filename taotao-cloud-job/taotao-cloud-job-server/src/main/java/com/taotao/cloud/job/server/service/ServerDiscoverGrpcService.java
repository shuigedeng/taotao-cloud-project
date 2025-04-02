package com.taotao.cloud.job.server.service;

import com.taotao.cloud.job.remote.protos.CommonCausa;
import com.taotao.cloud.job.remote.protos.ServerDiscoverCausa;
import com.taotao.cloud.job.server.service.handler.AppInfoHandler;
import com.taotao.cloud.job.server.service.handler.PongHandler;
import com.taotao.cloud.job.server.service.handler.ServerChangeHandler;
import com.taotao.cloud.remote.api.ServerDiscoverGrpc;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

@GrpcService
@Slf4j
public class ServerDiscoverGrpcService extends ServerDiscoverGrpc.ServerDiscoverImplBase {

	@Autowired
	HeartbeatHandler heartbeatHandler;
	@Autowired
	AppInfoHandler appInfoHandler;
	@Autowired
	PongHandler pongHandler;
	@Autowired
	ServerChangeHandler serverChangeHandler;

	public void heartbeatCheck(ServerDiscoverCausa.HeartbeatCheck request, StreamObserver<CommonCausa.Response> responseObserver) {
		heartbeatHandler.handle(request, responseObserver);
	}

	public void assertApp(ServerDiscoverCausa.AppName request, StreamObserver<CommonCausa.Response> responseObserver) {
		appInfoHandler.handle(request, responseObserver);
	}

	public void pingServer(ServerDiscoverCausa.Ping request, StreamObserver<CommonCausa.Response> responseObserver) {
		pongHandler.handle(request, responseObserver);
	}

	public void serverChange(ServerDiscoverCausa.ServerChangeReq request, StreamObserver<CommonCausa.Response> responseObserver) {
		serverChangeHandler.handle(request, responseObserver);
	}
}
