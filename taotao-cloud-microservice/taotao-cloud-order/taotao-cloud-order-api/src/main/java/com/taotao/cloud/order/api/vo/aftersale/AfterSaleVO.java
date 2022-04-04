package com.taotao.cloud.order.api.vo.aftersale;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 售后VO
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
