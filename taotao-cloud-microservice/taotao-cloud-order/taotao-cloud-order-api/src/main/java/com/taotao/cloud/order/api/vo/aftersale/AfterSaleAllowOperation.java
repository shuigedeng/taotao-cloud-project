package com.taotao.cloud.order.api.vo.aftersale;

import cn.lili.modules.order.aftersale.entity.dos.AfterSale;
import cn.lili.modules.order.trade.entity.enums.AfterSaleStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 售后可操作类型
 *
 * @since 2021/3/12 10:33 上午
 */
@Schema(description = "售后可操作类型")
public class AfterSaleAllowOperation {

	@Schema(description = "可以确认售后")
	private Boolean confirm = false;

	@Schema(description = "可以回寄物流")
	private Boolean returnGoods = false;

	@Schema(description = "可以收货")
	private Boolean rog = false;

	@Schema(description = "可以退款")
	private Boolean refund = false;

	@Schema(description = "买家确认收货")
	private Boolean buyerConfirm;

	@Schema(description = "可以取消")
	private Boolean cancel;


	/**
	 * 根据各种状态构建对象
	 *
	 * @param afterSale
	 */
	public AfterSaleAllowOperation(AfterSale afterSale) {
		//售后单状态
		String serviceStatus = afterSale.getServiceStatus();

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
