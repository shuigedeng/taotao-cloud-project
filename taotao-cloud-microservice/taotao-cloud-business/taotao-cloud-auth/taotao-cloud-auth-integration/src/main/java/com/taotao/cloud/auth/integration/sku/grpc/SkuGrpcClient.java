package com.taotao.cloud.auth.integration.sku.grpc;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.taotao.boot.common.utils.log.LogUtils;
import com.taotao.cloud.goods.api.grpc.HelloReply;
import com.taotao.cloud.goods.api.grpc.HelloRequest;
import com.taotao.cloud.goods.api.grpc.MyServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import java.util.concurrent.ExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Component;

@Component
public class SkuGrpcClient {

	@Autowired
	private DiscoveryClient discoveryClient;

	public HelloReply sayHello(String name) {
		try {
			final ServiceInstance instanceInfo = discoveryClient.getInstances("my-service-name").get(0);
			final ManagedChannel channel = ManagedChannelBuilder.forAddress(
					instanceInfo.getUri().toString(), instanceInfo.getPort())
				.usePlaintext()
				.build();
			final MyServiceGrpc.MyServiceFutureStub stub = MyServiceGrpc.newFutureStub(channel);
			HelloRequest helloRequest =  HelloRequest.parseFrom(ByteString.copyFromUtf8(name));
			ListenableFuture<HelloReply> helloReplyListenableFuture = stub.sayHello(helloRequest);
			return helloReplyListenableFuture.get();
		}
		catch (InvalidProtocolBufferException | InterruptedException | ExecutionException e) {
			LogUtils.error("sayHello---------------");
			throw new RuntimeException(e);
		}
	}
}
