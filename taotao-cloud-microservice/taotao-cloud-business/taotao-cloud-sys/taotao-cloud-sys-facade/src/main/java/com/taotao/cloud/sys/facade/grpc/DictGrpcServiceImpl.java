/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.sys.facade.grpc;

import com.taotao.cloud.sys.api.grpc.DictGrpcRequest;
import com.taotao.cloud.sys.api.grpc.DictGrpcResponse;
import com.taotao.cloud.sys.api.grpc.DictGrpcServiceGrpc.DictGrpcServiceImplBase;
import com.taotao.cloud.sys.api.grpc.DictTestGrpcRequest;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import org.lognet.springboot.grpc.GRpcService;

@Slf4j
@GRpcService
public class DictGrpcServiceImpl extends DictGrpcServiceImplBase {

	// @Autowired
	// private IDevicesFixService deviceService;

	@Override
	public void findByCode(DictGrpcRequest request,
		StreamObserver<DictGrpcResponse> responseObserver) {
		super.findByCode(request, responseObserver);

		log.info("findByCode:{}", request.toString());
		boolean replyTag = false;
		DictGrpcResponse reply = DictGrpcResponse.newBuilder().setId(1).build();
		responseObserver.onNext(reply);
		responseObserver.onCompleted();
	}

	@Override
	public void test(DictTestGrpcRequest request,
		StreamObserver<DictGrpcResponse> responseObserver) {
		super.test(request, responseObserver);
	}
}
