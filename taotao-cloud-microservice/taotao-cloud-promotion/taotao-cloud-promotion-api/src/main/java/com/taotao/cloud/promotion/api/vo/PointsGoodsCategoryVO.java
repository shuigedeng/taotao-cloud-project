package com.taotao.cloud.promotion.api.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;

/**
 * 积分商品分类视图对象
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PointsGoodsCategoryVO {

	private static final long serialVersionUID = 5528833118735059182L;

	private Long id;


	@NotEmpty(message = "分类名称不能为空")
	@Schema(description = "分类名称")
	private String name;

	@Schema(description = "父id, 根节点为0")
	private String parentId;

	@Schema(description = "层级, 从0开始")
	private Integer level;

	@Schema(description = "排序值")
	private BigDecimal sortOrder;
}
