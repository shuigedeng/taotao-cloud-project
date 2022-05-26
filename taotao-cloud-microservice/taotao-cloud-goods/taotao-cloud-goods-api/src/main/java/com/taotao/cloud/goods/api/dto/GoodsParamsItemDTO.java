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
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-25 16:31:32
 */
@Schema(description = "商品参数列表")
public record GoodsParamsItemDTO(
	@Schema(description = "参数ID")
	Long paramId,

	@Schema(description = "参数名字")
	String paramName,

	@Schema(description = "参数值")
	String paramValue,

	@Schema(description = "是否可索引，0 不索引 1 索引")
	Integer isIndex,

	@Schema(description = "是否必填，0 不显示 1 显示")
	Integer required,

	@Schema(description = "排序")
	Integer sort
	) implements Serializable {

	@Serial
	private static final long serialVersionUID = -8823775607604091035L;


}
