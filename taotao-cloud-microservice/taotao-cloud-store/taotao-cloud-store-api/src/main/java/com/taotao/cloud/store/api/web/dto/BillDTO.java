package com.taotao.cloud.store.api.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 结算单传输对象
 *
 * 
 * @since 2020/11/17 4:26 下午
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "结算单传输对象")
public class BillDTO implements Serializable {

	@Serial
	private static final long serialVersionUID = 4441580387361184989L;

	@Schema(description = "结算周期内订单付款总金额")
	private BigDecimal orderPrice;

	@Schema(description = "退单金额")
	private BigDecimal refundPrice;

	@Schema(description = "平台收取佣金")
	private BigDecimal commissionPrice;

	@Schema(description = "退单产生退还佣金金额")
	private BigDecimal refundCommissionPrice;

	@Schema(description = "分销返现支出")
	private BigDecimal distributionCommission;

	@Schema(description = "分销订单退还，返现佣金返还")
	private BigDecimal distributionRefundCommission;

	@Schema(description = "平台优惠券补贴")
	private BigDecimal siteCouponCommission;

	@Schema(description = "退货平台优惠券补贴返还")
	private BigDecimal siteCouponRefundCommission;

	@Schema(description = "平台优惠券 使用金额")
	private BigDecimal siteCouponPrice;

	@Schema(description = "平台优惠券 返点")
	private BigDecimal siteCouponPoint;
}
