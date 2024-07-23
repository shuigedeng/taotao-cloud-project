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

package com.taotao.cloud.order.application.command.cart.dto.clientobject;

import com.taotao.cloud.order.api.enums.cart.DeliveryMethodEnum;
import io.soabase.recordbuilder.core.RecordBuilder;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.math.BigDecimal;
import java.util.List;

/**
 * 购物车展示VO
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-21 16:59:38
 */
@RecordBuilder
@Schema(description = "购物车展示VO")
public record CartCO(
        @Schema(description = "购物车中的产品列表") List<CartSkuCO> skuList,
        @Schema(description = "sn") String sn,
        @Schema(description = "购物车页展示时，店铺内的商品是否全选状态.1为店铺商品全选状态,0位非全选") Boolean checked,
        @Schema(description = "满优惠活动") FullDiscountCO fullDiscount,
        @Schema(description = "满优惠促销的商品") List<String> fullDiscountSkuIds,
        @Schema(description = "是否满优惠") Boolean isFull,
        @Schema(description = "使用的优惠券列表") List<CartMemberCouponVO> couponList,
        @Schema(description = "使用的优惠券列表") List<CartCouponVO> canReceiveCoupon,
        @Schema(description = "赠品列表") List<String> giftList,
        @Schema(description = "赠送优惠券列表") List<String> giftCouponList,
        @Schema(description = "赠送积分") Integer giftPoint,
        @Schema(description = "重量") BigDecimal weight,
        @Schema(description = "购物车商品数量") Integer goodsNum,
        @Schema(description = "购物车商品数量") String remark,

        /**
         * @see DeliveryMethodEnum
         */
        @Schema(description = "配送方式") String deliveryMethod,
        @Schema(description = "已参与的的促销活动提示，直接展示给客户") String promotionNotice,
        CartBaseCO cartBase) {

    @Serial
    private static final long serialVersionUID = -5651775413457562422L;

    // public CartVO(CartSkuVO cartSkuVO) {
    // 	this.setStoreId(cartSkuVO.getStoreId());
    // 	this.setStoreName(cartSkuVO.getStoreName());
    // 	this.setSkuList(new ArrayList<>());
    // 	this.setCouponList(new ArrayList<>());
    // 	this.setGiftList(new ArrayList<>());
    // 	this.setGiftCouponList(new ArrayList<>());
    // 	this.setChecked(false);
    // 	this.isFull = false;
    // 	this.weight = 0d;
    // 	this.giftPoint = 0;
    // 	this.remark = "";
    // }
    //
    // public void addGoodsNum(Integer goodsNum) {
    // 	if (this.goodsNum == null) {
    // 		this.goodsNum = goodsNum;
    // 	} else {
    // 		this.goodsNum += goodsNum;
    // 	}
    // }
    //
    //
    // /**
    //  * 过滤购物车中已选择的sku
    //  *
    //  * @return
    //  */
    // public List<CartSkuVO> getCheckedSkuList() {
    // 	if (skuList != null && !skuList.isEmpty()) {
    // 		return skuList.stream().filter(CartSkuVO::getChecked).toList();
    // 	}
    // 	return skuList;
    // }

    public static class CartMemberCouponVO {}

    public static class CartCouponVO {}
}
