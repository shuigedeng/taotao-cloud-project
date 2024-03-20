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

package com.taotao.cloud.order.biz.service.business.cart.render.impl;

import com.taotao.cloud.common.enums.PromotionTypeEnum;
import com.taotao.cloud.common.utils.number.CurrencyUtils;
import com.taotao.cloud.order.api.enums.cart.RenderStepEnum;
import com.taotao.cloud.order.sys.model.dto.cart.TradeDTO;
import com.taotao.cloud.order.sys.model.dto.order.PriceDetailDTO;
import com.taotao.cloud.order.sys.model.vo.cart.CartSkuVO;
import com.taotao.cloud.order.sys.model.vo.cart.CartVO;
import com.taotao.cloud.order.sys.model.vo.cart.FullDiscountVO;
import com.taotao.cloud.order.biz.service.business.cart.render.ICartRenderStep;
import com.taotao.cloud.order.biz.service.business.cart.render.util.PromotionPriceUtil;
import com.taotao.cloud.promotion.api.enums.PromotionsScopeTypeEnum;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * FullDiscountRender
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-28 08:50:49
 */
@AllArgsConstructor
@Service
public class FullDiscountRender implements ICartRenderStep {

    private final FullDiscountService fullDiscountService;

    private final PromotionPriceUtil promotionPriceUtil;

    private final GoodsSkuService goodsSkuService;

    @Override
    public RenderStepEnum step() {
        return RenderStepEnum.FULL_DISCOUNT;
    }

    @Override
    public void render(TradeDTO tradeDTO) {

        // 店铺集合
        List<CartVO> cartList = tradeDTO.getCartList();

        // 店铺id集合
        List<String> storeIds =
                tradeDTO.getCartList().stream().map(CartVO::getStoreId).toList();
        // 获取当前店铺进行到满减活动
        List<FullDiscountVO> fullDiscounts = fullDiscountService.currentPromotion(storeIds);
        if (fullDiscounts == null || fullDiscounts.isEmpty()) {
            return;
        }

        // 循环满减信息
        for (FullDiscountVO fullDiscount : fullDiscounts) {
            // 判定参与活动的商品 全品类参与或者部分商品参与，则进行云散
            // 循环店铺购物车
            for (CartVO cart : cartList) {
                // 如果购物车中的店铺id与活动店铺id相等，则进行促销计算
                if (fullDiscount.getStoreId().equals(cart.getStoreId())) {

                    // 如果有赠品，则将赠品信息写入
                    if (Boolean.TRUE.equals(fullDiscount.getIsGift())) {
                        GoodsSku goodsSku = goodsSkuService.getGoodsSkuByIdFromCache(fullDiscount.getGiftId());
                        fullDiscount.setGiftSku(goodsSku);
                    }

                    // 写入满减活动
                    cart.setFullDiscount(fullDiscount);
                    Map<String, BigDecimal> skuPriceDetail;
                    // 参与活动的sku判定
                    skuPriceDetail = initFullDiscountGoods(fullDiscount, cart.getCheckedSkuList());
                    if (!skuPriceDetail.isEmpty()) {
                        // 记录参与满减活动的sku
                        cart.setFullDiscountSkuIds(new ArrayList<>(skuPriceDetail.keySet()));

                        BigDecimal countPrice = countPrice(skuPriceDetail);

                        if (isFull(countPrice, cart)) {
                            // 如果减现金
                            if (Boolean.TRUE.equals(fullDiscount.getIsFullMinus())) {
                                promotionPriceUtil.recountPrice(
                                        tradeDTO,
                                        skuPriceDetail,
                                        fullDiscount.getFullMinus(),
                                        PromotionTypeEnum.FULL_DISCOUNT);
                            }
                            // 打折
                            else if (Boolean.TRUE.equals(fullDiscount.getIsFullRate())) {
                                this.renderFullRate(
                                        cart, skuPriceDetail, CurrencyUtils.div(fullDiscount.getFullRate(), 10));
                            }
                            // 渲染满优惠
                            renderFullMinus(cart);
                        }
                    }
                }
            }
        }
    }

