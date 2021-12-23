package com.taotao.cloud.order.api.vo.aftersale;

import cn.lili.modules.order.aftersale.entity.dos.AfterSale;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 售后VO
 *
 * @since 2021/3/12 10:32 上午
 */
@Schema(description = "售后VO")
public class AfterSaleVO extends AfterSale {

	/**
	 * 初始化自身状态
	 */
	public AfterSaleAllowOperation getAfterSaleAllowOperationVO() {

		//设置订单的可操作状态
		return new AfterSaleAllowOperation(this);
	}
}
