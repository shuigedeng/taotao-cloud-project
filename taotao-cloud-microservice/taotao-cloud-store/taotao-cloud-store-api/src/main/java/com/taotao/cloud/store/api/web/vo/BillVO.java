package com.taotao.cloud.store.api.web.vo;

import com.taotao.cloud.store.api.enums.BillStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 结算单
 *
 */
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BillVO {

	private String sn;

	private LocalDateTime startTime;

	private LocalDateTime endTime;

	/**
	 * @see BillStatusEnum
	 */
	private String billStatus;

	private String storeId;

	private String storeName;

	private LocalDateTime payTime;

	private String bankAccountName;

	private String bankAccountNumber;

	private String bankName;

	private String bankCode;

	/**
	 * 算钱规则 billPrice=orderPrice-refundPrice -commissionPrice+refundCommissionPrice
	 * -distributionCommission+distributionRefundCommission +siteCouponCommission-siteCouponRefundCommission
	 * +kanjiaSettlementPrice+pointSettlementPrice
	 */
	private BigDecimal orderPrice;

	private BigDecimal refundPrice;

	private BigDecimal commissionPrice;

	private BigDecimal refundCommissionPrice;

	private BigDecimal distributionCommission;

	private BigDecimal distributionRefundCommission;

	private BigDecimal siteCouponCommission;

	private BigDecimal siteCouponRefundCommission;

	private BigDecimal pointSettlementPrice;

	private BigDecimal kanjiaSettlementPrice;

	private BigDecimal billPrice;
}
