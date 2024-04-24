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

package com.taotao.cloud.auth.facade.grpc;

import com.taotao.cloud.auth.api.grpc.BooleanReply;
import com.taotao.cloud.auth.api.grpc.Oauth2Request;
import com.taotao.cloud.auth.api.grpc.Oauth2Response;
import com.taotao.cloud.auth.api.grpc.Oauth2ServiceGrpc.Oauth2ServiceImplBase;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@GRpcService
public class Oauth2ServiceGrpc extends Oauth2ServiceImplBase {

	@Override
	public void loginByQQ(Oauth2Request request, StreamObserver<BooleanReply> responseObserver) {
		super.loginByQQ(request, responseObserver);
	}

	@Override
	public void loginByWechat(Oauth2Request request,
		StreamObserver<Oauth2Response> responseObserver) {
		super.loginByWechat(request, responseObserver);
	}
}
