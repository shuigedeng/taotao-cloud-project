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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

/**
 * 佣金计算
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-28 08:50:40
 */
@Service
public class CheckedFilterRender implements ICartRenderStep {

    @Override
    public RenderStepEnum step() {
        return RenderStepEnum.CHECKED_FILTER;
    }

    @Override
    public void render(TradeDTO tradeDTO) {
        // 将购物车到sku未选择信息过滤
        List<CartSkuVO> collect = tradeDTO.getSkuList().parallelStream()
                .filter(i -> Boolean.TRUE.equals(i.getChecked()))
                .toList();
        tradeDTO.setSkuList(collect);

        // 购物车信息过滤
        List<CartVO> cartVOList = new ArrayList<>();
        // 循环购物车信息
        for (CartVO cartVO : tradeDTO.getCartList()) {
            // 如果商品选中，则加入到对应购物车
            cartVO.setSkuList(cartVO.getCheckedSkuList());
            cartVOList.add(cartVO);
        }
        tradeDTO.setCartList(cartVOList);
    }
}
