package com.taotao.cloud.goods.api.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 分类品牌VO
 */
@Data
public class CategoryBrandVO {

	/**
	 * 品牌id
	 */
	@Schema(description = "品牌id", required = true)
	private String id;

	/**
	 * 品牌名称
	 */
	@Schema(description = "品牌名称", required = true)
	private String name;
}
