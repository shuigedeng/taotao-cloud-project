package com.taotao.cloud.order.api.dto.order;


import com.taotao.cloud.common.utils.number.CurrencyUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 商城流水，细节到orderItem
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "商城流水，细节到orderItem")
public class PriceDetailDTO implements Serializable {


	private static final long serialVersionUID = 8808470688518188146L;
	/**
	 * 订单原始总价格 用于订单价格修改金额计算使用
	 */
	@Schema(description = "订单原始总价格")
	private BigDecimal originalPrice;

	@Schema(description = "商品总金额（商品原价）")
	private BigDecimal goodsPrice;

	@Schema(description = "配送费")
	private BigDecimal freightPrice;

	//============discount price============

	@Schema(description = "支付积分")
	private Long payPoint;

	@Schema(description = "优惠金额")
	private BigDecimal discountPrice;

	@Schema(description = "优惠券金额")
	private BigDecimal couponPrice;

	//===========end discount price =============

	//=========distribution==========

	@Schema(description = "单品分销返现支出")
	private BigDecimal distributionCommission;


	@Schema(description = "平台收取交易佣金比例")
	private BigDecimal platFormCommissionPoint;

	@Schema(description = "平台收取交易佣金")
	private BigDecimal platFormCommission;

	//=========end distribution==========

	//========= platform coupon==========

	@Schema(description = "平台优惠券 使用金额")
	private BigDecimal siteCouponPrice;

	@Schema(description = "站点优惠券佣金比例")
	private BigDecimal siteCouponPoint;

	@Schema(description = "站点优惠券佣金")
	private BigDecimal siteCouponCommission;
	//=========end platform coupon==========

	//========= update price ==========

	@Schema(description = "订单修改金额")
	private BigDecimal updatePrice;

	//=========end update price==========

	@Schema(description = "流水金额(入账 出帐金额) = goodsPrice + freight - discountPrice - couponPrice + updatePrice")
	private BigDecimal flowPrice;

	@Schema(description = "结算价格 与 商家/供应商 结算价格（例如积分商品/砍价商品）")
	private BigDecimal settlementPrice;

	@Schema(description = "最终结算金额 = flowPrice - platFormCommission - distributionCommission")
	private BigDecimal billPrice;

	/**
	 * 参与的促销活动
	 */
	@Schema(description = "参与的促销活动")
	private List<PromotionSkuVO> joinPromotion;

	public BigDecimal getOriginalPrice() {
		if (originalPrice == 0D) {
			return flowPrice;
		}
		if (originalPrice < 0) {
			return 0d;
		}
		return originalPrice;
	}

	/**
	 * 写入修改金额，自动计算订单各个金额
	 *
	 * @param updatePrice 修改后的订单金额
	 */
	public void setUpdatePrice(BigDecimal updatePrice) {
		this.updatePrice = updatePrice;
		this.recount();
	}


	/**
	 * 全部重新计算
	 */
	public void recount() {
		//流水金额(入账 出帐金额) = "流水金额(入账 出帐金额) = goodsPrice + freight - discountPrice - couponPrice + updatePrice"
		this.flowPrice = CurrencyUtil.sub(
			CurrencyUtil.add(goodsPrice, freightPrice),
			CurrencyUtil.add(discountPrice,
				couponPrice != null ? couponPrice : 0));
		if (updatePrice != 0) {
			flowPrice = CurrencyUtil.add(flowPrice, updatePrice);
		}

		//计算平台佣金  流水金额*平台佣金比例
		if (platFormCommissionPoint != null && getPlatFormCommissionPoint() > 0) {
			platFormCommission = CurrencyUtil.div(
				CurrencyUtil.mul(flowPrice, platFormCommissionPoint), 100);
		}

		//如果结算信息包含结算金额，则最终结算金额直接等于该交易 平台与商户的结算金额
		if (settlementPrice > 0) {
			billPrice = settlementPrice;
		} else {
			//如果是普通订单最终结算金额 = flowPrice - platFormCommission - distributionCommission 流水金额-平台佣金-分销佣金
			billPrice = CurrencyUtil.sub(CurrencyUtil.sub(flowPrice, platFormCommission),
				distributionCommission);
		}
	}

	/**
	 * 累加金额
	 */
	public void increase(PriceDetailDTO priceDetailDTO) {

		originalPrice = CurrencyUtil.add(originalPrice, priceDetailDTO.getOriginalPrice());
		goodsPrice = CurrencyUtil.add(goodsPrice, priceDetailDTO.getGoodsPrice());
		freightPrice = CurrencyUtil.add(freightPrice, priceDetailDTO.getFreightPrice());

		payPoint = payPoint + priceDetailDTO.getPayPoint();
		discountPrice = CurrencyUtil.add(discountPrice, priceDetailDTO.getDiscountPrice());
		couponPrice = CurrencyUtil.add(couponPrice, priceDetailDTO.getCouponPrice());

		distributionCommission = CurrencyUtil.add(distributionCommission,
			priceDetailDTO.getDistributionCommission());
		platFormCommission = CurrencyUtil.add(platFormCommission,
			priceDetailDTO.getPlatFormCommission());

		siteCouponPrice = CurrencyUtil.add(siteCouponPrice, priceDetailDTO.getSiteCouponPrice());
		//平台优惠券一笔交易只有一个，所以这里直接移值就好了
		siteCouponPoint = priceDetailDTO.getSiteCouponPoint();
		siteCouponCommission = CurrencyUtil.add(siteCouponCommission,
			priceDetailDTO.getSiteCouponCommission());

		updatePrice = CurrencyUtil.add(updatePrice, priceDetailDTO.getUpdatePrice());

		flowPrice = CurrencyUtil.add(flowPrice, priceDetailDTO.getFlowPrice());
		billPrice = CurrencyUtil.add(billPrice, priceDetailDTO.getBillPrice());
		settlementPrice = CurrencyUtil.add(settlementPrice, priceDetailDTO.getSettlementPrice());

	}

