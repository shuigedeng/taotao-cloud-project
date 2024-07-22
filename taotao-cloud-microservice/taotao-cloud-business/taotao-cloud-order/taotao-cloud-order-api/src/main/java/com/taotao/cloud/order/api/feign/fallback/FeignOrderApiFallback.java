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

import com.taotao.cloud.order.api.feign.OrderApi;
import com.taotao.cloud.order.api.feign.request.OrderSaveApiRequest;
import com.taotao.cloud.order.api.feign.response.OrderDetailApiResponse;
import com.taotao.cloud.order.api.feign.response.OrderApiResponse;
import java.util.List;
import org.springframework.cloud.openfeign.FallbackFactory;

/**
 * RemoteLogFallbackImpl
 *
 * @author shuigedeng
 * @since 2020/4/29 21:43
 */
public class FeignOrderApiFallback implements FallbackFactory<OrderApi> {

	@Override
	public OrderApi create(Throwable throwable) {
		return new OrderApi() {
			@Override
			public OrderApiResponse findOrderInfoByCode(String code) {
				return null;
			}

			@Override
			public OrderApiResponse saveOrder(OrderSaveApiRequest orderDTO) {
				return null;
			}

			@Override
			public OrderDetailApiResponse queryDetail(String sn) {
				return null;
			}

			@Override
			public Boolean payOrder(String sn, String paymentMethod, String receivableNo) {
				return null;
			}

			@Override
			public OrderApiResponse getBySn(String sn) {
				return null;
			}

			@Override
			public List<OrderApiResponse> getByTradeSn(String sn) {
				return null;
			}
		};
	}
}
