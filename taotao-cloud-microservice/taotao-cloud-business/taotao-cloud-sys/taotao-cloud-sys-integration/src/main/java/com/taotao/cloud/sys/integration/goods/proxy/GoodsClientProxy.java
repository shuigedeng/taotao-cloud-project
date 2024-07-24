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

package com.taotao.cloud.sys.integration.goods.proxy;

import com.taotao.cloud.goods.api.dubbo.GoodsRpcService;
import com.taotao.cloud.goods.api.dubbo.request.GoodsQueryRpcRequest;
import com.taotao.cloud.goods.api.dubbo.response.GoodsQueryRpcResponse;
import com.taotao.cloud.goods.api.feign.GoodsApi;
import com.taotao.cloud.goods.api.grpc.CountStoreGoodsNumGrpcResponse;
import com.taotao.cloud.sys.integration.goods.adapter.GoodsClientAdapter;
import com.taotao.cloud.sys.integration.goods.grpc.GoodsGrpcClient;
import com.taotao.cloud.sys.integration.goods.vo.GoodsVO;
import jakarta.annotation.Resource;
import org.openjdk.nashorn.internal.ir.annotations.Reference;
import org.springframework.stereotype.Component;

@Component
public class GoodsClientProxy {

	@Resource
	private GoodsApi goodsApi;
	@Resource
	private GoodsClientAdapter userIntegrationAdapter;
	@Resource
	private GoodsGrpcClient goodsGrpcClient;
	@Reference
	private GoodsRpcService goodsRpcService;

	// 查询用户
	public GoodsVO getGoodsVO(Long storeId) {
		Long goodsNum = goodsApi.countStoreGoodsNum(storeId);
		GoodsQueryRpcResponse goods = goodsRpcService.queryGoodsByParams(new GoodsQueryRpcRequest());
		CountStoreGoodsNumGrpcResponse helloReply = goodsGrpcClient.countStoreGoodsNum("sfdasdf");

		return userIntegrationAdapter.convert(goodsNum, goods, helloReply);
	}
}
