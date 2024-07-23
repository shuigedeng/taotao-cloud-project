package com.taotao.cloud.goods.facade.grpc;

import com.taotao.cloud.goods.api.grpc.BooleanReply;
import com.taotao.cloud.goods.api.grpc.ConditionsRequest;
import com.taotao.cloud.goods.api.grpc.DeviceFix;
import com.taotao.cloud.goods.api.grpc.GoodsSkuGrpcServiceGrpc.GoodsSkuGrpcServiceImplBase;
import io.grpc.stub.StreamObserver;
import org.lognet.springboot.grpc.GRpcService;

@GRpcService
public class GoodsSkuGrpcServiceImpl extends GoodsSkuGrpcServiceImplBase {

	@Override
	public void updateGoodsStuck(DeviceFix request, StreamObserver<BooleanReply> responseObserver) {
		super.updateGoodsStuck(request, responseObserver);
	}

	@Override
	public void updateBatchById(DeviceFix request, StreamObserver<BooleanReply> responseObserver) {
		super.updateBatchById(request, responseObserver);
	}

	@Override
	public void getGoodsSkuByIdFromCache(ConditionsRequest request,
		StreamObserver<BooleanReply> responseObserver) {
		super.getGoodsSkuByIdFromCache(request, responseObserver);
	}
}
