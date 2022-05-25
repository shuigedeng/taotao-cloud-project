package com.taotao.cloud.order.api.vo.aftersale;

import com.taotao.cloud.order.api.enums.trade.AfterSaleTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 售后原因
 */
@Data
@Builder
@Schema(description = "售后日志VO")
public class AfterSaleReasonVO implements Serializable {
	@Serial
	private static final long serialVersionUID = 8808470688518188146L;

	@Schema(description = "id")
	private Long id;

	@Schema(description = "售后原因")
	private String reason;

	/**
	 * @see AfterSaleTypeEnum
	 */
	@Schema(description = "售后类型")
	private String serviceType;

}
