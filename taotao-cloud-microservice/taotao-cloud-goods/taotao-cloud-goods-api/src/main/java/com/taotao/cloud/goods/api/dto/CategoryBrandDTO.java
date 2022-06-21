package com.taotao.cloud.goods.api.dto;

import io.soabase.recordbuilder.core.RecordBuilder;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * 分类品牌DTO
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-15 20:59:38
 */
@RecordBuilder
@Schema(description = "分类品牌DTO")
public record CategoryBrandDTO(

	@NotNull(message = "分类id不能为空")
	@Schema(description = "分类id")
	Long categoryId,

	@NotEmpty(message = "品牌id列表不能为空")
	@Schema(description = "品牌id列表")
	List<Long> brandIds
) implements Serializable {

	@Serial
	private static final long serialVersionUID = 3829199991161122317L;


}
