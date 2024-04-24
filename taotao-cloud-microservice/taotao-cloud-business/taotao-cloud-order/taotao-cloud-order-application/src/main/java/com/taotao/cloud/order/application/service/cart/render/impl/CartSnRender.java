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

package com.taotao.cloud.order.application.service.cart.render.impl;

import com.taotao.cloud.order.application.service.cart.render.ICartRenderStep;
import org.springframework.stereotype.Service;

/**
 * sn 生成
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-28 08:50:35
 */
@Service
public class CartSnRender implements ICartRenderStep {

    @Override
    public RenderStepEnum step() {
        return RenderStepEnum.CART_SN;
    }

    @Override
    public void render(TradeDTO tradeDTO) {
        // 生成各个sn
        tradeDTO.setSn(SnowFlake.createStr("T"));
        tradeDTO.getCartList().forEach(item -> {
            // 写入备注
            if (tradeDTO.getStoreRemark() != null) {
                for (StoreRemarkDTO remark : tradeDTO.getStoreRemark()) {
                    if (item.getStoreId().equals(remark.getStoreId())) {
                        item.setRemark(remark.getRemark());
                    }
                }
            }
            item.setSn(SnowFlake.createStr("O"));
        });
    }
}
