package com.taotao.cloud.order.api.dto.aftersale;


import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 商城退款流水
 *
 * @since 2020/11/17 7:25 下午
 */
@Schema(description = "商城退款流水")
public class AfterSalePriceDetailDTO implements Serializable {

	@Serial
	private static final long serialVersionUID = 8808470688518188146L;

	@Schema(description = "商品总金额（商品原价）")
	private Double goodsPrice;

	@Schema(description = "配送费")
	private Double freightPrice;

	//============discount price============

	@Schema(description = "支付积分")
	private Integer payPoint;

	@Schema(description = "优惠金额")
	private Double discountPrice;

	@Schema(description = "优惠券金额")
	private Double couponPrice;

	//===========end discount price =============

	//=========distribution==========

	@Schema(description = "单品分销返现支出")
	private Double distributionCommission;


	@Schema(description = "平台收取交易佣金")
	private Double platFormCommission;

	//=========end distribution==========

	//========= platform coupon==========

	@Schema(description = "平台优惠券 使用金额")
	private Double siteCouponPrice;

	@Schema(description = "站点优惠券佣金比例")
	private Double siteCouponPoint;

	@Schema(description = "站点优惠券佣金")
	private Double siteCouponCommission;
	//=========end platform coupon==========

	@Schema(description = "流水金额(入账 出帐金额) = goodsPrice - discountPrice - couponPrice")
	private Double flowPrice;

	@Schema(description = "最终结算金额 = flowPrice - platFormCommission - distributionCommission")
	private Double billPrice;

	/**
	 * 参与的促销活动
	 */
	@Schema(description = "参与的促销活动")
	//private List<BasePromotions> joinPromotion;
	private List<String> joinPromotion;


	public AfterSalePriceDetailDTO() {
		goodsPrice = 0d;
		freightPrice = 0d;

		payPoint = 0;
		discountPrice = 0d;

		distributionCommission = 0d;
		platFormCommission = 0d;

		siteCouponPrice = 0d;
		siteCouponPoint = 0d;
		siteCouponCommission = 0d;

		flowPrice = 0d;
		billPrice = 0d;

		joinPromotion = new ArrayList<>();
	}

}
