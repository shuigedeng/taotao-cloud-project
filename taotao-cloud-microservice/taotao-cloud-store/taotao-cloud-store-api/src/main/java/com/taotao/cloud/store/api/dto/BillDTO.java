package com.taotao.cloud.store.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;

/**
 * 结算单传输对象
 *
 * @author Chopper
 * @since 2020/11/17 4:26 下午
 */
@Schema(description = "结算单传输对象")
public class BillDTO implements Serializable {

	private static final long serialVersionUID = 4441580387361184989L;


	@Schema(description = "结算周期内订单付款总金额")
	private Double orderPrice;

	@Schema(description = "退单金额")
	private Double refundPrice;

	@Schema(description = "平台收取佣金")
	private Double commissionPrice;

	@Schema(description = "退单产生退还佣金金额")
	private Double refundCommissionPrice;

	@Schema(description = "分销返现支出")
	private Double distributionCommission;

	@Schema(description = "分销订单退还，返现佣金返还")
	private Double distributionRefundCommission;

	@Schema(description = "平台优惠券补贴")
	private Double siteCouponCommission;

	@Schema(description = "退货平台优惠券补贴返还")
	private Double siteCouponRefundCommission;

	@Schema(description = "平台优惠券 使用金额")
	private Double siteCouponPrice;

	@Schema(description = "平台优惠券 返点")
	private Double siteCouponPoint;

	public Double getOrderPrice() {
		return orderPrice;
	}

	public void setOrderPrice(Double orderPrice) {
		this.orderPrice = orderPrice;
	}

	public Double getRefundPrice() {
		return refundPrice;
	}

	public void setRefundPrice(Double refundPrice) {
		this.refundPrice = refundPrice;
	}

	public Double getCommissionPrice() {
		return commissionPrice;
	}

	public void setCommissionPrice(Double commissionPrice) {
		this.commissionPrice = commissionPrice;
	}

	public Double getRefundCommissionPrice() {
		return refundCommissionPrice;
	}

	public void setRefundCommissionPrice(Double refundCommissionPrice) {
		this.refundCommissionPrice = refundCommissionPrice;
	}

	public Double getDistributionCommission() {
		return distributionCommission;
	}

	public void setDistributionCommission(Double distributionCommission) {
		this.distributionCommission = distributionCommission;
	}

	public Double getDistributionRefundCommission() {
		return distributionRefundCommission;
	}

	public void setDistributionRefundCommission(Double distributionRefundCommission) {
		this.distributionRefundCommission = distributionRefundCommission;
	}

	public Double getSiteCouponCommission() {
		return siteCouponCommission;
	}

	public void setSiteCouponCommission(Double siteCouponCommission) {
		this.siteCouponCommission = siteCouponCommission;
	}

	public Double getSiteCouponRefundCommission() {
		return siteCouponRefundCommission;
	}

	public void setSiteCouponRefundCommission(Double siteCouponRefundCommission) {
		this.siteCouponRefundCommission = siteCouponRefundCommission;
	}

	public Double getSiteCouponPrice() {
		return siteCouponPrice;
	}

	public void setSiteCouponPrice(Double siteCouponPrice) {
		this.siteCouponPrice = siteCouponPrice;
	}

	public Double getSiteCouponPoint() {
		return siteCouponPoint;
	}

	public void setSiteCouponPoint(Double siteCouponPoint) {
		this.siteCouponPoint = siteCouponPoint;
	}
}
