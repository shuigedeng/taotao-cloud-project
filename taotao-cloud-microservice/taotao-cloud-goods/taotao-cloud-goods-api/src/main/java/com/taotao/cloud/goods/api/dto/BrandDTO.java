package com.taotao.cloud.goods.api.dto;

import io.soabase.recordbuilder.core.RecordBuilder;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;

/**
 * 品牌VO
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-15 20:59:38
 */
@RecordBuilder
@Schema(description = "品牌VO")
public record BrandDTO(
	@Schema(description = "id")
	Long id,

	@Schema(description = "名称")
	String name,

	@Schema(description = "logo")
	String logo
) implements Serializable {

	@Serial
	private static final long serialVersionUID = 3829199991161122317L;


}
