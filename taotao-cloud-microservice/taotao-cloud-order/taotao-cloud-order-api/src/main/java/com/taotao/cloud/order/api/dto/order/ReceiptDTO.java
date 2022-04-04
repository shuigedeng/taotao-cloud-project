package com.taotao.cloud.order.api.dto.order;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 发票子内容
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "发票子内容")
public class ReceiptDTO {

	@Schema(description = "发票ID")
	private String receiptId;

	@Schema(description = "商品名称")
	private String goodsName;

	@Schema(description = "规格")
	private String specs;

	@Schema(description = "数量")
	private Integer num;

	@Schema(description = "单价")
	private BigDecimal goodPrice;

	@Schema(description = "小计")
	private BigDecimal subtotal;
}
