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

package com.taotao.cloud.auth.integration.sku.proxy;

import com.taotao.cloud.goods.api.dubbo.IDubboGoodsRpc;
import com.taotao.cloud.goods.api.dubbo.request.DubboGoodsQueryRequest;
import com.taotao.cloud.goods.api.dubbo.response.DubboGoodsQueryResponse;
import com.taotao.cloud.goods.api.feign.IFeignGoodsSkuApi;
import com.taotao.cloud.goods.api.feign.response.FeignGoodsSkuSpecGalleryResponse;
import com.taotao.cloud.goods.api.grpc.HelloReply;
import com.taotao.cloud.order.integration.sku.adapter.SkuClientAdapter;
import com.taotao.cloud.order.integration.sku.grpc.SkuGrpcClient;
import com.taotao.cloud.order.integration.sku.vo.SkuVO;
import jakarta.annotation.Resource;
import org.openjdk.nashorn.internal.ir.annotations.Reference;
import org.springframework.stereotype.Component;

@Component
public class SkuClientProxy {

	@Resource
	private IFeignGoodsSkuApi goodsSkuApi;
	@Resource
	private SkuClientAdapter skuClientAdapter;
	@Resource
	private SkuGrpcClient skuGrpcClient;
	@Reference
	private IDubboGoodsRpc goodsRpc;

	// 查询用户
	public SkuVO getUserInfo(Long skuId) {
		FeignGoodsSkuSpecGalleryResponse user = goodsSkuApi.getGoodsSkuByIdFromCache(skuId);
		DubboGoodsQueryResponse goodsQueryResponse = goodsRpc.queryGoodsByParams(
			new DubboGoodsQueryRequest());

		HelloReply helloReply = skuGrpcClient.sayHello("");

		return skuClientAdapter.convert(user, goodsQueryResponse, helloReply);
	}
}
