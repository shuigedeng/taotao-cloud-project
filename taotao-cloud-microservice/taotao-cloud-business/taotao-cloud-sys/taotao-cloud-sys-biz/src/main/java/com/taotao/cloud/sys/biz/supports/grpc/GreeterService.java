package com.taotao.cloud.sys.biz.supports.grpc;

import com.taotao.boot.common.utils.log.LogUtils;
import com.taotao.cloud.log.api.grpc.BooleanReply;
import com.taotao.cloud.log.api.grpc.Log;
import com.taotao.cloud.log.api.grpc.LogGrpcServiceGrpc;
import com.taotao.cloud.log.api.grpc.LogRequest;
import io.grpc.stub.StreamObserver;
import org.lognet.springboot.grpc.GRpcService;

/**
 * GreeterService
 *
 * @author shuigedeng
 * @version 2026.01
 * @since 2025-12-19 09:30:45
 */
@GRpcService
public class GreeterService extends LogGrpcServiceGrpc.LogGrpcServiceImplBase {

    @Override
    public void insertLog( Log request, StreamObserver<BooleanReply> responseObserver ) {
        LogUtils.info("获取到grpc insert Log日志: {}", request);
        responseObserver.onNext(BooleanReply.newBuilder().setReply(true).build());
        responseObserver.onCompleted();
    }

    @Override
    public void updateLog( Log request, StreamObserver<BooleanReply> responseObserver ) {
        LogUtils.info("获取到grpc update Log日志: {}", request);
        responseObserver.onNext(BooleanReply.newBuilder().setReply(true).build());
        responseObserver.onCompleted();
    }

    @Override
    public void searchLog( LogRequest request, StreamObserver<Log> responseObserver ) {
        Log log = Log.newBuilder().setType(1).setAddress("safasf").setCreatetime("salfdasfd").setId("111").setStatus(1)
                .build();
        responseObserver.onNext(log);
        responseObserver.onCompleted();
    }

    @Override
    public void deleteLog( LogRequest request, StreamObserver<BooleanReply> responseObserver ) {
        LogUtils.info("获取到grpc delete Log日志: {}", request);
        responseObserver.onNext(BooleanReply.newBuilder().setReply(true).build());
        responseObserver.onCompleted();
    }
}
