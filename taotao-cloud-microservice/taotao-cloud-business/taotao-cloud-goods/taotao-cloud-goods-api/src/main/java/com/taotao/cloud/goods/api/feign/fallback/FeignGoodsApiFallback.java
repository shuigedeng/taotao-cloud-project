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

import com.taotao.cloud.goods.api.feign.IFeignGoodsApi;
import org.springframework.cloud.openfeign.FallbackFactory;

/**
 * FeignGoodsServiceFallback
 *
 * @author shuigedeng
 * @since 2020/4/29 21:43
 */
public class FeignGoodsApiFallback implements FallbackFactory<IFeignGoodsApi> {
    @Override
    public IFeignGoodsApi create(Throwable throwable) {
        return new IFeignGoodsApi() {
            @Override
            public Boolean updateStoreDetail(String id) {
                return null;
            }

            @Override
            public Boolean underStoreGoods(String id) {
                return null;
            }

            @Override
            public Long countStoreGoodsNum(Long storeId) {
                return null;
            }
        };
    }
}
