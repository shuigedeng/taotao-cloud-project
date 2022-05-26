package com.taotao.cloud.order.api.vo.aftersale;

import com.taotao.cloud.order.api.enums.trade.AfterSaleStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;

/**
 * 售后可操作类型
 */
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
	 *
	 * @param afterSaleVO
	 */
	public AfterSaleAllowOperation(AfterSaleVO afterSaleVO) {
		//售后单状态
		String serviceStatus = afterSaleVO.getServiceStatus();

		//新提交售后
		if (serviceStatus.equals(AfterSaleStatusEnum.APPLY.name())) {
			confirm = true;
		}

		//待确认收货
		if (serviceStatus.equals(AfterSaleStatusEnum.BUYER_RETURN.name())) {
			rog = true;
		}

		//待平台线下退款
		if (serviceStatus.equals(AfterSaleStatusEnum.WAIT_REFUND.name())) {
			refund = true;
		}

		//待平台线下退款
		//if (serviceStatus.equals(AfterSaleStatusEnum.WAIT_REFUND.name())) {
		//	refund = true;
		//}

		//待平台线下退款
		if (serviceStatus.equals(AfterSaleStatusEnum.APPLY.name())
			|| serviceStatus.equals(AfterSaleStatusEnum.PASS.name())) {
			cancel = true;
		}
	}
}
