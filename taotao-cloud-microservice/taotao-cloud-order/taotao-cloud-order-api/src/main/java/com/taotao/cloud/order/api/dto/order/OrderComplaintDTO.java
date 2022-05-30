package com.taotao.cloud.order.api.dto.order;

import io.soabase.recordbuilder.core.RecordBuilder;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;
import javax.validation.constraints.NotBlank;

/**
 * 交易投诉DTO
 */
@RecordBuilder
@Schema(description = "交易投诉DTO")
public record OrderComplaintDTO(

	@NotBlank
	@Schema(description = "投诉主题")
	String complainTopic,

	@NotBlank
	@Schema(description = "投诉内容")
	String content,

	@Schema(description = "投诉凭证图片")
	String images,

	@NotBlank
	@Schema(description = "订单号")
	String orderSn,

	@NotBlank
	@Schema(description = "商品id")
	String goodsId,

	@NotBlank
	@Schema(description = "sku主键")
	String skuId
) implements Serializable {

	@Serial
	private static final long serialVersionUID = 8808470688518188146L;


}
