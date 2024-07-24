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

package com.taotao.cloud.sys.integration.sku.adapter;

import com.taotao.cloud.goods.api.dubbo.response.GoodsQueryRpcResponse;
import com.taotao.cloud.goods.api.feign.response.GoodsSkuSpecGalleryApiResponse;
import com.taotao.cloud.goods.api.grpc.GoodsSkuGrpcResponse;
import com.taotao.cloud.sys.integration.sku.vo.SkuVO;
import org.springframework.stereotype.Component;

@Component
public class SkuClientAdapter {

	public SkuVO convert(GoodsSkuSpecGalleryApiResponse user,
		GoodsQueryRpcResponse goodsQueryResponse, GoodsSkuGrpcResponse helloReply) {
		// 基础信息
		SkuVO skuVO = new SkuVO();
		// 联系方式
		return skuVO;
	}
}
