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
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

/**
 * 购物车渲染，将购物车中的各个商品，拆分到每个商家，形成购物车VO
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-28 08:50:24
 */
@Service
public class CartPriceRender implements ICartRenderStep {

    @Override
    public RenderStepEnum step() {
        return RenderStepEnum.CART_PRICE;
    }

    @Override
    public void render(TradeDTO tradeDTO) {
        // 价格过滤 在购物车商品失效时，需要对价格进行初始化操作
        initPriceDTO(tradeDTO);

        // 构造cartVO
        buildCartPrice(tradeDTO);
        buildTradePrice(tradeDTO);
    }

    /**
     * 特殊情况下对购物车金额进行护理
     *
     * @param tradeDTO tradeDTO
     */
    private void initPriceDTO(TradeDTO tradeDTO) {
        tradeDTO.getCartList().forEach(cartVO -> cartVO.setPriceDetailDTO(new PriceDetailDTO()));
        tradeDTO.setPriceDetailDTO(new PriceDetailDTO());
    }

    /**
     * 购物车价格
     *
     * @param tradeDTO 购物车展示信息
     * @since 2022-04-28 08:51:55
     */
    void buildCartPrice(TradeDTO tradeDTO) {
        // 购物车列表
        List<CartVO> cartVos = tradeDTO.getCartList();

		cartVos.forEach(cartVo -> {
			cartVo.getPriceDetailDTO()
                    .accumulationPriceDTO(cartVo.getCheckedSkuList().stream()
                            .filter(CartSkuVO::getChecked)
                            .map(CartSkuVO::getPriceDetailDTO)
                            .toList());
            List<Integer> skuNum = cartVO.getSkuList().stream()
                    .filter(CartSkuVO::getChecked)
                    .map(CartSkuVO::getNum)
                    .toList();
            for (Integer num : skuNum) {
                cartVO.addGoodsNum(num);
            }
        });
    }

    /**
     * 初始化购物车
     *
     * @param tradeDTO 购物车展示信息
     * @since 2022-04-28 08:51:52
     */
    void buildTradePrice(TradeDTO tradeDTO) {
        tradeDTO.getPriceDetailDTO()
                .accumulationPriceDTO(tradeDTO.getCartList().stream()
                        .map(CartVO::getPriceDetailDTO)
                        .toList());
    }
}
