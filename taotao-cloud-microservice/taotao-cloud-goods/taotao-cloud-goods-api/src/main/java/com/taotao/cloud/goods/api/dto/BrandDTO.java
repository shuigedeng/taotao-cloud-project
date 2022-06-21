package com.taotao.cloud.goods.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * 品牌VO
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-15 20:59:38
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "品牌VO")
public class BrandDTO implements Serializable {

	@Serial
	private static final long serialVersionUID = 3829199991161122317L;

	@Schema(description = "id")
	private Long id;

	@Schema(description = "名称")
	private String name;

	@Schema(description = "logo")
	private String logo;

}
