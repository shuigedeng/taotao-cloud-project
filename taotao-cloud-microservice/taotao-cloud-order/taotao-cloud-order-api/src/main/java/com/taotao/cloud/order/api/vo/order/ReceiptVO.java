package com.taotao.cloud.order.api.vo.order;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 发票
 *
 * @since 2020/11/28 11:38
 */
@Schema(description = "发票")
public class ReceiptVO {

	@Schema(description = "发票抬头")
	private String receiptTitle;

	@Schema(description = "纳税人识别号")
	private String taxpayerId;

	@Schema(description = "发票内容")
	private String receiptContent;

}
