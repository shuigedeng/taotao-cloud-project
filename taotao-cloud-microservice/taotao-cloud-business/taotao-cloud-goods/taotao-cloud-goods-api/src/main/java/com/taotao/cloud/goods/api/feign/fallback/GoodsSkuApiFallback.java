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

package com.taotao.cloud.goods.api.feign.fallback;

import com.taotao.cloud.goods.api.feign.GoodsSkuApi;
import com.taotao.cloud.goods.api.feign.request.GoodsSkuSpecGalleryApiRequest;
import com.taotao.cloud.goods.api.feign.response.GoodsSkuSpecGalleryApiResponse;
import java.util.List;
import org.springframework.cloud.openfeign.FallbackFactory;

/**
 * FeignGoodsSkuServiceFallback
 *
 * @author shuigedeng
 * @since 2020/4/29 21:43
 */
public class GoodsSkuApiFallback implements FallbackFactory<GoodsSkuApi> {
    @Override
    public GoodsSkuApi create(Throwable throwable) {
        return new GoodsSkuApi() {
            @Override
            public Boolean updateGoodsStuck(List<GoodsSkuSpecGalleryApiRequest> goodsSkus) {
                return null;
            }

            @Override
            public Boolean updateBatchById(List<GoodsSkuSpecGalleryApiRequest> goodsSkus) {
                return null;
            }

            @Override
            public List<GoodsSkuSpecGalleryApiResponse> getGoodsSkuByIdFromCache(List<Long> skuIds) {
                return null;
            }

            @Override
            public GoodsSkuSpecGalleryApiResponse getGoodsSkuByIdFromCache(Long skuId) {
                return null;
            }

            @Override
            public Integer getStock(String skuId) {
                return null;
            }
        };
    }
}
