package com.taotao.cloud.goods.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 商品参数项
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "商品参数列表")
public class GoodsParamsItemDTO implements Serializable {

	@Serial
	private static final long serialVersionUID = -8823775607604091035L;

	@Schema(description = "参数ID")
	private Long paramId;

	@Schema(description = "参数名字")
	private String paramName;

	@Schema(description = "参数值")
	private String paramValue;

	@Schema(description = "是否可索引，0 不索引 1 索引")
	private Integer isIndex;

	@Schema(description = "是否必填，0 不显示 1 显示")
	private Integer required;

	@Schema(description = "排序")
	private Integer sort;
}
