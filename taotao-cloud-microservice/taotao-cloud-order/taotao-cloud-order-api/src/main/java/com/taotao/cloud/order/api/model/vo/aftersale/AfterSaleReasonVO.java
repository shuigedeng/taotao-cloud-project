package com.taotao.cloud.order.api.model.vo.aftersale;

import com.taotao.cloud.order.api.enums.trade.AfterSaleTypeEnum;
import io.soabase.recordbuilder.core.RecordBuilder;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;

/**
 * 售后原因
 */
@RecordBuilder
@Schema(description = "售后日志VO")
public record AfterSaleReasonVO(

	@Schema(description = "id")
	Long id,

	@Schema(description = "售后原因")
	String reason,

	/**
	 * @see AfterSaleTypeEnum
	 */
	@Schema(description = "售后类型")
	String serviceType
) implements Serializable {

	@Serial
	private static final long serialVersionUID = 8808470688518188146L;


}
