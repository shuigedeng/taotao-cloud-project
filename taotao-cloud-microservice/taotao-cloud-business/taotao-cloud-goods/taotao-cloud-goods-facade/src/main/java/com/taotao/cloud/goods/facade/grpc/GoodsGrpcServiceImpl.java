package com.taotao.cloud.goods.facade.grpc;

import com.taotao.cloud.goods.api.grpc.CategoryGrpcServiceGrpc.CategoryGrpcServiceImplBase;
import com.taotao.cloud.goods.api.grpc.CountStoreGoodsNumRequest;
import com.taotao.cloud.goods.api.grpc.CountStoreGoodsNumResponse;
import com.taotao.cloud.goods.api.grpc.GoodsGrpcServiceGrpc.GoodsGrpcServiceImplBase;
import com.taotao.cloud.goods.api.grpc.FirstCategoryRequest;
import com.taotao.cloud.goods.api.grpc.FirstCategoryResponse;
import io.grpc.stub.StreamObserver;

public class GoodsGrpcServiceImpl extends GoodsGrpcServiceImplBase {

	@Override
	public void countStoreGoodsNum(CountStoreGoodsNumRequest request,
		StreamObserver<CountStoreGoodsNumResponse> responseObserver) {
		super.countStoreGoodsNum(request, responseObserver);
	}
}
