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

package com.taotao.cloud.order.application.service.cart.render.util;

import com.taotao.boot.common.enums.PromotionTypeEnum;
import com.taotao.boot.common.utils.log.LogUtils;
import com.taotao.boot.common.utils.number.CurrencyUtils;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

/**
 * 促销价格计算业务层实现
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-28 08:50:15
 */
@Service
public class PromotionPriceUtil {

    /**
     * 重新计算购物车价格
     *
     * @param tradeDTO 交易DTO
     * @param skuPromotionDetail 参与活动的商品，以及商品总金额
     * @param discountPrice 需要分发的优惠金额
     * @param promotionTypeEnum 促销类型
     */
    public void recountPrice(
            TradeDTO tradeDTO,
            Map<String, BigDecimal> skuPromotionDetail,
            BigDecimal discountPrice,
            PromotionTypeEnum promotionTypeEnum) {

        // sku 促销信息非空判定
        if (skuPromotionDetail == null || skuPromotionDetail.size() == 0) {
            return;
        }

        // 计算总金额
        BigDecimal totalPrice = BigDecimal.ZERO;
        for (BigDecimal value : skuPromotionDetail.values()) {
            totalPrice = CurrencyUtils.add(totalPrice, value);
        }

        // 极端情况，如果扣减金额小于需要支付的金额，则扣减金额=支付金额，不能成为负数
        if (discountPrice > totalPrice) {
            discountPrice = totalPrice;
            for (String skuId : skuPromotionDetail.keySet()) {
                // 获取对应商品进行计算
                for (CartSkuVO cartSkuVO : tradeDTO.getSkuList()) {
                    if (cartSkuVO.getGoodsSku().getId().equals(skuId)) {
                        // 优惠券金额，则计入优惠券 ，其他则计入总的discount price
                        if (promotionTypeEnum == PromotionTypeEnum.COUPON) {
                            cartSkuVO
                                    .getPriceDetailDTO()
                                    .setCouponPrice(
                                            cartSkuVO.getPriceDetailDTO().getGoodsPrice());
                        } else {
                            cartSkuVO
                                    .getPriceDetailDTO()
                                    .setDiscountPrice(
                                            cartSkuVO.getPriceDetailDTO().getGoodsPrice());
                        }
                    }
                }
            }
        }

        // 获取购物车信息
        List<CartSkuVO> skuVOList = tradeDTO.getSkuList();

        // 获取map分配sku的总数，如果是最后一个商品分配金额，则将金额从百分比改为总金额扣减，避免出现小数除不尽
        Integer count = skuPromotionDetail.size();

        // 已优惠金额
        BigDecimal deducted = BigDecimal.ZERO;
        for (String skuId : skuPromotionDetail.keySet()) {
            // 获取对应商品进行计算
            for (CartSkuVO cartSkuVO : skuVOList) {
                if (cartSkuVO.getGoodsSku().getId().equals(skuId)) {
                    count--;

                    // sku 优惠金额
                    BigDecimal skuDiscountPrice = BigDecimal.ZERO;
                    // 非最后一个商品，则按照比例计算
                    if (count > 0) {
                        // 商品金额占比
                        BigDecimal point =
                                CurrencyUtils.div(cartSkuVO.getPriceDetailDTO().getGoodsPrice(), totalPrice, 4);
                        // 商品优惠金额
                        skuDiscountPrice = CurrencyUtils.mul(discountPrice, point);
                        // 累加已优惠金额
                        deducted = CurrencyUtils.add(deducted, skuDiscountPrice);
                    }
                    // 如果是最后一个商品 则减去之前优惠的金额来进行计算
                    else {
                        skuDiscountPrice = CurrencyUtils.sub(discountPrice, deducted);
                    }
                    // 优惠券金额，则计入优惠券 ，其他则计入总的discount price
                    if (promotionTypeEnum == PromotionTypeEnum.COUPON) {

                        cartSkuVO
                                .getPriceDetailDTO()
                                .setCouponPrice(CurrencyUtils.add(
                                        cartSkuVO.getPriceDetailDTO().getCouponPrice(), skuDiscountPrice));
                    } else if (promotionTypeEnum == PromotionTypeEnum.PLATFORM_COUPON) {

                        cartSkuVO
                                .getPriceDetailDTO()
                                .setSiteCouponPrice(CurrencyUtils.add(
                                        cartSkuVO.getPriceDetailDTO().getCouponPrice(), skuDiscountPrice));

                        cartSkuVO
                                .getPriceDetailDTO()
                                .setCouponPrice(CurrencyUtils.add(
                                        cartSkuVO.getPriceDetailDTO().getCouponPrice(),
                                        cartSkuVO.getPriceDetailDTO().getSiteCouponPrice()));
                    } else {
                        cartSkuVO
                                .getPriceDetailDTO()
                                .setDiscountPrice(CurrencyUtils.add(
                                        cartSkuVO.getPriceDetailDTO().getDiscountPrice(), skuDiscountPrice));
                    }
                }
            }
        }
    }

    /**
     * 检查活动有效时间
     *
     * @param startTime 活动开始时间
     * @param endTime 活动结束时间
     * @param promotionType 活动类型
     * @param promotionId 活动ID
     * @return 是否有效
     */
    private boolean checkPromotionValidTime(Date startTime, Date endTime, String promotionType, String promotionId) {
        long now = System.currentTimeMillis();
        if (startTime.getTime() > now) {
            LogUtils.error("商品ID为{}的{}活动开始时间小于当时时间，活动未开始！", promotionId, promotionType);
            return false;
        }
        if (endTime.getTime() < now) {
            LogUtils.error("活动ID为{}的{}活动结束时间大于当时时间，活动已结束！", promotionId, promotionType);
            return false;
        }
        return true;
    }
}
