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

import com.taotao.cloud.order.api.enums.cart.CartTypeEnum;
import io.soabase.recordbuilder.core.RecordBuilder;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/** 购物车中的产品 */
@RecordBuilder
@Schema(description = "购物车中的产品")
public record CartSkuCO(
        String sn,
        @Schema(description = "对应的sku信息") CartGoodsSkuVO goodsSku,
        @Schema(description = "分销商品信息") DistributionGoodsVO distributionGoods,
        @Schema(description = "购买数量") Integer num,
        @Schema(description = "购买时的成交价") BigDecimal purchasePrice,
        @Schema(description = "小记") BigDecimal subTotal,
        @Schema(description = "小记") BigDecimal utilPrice,
        @Schema(description = "是否选中，要去结算 0:未选中 1:已选中，默认") Boolean checked,
        @Schema(description = "是否免运费") Boolean isFreeFreight,
        @Schema(description = "是否失效 ") Boolean invalid,
        @Schema(description = "购物车商品错误消息") String errorMessage,
        @Schema(description = "是否可配送") Boolean isShip,
        @Schema(description = "拼团id 如果是拼团购买 此值为拼团活动id，" + "当pintuanId为空，则表示普通购买（或者拼团商品，单独购买）") String pintuanId,
        @Schema(description = "砍价ID") String kanjiaId,
        @Schema(description = "积分兑换ID") String pointsId,
        @Schema(description = "积分购买 积分数量") Long point,
        @Schema(description = "可参与的单品活动") List<PromotionGoodsVO> promotions,
        @Schema(description = "参与促销活动更新时间(一天更新一次) 例如时间为：2020-01-01  00：00：01") Date updatePromotionTime,

        /**
         * @see CartTypeEnum
         */
        @Schema(description = "购物车类型") CartTypeEnum cartType,
        CartBaseCO CartBase)
        implements Serializable {

    @Serial
    private static final long serialVersionUID = -894598033321906974L;

    // /**
    //  * 在构造器里初始化促销列表，规格列表
    //  */
    // public CartSkuVO(GoodsSku goodsSku) {
    // 	this.goodsSku = goodsSku;
    // 	this.checked = true;
    // 	this.invalid = false;
    // 	//默认时间为0，让系统为此商品更新缓存
    // 	this.updatePromotionTime = new Date(0);
    // 	this.errorMessage = "";
    // 	this.isShip = true;
    // 	this.purchasePrice = goodsSku.getIsPromotion() != null && goodsSku.getIsPromotion()
    // 		? goodsSku.getPromotionPrice() : goodsSku.getPrice();
    // 	this.isFreeFreight = false;
    // 	this.utilPrice = 0D;
    // 	this.setStoreId(goodsSku.getStoreId());
    // 	this.setStoreName(goodsSku.getStoreName());
    // }

    public static class CartGoodsSkuVO {}

    public static class DistributionGoodsVO {}

    public static class PromotionGoodsVO {}
}
