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
import de.danielbechler.diff.category.CategoryService;
import java.math.BigDecimal;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 佣金计算
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-28 08:50:42
 */
@AllArgsConstructor
@Service
public class CommissionRender implements ICartRenderStep {

    /** 商品分类 */
    private final CategoryService categoryService;
    /** 积分商品 */
    private final PointsGoodsService pointsGoodsService;
    /** 砍价商品 */
    private final KanjiaActivityGoodsService kanjiaActivityGoodsService;

    @Override
    public RenderStepEnum step() {
        return RenderStepEnum.PLATFORM_COMMISSION;
    }

    @Override
    public void render(TradeDTO tradeDTO) {
        buildCartPrice(tradeDTO);
    }

    /**
     * 购物车佣金计算
     *
     * @param tradeDTO 购物车展示信息
     */
    void buildCartPrice(TradeDTO tradeDTO) {
        // 购物车列表
        List<CartVO> cartVOS = tradeDTO.getCartList();

        // 计算购物车价格
        for (CartVO cart : cartVOS) {
            // 累加价格
            for (CartSkuVO cartSkuVO : cart.getCheckedSkuList()) {

                PriceDetailDTO priceDetailDTO = cartSkuVO.getPriceDetailDTO();
                // 平台佣金根据分类计算
                String categoryId = cartSkuVO
                        .getGoodsSku()
                        .getCategoryPath()
                        .substring(cartSkuVO.getGoodsSku().getCategoryPath().lastIndexOf(",") + 1);
                if (StrUtil.isNotEmpty(categoryId)) {
                    BigDecimal commissionRate =
                            categoryService.getById(categoryId).getCommissionRate();
                    priceDetailDTO.setPlatFormCommissionPoint(commissionRate);
                }

                // 如果积分订单 积分订单，单独操作订单结算金额和商家结算字段
                if (tradeDTO.getCartTypeEnum().equals(CartTypeEnum.POINTS)) {
                    PointsGoodsVO pointsGoodsVO = pointsGoodsService.getPointsGoodsDetailBySkuId(
                            cartSkuVO.getGoodsSku().getId());
                    priceDetailDTO.setSettlementPrice(pointsGoodsVO.getSettlementPrice());
                }
                // 如果砍价订单 计算金额，单独操作订单结算金额和商家结算字段
                else if (tradeDTO.getCartTypeEnum().equals(CartTypeEnum.KANJIA)) {
                    KanjiaActivityGoods kanjiaActivityGoods = kanjiaActivityGoodsService.getKanjiaGoodsBySkuId(
                            cartSkuVO.getGoodsSku().getId());
                    priceDetailDTO.setSettlementPrice(kanjiaActivityGoods.getSettlementPrice());
                }
            }
        }
    }
}
