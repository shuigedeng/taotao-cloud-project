package com.taotao.cloud.member.api.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 评分VO
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "评分VO")
public class StoreRatingVO {

	@Schema(description = "物流评分")
	private String deliveryScore;

	@Schema(description = "服务评分")
	private String serviceScore;

	@Schema(description = "描述评分")
	private String descriptionScore;
}
