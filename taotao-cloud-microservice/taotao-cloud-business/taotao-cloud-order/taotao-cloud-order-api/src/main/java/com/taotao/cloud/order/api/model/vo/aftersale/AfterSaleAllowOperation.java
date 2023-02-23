package com.taotao.cloud.order.api.model.vo.aftersale;

import com.taotao.cloud.order.api.enums.trade.AfterSaleStatusEnum;
import io.soabase.recordbuilder.core.RecordBuilder;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serial;
import java.io.Serializable;

/**
 * 售后可操作类型
 */
@RecordBuilder
@Schema(description = "售后可操作类型")
public record AfterSaleAllowOperation(
	@Schema(description = "可以确认售后")
	Boolean confirm,

	@Schema(description = "可以回寄物流")
	Boolean returnGoods,

	@Schema(description = "可以收货")
	Boolean rog,

	@Schema(description = "可以退款")
	Boolean refund,

	@Schema(description = "买家确认收货")
	Boolean buyerConfirm,

	@Schema(description = "可以取消")
	Boolean cancel
) implements Serializable {

	@Serial
	private static final long serialVersionUID = 8808470688518188146L;

	/**
	 * 根据各种状态构建对象
	 */
	public AfterSaleAllowOperation(AfterSaleVO afterSaleVO) {
		this(afterSaleVO.serviceStatus().equals(AfterSaleStatusEnum.APPLY.name()),
			false,
			afterSaleVO.serviceStatus().equals(AfterSaleStatusEnum.BUYER_RETURN.name()),
			afterSaleVO.serviceStatus().equals(AfterSaleStatusEnum.WAIT_REFUND.name()),
			false,
			afterSaleVO.serviceStatus().equals(AfterSaleStatusEnum.APPLY.name()));
	}
}
