package com.taotao.cloud.goods.api.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 品牌VO
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "品牌VO")
public class BrandVO {

	private static final long serialVersionUID = 3829199991161122317L;

	@Schema(description = "id")
	private String id;

	@Schema(description = "名称")
	private String name;

	@Schema(description = "logo")
	private String logo;

}