    /**
     * 渲染满折
     *
     * @param cart cart
     * @param skuPriceDetail skuPriceDetail
     * @param rate rate
     */
    private void renderFullRate(CartVO cart, Map<String, BigDecimal> skuPriceDetail, BigDecimal rate) {

        List<CartSkuVO> cartSkuVOS = cart.getCheckedSkuList().stream()
                .filter(cartSkuVO -> {
                    return skuPriceDetail.containsKey(cartSkuVO.getGoodsSku().getId());
                })
                .toList();

        // 循环计算扣减金额
        cartSkuVOS.forEach(cartSkuVO -> {
            PriceDetailDTO priceDetailDTO = cartSkuVO.getPriceDetailDTO();

            // 优惠金额=旧的优惠金额+商品金额*商品折扣比例
            priceDetailDTO.setDiscountPrice(CurrencyUtils.add(
                    priceDetailDTO.getDiscountPrice(),
                    CurrencyUtils.mul(priceDetailDTO.getGoodsPrice(), CurrencyUtils.sub(1, rate))));
        });
    }

    /**
     * 获取参与满优惠的商品id
     *
     * @param fullDiscount 满优惠信息
     * @param cartSkuVOS 购物车商品sku信息
     * @return 参与满优惠的商品id
     */
    public Map<String, BigDecimal> initFullDiscountGoods(FullDiscountVO fullDiscount, List<CartSkuVO> cartSkuVOS) {
        Map<String, BigDecimal> skuPriceDetail = new HashMap<>(16);

        // 全品类参与
        if (PromotionsScopeTypeEnum.ALL.name().equals(fullDiscount.getScopeType())) {
            for (CartSkuVO cartSkuVO : cartSkuVOS) {
                skuPriceDetail.put(
                        cartSkuVO.getGoodsSku().getId(),
                        cartSkuVO.getPriceDetailDTO().getGoodsPrice());
            }
        } else {
            List<String> collect = fullDiscount.getPromotionGoodsList().stream()
                    .map(PromotionGoods::getSkuId)
                    .toList();
            // sku 集合判定
            for (CartSkuVO cartSkuVO : cartSkuVOS) {
                // 如果参加满减，并且购物车选中状态 ，则记录商品sku
                if (Boolean.TRUE.equals(cartSkuVO.getChecked())
                        && collect.contains(cartSkuVO.getGoodsSku().getId())) {
                    skuPriceDetail.put(
                            cartSkuVO.getGoodsSku().getId(),
                            cartSkuVO.getPriceDetailDTO().getGoodsPrice());
                }
            }
        }
        return skuPriceDetail;
    }

    /**
     * 渲染满减优惠
     *
     * @param cartVO 购物车满优惠渲染
     */
    private void renderFullMinus(CartVO cartVO) {
        // 获取参与活动的商品总价
        FullDiscountVO fullDiscount = cartVO.getFullDiscount();

        if (Boolean.TRUE.equals(fullDiscount.getIsCoupon())) {
            cartVO.getGiftCouponList().add(fullDiscount.getCouponId());
        }
        if (Boolean.TRUE.equals(fullDiscount.getIsGift())) {
            cartVO.setGiftList(Arrays.asList(fullDiscount.getGiftId().split(",")));
        }
        if (Boolean.TRUE.equals(fullDiscount.getIsPoint())) {
            cartVO.setGiftPoint(fullDiscount.getPoint());
        }
        // 如果满足，判定是否免邮，免邮的话需要渲染一边sku
        if (Boolean.TRUE.equals(fullDiscount.getIsFreeFreight())) {
            for (CartSkuVO skuVO : cartVO.getCheckedSkuList()) {
                skuVO.setIsFreeFreight(true);
            }
        }
    }

    /**
     * 是否满足满优惠
     *
     * @param cart 购物车展示信息
     * @return 是否满足满优惠
     */
    private boolean isFull(BigDecimal price, CartVO cart) {
        if (cart.getFullDiscount().getFullMoney() <= price) {
            cart.setPromotionNotice("正在参与满优惠活动["
                    + cart.getFullDiscount().getPromotionName()
                    + "]"
                    + cart.getFullDiscount().notice());
            return true;
        } else {
            cart.setPromotionNotice("还差"
                    + CurrencyUtils.sub(cart.getFullDiscount().getFullMoney(), price)
                    + " 即可参与活动（"
                    + cart.getFullDiscount().getPromotionName()
                    + "）"
                    + cart.getFullDiscount().notice());
            return false;
        }
    }

    /**
     * 统计参与满减商品价格
     *
     * @param skuPriceMap sku价格
     * @return 总价
     */
    private BigDecimal countPrice(Map<String, BigDecimal> skuPriceMap) {
        BigDecimal count = 0d;

        for (BigDecimal price : skuPriceMap.values()) {
            count = CurrencyUtils.add(count, price);
        }

        return count;
    }
}
