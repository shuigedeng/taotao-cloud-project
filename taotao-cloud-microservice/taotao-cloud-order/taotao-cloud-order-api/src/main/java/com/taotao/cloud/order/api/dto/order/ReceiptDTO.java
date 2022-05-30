package com.taotao.cloud.order.api.dto.order;

import io.soabase.recordbuilder.core.RecordBuilder;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;


/**
 * 发票子内容
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-28 09:19:44
 */
@RecordBuilder
@Schema(description = "发票子内容")
public record ReceiptDTO(
	@Schema(description = "发票ID")
	String receiptId,

	@Schema(description = "商品名称")
	String goodsName,

	@Schema(description = "规格")
	String specs,

	@Schema(description = "数量")
	Integer num,

	@Schema(description = "单价")
	BigDecimal goodPrice,

	@Schema(description = "小计")
	BigDecimal subtotal
) implements Serializable {

	@Serial
	private static final long serialVersionUID = 8808470688518188146L;


}
