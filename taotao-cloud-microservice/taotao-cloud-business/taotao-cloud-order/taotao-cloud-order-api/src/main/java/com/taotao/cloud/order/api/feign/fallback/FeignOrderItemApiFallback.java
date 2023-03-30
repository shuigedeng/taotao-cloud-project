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

import com.taotao.cloud.order.api.enums.order.CommentStatusEnum;
import com.taotao.cloud.order.api.feign.IFeignOrderItemApi;
import com.taotao.cloud.order.api.model.dto.order_item.OrderItemSaveDTO;
import com.taotao.cloud.order.api.model.vo.order.OrderItemVO;
import org.springframework.cloud.openfeign.FallbackFactory;

/**
 * RemoteLogFallbackImpl
 *
 * @author shuigedeng
 * @since 2020/4/29 21:43
 */
public class FeignOrderItemApiFallback implements FallbackFactory<IFeignOrderItemApi> {
    @Override
    public IFeignOrderItemApi create(Throwable throwable) {
        return new IFeignOrderItemApi() {
            @Override
            public Boolean saveOrderItem(OrderItemSaveDTO orderItemSaveDTO) {
                return null;
            }

            @Override
            public Boolean updateById(OrderItemVO orderItem) {
                return null;
            }

            @Override
            public OrderItemVO getByOrderSnAndSkuId(String orderSn, String skuId) {
                return null;
            }

            @Override
            public OrderItemVO getBySn(String orderItemSn) {
                return null;
            }

            @Override
            public Boolean updateCommentStatus(String sn, CommentStatusEnum finished) {
                return null;
            }
        };
    }
}
