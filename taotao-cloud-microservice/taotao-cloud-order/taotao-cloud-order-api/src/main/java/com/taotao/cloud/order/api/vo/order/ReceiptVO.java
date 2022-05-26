package com.taotao.cloud.order.api.vo.order;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 发票
 */
@Schema(description = "发票")
public record ReceiptVO(
	@Schema(description = "发票抬头")
	String receiptTitle,

	@Schema(description = "纳税人识别号")
	String taxpayerId,

	@Schema(description = "发票内容")
	String receiptContent
) implements Serializable {

	@Serial
	private static final long serialVersionUID = -6293102172184734928L;


}
