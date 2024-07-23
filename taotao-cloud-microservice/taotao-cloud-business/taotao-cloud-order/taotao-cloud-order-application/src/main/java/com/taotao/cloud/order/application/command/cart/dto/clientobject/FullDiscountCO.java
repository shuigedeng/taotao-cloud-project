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

import io.soabase.recordbuilder.core.RecordBuilder;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

/**
 * 满额活动VO
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-21 16:59:38
 */
@RecordBuilder
@Schema(description = "满额活动VO")
public record FullDiscountCO(
        @Schema(description = "促销关联的商品") List<PromotionGoodsVO> promotionGoodsList,
        @Schema(description = "赠品信息") GoodsSkuVO giftSku,
        @Schema(description = "参与商品，为-1则代表所有商品参加") Integer number,
        @Schema(description = "满额活动") FullDiscountBaseVO fullDiscount) {

    private static final long serialVersionUID = -2330552735874105354L;

    // public FullDiscountVO(FullDiscount fullDiscount) {
    // 	BeanUtils.copyProperties(fullDiscount, this);
    // }
    //
    // public String notice() {
    // 	StringBuilder stringBuffer = new StringBuilder();
    // 	if (Boolean.TRUE.equals(this.getIsFullMinus())) {
    // 		stringBuffer.append(" 减").append(this.getFullMinus()).append("元 ");
    // 	}
    // 	if (Boolean.TRUE.equals(this.getIsFullRate())) {
    // 		stringBuffer.append(" 打").append(this.getFullRate()).append("折 ");
    // 	}
    //
    // 	if (Boolean.TRUE.equals(this.getIsFreeFreight())) {
    // 		stringBuffer.append(" 免运费 ");
    // 	}
    //
    // 	if (Boolean.TRUE.equals(this.getIsPoint())) {
    // 		stringBuffer.append(" 赠").append(this.getPoint()).append("积分 ");
    // 	}
    // 	if (Boolean.TRUE.equals(this.getIsCoupon())) {
    // 		stringBuffer.append(" 赠").append("优惠券 ");
    // 	}
    // 	if (Boolean.TRUE.equals(this.getIsGift() && giftSku != null)) {
    // 		stringBuffer.append(" 赠品[").append(giftSku.getGoodsName()).append("]");
    // 	}
    //
    // 	return stringBuffer.toString();
    // }

    public static class PromotionGoodsVO {}

    public static class GoodsSkuVO {}

    public static class FullDiscountBaseVO {}
}
