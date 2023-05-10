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

package com.taotao.cloud.promotion.api.feign.fallback;

import com.taotao.cloud.promotion.api.feign.IFeignPromotionGoodsApi;
import com.taotao.cloud.promotion.api.model.page.PromotionGoodsPageQuery;
import com.taotao.cloud.promotion.api.model.vo.PromotionGoodsVO;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.cloud.openfeign.FallbackFactory;

/**
 * RemoteLogFallbackImpl
 *
 * @author shuigedeng
 * @since 2020/4/29 21:43
 */
public class FeignPromotionGoodsApiFallback implements FallbackFactory<IFeignPromotionGoodsApi> {

    @Override
    public IFeignPromotionGoodsApi create(Throwable throwable) {
        return new IFeignPromotionGoodsApi() {

            @Override
            public PromotionGoodsVO getPromotionsGoods(PromotionGoodsPageQuery searchParams) {
                return null;
            }

            @Override
            public Boolean updateBatchById(List<PromotionGoodsVO> promotionGoods) {
                return null;
            }

            @Override
            public BigDecimal getValidPromotionsGoodsPrice(Long skuId, List<String> singletonList) {
                return null;
            }

			@Override
			public List<PromotionGoodsVO> findSkuValidPromotions(List<String> categories, List<String> skuIds) {
				return null;
			}
		};
    }
}
