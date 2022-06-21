package com.taotao.cloud.goods.api.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 商品分类
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "商品分类VO")
public class CategoryVO {

	@Schema(description = "id")
	private Long id;

	@Schema(description = "分类名称")
	private String name;

	@Schema(description = " 父id, 根节点为0")
	private Long parentId;

	@Schema(description = "层级, 从0开始")
	private Integer level;

	@Schema(description = "排序值")
	private Integer sortOrder;

	@Schema(description = "佣金比例")
	private BigDecimal commissionRate;

	@Schema(description = "分类图标")
	private String image;

	@Schema(description = "是否支持频道")
	private Boolean supportChannel;
}
