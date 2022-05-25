package com.taotao.cloud.order.api.dto.order;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serial;
import java.io.Serializable;

/**
 * 交易投诉DTO
 */
@Data
@Builder
@Schema(description = "交易投诉DTO")
public class OrderComplaintDTO implements Serializable {

	@Serial
	private static final long serialVersionUID = 8808470688518188146L;

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
