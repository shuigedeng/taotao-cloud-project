package com.taotao.cloud.goods.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 直播商品DTO 用于获取直播商品状态时使用
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommodityDTO {

	@Schema(description = "商品ID")
	private Integer goodsId;

	@Schema(description = "商品名称")
	private String name;

	@Schema(description = "url")
	private String url;

	@Schema(description = "审核状态")
	private Integer auditStatus;
}
