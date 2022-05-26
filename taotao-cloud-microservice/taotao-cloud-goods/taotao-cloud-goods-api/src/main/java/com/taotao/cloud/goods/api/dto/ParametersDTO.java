package com.taotao.cloud.goods.api.dto;

import io.soabase.recordbuilder.core.RecordBuilder;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 商品参数
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-25 16:31:42
 */
@RecordBuilder
@Schema(description = "商品参数")
public record ParametersDTO(

	@Schema(description = "参数名称")
	String paramName,

	@Schema(description = "选择值")
	String options,

	@Schema(description = "是否可索引，0 不显示 1 显示")
	Integer isIndex,

	@Schema(description = "是否必填 是1否0")
	Integer required,

	@Schema(description = "参数分组id")
	Long groupId,

	@Schema(description = "分类id")
	Long categoryId,

	@Schema(description = "排序")
	Integer sort
	) implements Serializable {

	@Serial
	private 	static final long serialVersionUID = 724427321881170297L;

}
