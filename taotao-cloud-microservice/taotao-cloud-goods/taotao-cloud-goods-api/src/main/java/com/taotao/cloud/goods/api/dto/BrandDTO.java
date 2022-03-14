package com.taotao.cloud.goods.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 品牌VO
 */
@Data
@Schema(description = "品牌VO")
public class BrandDTO {

	private static final long serialVersionUID = 3829199991161122317L;

	private String id;

	@Schema(description = "名称")
	private String name;

	@Schema(description = "logo")
	private String logo;

}
