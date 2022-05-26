package com.taotao.cloud.goods.api.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;

/**
 * 品牌VO
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-25 16:33:30
 */
@Schema(description = "品牌VO")
public record BrandVO(
	@Schema(description = "id")
	String id,

	@Schema(description = "名称")
	String name,

	@Schema(description = "logo")
	String logo
) implements Serializable {

	@Serial
	private static final long serialVersionUID = 3829199991161122317L;

}
