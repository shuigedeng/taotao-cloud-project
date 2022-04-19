package com.taotao.cloud.goods.api.query;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
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
public class CategorySearchQuery {

	@Schema(description = "分类名称")
	private String name;

	@Schema(description = "父id")
	private String parentId;

	@Schema(description = "层级")
	private Integer level;

	@Schema(description = "排序值")
	private BigDecimal sortOrder;

	@Schema(description = "佣金比例")
	@Digits(integer = 9, fraction=2, message = "佣金比例格式不正确")
	@DecimalMin(value = "0.00", message = "佣金比例最小为0.00")
	@DecimalMax(value = "1.00", message = "佣金比例最大为1.00")
	private BigDecimal commissionRate;

	@Schema(description = "父节点名称")
	private String parentTitle;

}
