package com.taotao.cloud.order.api.dto.order;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 交易投诉DTO
 */
@Data
@Schema(description = "交易投诉DTO")
public class OrderComplaintDTO {

	@NotBlank
	@Schema(description = "投诉主题")
	private String complainTopic;

	@NotBlank
	@Schema(description = "投诉内容")
	private String content;

	@Schema(description = "投诉凭证图片")
	private String images;

	@NotBlank
	@Schema(description = "订单号")
	private String orderSn;

	@NotBlank
	@Schema(description = "商品id")
	private String goodsId;

	@NotBlank
	@Schema(description = "sku主键")
	private String skuId;
}