	/**
	 * 批量累加
	 *
	 * @param priceDetailDTOS
	 * @return
	 */
	public void accumulationPriceDTO(List<PriceDetailDTO> priceDetailDTOS) {
		for (PriceDetailDTO price : priceDetailDTOS) {
			this.increase(price);
		}
	}


	public BigDecimal getGoodsPrice() {
		if (goodsPrice == null || goodsPrice <= 0) {
			return 0D;
		}
		return goodsPrice;
	}

	public BigDecimal getFreightPrice() {
		if (freightPrice == null || freightPrice <= 0) {
			return 0D;
		}
		return freightPrice;
	}

	public Long getPayPoint() {
		if (payPoint == null || payPoint <= 0) {
			return 0L;
		}
		return payPoint;
	}

	public BigDecimal getDiscountPrice() {
		if (discountPrice == null || discountPrice <= 0) {
			return 0D;
		}
		return discountPrice;
	}

	public BigDecimal getCouponPrice() {
		if (couponPrice == null || couponPrice <= 0) {
			return 0D;
		}
		return couponPrice;
	}

	public BigDecimal getDistributionCommission() {
		if (distributionCommission == null || distributionCommission <= 0) {
			return 0D;
		}
		return distributionCommission;
	}

	public BigDecimal getPlatFormCommission() {
		if (platFormCommission == null || platFormCommission <= 0) {
			return 0D;
		}
		return platFormCommission;
	}

	public BigDecimal getSiteCouponPrice() {
		if (siteCouponPrice == null || siteCouponPrice <= 0) {
			return 0D;
		}
		return siteCouponPrice;
	}

	public BigDecimal getSiteCouponPoint() {
		if (siteCouponPoint == null || siteCouponPoint <= 0) {
			return 0D;
		}
		return siteCouponPoint;
	}

	public BigDecimal getSiteCouponCommission() {
		if (siteCouponCommission == null || siteCouponCommission <= 0) {
			return 0D;
		}
		return siteCouponCommission;
	}


	public BigDecimal getFlowPrice() {
		if (flowPrice == null || flowPrice <= 0) {
			return 0D;
		}
		return flowPrice;
	}

	public BigDecimal getSettlementPrice() {
		if (settlementPrice == null || settlementPrice <= 0) {
			return 0D;
		}
		return settlementPrice;
	}

	public BigDecimal getBillPrice() {
		if (billPrice == null || billPrice <= 0) {
			return 0D;
		}
		return billPrice;
	}

	public BigDecimal getUpdatePrice() {
		if (updatePrice == null || updatePrice <= 0) {
			return 0D;
		}
		return updatePrice;
	}


	public void setSiteCouponPrice(BigDecimal siteCouponPrice) {
		this.siteCouponPrice = siteCouponPrice;

		if (siteCouponPoint != null && siteCouponPoint != 0) {
			this.siteCouponCommission = CurrencyUtil.mul(siteCouponPrice, siteCouponPoint);
		}
	}

	public void setSiteCouponPoint(BigDecimal siteCouponPoint) {
		this.siteCouponPoint = siteCouponPoint;

		if (siteCouponPoint != null && siteCouponPoint != 0) {
			this.siteCouponCommission = CurrencyUtil.div(
				CurrencyUtil.mul(siteCouponPrice, siteCouponPoint), 100);
		}
	}


	public void setGoodsPrice(BigDecimal goodsPrice) {
		this.goodsPrice = goodsPrice;
		this.recount();
	}

	public void setFreightPrice(BigDecimal freightPrice) {
		this.freightPrice = freightPrice;
		this.recount();
	}

	public void setPayPoint(Long payPoint) {
		this.payPoint = payPoint;
	}

	public void setDiscountPrice(BigDecimal discountPrice) {
		this.discountPrice = discountPrice;
		this.recount();
	}

	public void setCouponPrice(BigDecimal couponPrice) {
		this.couponPrice = couponPrice;
		this.recount();
	}

	public void setDistributionCommission(BigDecimal distributionCommission) {
		this.distributionCommission = distributionCommission;
		this.recount();
	}

	public void setPlatFormCommissionPoint(BigDecimal platFormCommissionPoint) {
		this.platFormCommissionPoint = platFormCommissionPoint;
		this.recount();
	}

	public void setPlatFormCommission(BigDecimal platFormCommission) {
		this.platFormCommission = platFormCommission;
		this.recount();
	}

	public void setSiteCouponCommission(BigDecimal siteCouponCommission) {
		this.siteCouponCommission = siteCouponCommission;
		this.recount();
	}

	public void setFlowPrice(BigDecimal flowPrice) {
		this.flowPrice = flowPrice;
		this.recount();
	}

	public void setSettlementPrice(BigDecimal settlementPrice) {
		this.settlementPrice = settlementPrice;
		this.recount();
	}

	public void setBillPrice(BigDecimal billPrice) {
		this.billPrice = billPrice;
		this.recount();
	}
}
