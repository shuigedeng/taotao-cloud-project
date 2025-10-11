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

package com.taotao.cloud.order.api.feign.fallback;

import com.taotao.boot.common.model.request.PageQuery;
import com.taotao.boot.common.model.result.PageResult;
import com.taotao.cloud.order.api.feign.StoreFlowApi;
import com.taotao.cloud.order.api.feign.response.TradeApiResponse;
import com.taotao.cloud.order.api.feign.response.StoreFlowApiResponse;
import org.springframework.cloud.openfeign.FallbackFactory;

/**
 * RemoteLogFallbackImpl
 *
 * @author shuigedeng
 * @since 2020/4/29 21:43
 */
public class FeignStoreFlowFallback implements FallbackFactory<StoreFlowApi> {

	@Override
	public StoreFlowApi create(Throwable throwable) {
		return new StoreFlowApi() {


			@Override
			public TradeApiResponse getBySn(String sn) {
				return null;
			}

			@Override
			public PageResult<StoreFlowApiResponse> getStoreFlow(String id, String flowType,
				PageQuery PageQuery) {
				return null;
			}

			@Override
			public PageResult<StoreFlowApiResponse> getDistributionFlow(String id,
				PageQuery PageQuery) {
				return null;
			}
		};
	}
}
