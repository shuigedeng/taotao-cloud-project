package com.taotao.cloud.order.api.vo.aftersale;

import com.taotao.cloud.order.api.enums.trade.AfterSaleStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * 售后可操作类型
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "售后可操作类型")
public class AfterSaleAllowOperation implements Serializable {

	@Serial
	private static final long serialVersionUID = 8808470688518188146L;

	@Schema(description = "可以确认售后")
	@Builder.Default
	private Boolean confirm = false;

	@Schema(description = "可以回寄物流")
	@Builder.Default
	private Boolean returnGoods = false;

	@Schema(description = "可以收货")
	@Builder.Default
	private Boolean rog = false;

	@Schema(description = "可以退款")
	@Builder.Default
	private Boolean refund = false;

	@Schema(description = "买家确认收货")
	private Boolean buyerConfirm;

	@Schema(description = "可以取消")
	private Boolean cancel;


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
		if (serviceStatus.equals(AfterSaleStatusEnum.WAIT_REFUND.name())) {
			refund = true;
		}

		//待平台线下退款
		if (serviceStatus.equals(AfterSaleStatusEnum.APPLY.name())
			|| serviceStatus.equals(AfterSaleStatusEnum.PASS.name())) {
			cancel = true;
		}
	}
}
