package com.taotao.cloud.sys.integration.goods.grpc;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.taotao.boot.common.utils.log.LogUtils;
import com.taotao.cloud.goods.api.grpc.CountStoreGoodsNumGrpcRequest;
import com.taotao.cloud.goods.api.grpc.CountStoreGoodsNumGrpcResponse;
import com.taotao.cloud.goods.api.grpc.GoodsGrpcServiceGrpc;
import com.taotao.cloud.goods.api.grpc.GoodsGrpcServiceGrpc.GoodsGrpcServiceFutureStub;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import java.util.concurrent.ExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Component;

@Component
public class GoodsGrpcClient {

	@Autowired
	private DiscoveryClient discoveryClient;

	public CountStoreGoodsNumGrpcResponse countStoreGoodsNum(String name) {
		try {
			final ServiceInstance instanceInfo = discoveryClient.getInstances("my-service-name")
				.get(0);
			final ManagedChannel channel = ManagedChannelBuilder.forAddress(
					instanceInfo.getUri().toString(), instanceInfo.getPort())
				.usePlaintext()
				.build();
			GoodsGrpcServiceFutureStub stub = GoodsGrpcServiceGrpc.newFutureStub(
				channel);
			CountStoreGoodsNumGrpcRequest helloRequest = CountStoreGoodsNumGrpcRequest.parseFrom(
				ByteString.copyFromUtf8(name));
			ListenableFuture<CountStoreGoodsNumGrpcResponse> helloReplyListenableFuture = stub.countStoreGoodsNum(
				helloRequest);
			return helloReplyListenableFuture.get();
		}
		catch (InvalidProtocolBufferException | InterruptedException | ExecutionException e) {
			LogUtils.error("sayHello---------------");
			throw new RuntimeException(e);
		}
	}
}
