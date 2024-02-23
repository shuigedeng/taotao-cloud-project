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

package com.taotao.cloud.customer.integration.user.proxy;

import com.taotao.cloud.customer.integration.user.adapter.UserClientAdapter;
import com.taotao.cloud.customer.integration.user.grpc.UserGrpcClient;
import com.taotao.cloud.customer.integration.user.vo.UserBaseInfoVO;
import com.taotao.cloud.sys.api.dubbo.IDubboDictRpc;
import com.taotao.cloud.sys.api.dubbo.response.DubboDictResponse;
import com.taotao.cloud.sys.api.feign.IFeignUserApi;
import com.taotao.cloud.sys.api.grpc.HelloReply;
import com.taotao.cloud.sys.api.model.vo.user.UserQueryVO;
import jakarta.annotation.Resource;
import org.openjdk.nashorn.internal.ir.annotations.Reference;
import org.springframework.stereotype.Component;

@Component
public class UserClientProxy {

	@Resource
	private IFeignUserApi feignUserApi;
	@Resource
	private UserClientAdapter userIntegrationAdapter;
	@Resource
	private UserGrpcClient userGrpcClient;
	@Reference
	private IDubboDictRpc dubboDictRpc;

	// 查询用户
	public UserBaseInfoVO getUserInfo(String username) {
		UserQueryVO user = feignUserApi.findUserInfoByUsername(username);
		DubboDictResponse dict = dubboDictRpc.findByCode(22);

		HelloReply helloReply = userGrpcClient.sayHello(username);

		return userIntegrationAdapter.convert(user, dict);
	}
}
