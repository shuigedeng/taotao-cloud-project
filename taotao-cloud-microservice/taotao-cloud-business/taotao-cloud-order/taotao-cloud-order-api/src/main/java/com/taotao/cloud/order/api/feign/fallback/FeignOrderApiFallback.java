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

import com.taotao.cloud.order.api.feign.IFeignOrderApi;
import com.taotao.cloud.order.api.feign.request.FeignOrderSaveRequest;
import com.taotao.cloud.order.api.feign.response.FeignOrderDetailResponse;
import com.taotao.cloud.order.api.feign.response.FeignOrderResponse;
import java.util.List;
import org.springframework.cloud.openfeign.FallbackFactory;

/**
 * RemoteLogFallbackImpl
 *
 * @author shuigedeng
 * @since 2020/4/29 21:43
 */
public class FeignOrderApiFallback implements FallbackFactory<IFeignOrderApi> {

	@Override
	public IFeignOrderApi create(Throwable throwable) {
		return new IFeignOrderApi() {
			@Override
			public FeignOrderResponse findOrderInfoByCode(String code) {
				return null;
			}

			@Override
			public FeignOrderResponse saveOrder(FeignOrderSaveRequest orderDTO) {
				return null;
			}

			@Override
			public FeignOrderDetailResponse queryDetail(String sn) {
				return null;
			}

			@Override
			public Boolean payOrder(String sn, String paymentMethod, String receivableNo) {
				return null;
			}

			@Override
			public FeignOrderResponse getBySn(String sn) {
				return null;
			}

			@Override
			public List<FeignOrderResponse> getByTradeSn(String sn) {
				return null;
			}
		};
	}
}
