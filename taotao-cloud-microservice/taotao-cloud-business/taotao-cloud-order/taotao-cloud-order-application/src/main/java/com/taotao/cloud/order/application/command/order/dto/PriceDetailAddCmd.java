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

package com.taotao.cloud.order.application.command.order.dto;

import io.soabase.recordbuilder.core.RecordBuilder;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/** 商城流水，细节到orderItem */
@RecordBuilder
@Schema(description = "商城流水，细节到orderItem")
public record PriceDetailAddCmd(
        @Schema(description = "订单原始总价格 用于订单价格修改金额计算使用") BigDecimal originalPrice,
        @Schema(description = "商品总金额（商品原价）") BigDecimal goodsPrice,
        @Schema(description = "配送费") BigDecimal freightPrice,

        // ============discount price============

        @Schema(description = "支付积分") Long payPoint,
        @Schema(description = "优惠金额") BigDecimal discountPrice,
        @Schema(description = "优惠券金额") BigDecimal couponPrice,

        // ===========end discount price =============

        // =========distribution==========

        @Schema(description = "单品分销返现支出") BigDecimal distributionCommission,
        @Schema(description = "平台收取交易佣金比例") BigDecimal platFormCommissionPoint,
        @Schema(description = "平台收取交易佣金") BigDecimal platFormCommission,

        // =========end distribution==========

        // ========= platform coupon==========

        @Schema(description = "平台优惠券 使用金额") BigDecimal siteCouponPrice,
        @Schema(description = "站点优惠券佣金比例") BigDecimal siteCouponPoint,
        @Schema(description = "站点优惠券佣金") BigDecimal siteCouponCommission,
        // =========end platform coupon==========

        // ========= update price ==========

        @Schema(description = "订单修改金额") BigDecimal updatePrice,

        // =========end update price==========

        @Schema(description = "流水金额(入账 出帐金额) = goodsPrice + freight - discountPrice - couponPrice" + " + updatePrice")
                BigDecimal flowPrice,
        @Schema(description = "结算价格 与 商家/供应商 结算价格（例如积分商品/砍价商品）") BigDecimal settlementPrice,
        @Schema(description = "最终结算金额 = flowPrice - platFormCommission - distributionCommission") BigDecimal billPrice,

        /** 参与的促销活动 */
        @Schema(description = "参与的促销活动") List<PromotionSkuVO> joinPromotion)
        implements Serializable {

    @Serial
    private static final long serialVersionUID = 8808470688518188146L;

    // public BigDecimal getOriginalPrice() {
    // 	if (originalPrice.compareTo(BigDecimal.ZERO) == 0) {
    // 		return flowPrice;
    // 	}
    // 	if (originalPrice.compareTo(BigDecimal.ZERO) < 0) {
    // 		return BigDecimal.ZERO;
    // 	}
    // 	return originalPrice;
    // }
    //
    // /**
    //  * 写入修改金额，自动计算订单各个金额
    //  *
    //  * @param updatePrice 修改后的订单金额
    //  */
    // public void setUpdatePrice(BigDecimal updatePrice) {
    // 	this.updatePrice = updatePrice;
    // 	this.recount();
    // }
    //
    //
    // /**
    //  * 全部重新计算
    //  */
    // public void recount() {
    // 	//流水金额(入账 出帐金额) = "流水金额(入账 出帐金额) = goodsPrice + freight - discountPrice - couponPrice +
    // updatePrice"
    // 	this.flowPrice = CurrencyUtil.sub(
    // 		CurrencyUtil.add(goodsPrice, freightPrice),
    // 		CurrencyUtil.add(discountPrice,
    // 			couponPrice != null ? couponPrice : BigDecimal.ZERO));
    // 	if (updatePrice.compareTo(BigDecimal.ZERO) != 0) {
    // 		flowPrice = CurrencyUtil.add(flowPrice, updatePrice);
    // 	}
    //
    // 	//计算平台佣金  流水金额*平台佣金比例
    // 	if (platFormCommissionPoint != null
    // 		&& getPlatFormCommissionPoint().compareTo(BigDecimal.ZERO) > 0) {
    // 		platFormCommission = CurrencyUtil.div(
    // 			CurrencyUtil.mul(flowPrice, platFormCommissionPoint), 100);
    // 	}
    //
    // 	//如果结算信息包含结算金额，则最终结算金额直接等于该交易 平台与商户的结算金额
    // 	if (settlementPrice.compareTo(BigDecimal.ZERO) > 0) {
    // 		billPrice = settlementPrice;
    // 	} else {
    // 		//如果是普通订单最终结算金额 = flowPrice - platFormCommission - distributionCommission 流水金额-平台佣金-分销佣金
    // 		billPrice = CurrencyUtil.sub(CurrencyUtil.sub(flowPrice, platFormCommission),
    // 			distributionCommission);
    // 	}
    // }
    //
    // /**
    //  * 累加金额
    //  */
    // public void increase(PriceDetailDTO priceDetailDTO) {
    //
    // 	originalPrice = CurrencyUtil.add(originalPrice, priceDetailDTO.getOriginalPrice());
    // 	goodsPrice = CurrencyUtil.add(goodsPrice, priceDetailDTO.getGoodsPrice());
    // 	freightPrice = CurrencyUtil.add(freightPrice, priceDetailDTO.getFreightPrice());
    //
    // 	payPoint = payPoint + priceDetailDTO.getPayPoint();
    // 	discountPrice = CurrencyUtil.add(discountPrice, priceDetailDTO.getDiscountPrice());
    // 	couponPrice = CurrencyUtil.add(couponPrice, priceDetailDTO.getCouponPrice());
    //
    // 	distributionCommission = CurrencyUtil.add(distributionCommission,
    // 		priceDetailDTO.getDistributionCommission());
    // 	platFormCommission = CurrencyUtil.add(platFormCommission,
    // 		priceDetailDTO.getPlatFormCommission());
    //
    // 	siteCouponPrice = CurrencyUtil.add(siteCouponPrice, priceDetailDTO.getSiteCouponPrice());
    // 	//平台优惠券一笔交易只有一个，所以这里直接移值就好了
    // 	siteCouponPoint = priceDetailDTO.getSiteCouponPoint();
    // 	siteCouponCommission = CurrencyUtil.add(siteCouponCommission,
    // 		priceDetailDTO.getSiteCouponCommission());
    //
    // 	updatePrice = CurrencyUtil.add(updatePrice, priceDetailDTO.getUpdatePrice());
    //
    // 	flowPrice = CurrencyUtil.add(flowPrice, priceDetailDTO.getFlowPrice());
    // 	billPrice = CurrencyUtil.add(billPrice, priceDetailDTO.getBillPrice());
    // 	settlementPrice = CurrencyUtil.add(settlementPrice, priceDetailDTO.getSettlementPrice());
    //
    // }
    //
    // /**
    //  * 批量累加
    //  *
    //  * @param priceDetailDTOS priceDetailDTOS
    //  */
    // public void accumulationPriceDTO(List<PriceDetailDTO> priceDetailDTOS) {
    // 	for (PriceDetailDTO price : priceDetailDTOS) {
    // 		this.increase(price);
    // 	}
    // }
    //
    //
    // public BigDecimal getGoodsPrice() {
    // 	if (goodsPrice == null || goodsPrice.compareTo(BigDecimal.ZERO) <= 0) {
    // 		return BigDecimal.ZERO;
    // 	}
    // 	return goodsPrice;
    // }
    //
    // public BigDecimal getFreightPrice() {
    // 	if (freightPrice == null || freightPrice.compareTo(BigDecimal.ZERO) <= 0) {
    // 		return BigDecimal.ZERO;
    // 	}
    // 	return freightPrice;
    // }
    //
    // public Long getPayPoint() {
    // 	if (payPoint == null || payPoint <= 0) {
    // 		return 0L;
    // 	}
    // 	return payPoint;
    // }
    //
    // public BigDecimal getDiscountPrice() {
    // 	if (discountPrice == null || discountPrice.compareTo(BigDecimal.ZERO) <= 0) {
    // 		return BigDecimal.ZERO;
    // 	}
    // 	return discountPrice;
    // }
    //
    // public BigDecimal getCouponPrice() {
    // 	if (couponPrice == null || couponPrice.compareTo(BigDecimal.ZERO) <= 0) {
    // 		return BigDecimal.ZERO;
    // 	}
    // 	return couponPrice;
    // }
    //
    // public BigDecimal getDistributionCommission() {
    // 	if (distributionCommission == null
    // 		|| distributionCommission.compareTo(BigDecimal.ZERO) <= 0) {
    // 		return BigDecimal.ZERO;
    // 	}
    // 	return distributionCommission;
    // }
    //
    // public BigDecimal getPlatFormCommission() {
    // 	if (platFormCommission == null || platFormCommission.compareTo(BigDecimal.ZERO) <= 0) {
    // 		return BigDecimal.ZERO;
    // 	}
    // 	return platFormCommission;
    // }
    //
    // public BigDecimal getSiteCouponPrice() {
    // 	if (siteCouponPrice == null || siteCouponPrice.compareTo(BigDecimal.ZERO) <= 0) {
    // 		return BigDecimal.ZERO;
    // 	}
    // 	return siteCouponPrice;
    // }
    //
    // public BigDecimal getSiteCouponPoint() {
    // 	if (siteCouponPoint == null || siteCouponPoint.compareTo(BigDecimal.ZERO) <= 0) {
    // 		return BigDecimal.ZERO;
    // 	}
    // 	return siteCouponPoint;
    // }
    //
    // public BigDecimal getSiteCouponCommission() {
    // 	if (siteCouponCommission == null || siteCouponCommission.compareTo(BigDecimal.ZERO) <= 0) {
    // 		return BigDecimal.ZERO;
    // 	}
    // 	return siteCouponCommission;
    // }
    //
    //
    // public BigDecimal getFlowPrice() {
    // 	if (flowPrice == null || flowPrice.compareTo(BigDecimal.ZERO) <= 0) {
    // 		return BigDecimal.ZERO;
    // 	}
    // 	return flowPrice;
    // }
    //
    // public BigDecimal getSettlementPrice() {
    // 	if (settlementPrice == null || settlementPrice.compareTo(BigDecimal.ZERO) <= 0) {
    // 		return BigDecimal.ZERO;
    // 	}
    // 	return settlementPrice;
    // }
    //
    // public BigDecimal getBillPrice() {
    // 	if (billPrice == null || billPrice.compareTo(BigDecimal.ZERO) <= 0) {
    // 		return BigDecimal.ZERO;
    // 	}
    // 	return billPrice;
    // }
    //
    // public BigDecimal getUpdatePrice() {
    // 	if (updatePrice == null || updatePrice.compareTo(BigDecimal.ZERO) <= 0) {
    // 		return BigDecimal.ZERO;
    // 	}
    // 	return updatePrice;
    // }
    //
    //
    // public void setSiteCouponPrice(BigDecimal siteCouponPrice) {
    // 	this.siteCouponPrice = siteCouponPrice;
    //
    // 	if (siteCouponPoint != null && siteCouponPoint.compareTo(BigDecimal.ZERO) != 0) {
    // 		this.siteCouponCommission = CurrencyUtil.mul(siteCouponPrice, siteCouponPoint);
    // 	}
    // }
    //
    // public void setSiteCouponPoint(BigDecimal siteCouponPoint) {
    // 	this.siteCouponPoint = siteCouponPoint;
    //
    // 	if (siteCouponPoint != null && siteCouponPoint.compareTo(BigDecimal.ZERO) != 0) {
    // 		this.siteCouponCommission = CurrencyUtil.div(
    // 			CurrencyUtil.mul(siteCouponPrice, siteCouponPoint), 100);
    // 	}
    // }
    //
    //
    // public void setGoodsPrice(BigDecimal goodsPrice) {
    // 	this.goodsPrice = goodsPrice;
    // 	this.recount();
    // }
    //
    // public void setFreightPrice(BigDecimal freightPrice) {
    // 	this.freightPrice = freightPrice;
    // 	this.recount();
    // }
    //
    // public void setPayPoint(Long payPoint) {
    // 	this.payPoint = payPoint;
    // }
    //
    // public void setDiscountPrice(BigDecimal discountPrice) {
    // 	this.discountPrice = discountPrice;
    // 	this.recount();
    // }
    //
    // public void setCouponPrice(BigDecimal couponPrice) {
    // 	this.couponPrice = couponPrice;
    // 	this.recount();
    // }
    //
    // public void setDistributionCommission(BigDecimal distributionCommission) {
    // 	this.distributionCommission = distributionCommission;
    // 	this.recount();
    // }
    //
    // public void setPlatFormCommissionPoint(BigDecimal platFormCommissionPoint) {
    // 	this.platFormCommissionPoint = platFormCommissionPoint;
    // 	this.recount();
    // }
    //
    // public void setPlatFormCommission(BigDecimal platFormCommission) {
    // 	this.platFormCommission = platFormCommission;
    // 	this.recount();
    // }
    //
    // public void setSiteCouponCommission(BigDecimal siteCouponCommission) {
    // 	this.siteCouponCommission = siteCouponCommission;
    // 	this.recount();
    // }
    //
    // public void setFlowPrice(BigDecimal flowPrice) {
    // 	this.flowPrice = flowPrice;
    // 	this.recount();
    // }
    //
    // public void setSettlementPrice(BigDecimal settlementPrice) {
    // 	this.settlementPrice = settlementPrice;
    // 	this.recount();
    // }
    //
    // public void setBillPrice(BigDecimal billPrice) {
    // 	this.billPrice = billPrice;
    // 	this.recount();
    // }

    public static class PromotionSkuVO {}
}
