package com.taotao.cloud.goods.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import lombok.Data;

/**
 * 商品参数项
 */
@Data
@Schema(description = "商品参数列表")
public class GoodsParamsItemDTO implements Serializable {

	private static final long serialVersionUID = -8823775607604091035L;

	@Schema(description = "参数ID")
	private String paramId;

	@Schema(description = "参数名字")
	private String paramName;

	@Schema(description = "参数值")
	private String paramValue;

	@Schema(description = "是否可索引，0 不索引 1 索引")
	private Integer isIndex = 0;

	@Schema(description = "是否必填，0 不显示 1 显示")
	private Integer required = 0;

	@Schema(description = "排序")
	private Integer sort;
}
