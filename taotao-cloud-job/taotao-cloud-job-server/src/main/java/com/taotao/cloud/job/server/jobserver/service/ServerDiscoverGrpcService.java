package com.taotao.cloud.job.server.jobserver.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Sets;
import com.taotao.cloud.job.server.jobserver.service.handler.AppInfoHandler;
import com.taotao.cloud.job.server.jobserver.service.handler.HeartbeatHandler;
import com.taotao.cloud.job.server.jobserver.service.handler.PongHandler;
import com.taotao.cloud.job.server.jobserver.service.handler.ServerChangeHandler;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Objects;
import java.util.Set;

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