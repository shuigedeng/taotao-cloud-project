package com.taotao.cloud.goods.api.vo;

import io.soabase.recordbuilder.core.RecordBuilder;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;

/**
 * 规格项表规格项
 */
@RecordBuilder
@Schema(description = "参数组关联的参数集合")
public record SpecificationVO(
	Long id,

	@Schema(description = "规格名称")
	String specName,

	@Schema(description = "所属卖家 0属于平台")
	Long storeId,

	@Schema(description = "规格值名字, 《,》分割")
	String specValue
) implements Serializable {

	@Serial
	private static final long serialVersionUID = -4433579132929428572L;


}
