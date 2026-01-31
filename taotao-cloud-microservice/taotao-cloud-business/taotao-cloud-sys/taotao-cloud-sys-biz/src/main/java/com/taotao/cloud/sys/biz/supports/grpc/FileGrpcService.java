package com.taotao.cloud.sys.biz.supports.grpc;

import com.taotao.cloud.file.api.grpc.BooleanReply;
import com.taotao.cloud.file.api.grpc.ConditionsRequest;
import com.taotao.cloud.file.api.grpc.File;
import com.taotao.cloud.file.api.grpc.FileGrpcServiceGrpc;
import io.grpc.stub.StreamObserver;
import org.lognet.springboot.grpc.GRpcService;

/**
 * FileGrpcService
 *
 * @author shuigedeng
 * @version 2026.03
 * @since 2025-12-19 09:30:45
 */
@GRpcService
public class FileGrpcService extends FileGrpcServiceGrpc.FileGrpcServiceImplBase {

    @Override
    public void insertFile( File request, StreamObserver<BooleanReply> responseObserver ) {
        // final GreeterOuterClass.HelloReply.Builder replyBuilder = GreeterOuterClass.HelloReply.newBuilder().setMessage("Hello " + request.getName());
        responseObserver.onNext(BooleanReply.getDefaultInstance());
        responseObserver.onCompleted();
    }

    @Override
    public void updateFile( File request, StreamObserver<BooleanReply> responseObserver ) {
        responseObserver.onNext(BooleanReply.getDefaultInstance());
        responseObserver.onCompleted();
    }

    @Override
    public void searchFile( ConditionsRequest request, StreamObserver<File> responseObserver ) {
        File adfasfd = File.newBuilder().setType(1).setAddress("adfasfd").build();
        responseObserver.onNext(adfasfd);
        responseObserver.onCompleted();
    }

    @Override
    public void deleteFile( ConditionsRequest request, StreamObserver<BooleanReply> responseObserver ) {
        responseObserver.onNext(BooleanReply.getDefaultInstance());
        responseObserver.onCompleted();
    }
}
