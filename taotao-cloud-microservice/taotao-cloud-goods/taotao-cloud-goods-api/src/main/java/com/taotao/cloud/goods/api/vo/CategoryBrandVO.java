package com.taotao.cloud.goods.api.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 分类品牌VO
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "分类品牌VO")
public class CategoryBrandVO {

	@Schema(description = "品牌id")
	private String id;

	@Schema(description = "品牌名称")
	private String name;
}
