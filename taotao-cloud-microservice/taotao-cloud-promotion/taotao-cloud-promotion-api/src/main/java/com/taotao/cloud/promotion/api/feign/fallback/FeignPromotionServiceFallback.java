/*
 * Copyright 2002-2021 the original author or authors.
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
package com.taotao.cloud.promotion.api.feign.fallback;

import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.promotion.api.feign.IFeignCouponService;
import com.taotao.cloud.promotion.api.feign.IFeignPromotionService;
import org.springframework.cloud.openfeign.FallbackFactory;

import java.util.Map;

/**
 * RemoteLogFallbackImpl
 *
 * @author shuigedeng
 * @since 2020/4/29 21:43
 */
public class FeignPromotionServiceFallback implements FallbackFactory<IFeignPromotionService> {
	@Override
	public IFeignPromotionService create(Throwable throwable) {
		return new IFeignPromotionService() {

			@Override
			public Result<Map<String, Object>> getGoodsSkuPromotionMap(Long storeId, Long goodsIndexId) {
				return null;
			}
		};
	}
}
