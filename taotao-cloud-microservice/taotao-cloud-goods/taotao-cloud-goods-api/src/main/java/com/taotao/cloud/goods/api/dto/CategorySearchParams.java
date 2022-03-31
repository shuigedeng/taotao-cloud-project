package com.taotao.cloud.goods.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 分类查询参数
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategorySearchParams {

	@Schema(description = "分类名称")
	private String name;

	@Schema(description = "父id")
	private String parentId;

	@Schema(description = "层级")
	private Integer level;

	@Schema(description = "排序值")
	private BigDecimal sortOrder;

	@Schema(description = "佣金比例")
	private BigDecimal commissionRate;

	@Schema(description = "父节点名称")
	private String parentTitle;

}
