package com.taotao.cloud.order.api.dto.aftersale;

import com.taotao.cloud.order.api.enums.trade.AfterSaleTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

/**
 * 售后原因dto
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-21 16:59:38
 * @since 2021/7/9 1:39 上午
 */
@Data
@Builder
@Schema(description = "售后原因dto")
public class AfterSaleReasonDTO {

	@Schema(description = "售后原因")
	private String reason;

	/**
	 * @see AfterSaleTypeEnum
	 */
	@Schema(description = "售后类型")
	private String serviceType;
}
