package com.taotao.cloud.goods.api.vo;

import io.soabase.recordbuilder.core.RecordBuilder;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;

/**
 * 分类品牌VO
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-25 16:33:36
 */
@RecordBuilder
@Schema(description = "分类品牌VO")
public record CategoryBrandVO(
	@Schema(description = "品牌id")
	String id,

	@Schema(description = "品牌名称")
	String name
) implements Serializable {

	@Serial
	private static final long serialVersionUID = 3775766246075838410L;


}
